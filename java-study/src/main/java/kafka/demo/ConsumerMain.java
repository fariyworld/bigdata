package kafka.demo;

import config.KafkaProperties;
import kafka.SampleKafkaConsumer;

public class ConsumerMain {

	public static void main(String[] args) {
		KafkaProperties.consumerConfigPath = "kafkaConsumerConfig.properties";
		SampleKafkaConsumer kafka_Consumer = new SampleKafkaConsumer("test");
		try {
			kafka_Consumer.execute();
			Thread.sleep(70000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			kafka_Consumer.shutdown();
		}
	}
}
