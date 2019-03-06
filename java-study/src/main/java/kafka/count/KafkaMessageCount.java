package kafka.count;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndTimestamp;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.TopicPartition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 统计指定时间段内 kafka消息 用户数
 */
public class KafkaMessageCount {

	private static final Logger LOGGER = LoggerFactory.getLogger(KafkaMessageCount.class);

	public static void main(String[] args) {

//		String bootstrapServers = args[0];
		String bootstrapServers = "bigdata:9092";
		String topics = "kafka2oracle";// 用[,]分隔
		int partitionNum = 3;
		String groupId = "test";
		String startTime = args[0];// 20181116170000
		String endTime = args[1];

		Properties props = new Properties();
		props.put("bootstrap.servers", bootstrapServers);
		props.put("group.id", groupId);
//		props.put("enable.auto.commit", "false");
//		props.put("auto.commit.interval.ms", "1000");
//		props.put("session.timeout.ms", "30000");
//		props.put("auto.offset.reset", "earliest");
		props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");


		KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);

		for (String topic : topics.split(",", -1)) {
			getOffsetsForTimes(consumer, topic, partitionNum, startTime);
			getOffsetsForTimes(consumer, topic, partitionNum, endTime);
			consumerForTimes(consumer, topic, startTime, endTime);
		}
	}

	//说明：基于时间戳查询消息，consumer 订阅 topic 的方式必须是 Assign
	public static void consumerForTimes(KafkaConsumer<String, String> consumer, String topic, String startTime, String endTime) {

		try {
			// 获取topic的partition信息
			List<PartitionInfo> partitionInfos = consumer.partitionsFor(topic);
			List<TopicPartition> topicPartitions = new ArrayList<>();

			Map<TopicPartition, Long> timestampsToSearch = new HashMap<>();
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date now = new Date();
			long nowTime = now.getTime();
			LOGGER.info("当前时间: " + df.format(now));
			long fetchDataTime = nowTime - 1000 * 60 * 60;  // 计算30分钟之前的时间戳

			for(PartitionInfo partitionInfo : partitionInfos) {
				topicPartitions.add(new TopicPartition(partitionInfo.topic(), partitionInfo.partition()));
				timestampsToSearch.put(new TopicPartition(partitionInfo.topic(), partitionInfo.partition()), fetchDataTime);
			}

			consumer.assign(topicPartitions);

			// 获取每个partition一个小时之前的偏移量
			Map<TopicPartition, OffsetAndTimestamp> map = consumer.offsetsForTimes(timestampsToSearch);

			OffsetAndTimestamp offsetTimestamp = null;
			System.out.println("开始设置各分区初始偏移量...");
			for(Map.Entry<TopicPartition, OffsetAndTimestamp> entry : map.entrySet()) {
				// 如果设置的查询偏移量的时间点大于最大的索引记录时间，那么value就为空
				offsetTimestamp = entry.getValue();
				if(offsetTimestamp != null) {
					int partition = entry.getKey().partition();
					long timestamp = offsetTimestamp.timestamp();
					long offset = offsetTimestamp.offset();
					LOGGER.info("partition:{},  time:{}, offset:{}", partition, df.format(new Date(timestamp)), offset);
					// 设置读取消息的偏移量
					consumer.seek(entry.getKey(), offset);
				}
			}
			System.out.println("设置各分区初始偏移量结束...");

			while(true) {
				ConsumerRecords<String, String> records = consumer.poll(1000);
				for (ConsumerRecord<String, String> record : records) {
					LOGGER.info("partition:{}, offset:{}, message:{}", record.partition(), record.offset(), record.value());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			consumer.close();
		}
	}

	public static void getOffsetsForTimes(KafkaConsumer<String, String> consumer, String topic, int partitionNum, String startTime) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			long start = 0;
			try {
				start = sdf.parse(startTime).getTime();
			} catch (ParseException e) {
				e.printStackTrace();
			}
			Map<TopicPartition, Long> startMap = new HashMap<>();
			for (int i = 0; i < partitionNum; i++) {
				startMap.put(new TopicPartition(topic, i), start);
			}
			Map<TopicPartition, OffsetAndTimestamp> startOffsetMap = consumer.offsetsForTimes(startMap);
			for (Entry<TopicPartition, OffsetAndTimestamp> entry : startOffsetMap.entrySet()) {
				LOGGER.info("partition:{}, startoffset:{}", entry.getKey().partition(), entry.getValue().offset());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
