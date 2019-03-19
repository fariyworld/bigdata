package topology.kafkaclient;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.StormSubmitter;
import org.apache.storm.kafka.bolt.KafkaBolt;
import org.apache.storm.kafka.bolt.mapper.FieldNameBasedTupleToKafkaMapper;
import org.apache.storm.kafka.bolt.selector.DefaultTopicSelector;
import org.apache.storm.kafka.spout.*;
import org.apache.storm.topology.TopologyBuilder;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import topology.kafkademo.SimpleBolt;

import java.util.Properties;

import static org.apache.storm.kafka.spout.KafkaSpoutConfig.FirstPollOffsetStrategy.UNCOMMITTED_LATEST;
import static org.apache.storm.kafka.spout.KafkaSpoutRetryExponentialBackoff.TimeInterval;

/**
 * description:
 * <br />
 * Created by mace on 2019/3/5 11:01.
 */
public class NewKafkaTopo {

    private static final Logger LOGGER = LoggerFactory.getLogger(NewKafkaTopo.class);

    /**
     * description: 如果某条消息处理失败了，就会重试一定的次数，并且每次重试的时间按照指数时间增加。
     * 当然如果超过了最大的重试次数，KafkaSpou默认会将它ACK掉 这条消息如果多次失败，也会被标记为处理成功了
     * private long nextTime(KafkaSpoutMessageId msgId) {
     * 	long currentTimeNanos = Time.nanoTime();
     * 	long nextTimeNanos = msgId.numFails() == 1 ? currentTimeNanos + this.initialDelay.lengthNanos : currentTimeNanos + this.delayPeriod.lengthNanos * (long)Math.pow(2.0D, (double)(msgId.numFails() - 1));
     * 	return Math.min(nextTimeNanos, currentTimeNanos + this.maxDelay.lengthNanos);
     * }
     * <br /><br />
     * create by mace on 2019/3/18 10:31.
     * @param
     * @return: org.apache.storm.kafka.spout.KafkaSpoutRetryService
     */
    protected static KafkaSpoutRetryService getRetryService() {
        return new KafkaSpoutRetryExponentialBackoff(
                //初始延迟
                TimeInterval.microSeconds(500L),
                //延迟周期
                TimeInterval.milliSeconds(2L),
                //重试次数
                3,
                //最大延迟时间
                TimeInterval.seconds(10L)
        );
    }

    public static void main(String[] args) throws Exception {
        TopologyBuilder builder = new TopologyBuilder();

//        ByTopicRecordTranslator<String, String> recordTranslator = new ByTopicRecordTranslator<>(
//                // ConsumerRecord<String, String>
//                (record) -> new Values(record.topic(), record.partition(), record.value()), new Fields("topic", "partition", "value")
//        );

        MyRecordTranslator<String, String> myRecordTranslator = new MyRecordTranslator<>(
                (record) -> new MyFunc<String, String>().apply(record), new Fields("topic", "partition", "value")
        );

//        ByTopicRecordTranslator<String, String> trans = new ByTopicRecordTranslator<>(
//                (r) -> new Values(r.topic(), r.partition(), r.offset(), r.key(), r.value()),
//                new Fields("topic", "partition", "offset", "key", "value"), "kafka2oracle");

        //rebalance.max.retries * rebalance.backoff.ms > zookeeper.session.timeout.ms.
        //默认 2000 * 4 > 6000
        KafkaSpoutConfig<String,String> kafkaSpoutConfig = KafkaSpoutConfig
                //bootstrapServers 以及 topic
                .builder("bigdata:9092", "kafka2oracle", "test")
                //设置group.id
                .setGroupId("test-kafka-new")
                //设置消费的初始偏移量
                .setFirstPollOffsetStrategy(UNCOMMITTED_LATEST)
                //手动提交offset
                .setProp(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false")
                //设置多长时间向kafka提交一次offset  配合 enable.auto.commit=false 使用
                .setOffsetCommitPeriodMs(1_000)
                //控制在可以进行另一次轮询之前有多少偏移可以等待提交 允许未提交的offset数量，达到这个值后将提交offset
                .setMaxUncommittedOffsets(1_000)
                //key 反序列化类
                .setProp(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getCanonicalName())
                //value 反序列化类
                .setProp(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,StringDeserializer.class.getCanonicalName())
                //自定义消息处理逻辑和声明流格式
                .setRecordTranslator(myRecordTranslator)
                //Storm对失败的消息如何处理
                .setRetry(getRetryService())
                //是否发送为空的tuple
                .setEmitNullTuples(false)
                .build();
        //1. spout 消费kafka消息
        //3个线程executor 3个task 消费三个分区
        //如果为多topic executor 数量为topic分区最大值
        builder.setSpout("KafkaSpout_kafka2oracle", new KafkaSpout<>(kafkaSpoutConfig), 3).setNumTasks(3);

        //2. 业务处理 bolt
        builder.setBolt("printBolt", new SimpleBolt(), 1).setNumTasks(1)
                .localOrShuffleGrouping("KafkaSpout_kafka2oracle");
//                .fieldsGrouping("KafkaSpout_kafka2oracle", new Fields("value"));

        //3. storm结果生产到kafka
        // 3.1 设置kafka producer的配置
        Properties producerProperties = new Properties();
        producerProperties.put("bootstrap.servers", "bigdata:9092");
        // leader节点会将记录写入本地日志，并且在所有 follower 节点反馈之前就先确认成功。
        // 在这种情况下，如果 leader 节点在接收记录之后，并且在 follower 节点复制数据完成之前产生错误，则这条记录会丢失
        producerProperties.put("acks", "1");
        producerProperties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        producerProperties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        // 3.2 给KafkaBolt配置topic及前置tuple消息到kafka的mapping关系
        // 当发送至kafka的消息没有key时, 前置tuple应设置key==null
        // 默认异步发送且发送成功后调用ack, 失败 fail
        KafkaBolt kafkaBolt = new KafkaBolt<>()
                .withProducerProperties(producerProperties)
                .withTopicSelector(new DefaultTopicSelector("storm-kafka-bolt-test"))
                // 默认的Field是"key" "message"
                .withTupleToKafkaMapper(new FieldNameBasedTupleToKafkaMapper<>());

        // fireAndForget kafkaBolt.setFireAndForget(true)
        // 发送消息后不需要关心是否发送成功。因为Kafka是高可用的，而且生产者会自动重新发送，所以大多数情况都会成功，但是有时也会失败。
        // 异步发送 kafkaBolt.setAsync();


        builder.setBolt("forwardToKafka", kafkaBolt, 2).setNumTasks(2).fieldsGrouping("printBolt", new Fields("message"));

        Config config = new Config();
        config.setNumWorkers(1);
        config.setNumAckers(1);
        config.setDebug(false);

        if(args !=null && args.length > 0){
            // 集群模式
            StormSubmitter.submitTopology(args[0], config, builder.createTopology());
        }else{
            // 本地模式
            LocalCluster cluster = new LocalCluster();
            cluster.submitTopology("local", config, builder.createTopology());
//			Utils.sleep(30000);
//			cluster.killTopology("topologyName");
//			cluster.shutdown();
        }
    }
}
