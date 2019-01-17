package topology.wordcount;

import java.util.Map;
import java.util.Random;
import java.util.UUID;

import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichSpout;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;
import org.apache.storm.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * spout pull data
 * @author 15257
 *
 */
public class RandomWordSpout extends BaseRichSpout {
	
	private static final long serialVersionUID = 6334783396834462143L;
	private static final Logger LOGGER = LoggerFactory.getLogger(RandomWordSpout.class);
	
	private SpoutOutputCollector _collector;
	private Random _random;
	
	// 单词数组
	private static String[] words = {"a b c d e", "b d a", "a a a", "c e f d"};
	
	/**
	 * 1. 初始化
	 * conf: stormConf和本Topology的配置合集
	 * context: storm上下文
	 * collector 收集器,发射数据到bolt,应保存为本类的实例变量
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("1.open(Map conf, TopologyContext context, SpoutOutputCollector collector)...");
		}
		this._collector = collector;
		this._random = new Random();
	}

	
	/**
	 * 发射数据
	 */
	@Override
	public void nextTuple() {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("2.nextTuple()...");
		}
		// 睡眠 1s, 产生一个数据
		Utils.sleep(1000);
		// 随机选择一组单词
		String word = words[_random.nextInt(words.length)];
		// 发送该组单词给Bolt
		_collector.emit(new Values(word), UUID.randomUUID().toString());
	}

	
	/**
	 * 声明流格式
	 */
	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("3.declareOutputFields(OutputFieldsDeclarer declarer)...");
		}
		declarer.declare(new Fields("word"));		
	}

	
}
