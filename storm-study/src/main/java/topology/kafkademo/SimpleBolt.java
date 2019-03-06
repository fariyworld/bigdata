package topology.kafkademo;

import org.apache.storm.Config;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.IRichBolt;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import topology.tools.StormUtil;
import topology.wordcount.WordCountBolt;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * description:
 * <br />
 * Created by mace on 2019/3/4 10:56.
 */
public class SimpleBolt implements IRichBolt {

    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleBolt.class);

    private OutputCollector _collector;

    @Override
    public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
        this._collector = collector;
        LOGGER.info("SimpleBolt prepare()......");
    }

    @Override
    public void execute(Tuple input) {
        if (StormUtil.isTickTuple(input)) {
            return;
        }
        LOGGER.info("topic:{}, partition:{}, message:{}", input.getStringByField("topic"), input.getIntegerByField("partition"), input.getStringByField("value"));
        _collector.emit(new Values(null, String.format("%s-%s:%s", input.getStringByField("topic"), input.getIntegerByField("partition"), input.getStringByField("value"))));
    }

    @Override
    public void cleanup() {

    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("key", "message"));
    }

    /**
     * description: 该bolt tick tuple发送时间间隔
     * <br /><br />
     * create by mace on 2019/3/4 11:01.
     * @param
     * @return: java.util.Map<java.lang.String,java.lang.Object>
     */
    @Override
    public Map<String, Object> getComponentConfiguration() {
        Config config = new Config();
        config.put(Config.TOPOLOGY_TICK_TUPLE_FREQ_SECS, 10);
        return config;
    }
}
