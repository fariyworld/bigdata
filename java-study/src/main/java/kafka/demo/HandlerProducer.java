package kafka.demo;

import kafka.KafkaProducerSingleton;

/**
 * 生产者线程
 */
public class HandlerProducer implements Runnable {

	private String message;
	 
	public HandlerProducer(String message) {
		this.message = message;
	}
	
	@Override
	public void run() {
		KafkaProducerSingleton kafkaProducerSingleton = KafkaProducerSingleton.getInstance();
		kafkaProducerSingleton.init("test", 0, 1);
		System.out.println("当前线程:" + Thread.currentThread().getName() + ",获取的kafka实例:" + kafkaProducerSingleton + ",待发送的消息:" + message);
		kafkaProducerSingleton.sendMessageToRandomPartition(message);
	}
}
