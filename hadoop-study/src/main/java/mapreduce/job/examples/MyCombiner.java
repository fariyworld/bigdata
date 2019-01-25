package mapreduce.job.examples;

import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyCombiner extends Reducer<TestKey, Text, TestKey, Text> {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(MyCombiner.class);
	private int count;
	
	@Override
	protected void setup(Reducer<TestKey, Text, TestKey, Text>.Context context)
			throws IOException, InterruptedException {
		LOGGER.info("MyCombiner setup()");
		super.setup(context);
		count = 0;
	}

	@Override
	protected void reduce(TestKey key, Iterable<Text> values, Reducer<TestKey, Text, TestKey, Text>.Context context)
			throws IOException, InterruptedException {
		LOGGER.info("MyCombiner reduce()");
		LOGGER.info("第 {} 次调用 MyCombiner reduce()", ++count);
		LOGGER.info("key: {}", key);
		for (Text value : values) {
			LOGGER.info("value: {}", value);
			context.write(key, value);
		}
		LOGGER.info("一组values迭代结束");
	}

	@Override
	protected void cleanup(Reducer<TestKey, Text, TestKey, Text>.Context context)
			throws IOException, InterruptedException {
		LOGGER.info("MyCombiner cleanup()");
		super.cleanup(context);
	}

}
