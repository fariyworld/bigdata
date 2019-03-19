package topology.kafkaclient;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.storm.kafka.spout.Func;
import org.apache.storm.kafka.spout.KafkaTuple;
import org.apache.storm.kafka.spout.RecordTranslator;
import org.apache.storm.tuple.Fields;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * description:
 * <br />
 * Created by mace on 2019/3/19 10:24.
 */
public class MyRecordTranslator<K, V> implements RecordTranslator<K, V> {

    private static final Logger LOGGER = LoggerFactory.getLogger(MyRecordTranslator.class);

    private static final long serialVersionUID = 7205001271457667146L;
    private final Fields fields;
    private final Func<ConsumerRecord<K, V>, List<Object>> func;
    private final String stream;

    public MyRecordTranslator(Func<ConsumerRecord<K, V>, List<Object>> func, Fields fields) {
        this(func, fields, "default");
    }

    public MyRecordTranslator(Func<ConsumerRecord<K, V>, List<Object>> func, Fields fields, String stream) {
        this.func = func;
        this.fields = fields;
        this.stream = stream;
    }

    public List<Object> apply(ConsumerRecord<K, V> record) {
        List<Object> apply = this.func.apply(record);
        if(apply == null){
            LOGGER.info("异常数据过滤...");
            return null;
        }
        KafkaTuple ret = new KafkaTuple();
        ret.addAll((Collection)apply);
        return ret.routedTo(this.stream);
    }

    public Fields getFieldsFor(String stream) {
        return this.fields;
    }

    public List<String> streams() {
        return Arrays.asList(this.stream);
    }
}
