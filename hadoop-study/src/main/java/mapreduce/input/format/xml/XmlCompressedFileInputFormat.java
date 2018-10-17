package mapreduce.input.format.xml;

import java.io.IOException;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.JobContext;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.input.CombineFileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.CombineFileRecordReader;
import org.apache.hadoop.mapreduce.lib.input.CombineFileSplit;

/**
 * xml 压缩文件 .xml.gz
 * @author 15257
 *
 */
public class XmlCompressedFileInputFormat extends CombineFileInputFormat<XmlCompressedCombineFileWritable, Text>  {

	public XmlCompressedFileInputFormat() {
		super();
	}

	@Override
	public RecordReader<XmlCompressedCombineFileWritable, Text> createRecordReader(InputSplit split,
			TaskAttemptContext context) throws IOException {
		
		CombineFileRecordReader reader = new CombineFileRecordReader<XmlCompressedCombineFileWritable,Text>((CombineFileSplit)split, context, 	XmlCompressedCombineFileRecordReader.class);
		
		return reader;
	}

	@Override
	protected boolean isSplitable(JobContext context, Path file) {
		
		return false;
	}

	
}
