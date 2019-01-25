package mapreduce.job.decodeparquet;

import java.io.IOException;

import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.input.CombineFileInputFormat;
import org.apache.parquet.example.data.Group;

/**
 * 自定义parquet合并小文件InputFormat
 */
public class CombineParquetInputFormat extends CombineFileInputFormat<Void, Group> {

	public CombineParquetInputFormat() {
		super();
	}

	@Override
	public RecordReader<Void, Group> createRecordReader(InputSplit split, TaskAttemptContext context)
			throws IOException {
		
		
		return null;
	}
	
	
}
