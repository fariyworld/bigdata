package topology.wordcount;

import java.util.Map;

import org.apache.storm.Config;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import topology.entity.Person;
import topology.tools.StormUtil;

/**
 * 
 * @author 15257
 *
 */
public class SplitWordBolt extends BaseRichBolt {

	private static final long serialVersionUID = 793676976281976486L;
	
	private Person _person;
	
	@Override
	public Map<String, Object> getComponentConfiguration() {
		Config config = new Config();
		config.put(Config.TOPOLOGY_TICK_TUPLE_FREQ_SECS, 10);
		return config;
	}

	private static final Logger LOGGER = LoggerFactory.getLogger(SplitWordBolt.class);
	
	private OutputCollector _collector;
	
	/**
	 * 初始化被调用,提供Bolt执行所需要的环境
	 */
	@Override
	public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("1.prepare(Map stormConf, TopologyContext context, OutputCollector collector)...");
		}
		this._collector = collector;
		_person = new Person("mace");
		LOGGER.info("person static field L0 == " + Person.L0 + "\t-------------------------------------------");
		LOGGER.info("person static field L0 == " + _person.L0 + "\t-------------------------------------------");
	}

	
	/**
	 * 处理上游的每一个数据
	 */
	@Override
	public void execute(Tuple input) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("2.execute(Tuple input)...");
			LOGGER.debug("input mid == " + input.getMessageId());
		}
		LOGGER.info("current name == " + _person.getName() + "\t-------------------------------------------");
//		if(StormUtil.isTickTuple(input)){
//			LOGGER.info("tick tuple to update personName" + "\t-------------------------------------------");
//			_person.setName("fariy");
//			return;
//		}
		try {
			String word = input.getStringByField("word");
			String[] wordArray = word.split("\\s", -1);
			for (String string : wordArray) {
				_collector.emit(input, new Values(string));
			}
			_collector.ack(input);
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			LOGGER.info("tick tuple ................................");
			if(StormUtil.isTickTuple(input)){
				LOGGER.info("tick tuple to update personName" + "\t-------------------------------------------");
				_person = new Person("fariy");
				return;
			}
		}
	//	_collector.fail(input);
	}

	
	/**
	 * 声明发送流的格式
	 */
	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("3.declareOutputFields(OutputFieldsDeclarer declarer)...");
		}
		declarer.declare(new Fields("word"));
	}

}
