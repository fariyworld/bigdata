package topology.wordcount;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

/**
 * 
 * @author 15257
 *
 */
public class SplitWordBolt extends BaseRichBolt {

	private static final long serialVersionUID = 793676976281976486L;
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
	}

	
	/**
	 * 处理上游的每一个数据
	 */
	@Override
	public void execute(Tuple input) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("2.execute(Tuple input)...");
		}
		String word = input.getStringByField("word");
		String[] wordArray = word.split("\\s", -1);
		for (String string : wordArray) {
			_collector.emit(new Values(string));
		}
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
