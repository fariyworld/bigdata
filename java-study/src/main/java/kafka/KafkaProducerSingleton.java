package kafka;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Random;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import config.KafkaProperties;

/**
 * KafkaProducer是线程安全对象 建议KafkaProducer采用单例模式,多个线程共享一个实例
 */
public class KafkaProducerSingleton {

	private static final Logger LOGGER = LoggerFactory.getLogger(KafkaProducerSingleton.class);
	private static KafkaProducer<String, String> kafkaProducer;
	private String topic;
	private int retry;
	private int partitionNum;
	private Random random = new Random();

	// 构造器私有化
	private KafkaProducerSingleton() {

	}

	// 静态内部类
	private static class LazyHandler {
		private static final KafkaProducerSingleton INSTANCE = new KafkaProducerSingleton();
	}

	// 单例模式,kafkaProducer是线程安全的,可以多线程共享一个实例
	public static final KafkaProducerSingleton getInstance() {
		return LazyHandler.INSTANCE;
	}

	/**
	 * 初始化kafka生产者
	 * @param topic			生产至指定topic
	 * @param retry			重发次数
	 * @param partitionNum	分区数
	 */
	public void init(String topic, int retry, int partitionNum) {
		this.topic = topic;
		this.retry = retry;
		this.partitionNum = partitionNum;
		if (null == kafkaProducer) {
			Properties props = new Properties();
			InputStream inStream = null;
			try {
				inStream = this.getClass().getClassLoader().getResourceAsStream(KafkaProperties.producerConfigPath);
				props.load(inStream);
				kafkaProducer = new KafkaProducer<String, String>(props);
			} catch (IOException e) {
				LOGGER.error("kafkaProducer初始化失败:" + e.getMessage(), e);
			} finally {
				if (null != inStream) {
					try {
						inStream.close();
					} catch (IOException e) {
						LOGGER.error("kafkaProducer初始化失败:" + e.getMessage(), e);
					}
				}
			}
		}
	}
	
	
	/**
	 * 随机发送消息至某个分区
	 * 如果发送失败则尝试重发,重发次数为配置的retry
	 * @param message	待发送消息
	 */
	public void sendMessageToRandomPartition(final String message){
		/**
		 * 1、如果指定了某个分区,会只讲消息发到这个分区上 
		 * 2、如果同时指定了某个分区和key,则也会将消息发送到指定分区上,key不起作用
		 * 3、如果没有指定分区和key,那么将会随机发送到topic的分区中 
		 * 4、如果指定了key,那么将会以hash<key>的方式发送到分区中
		 */
		ProducerRecord<String, String> record = new ProducerRecord<String, String>(this.topic, this.random.nextInt(this.partitionNum), "", message);
		// send方法是异步的,添加消息到缓存区等待发送,并立即返回，这使生产者通过批量发送消息来提高效率
		// kafka生产者是线程安全的,可以单实例发送消息
		kafkaProducer.send(record, new Callback() {
			public void onCompletion(RecordMetadata recordMetadata,
					Exception exception) {
				if (null != exception) {
					LOGGER.error("kafka发送消息失败:" + exception.getMessage(), exception);
					retryKakfaMessage(message);
				}
			}
		});
	}
	
	
	/**
	 * 发送消息至指定分区
	 * 如果发送失败则尝试重发,重发次数为配置的retry
	 * @param message		待发送消息
	 * @param partition		指定分区
	 */
	public void sendMessageToDestPartition(final String message, final int partition){
		/**
		 * 1、如果指定了某个分区,会只讲消息发到这个分区上 
		 * 2、如果同时指定了某个分区和key,则也会将消息发送到指定分区上,key不起作用
		 * 3、如果没有指定分区和key,那么将会随机发送到topic的分区中 
		 * 4、如果指定了key,那么将会以hash<key>的方式发送到分区中
		 */
		ProducerRecord<String, String> record = new ProducerRecord<String, String>(this.topic, partition, "", message);
		// send方法是异步的,添加消息到缓存区等待发送,并立即返回，这使生产者通过批量发送消息来提高效率
		// kafka生产者是线程安全的,可以单实例发送消息
		kafkaProducer.send(record, new Callback() {
			public void onCompletion(RecordMetadata recordMetadata,
					Exception exception) {
				if (null != exception) {
					LOGGER.error("kafka发送消息失败:" + exception.getMessage(), exception);
					retryKakfaMessageToDestPartition(message, partition);
				}
			}
		});
	}
	

	/**
	 * 当kafka消息发送失败后,重试发送至随机分区
	 * @param retryMessage	待重发的消息
	 */
	private void retryKakfaMessage(final String retryMessage) {
		ProducerRecord<String, String> record = new ProducerRecord<String, String>(topic, random.nextInt(this.partitionNum), "", retryMessage);
		for (int i = 1; i <= retry; i++) {
			try {
				kafkaProducer.send(record);
				return;
			} catch (Exception e) {
				LOGGER.error("kafka发送消息失败:" + e.getMessage(), e);
//				retryKakfaMessage(retryMessage);
			}
		}
	}
	
	/**
	 * 当kafka消息发送失败后,重试发送至随机分区
	 * @param retryMessage	待重发的消息
	 */
	private void retryKakfaMessageToDestPartition(final String retryMessage, final int partition) {
		ProducerRecord<String, String> record = new ProducerRecord<String, String>(topic, partition, "", retryMessage);
		for (int i = 1; i <= retry; i++) {
			try {
				kafkaProducer.send(record);
				return;
			} catch (Exception e) {
				LOGGER.error("kafka发送消息失败:" + e.getMessage(), e);
				retryKakfaMessageToDestPartition(retryMessage, partition);
			}
		}
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public int getRetry() {
		return retry;
	}

	public void setRetry(int retry) {
		this.retry = retry;
	}

	public int getPartitionNum() {
		return partitionNum;
	}

	public void setPartitionNum(int partitionNum) {
		this.partitionNum = partitionNum;
	}
	
	
}
