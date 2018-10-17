package mapreduce.input.format.xml;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.DataOutputBuffer;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.compress.CompressionCodec;
import org.apache.hadoop.io.compress.CompressionCodecFactory;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.input.CombineFileSplit;

public class XmlCompressedCombineFileRecordReader extends RecordReader<XmlCompressedCombineFileWritable, Text> {
	
	private long startOffset;  
    private long end;
    private long pos;
    private XmlCompressedCombineFileWritable key = new XmlCompressedCombineFileWritable();//自定义的key
    private Text value = new Text();//自定义的value
    
    private FSDataInputStream fsin;  
    private DataOutputBuffer buffer = new DataOutputBuffer();  
    
    private FileSystem fs;
    private Path path;
    
    private int currentIndex;
    private TaskAttemptContext context;
    private CombineFileSplit combineFileSplit;
    
    private byte[] startTag;  
    private byte[] endTag;
	public static final String START_TAG_KEY = "xmlinput.start";
	public static final String END_TAG_KEY = "xmlinput.end";
	
	private int isRead = 0;

	public XmlCompressedCombineFileRecordReader(int currentIndex, TaskAttemptContext context, CombineFileSplit combineFileSplit) {

		this.currentIndex = currentIndex;
		this.context = context;
		this.combineFileSplit = combineFileSplit;
	}

	@Override
	public void close() throws IOException {
		
	}

	/**
	 * 4. 返回 key  --> context.getCurrentKey()调用此方法
	 */
	@Override
	public XmlCompressedCombineFileWritable getCurrentKey() throws IOException, InterruptedException {
		return key;
	}

	/**
	 * 5. 返回 value --> context.getCurrentValue()调用此方法
	 */
	@Override
	public Text getCurrentValue() throws IOException, InterruptedException {
		return value;
	}

	/**
	 * 3. 获取分片进度
	 */
	@Override
	public float getProgress() throws IOException, InterruptedException {
		if (startOffset == end) {
			return 0.0f;
		} else {
			return Math.min(1.0f, (pos - startOffset) / (float) (end - startOffset));
		}
	}

	/**
	 * 1. 初始化读取资源以及相关的参数
	 */
	@Override
	public void initialize(InputSplit split, TaskAttemptContext context) throws IOException, InterruptedException {

		this.combineFileSplit = (CombineFileSplit) split;
		this.context = context; 
		
		Configuration conf = context.getConfiguration();
		// 获取开传入的开始和结束标签
		startTag = conf.get(START_TAG_KEY).getBytes("UTF-8");
		endTag = conf.get(END_TAG_KEY).getBytes("UTF-8");
		
		// 获取分片的开始位置和结束的位置
		this.path = combineFileSplit.getPath(currentIndex);
		this.fs = this.path.getFileSystem(conf);
		this.startOffset = combineFileSplit.getOffset(currentIndex);
		this.end = startOffset + combineFileSplit.getLength(currentIndex);
	}

	/**
	 * 2. context.nextKeyValue()
	 */
	@Override
	public boolean nextKeyValue() throws IOException, InterruptedException {
		
		if(key.getFileName() == null){
			key.setFileName(path.getName());
		}
		value.clear();//清空value
		if (isRead==0) {//只执行一次nextKeyValue() ==> 调用map() 一次
			//读取文件逻辑
			readFile();
			return true;
		}
		return false;
	}
	
	public void readFile() {
		isRead=1;
		byte[] contents = new byte[(int) (end - startOffset)];
		ByteArrayOutputStream bos =  new ByteArrayOutputStream();//文件内容
		//识别压缩格式
		
	}
	
	
	/**
	 * 判断是否为压缩文件
	 * @param conf
	 * @param p
	 * @return
	 */
	private boolean findCodec(Configuration conf, Path p){
		CompressionCodecFactory factory = new CompressionCodecFactory(conf);
	    CompressionCodec codec = factory.getCodec(path);
	    return codec != null;
	}
}
