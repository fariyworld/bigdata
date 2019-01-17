package test;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.junit.Test;

import config.KafkaProperties;
import dataprocess.utils.XmlParse;
import entity.Person;

public class App01 {

	@Test
	public void test01() {
		
		Person person1 = new Person();
		person1.age = 16;
		person1.name = "mace";
//		person1.setAge(16);
//		person1.setName("mace");
		
		Person person2 = new Person();
		person2.age = 16;
		person2.name = "mace";
//		person2.setAge(17);
//		person2.setName("mace");
		
		System.out.println(person1.equals(person2));
	}
	
	@Test
	public void test02() {
		
		Person person1 = new ChinaPerson();
		person1.setAge(16);
		person1.setName("mace");
		
		Person person2 = new ChinaPerson();
		person2.setAge(17);
		person2.setName("mace");
		
		System.out.println(person1.equals(person2));
	}
	
	/**
	 * 测试List的增删改查、排序
	 */
	@Test
	public void test03(){
		
		List<Integer> list = new ArrayList<>();
		list.add(1);
		list.add(3);
		list.add(4);
		list.add(2);
		
		System.out.println(list.get(0));
	}
	
	
	@Test
	public void test04(){
		
		String path = "FDD_LTE_MRE_HUAWEI_552961_20180301170000.zip";
		
		System.out.println(path.endsWith(".zip"));
		
		System.out.println(System.getProperty("user.home"));
	}
	
	@Test
	public void test05(){
		
		String string = "a b";
		System.out.println(string.split("\\s", -1).length);
	}
	
	
	@Test
	public void test06(){
		
		System.out.println(Double.parseDouble("-Infinity") == Double.NEGATIVE_INFINITY);
	}
	
	@Test
	public void test07(){
		
		String empty = null;
		System.out.println(null == empty);
	}
	
	
	@Test
	public void test08(){
		
		flag :for(int i = 0; i < 10; i++){
			
			for(int j = 0; j < 10; j ++){
				
				if (j == 2)
					continue flag;
				else
					System.out.println(i+" _ "+j);
			}
		}
	}
	
	
	@Test
	public void test09(){
		
		Map<String, Integer> map = new HashMap<>();
		map.put("1", 1);
		map.put(null, 2);
		
		System.out.println(map.containsKey(null));
		System.out.println(map.remove("2"));
		System.out.println(map.remove(null));
		System.out.println(map.containsKey(null));
	}
	
	
	@Test
	public void test10(){
		
		String val = "2|a7ce000407c3c1bf|4613393904421980516|862263033691717|17760173817|6|510000|3|1540881273035|1540881273038|0|-1|-1|1|197188314|460-11-24833-6-3867414545|-1|FF.FF.FF.FF|FFFF:FFFF:FFFF:FFFF|1|6.52.238.130|6.53.207.1|36412|36412|4601110479|460110042859826|-1|FFFF|FFFFFFFF|FFFF|FF|FFFFFFFF|24833|6|3867414545|FFFFFFFF|172316|167421|50|1|5|1|9|1|335038614|-1|-1|-1";
		
		System.out.println(val.split("\\|", -1).length);
	}
	
	
	@Test
	public void test11(){
		
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			System.out.println((System.currentTimeMillis()) - formatter.parse("2018-10-31 17:21:55").getTime() >= 3600);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
//	@Before
	public void setL0(){
		System.out.println(Person.L0);
		Person.setL0(105D);
	}
	
	@Test
	public void test12(){
		System.out.println(Person.L0);
	}
	
	
	@Test
	public void test13(){
		
		LinkedList<Integer> linkedList = new LinkedList<>();
		linkedList.add(2);
		linkedList.add(1);
		linkedList.add(3);
		
		for (Integer integer : linkedList) {
			System.out.println(integer);
		}
		
		System.out.println(linkedList.removeLast());
		
		for (Integer integer : linkedList) {
			System.out.println(integer);
		}
	}
	
	/**
	 * 计算指定consumer group在__consumer_offsets topic中分区信息
	 * Kafka会使用下面公式计算该group位移保存在__consumer_offsets的哪个分区上：
	 * Math.abs(groupID.hashCode()) % numPartitions
	 */
	@Test
	public void test14(){
		// 31
		System.out.println(Math.abs("test-consumer-group".hashCode()) % 50);
	}
	
	
	@Test
	public void test15(){
		System.out.println("aredid_" + "".hashCode()%110);
		System.out.println("aredid_" + "757#3".hashCode()%110);
		System.out.println("aredid_" + "757#10".hashCode()%110);
		System.out.println("aredid_" + "757#3".hashCode()%110);
		System.out.println("aredid_" + "756#3".hashCode()%110);
		System.out.println("aredid_" + "756#5".hashCode()%110);
	}
	
	
	@Test
	public void test16(){
		
		System.out.println(561908*256+2);
	}
	
	@Test
	public void test17(){
		
		if(true && sayhello()){
			System.out.println(",world");
		}
	}
	
	public boolean sayhello(){
		System.out.print("hello");
		return true;
	}
	
	
	@Test
	public void test18(){
		
		XmlParse.readXml("D:\\Workspaces\\eclipse_4.5_workspace\\04-cqlbs\\doc\\研发\\重庆实时场景营销交接文档\\scene.xml");
	}
	
	/**
	 * kafka 消费者
	 */
	@Test
	public void test19(){
		KafkaProperties.consumerConfigPath = "kafkaConfig.properties";
		Properties props = new Properties(); 
		try {
			props.load(this.getClass().getClassLoader().getResourceAsStream(KafkaProperties.consumerConfigPath));
			props.put("client.id", "win10-test-consumer-1");
			Thread.sleep(500);
		} catch (Exception e) {
			e.printStackTrace();
		}
		@SuppressWarnings("resource")
		KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
		// 配置topic
		consumer.subscribe(Arrays.asList("test"));
		while (true) {
			ConsumerRecords<String, String> records = consumer.poll(1000);
			for (ConsumerRecord<String, String> record : records) {
				System.out.println(String.format("timestamp == %d, offset == %d, partition == %d, key == %s, value == %s", record.timestamp(), record.offset(), record.partition(), record.key(), record.value()));
			}
		}
	}
	
}


