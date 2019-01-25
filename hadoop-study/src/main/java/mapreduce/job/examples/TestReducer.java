package mapreduce.job.examples;

import java.io.IOException;

import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestReducer extends Reducer<TestKey, Text, NullWritable, Text> {

	private static final Logger LOGGER = LoggerFactory.getLogger(TestReducer.class);
	private int count;
	
	@Override
	protected void setup(Reducer<TestKey, Text, NullWritable, Text>.Context context)
			throws IOException, InterruptedException {
		LOGGER.info("TestReducer setup()");
		super.setup(context);
		count = 0;
	}
	
	@Override
	protected void reduce(TestKey key, Iterable<Text> values,
			Reducer<TestKey, Text, NullWritable, Text>.Context context) throws IOException, InterruptedException {
		LOGGER.info("第 {} 次调用reduce()", ++count);
		LOGGER.info("key: {}", key);
		for (Text value : values) {
			LOGGER.info("value: {}", value);
		}
		LOGGER.info("一组values迭代结束");
	}

	@Override
	protected void cleanup(Reducer<TestKey, Text, NullWritable, Text>.Context context)
			throws IOException, InterruptedException {
		LOGGER.info("TestReducer cleanup()");
		super.cleanup(context);
	}
}
