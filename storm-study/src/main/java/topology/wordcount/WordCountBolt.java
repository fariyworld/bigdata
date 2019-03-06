package topology.wordcount;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

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
import topology.tools.StormUtil;

public class WordCountBolt extends BaseRichBolt  {

	private static final long serialVersionUID = 7601326103767185365L;
	private static final Logger LOGGER = LoggerFactory.getLogger(WordCountBolt.class);

	private OutputCollector _collector;
	private ConcurrentHashMap<String, Long> countMap = new ConcurrentHashMap<>();
	private Set<String> set = new ConcurrentSkipListSet<>();

	@Override
	public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("1.prepare(Map stormConf, TopologyContext context, OutputCollector collector)...");
		}
		this._collector = collector;		
	}

	@Override
	public Map<String, Object> getComponentConfiguration() {
		// return super.getComponentConfiguration();
		Config config = new Config();
		config.put(Config.TOPOLOGY_TICK_TUPLE_FREQ_SECS, 10);
		return config;
	}

	@Override
	public void execute(Tuple input) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("2.execute(Tuple input)...");
			LOGGER.debug("input mid == " + input.getMessageId());
		}
		if (StormUtil.isTickTuple(input)) {
			LOGGER.info("word size " + set.size() + "------------------------------------------------------------");
			LOGGER.info("words: " + set.toString() + "------------------------------------------------------------");
			return;
		}
		String word = input.getStringByField("word");
//		LOGGER.info("word: " + word + "------------------------------------------------------------");
		set.add(word);
		// 获取该单词对应的计数
		Long count = countMap.get(word);
		if(count == null)
			count = 0L;
		// 计数增加
		count ++;
		// 将单词和对应的计数加入map中
		countMap.put(word, count);
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(word+"\t"+count);
		}
		LOGGER.info(word+"\t"+count);
		// 发送单词和计数（分别对应字段word和count）
		_collector.emit(input, new Values(word, count));
		_collector.ack(input);
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("3.declareOutputFields(OutputFieldsDeclarer declarer)...");
		}
		// 定义两个字段word和count
		declarer.declare(new Fields("word","count"));
	}

}
