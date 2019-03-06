package topology.kafkaclient;

import org.apache.kafka.clients.consumer.ConsumerConfig;
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
import topology.kafkademo.SimpleBolt;

import java.util.Properties;

import static org.apache.storm.kafka.spout.KafkaSpoutConfig.FirstPollOffsetStrategy.*;
import static org.apache.storm.kafka.spout.KafkaSpoutRetryExponentialBackoff.TimeInterval;

/**
 * description:
 * <br />
 * Created by mace on 2019/3/5 11:01.
 */
public class NewKafkaTopo {

    protected static KafkaSpoutRetryService getRetryService() {
        return new KafkaSpoutRetryExponentialBackoff(
                TimeInterval.microSeconds(500),
                TimeInterval.milliSeconds(2),
                Integer.MAX_VALUE,
                TimeInterval.seconds(10)
        );
    }

    public static void main(String[] args) throws Exception {
        TopologyBuilder builder = new TopologyBuilder();
        ByTopicRecordTranslator<String, String> recordTranslator = new ByTopicRecordTranslator<>(
                // ConsumerRecord<String, String>
                (r) -> new Values(r.topic(), r.partition(), r.value()),new Fields("topic", "partition", "value")
        );

//        ByTopicRecordTranslator<String, String> trans = new ByTopicRecordTranslator<>(
//                (r) -> new Values(r.topic(), r.partition(), r.offset(), r.key(), r.value()),
//                new Fields("topic", "partition", "offset", "key", "value"), "kafka2oracle");

        KafkaSpoutConfig<String,String> kafkaSpoutConfig = KafkaSpoutConfig
                //bootstrapServers 以及 topic
                .builder("bigdata:9092", "kafka2oracle", "test")
                //设置group.id
                .setProp(ConsumerConfig.GROUP_ID_CONFIG, "test-kafka-new")
                //Storm对失败的消息如何处理
                .setRetry(getRetryService())
                //设置消费的初始偏移量
                .setFirstPollOffsetStrategy(UNCOMMITTED_EARLIEST)
                //设置多长时间向kafka提交一次offset
                .setOffsetCommitPeriodMs(10_000)
                //控制在可以进行另一次轮询之前有多少偏移可以等待提交 允许未提交的offset数量，达到这个值后将提交offset
                .setMaxUncommittedOffsets(1000)
                //自定义消息处理逻辑和声明流格式
                .setRecordTranslator(recordTranslator)
                //Storm对失败的消息如何处理
                .build();
        //1. spout 消费kafka消息
        //3个线程executor 3个task 消费三个分区
        //如果为多topic executor 数量为topic分区最大值
        builder.setSpout("KafkaSpout_kafka2oracle", new KafkaSpout<>(kafkaSpoutConfig), 4).setNumTasks(4);

        //2. 业务处理 bolt
        builder.setBolt("printBolt", new SimpleBolt(), 1).setNumTasks(1)
                .localOrShuffleGrouping("KafkaSpout_kafka2oracle");
//                .fieldsGrouping("KafkaSpout_kafka2oracle", new Fields("value"));

        //3. storm结果生产到kafka
        // 3.1 设置kafka producer的配置
        Properties producerProperties = new Properties();
        producerProperties.put("bootstrap.servers", "bigdata:9092");
        producerProperties.put("acks", "1");
        producerProperties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        producerProperties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        // 3.2 给KafkaBolt配置topic及前置tuple消息到kafka的mapping关系
        // 当发送至kafka的消息没有key时key==null
        KafkaBolt kafkaBolt = new KafkaBolt<>()
                .withProducerProperties(producerProperties)
                .withTopicSelector(new DefaultTopicSelector("storm-kafka-bolt-test"))
                // 默认的Field是"key" "message"
                .withTupleToKafkaMapper(new FieldNameBasedTupleToKafkaMapper<>());

        builder.setBolt("forwardToKafka", kafkaBolt, 2).setNumTasks(2).fieldsGrouping("printBolt", new Fields("message"));

        Config config = new Config();
        config.setNumWorkers(1);
        config.setNumAckers(0);
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
