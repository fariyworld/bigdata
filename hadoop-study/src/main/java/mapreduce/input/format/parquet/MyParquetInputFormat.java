package mapreduce.input.format.parquet;

import java.io.IOException;
import java.util.List;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapreduce.InputFormat;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.JobContext;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.parquet.example.data.Group;

/**
 * 自定义parquet inpuformat 合并小文件为一个mapTask
 */
public class MyParquetInputFormat extends FileInputFormat<Void, Group> {

	
	/**
	 * 如何分片
	 * 此方法将返回InputSplit对象的列表。创建相同数量的MapTask去处理
	 */
	@Override
	public List<InputSplit> getSplits(JobContext context) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 如何读取分片中的记录 
	 */
	@Override
	public RecordReader<Void, Group> createRecordReader(InputSplit split, TaskAttemptContext context)
			throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 是否可以进行分片
	 */
	@Override
	protected boolean isSplitable(JobContext context, Path filename) {
		// TODO Auto-generated method stub
		return super.isSplitable(context, filename);
	}

	
}
