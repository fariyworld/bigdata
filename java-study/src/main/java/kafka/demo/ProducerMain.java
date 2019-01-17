package kafka.demo;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import config.KafkaProperties;

public class ProducerMain {

	public static void main(String[] args) throws Exception {
		KafkaProperties.producerConfigPath = "kafkaProducerConfig.properties";
		ExecutorService executor = Executors.newFixedThreadPool(3);
		for (int i = 1; i <= 10; i++) {
			Thread.sleep(1000);
			executor.submit(new HandlerProducer("" + i));
		}
	}
}
