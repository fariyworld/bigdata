package mapreduce.job.decodeparquet;

import java.io.IOException;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.input.CombineFileSplit;
import org.apache.log4j.Logger;
import org.apache.parquet.example.data.Group;

public class CombineParquetRecodeReader extends RecordReader<Void, Group>{
	
	private static final Logger LOGGER = Logger.getLogger(CombineParquetRecodeReader.class);
	
	private CombineFileSplit combineFileSplit;
	private TaskAttemptContext context;
	private int currentIndex;

	private Void currentKey;
	private Group currentValue;
	
	private int totalLength;
	private Path[] paths;

	
	/**
	 * 构造器
	 * @param combineFileSplit
	 * @param currentIndex
	 */
	public CombineParquetRecodeReader(CombineFileSplit combineFileSplit,TaskAttemptContext context, int currentIndex) {
		LOGGER.info("调用CombineParquetRecodeReader构造器...");
		this.combineFileSplit = combineFileSplit;
		this.context = context;
		//  当前要处理的小文件Block在CombineFileSplit中的索引
		this.currentIndex = currentIndex;
	}

	/**
	 * 1.初始化读取资源以及相关的参数
	 */
	@Override
	public void initialize(InputSplit split, TaskAttemptContext context) throws IOException, InterruptedException {
		this.combineFileSplit = (CombineFileSplit) split;
		this.context = context;

	}

	@Override
	public boolean nextKeyValue() throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Void getCurrentKey() throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Group getCurrentValue() throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public float getProgress() throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void close() throws IOException {
		// TODO Auto-generated method stub
		
	}

}
