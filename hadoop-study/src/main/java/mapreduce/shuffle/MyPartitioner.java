package mapreduce.shuffle;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Partitioner;

public class MyPartitioner extends Partitioner<CustomCombineKey, Text> {

	@Override
	public int getPartition(CustomCombineKey key, Text value, int numPartitions) {
		
		return (key.hashCode()/*hashCode(key)*/ & Integer.MAX_VALUE) % numPartitions;
	}

}
