package mapreduce.output.format;

import java.io.IOException;

import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.JobContext;
import org.apache.hadoop.mapreduce.OutputCommitter;
import org.apache.hadoop.mapreduce.OutputFormat;
import org.apache.hadoop.mapreduce.RecordWriter;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.output.NullOutputFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//此处的泛型是reduce端的输出的类型的key、 value
public class RedisOutPutFormat extends OutputFormat<NullWritable, Text> {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(RedisOutPutFormat.class);

	@Override
	public RecordWriter<NullWritable, Text> getRecordWriter(TaskAttemptContext context)
			throws IOException, InterruptedException {
		LOGGER.info("RedisOutPutFormat.getRecordWriter().............");
		try {
			Thread.sleep(1000L);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return new RedisRecordWriter(context.getConfiguration());
	}

	
	/**
	 * 检查配置
	 */
	@Override
	public void checkOutputSpecs(JobContext context) throws IOException, InterruptedException {
		LOGGER.info("RedisOutPutFormat.checkOutputSpecs().............");
		try {
			Thread.sleep(1000L);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public OutputCommitter getOutputCommitter(TaskAttemptContext context) throws IOException, InterruptedException {
		LOGGER.info("RedisOutPutFormat.getOutputCommitter().............");
		try {
			Thread.sleep(1000L);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return new NullOutputFormat<>().getOutputCommitter(context);
	}

}
