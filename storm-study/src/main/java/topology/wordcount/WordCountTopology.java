package topology.wordcount;

import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.StormSubmitter;
import org.apache.storm.topology.TopologyBuilder;
import org.apache.storm.tuple.Fields;
import org.apache.storm.utils.Utils;

import topology.entity.Person;

/**
 * 主函数
 * 
 * @author 15257
 *
 */
public class WordCountTopology {

	public static void main(String[] args) throws Exception {

		// 创建一个拓扑
		TopologyBuilder builder = new TopologyBuilder();
		// 设置Spout，这个Spout的名字叫做"Spout"，设置并行度为2
		builder.setSpout("Spout", new RandomWordSpout(), 1).setNumTasks(4);
		// 设置Blot——“Split”，并行度为2，它的数据来源是Spout的
		builder.setBolt("Split", new SplitWordBolt(), 1).setNumTasks(4).shuffleGrouping("Spout");
		// 设置slot——“Count”,你并行度为2，它的数据来源是Split的word字段相同的数据
		builder.setBolt("Count", new WordCountBolt(), 1).setNumTasks(4).fieldsGrouping("Split", new Fields("word"));
		Config conf = new Config();
		conf.setDebug(false);
		Person.L0 = 105D;
//		Config.TOPOLOGY_SLEEP_SPOUT_WAIT_STRATEGY_TIME_MS;
		if (args != null && args.length > 0) {
			// 集群模式
			conf.setNumWorkers(3);
			StormSubmitter.submitTopology(args[0], conf, builder.createTopology());
		}else{
			// 本地模式
			LocalCluster cluster = new LocalCluster();
			conf.setMaxTaskParallelism(2);
			// 提交拓扑（该拓扑的名字叫wordcount）
			cluster.submitTopology("wordcount", conf, builder.createTopology());
//			Utils.sleep(30000);
//			cluster.killTopology("wordcount");
//			cluster.shutdown();
		}

	}
}
