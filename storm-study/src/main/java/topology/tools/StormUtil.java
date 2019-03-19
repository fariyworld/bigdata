package topology.tools;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.storm.Constants;
import org.apache.storm.kafka.spout.ByTopicRecordTranslator;
import org.apache.storm.kafka.spout.Func;
import org.apache.storm.kafka.spout.KafkaSpout;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

public class StormUtil {

	private static final Logger LOGGER = LoggerFactory.getLogger(StormUtil.class);
	/**
	 * 根据传送过来的Tuple，判断本Tuple是否是tickTuple 如果是tickTuple，则触发相应的动作
	 * @param tuple
	 * @return
	 */
	public static boolean isTickTuple(Tuple tuple){
		return tuple.getSourceComponent().equals(Constants.SYSTEM_COMPONENT_ID) && tuple.getSourceStreamId().equals(Constants.SYSTEM_TICK_STREAM_ID);
	}


	public static KafkaSpout<String, String> getKafkaSpoutInstance() {
		ByTopicRecordTranslator<String, String> recordTranslator = new ByTopicRecordTranslator<>(
				// ConsumerRecord<String, String>
				(record) -> new Values(record.value()), new Fields("value")
		);
		return null;
	}
}
