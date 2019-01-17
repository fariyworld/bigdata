package topology.kafkademo;

import java.util.Arrays;
import java.util.Map;

import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichSpout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import topology.entity.KafkaProperties;

/**
 * 消费kafka topic
 */
public class SimpleKafkaSpout extends BaseRichSpout {

	private static final long serialVersionUID = -4801100115279911483L;
	private static final Logger LOGGER = LoggerFactory.getLogger(SimpleKafkaSpout.class);
	
	private SpoutOutputCollector _collector;

	// 消费者实例
	private KafkaConsumer<String, String> _consumer;
	private String topic;
	
	public SimpleKafkaSpout(String topic) {
		this.topic = topic;
	}

	@Override
	public void open(Map conf, final TopologyContext context, final SpoutOutputCollector collector) {
		_collector = collector;
		_consumer = new KafkaConsumer<String, String>(KafkaProperties.props);
		_consumer.subscribe(Arrays.asList(topic));
		
	}

	@Override
	public void nextTuple() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		// TODO Auto-generated method stub
		
	}

	
}
