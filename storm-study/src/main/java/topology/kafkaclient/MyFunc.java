package topology.kafkaclient;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.storm.kafka.spout.Func;
import org.apache.storm.tuple.Values;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * description:
 * <br />
 * Created by mace on 2019/3/19 11:36.
 */
public class MyFunc<K, V> implements Func<ConsumerRecord<K, V>, List<Object>> {

    private static final long serialVersionUID = -1841991130185276629L;
    private static final Logger LOGGER = LoggerFactory.getLogger(MyFunc.class);

    @Override
    public List<Object> apply(ConsumerRecord<K, V> record) {
        if (record.value().equals("a") ){
            return null;
        } else {
            LOGGER.info("只处理不为a的数据");
            return new Values(record.topic(), record.partition(), record.value());
        }
    }
}
