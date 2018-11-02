package topology.wordcount;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

public class WordCountBolt extends BaseRichBolt  {

	private static final long serialVersionUID = 7601326103767185365L;
	private static final Logger LOGGER = LoggerFactory.getLogger(WordCountBolt.class);
	
	private OutputCollector _collector;
	private ConcurrentHashMap<String, Long> countMap = new ConcurrentHashMap<>();

	@Override
	public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("1.prepare(Map stormConf, TopologyContext context, OutputCollector collector)...");
		}
		this._collector = collector;		
	}

	@Override
	public void execute(Tuple input) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("2.execute(Tuple input)...");
			LOGGER.debug("input mid == " + input.getMessageId());
		}
		String word = input.getStringByField("word");
		
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
