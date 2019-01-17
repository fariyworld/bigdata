package kafka;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import config.KafkaProperties;

/**
 * kafka消费者不是线程安全的
 */
public class SampleKafkaConsumer {

	private static final Logger LOGGER = LoggerFactory.getLogger(SampleKafkaConsumer.class);
	// 消费者实例
	private KafkaConsumer<String, String> consumer;
	// 线程池
	private ExecutorService executorService;
	
	public SampleKafkaConsumer(String topic) {
		Properties props = new Properties();
		InputStream inStream = null;
		try {
			inStream = this.getClass().getClassLoader().getResourceAsStream(KafkaProperties.consumerConfigPath);
			props.load(inStream);
			consumer = new KafkaConsumer<String, String>(props);
			consumer.subscribe(Arrays.asList(topic));
		} catch (IOException e) {
			LOGGER.error("KafkaConsumer初始化失败:" + e.getMessage(), e);
		} finally {
			if (null != inStream) {
				try {
					inStream.close();
				} catch (IOException e) {
					LOGGER.error("KafkaConsumer初始化失败:" + e.getMessage(), e);
				}
			}
		}
	}
	
	
	public void execute() {
		// 创建固定数量的可复用的线程数，来执行任务。当线程数达到最大核心线程数，则加入队列等待有空闲线程时再执行
		executorService = Executors.newFixedThreadPool(3);
		Executors.newCachedThreadPool();
		while (true) {
			ConsumerRecords<String, String> records = consumer.poll(10);
			if (null != records) {
				executorService.submit(new ConsumerThread(records, consumer));
			}
		}
	}
	

	public void shutdown() {
		try {
			if (consumer != null) {
				consumer.close();
			}
			if (executorService != null) {
				executorService.shutdown();
			}
			if (!executorService.awaitTermination(10, TimeUnit.SECONDS)) {
				System.out.println("Timeout");
			}
		} catch (InterruptedException ignored) {
			Thread.currentThread().interrupt();
		}
	}
}
