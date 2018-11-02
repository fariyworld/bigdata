package topology.wordcount;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.tuple.Fields;
import backtype.storm.utils.Utils;

/**
 * 主函数
 * @author 15257
 *
 */
public class WordCountTopology {

	public static void main(String[] args) {
		
		// 创建一个拓扑
		TopologyBuilder builder = new TopologyBuilder();
		// 设置Spout，这个Spout的名字叫做"Spout"，设置并行度为2
		builder.setSpout("Spout", new RandomWordSpout(), 2);
		// 设置Blot——“Split”，并行度为2，它的数据来源是Spout的
		builder.setBolt("Split", new SplitWordBolt(), 2).shuffleGrouping("Spout");
		// 设置slot——“Count”,你并行度为2，它的数据来源是Split的word字段相同的数据
		builder.setBolt("Count", new WordCountBolt(), 2).fieldsGrouping("Split", new Fields("word"));
		Config conf = new Config();
		conf.setDebug(true);
		conf.setMaxTaskParallelism(3);
		// 本地集群
		LocalCluster cluster = new LocalCluster();
		// 提交拓扑（该拓扑的名字叫wordcount）
		cluster.submitTopology("wordcount", conf, builder.createTopology() );
		Utils.sleep(10000);
		cluster.shutdown();
	}
}
