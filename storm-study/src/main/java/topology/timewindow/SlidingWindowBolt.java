package topology.timewindow;

import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseWindowedBolt;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.windowing.TupleWindow;

import java.util.Map;

/**
 * description: 窗口计数
 * <br />
 * Created by mace on 2019/3/8 10:35.
 */
public class SlidingWindowBolt extends BaseWindowedBolt {

    private OutputCollector _collector;

    @Override
    public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
        this._collector = collector;
    }


    @Override
    public Map<String, Object> getComponentConfiguration() {
        return super.getComponentConfiguration();
    }


    //TupleWindow参数里面装载了一个窗口长度类的tuple数据。通过对TupleWindow遍历，我们可以计算这一个窗口内tuple数
    @Override
    public void execute(TupleWindow inputWindow) {

        for(Tuple tuple: inputWindow.get()) {
            
        }
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        super.declareOutputFields(declarer);
    }
}
