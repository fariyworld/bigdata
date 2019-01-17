package topology.tools;

import java.util.Map;
import java.util.Properties;

import org.apache.storm.Constants;
import org.apache.storm.tuple.Tuple;

public class StormUtil {

	
	/**
	 * 根据传送过来的Tuple，判断本Tuple是否是tickTuple 如果是tickTuple，则触发相应的动作
	 * @param tuple
	 * @return
	 */
	public static boolean isTickTuple(Tuple tuple){
		return tuple.getSourceComponent().equals(Constants.SYSTEM_COMPONENT_ID) && tuple.getSourceStreamId().equals(Constants.SYSTEM_TICK_STREAM_ID);
	}

}
