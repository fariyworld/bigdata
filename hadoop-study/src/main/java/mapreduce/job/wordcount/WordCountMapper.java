package mapreduce.job.wordcount;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.log4j.Logger;

import com.sun.tools.javac.util.Log;

public class WordCountMapper extends Mapper<LongWritable, Text, Text, IntWritable> {
	
	private static final Logger LOG = Logger.getLogger(WordCountMapper.class);
	
	private final static IntWritable one = new IntWritable(1);
	private Text text = new Text();

	
	@Override
	protected void setup(Mapper<LongWritable, Text, Text, IntWritable>.Context context)
			throws IOException, InterruptedException {
		super.setup(context);
		
		URI[] cacheFiles = context.getCacheFiles();
		if(cacheFiles != null && cacheFiles.length > 0){
			Path cachePath1 = new Path(cacheFiles[0]);
			 BufferedReader reader = null;  
		        try {  
		        	reader = new BufferedReader(new FileReader(cachePath1.getName()));
		            String lineTxt = null;  
		            while ((lineTxt = reader.readLine()) != null) { 
		            	LOG.info(lineTxt);
		            }
		            reader.close();  
			    } catch (IOException e) {  
			        e.printStackTrace();  
			    } finally {  
			        if (reader != null) {  
			            try {  
			                reader.close();  
			            } catch (IOException e1) {  
			            }  
			        }  
			    }
		}
	}




	@Override
	protected void map(LongWritable key, Text value, Mapper<LongWritable, Text, Text, IntWritable>.Context context) throws IOException, InterruptedException {
		
		String line = value.toString();
		String[] words = StringUtils.split(line, " ");
		for (String word : words) {
			text.set(word);
			context.write(text, one);
		}
	}
}
