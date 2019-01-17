package topology.kafkademo;

import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.StormSubmitter;
import org.apache.storm.generated.AlreadyAliveException;
import org.apache.storm.generated.AuthorizationException;
import org.apache.storm.generated.InvalidTopologyException;
import org.apache.storm.kafka.KafkaSpout;
import org.apache.storm.kafka.SpoutConfig;
import org.apache.storm.kafka.ZkHosts;
import org.apache.storm.kafka.bolt.KafkaBolt;
import org.apache.storm.kafka.bolt.mapper.FieldNameBasedTupleToKafkaMapper;
import org.apache.storm.kafka.bolt.selector.DefaultTopicSelector;
import org.apache.storm.spout.SchemeAsMultiScheme;
import org.apache.storm.topology.TopologyBuilder;
import org.apache.storm.tuple.Fields;

import java.text.ParseException;
import java.util.Properties;

/**
 * description:
 * <br />
 * Created by mace on 2018/11/20 10:25.
 */
public class KafkaTopologyDemo {

    public static void main(String[] args) throws ParseException, InvalidTopologyException, AuthorizationException, AlreadyAliveException {

        String brokerZkStr = "118.25.229.83:2181";
//        String brokerZkPath = "";//默认/brokers
//        ZkHosts zkHosts = new ZkHosts(brokerZkStr, brokerZkPath);
        ZkHosts zkHosts = new ZkHosts(brokerZkStr);
        //消费者的属性信息
        String topicIn = "kafka2oracle";//从哪个topic读取消息
        String zkRoot = "D:\\WebLogs\\zklog";//存储消费者的偏移量记录于zookeeper的哪个路径下
        String clientId = "kafka2oracle";//进度记录的id，想要一个新的Spout读取之前的记录，应把它的id设为跟之前的一样
        //clientId用于指定存放当前topic consumer的offset的位置
        SpoutConfig spoutConfig = new SpoutConfig(zkHosts, topicIn, zkRoot, clientId);
        //规定Spout如何从Kafka消息流中解析所需数据
        spoutConfig.scheme = new SchemeAsMultiScheme(new SpoutMessageScheme());
        spoutConfig.stateUpdateIntervalMs = 2000L;//用于metrics,多久更新一次状态,设置将当前kafka偏移量保存到ZooKeeper的频率
        spoutConfig.ignoreZkOffsets = false;//如果此字段forceFromStart设为true,忽略之前相同id的topology的进度,startOffsetTime决定
//        spoutConfig.startOffsetTime = kafka.api.OffsetRequest.EarliestTime();//默认从最早的消息开始
//        spoutConfig.startOffsetTime = kafka.api.OffsetRequest.LatestTime();//从最新的消息开始，即从队列队伍最末端开始
//        String startOffsetTime = "";//根据时间点
//        spoutConfig.startOffsetTime = new SimpleDateFormat("yyyy.MM.dd-HH:mm:ss").parse(startOffsetTime).getTime();
        //设置生产者的属性信息
        Properties props = new Properties();
        props.put("bootstrap.servers", "118.25.229.83:9092");//用于建立初始连接到kafka集群的"主机/端口对"配置列表
        props.put("acks", "0");//Producer 在确认一个请求发送完成之前需要收到的反馈信息的数量。 这个参数是为了保证发送请求的可靠性
        props.put("key.serializer","org.apache.kafka.common.serialization.StringSerializer");//key 序列化类
        props.put("value.serializer","org.apache.kafka.common.serialization.StringSerializer");//value 序列化类
        String topicOut = "test-producer";//生产到哪个topic
        //storm+topology Config
        Config conf = new Config();
        //不输出调试信息
        conf.setDebug(false);
        //设置worker数量 <= supervisor.slots.ports 数 * supervisor节点数
        conf.setNumWorkers(1);
        //acker任务
        conf.setNumAckers(0);
//        版本不同
//        Map<String, String> map = new HashMap<String, String>();
//        map.put("metadata.broker.list", "192.168.1.216:9092");//配置Kafka broker地址
//        map.put("serializer.class", "kafka.serializer.StringEncoder");//serializer.class为消息的序列化类
//        conf.put("kafka.broker.properties", map);
//        conf.put("topic", "topic2");//配置KafkaBolt生成的topic

        TopologyBuilder builder = new TopologyBuilder();
//        consumer kafka    如果有需要消费多个topic,则需创建多个SpoutConfig
        // spout 并行度
        int KafkaSpout_parallelism = 1;
        builder.setSpout("kafkaSpout", new KafkaSpout(spoutConfig), KafkaSpout_parallelism).setNumTasks(2 * KafkaSpout_parallelism);

//        业务处理 bolt

//        producer to kafka
        builder.setBolt("kafkaBolt", new KafkaBolt<String,String>()
                .withProducerProperties(props)
                .withTopicSelector(new DefaultTopicSelector(topicOut))
                .withTupleToKafkaMapper(new FieldNameBasedTupleToKafkaMapper())).fieldsGrouping("kafkaSpout", new Fields("msg"));

        if(args !=null && args.length > 0){
            // 集群模式
            StormSubmitter.submitTopology(args[0], conf, builder.createTopology());
        }else{
            // 本地模式
            LocalCluster cluster = new LocalCluster();
            cluster.submitTopology("topologyName", conf, builder.createTopology());
//			Utils.sleep(30000);
//			cluster.killTopology("topologyName");
//			cluster.shutdown();
        }
    }
}
