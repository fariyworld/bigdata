package topology.kafkaclient;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.kafka.spout.*;
import org.apache.storm.kafka.spout.KafkaSpoutRetryExponentialBackoff.TimeInterval;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.TopologyBuilder;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.apache.storm.kafka.spout.KafkaSpoutConfig.FirstPollOffsetStrategy.EARLIEST;

/**
 * description: 新版 storm-kafka-client
 * <br />
 * Created by mace on 2019/1/14 15:45.
 */
public class NewKafkaClientTopo {

    private static final String TOPIC_Consumer = "kafka2oracle";
    private static final String TOPIC_Producer = "test-trident-1";
    private static final String KAFKA_LOCAL_BROKER = "118.25.229.83:9092";

    private static Func<ConsumerRecord<String, String>, List<Object>> JUST_VALUE_FUNC = new JustValueFunc();

    protected static KafkaSpoutConfig<String,String> newKafkaSpoutConfig() {
        return KafkaSpoutConfig.builder(KAFKA_LOCAL_BROKER, TOPIC_Consumer)
                .setGroupId("kafkaSpoutTestGroup_" + System.nanoTime())//消费者组
                .setMaxPartitionFectchBytes(200)//每个分区服务器将返回的最大数据量
                .setRecordTranslator(JUST_VALUE_FUNC, new Fields("msg"))//自定义消息处理逻辑和声明流格式
                .setRetry(newRetryService())//Storm对失败的消息如何处理
                .setOffsetCommitPeriodMs(1_000)//设置多长时间向kafka提交一次offset
                .setFirstPollOffsetStrategy(EARLIEST)//消费的初始偏移量
                .setMaxUncommittedOffsets(250)//控制在可以进行另一次轮询之前有多少偏移可以等待提交 允许未提交的offset数量，达到这个值后将提交offset
                .build();
    }


    /**
     * Needs to be serializable
     * 消息处理逻辑
     */
    private static class JustValueFunc implements Func<ConsumerRecord<String, String>, List<Object>>, Serializable {
        @Override
        public List<Object> apply(ConsumerRecord<String, String> record) {
            System.out.printf("partition=%d, offset=%d\n",record.partition(), record.offset());
            return new Values(record.value());
        }
    }

    /**
     * description: Storm对失败的消息如何处理
     * <br /><br />
     * create by mace on 2019/1/14 16:09.
     * @param
     * @return: org.apache.storm.kafka.spout.KafkaSpoutRetryService
     */
    protected static KafkaSpoutRetryService newRetryService() {
        return new KafkaSpoutRetryExponentialBackoff(new TimeInterval(500L, TimeUnit.MICROSECONDS),
                TimeInterval.milliSeconds(2), Integer.MAX_VALUE, TimeInterval.seconds(10));
    }

    static KafkaSpout newKafkaSpout(){
        return new KafkaSpout<>(newKafkaSpoutConfig());
    }

    static class PrintBolt extends BaseRichBolt {
        private OutputCollector _collector;
        @Override
        public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
            _collector = collector;
        }

        @Override
        public void execute(Tuple input) {
            System.out.println(input.getStringByField("msg"));
        }

        @Override
        public void declareOutputFields(OutputFieldsDeclarer declarer) {

        }
    }

    public static void main(String[] args) {
//        Config tpConf = LocalSubmitter.defaultConfig(true);
        LocalCluster local = new LocalCluster();
        Config config = new Config();
        //关闭DEBUG日志模式
        config.setDebug(false);
        //去掉ack
        config.setNumAckers(0);
        //worker数量
        config.setNumWorkers(1);
        TopologyBuilder builder = new TopologyBuilder();
        builder.setSpout("kafkaSpout", newKafkaSpout(), 2).setNumTasks(3);//2个并行,3个task
        builder.setBolt("printBolt", new PrintBolt()).fieldsGrouping("kafkaSpout", new Fields("msg"));
        local.submitTopology("test", config, builder.createTopology());
    }
}
