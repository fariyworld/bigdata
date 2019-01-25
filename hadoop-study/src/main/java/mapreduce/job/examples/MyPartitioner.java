package mapreduce.job.examples;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Partitioner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyPartitioner extends Partitioner<TestKey, Text> {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(MyPartitioner.class);

	/**
	 * 数据输入来源：map输出
	 * 
	 * @param key
	 *            map输出键值
	 * @param value
	 *            map输出value值
	 * @param numPartitions
	 *            分区总数，即reduce task个数
	 */
	@Override
	public int getPartition(TestKey key, Text value, int numPartitions) {
		int id = (key.hashCode() & Integer.MAX_VALUE) % numPartitions;
		LOGGER.info("调用了getPartition()....................key:{}, num:{}, id:{}", key, numPartitions, id);
		try {
			Thread.sleep(1000L);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return id;
	}

}
