package kafka.count;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndTimestamp;
import org.apache.kafka.common.TopicPartition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 统计指定时间段内 kafka消息 用户数
 */
public class KafkaMessageCount {

	private static final Logger LOGGER = LoggerFactory.getLogger(KafkaMessageCount.class);

	public static void main(String[] args) {

		String bootstrapServers = args[0];
		String topics = args[1];// 用[,]分隔
		int partitionNum = Integer.valueOf(args[2]);
		String groupId = args[3];
		String startTime = args[4];// 20181116170000
		String endTime = args[5];

		for (String topic : topics.split(",", -1)) {
			getOffsetsForTimes(bootstrapServers, topic, partitionNum, groupId, startTime, endTime);
		}
	}

	public static void getOffsetsForTimes(String bootstrapServers, String topic, int partitionNum, String groupId,
			String startTime, String endTime) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		long start = 0;
		long end = 0;
		try {
			start = sdf.parse(startTime).getTime();
			end = sdf.parse(endTime).getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}

		Properties props = new Properties();
		props.put("bootstrap.servers", bootstrapServers);
		props.put("group.id", groupId);
		props.put("enable.auto.commit", "false");
		props.put("auto.commit.interval.ms", "1000");
		props.put("session.timeout.ms", "30000");
		props.put("auto.offset.reset", "earliest");
		props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

		KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
		
		Map<TopicPartition, Long> startMap = new HashMap<>();
		Map<TopicPartition, Long> endMap = new HashMap<>();
		long startoffset = 0;
		long endoffset = 0;

		for (int i = 0; i < partitionNum; i++) {
			startMap.put(new TopicPartition(topic, i), start);
			endMap.put(new TopicPartition(topic, i), end);
		}

		Map<TopicPartition, OffsetAndTimestamp> startOffsetMap = consumer.offsetsForTimes(startMap);
		Map<TopicPartition, OffsetAndTimestamp> endOffsetMap = consumer.offsetsForTimes(endMap);

		for (Entry<TopicPartition, OffsetAndTimestamp> entry : startOffsetMap.entrySet()) {
			startoffset += entry.getValue() == null ? 0 : entry.getValue().offset();
		}
		for (Entry<TopicPartition, OffsetAndTimestamp> entry : endOffsetMap.entrySet()) {
			endoffset += entry.getValue() == null ? 0 : entry.getValue().offset();
		}
		LOGGER.info(topic + " offsets:" + (endoffset - startoffset));
	}

}
