package mapreduce.shuffle;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Partitioner;
import org.apache.log4j.Logger;

public class MyPartitioner extends Partitioner<CustomCombineKey, Text> {

	private static final Logger LOG = Logger.getLogger(MyPartitioner.class);
	
	@Override
	public int getPartition(CustomCombineKey key, Text value, int numPartitions) {
		LOG.info("自定义分区...");
		return (key.hashCode()/*hashCode(key)*/ & Integer.MAX_VALUE) % numPartitions;
	}

}
