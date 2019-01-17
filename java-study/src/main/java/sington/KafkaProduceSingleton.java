package sington;

import kafka.KafkaProducerSingleton;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * description: KafkaProducer是线程安全对象 建议KafkaProducer采用单例模式,多个线程共享一个实例
 * <br />
 * Created by mace on 2019/1/11 9:36.
 */
public class KafkaProduceSingleton {

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaProducerSingleton.class);
    private volatile static KafkaProducer<String, String> kafkaProducer;


}
