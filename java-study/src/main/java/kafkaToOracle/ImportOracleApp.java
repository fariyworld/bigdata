package kafkaToOracle;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//import org.apache.log4j.Logger;

import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import kafka.serializer.StringDecoder;
import kafka.utils.VerifiableProperties;

public class ImportOracleApp {
	
	private static final Logger LOG = LoggerFactory.getLogger(ImportOracleApp.class);
	
	public static final String drive = "oracle.jdbc.driver.OracleDriver";
	public static final String url = "jdbc:oracle:thin:@//118.25.229.83:1521/XE";
	public static final String username = "mace";
	public static final String password = "Liuye0425";

	@SuppressWarnings("deprecation")
	public static void main(String[] args) throws Exception {
		
		//1. 从redis读取场景短信发送文本
		Map<String, String> senceMsgMap = new HashMap<>();
		senceMsgMap.put("scene001", "欢迎进入陕西神木");
		
		//2. 读取oracle、kafka连接配置
    	// kafka配置文件
        Properties props = new Properties();
        // zookeeper路径
        props.put("zookeeper.connect", "118.25.229.83:2181");
        //group 代表一个消费组
        String topic = "test";
        props.put("group.id", topic);
        // zk连接超时
        props.put("zookeeper.session.timeout.ms", "4000");
        props.put("zookeeper.sync.time.ms", "200");
        props.put("auto.commit.interval.ms", "1000");
        // 序列化类
        props.put("serializer.class", "kafka.serializer.StringEncoder");
        //初始化consumer配置
        ConsumerConfig config = new ConsumerConfig(props);
		ConsumerConnector consumer = Consumer.createJavaConsumerConnector(config);
		Map<String, Integer> topicCountMap = new HashMap<String, Integer>();
		topicCountMap.put(topic, 1);
        StringDecoder keyDecoder = new StringDecoder(new VerifiableProperties());
        StringDecoder valueDecoder = new StringDecoder(new VerifiableProperties());
        //开始消费
        Map<String, List<KafkaStream<String, String>>> consumerMap = consumer.createMessageStreams(topicCountMap,keyDecoder,valueDecoder);
        KafkaStream<String, String> stream = consumerMap.get(topic).get(0);
        ConsumerIterator<String, String> it = stream.iterator();
		
        while (it.hasNext()){
        	String message = it.next().message();
        	LOG.info("to be inserted msg: {}", message);
        	insertData2Oracle(message);
        }
	}
	
	public static void insertData2Oracle(String message) throws Exception{
		
		Connection conn = null;
		Statement stmt= null;
		Class.forName(drive);
		conn =DriverManager.getConnection(url,username,password);
		stmt = conn.createStatement();
		int result = stmt.executeUpdate("insert into mace.lbs_msg t values (mace.seq_mace_lbsmsg_id.nextval, '18500348251', '" + message + "')");
		if( result == 1 ){
			LOG.info("insert data success");
		}else{
			LOG.error("insert data failed");
		}
		stmt.close();
        conn.close();
	}
}
