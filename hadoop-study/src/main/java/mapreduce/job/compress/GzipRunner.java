package mapreduce.job.compress;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.MultipleInputs;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.log4j.Logger;

import mapreduce.input.format.xml.XmlCompressedCombineFileWritable;
import mapreduce.input.format.xml.XmlCompressedFileInputFormat;

public class GzipRunner {
	
	private static final Logger LOG = Logger.getLogger(GzipRunner.class);

	public static class GzipMapper extends Mapper<XmlCompressedCombineFileWritable, Text, NullWritable, Text>{
		
		NullWritable outkey = NullWritable.get();
		private String inputString = null;

		@Override
		protected void map(XmlCompressedCombineFileWritable key, Text value, Mapper<XmlCompressedCombineFileWritable, Text, NullWritable, Text>.Context context)
				throws IOException, InterruptedException {
			inputString = null;
			LOG.info(key);
			inputString = value.toString();
			String[] paramArray = inputString.split("\r\n", -1);
			int paramLength = paramArray.length;
			LOG.info(paramLength);
			for (int i = 0; i < paramLength - 1; i++) {
				LOG.info(paramArray[i]);
			}
			context.write(outkey, new Text("1"));
		}
		
		
	}
	
	public static class GzipReducer extends Reducer<NullWritable, Text, NullWritable, Text>{

		NullWritable outkey = NullWritable.get();
		
		@Override
		protected void reduce(NullWritable key, Iterable<Text> values,
				Reducer<NullWritable, Text, NullWritable, Text>.Context context) throws IOException, InterruptedException {

			context.write(outkey, new Text("1"));
		}
	}
	
	
	public static void main(String[] args) throws Exception {
		
		
		Configuration conf = new Configuration();
		
		conf.set("xmlinput.start", "<measurement>"); 
 		conf.set("xmlinput.end", "</measurement>");
 		conf.setBoolean("isMultiLevelCompression", false);
 		
 		Job job = Job.getInstance(conf);
		job.setJarByClass(GzipRunner.class);
		job.setJobName("compress_gzip_more");
//		job.setMapperClass(GzipMapper.class);
		job.setReducerClass(GzipReducer.class);
 		
 		MultipleInputs.addInputPath(job, new Path(args[0]), XmlCompressedFileInputFormat.class, GzipMapper.class);
 		
		job.setMapOutputKeyClass(NullWritable.class);
		job.setMapOutputValueClass(Text.class);
		job.setOutputKeyClass(NullWritable.class);
		job.setOutputValueClass(Text.class);
		
//		FileInputFormat.setInputPaths(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));
		FileOutputFormat.setCompressOutput(job, false);
		
		 Path path = new Path(args[1]);
	        FileSystem fileSystem = path.getFileSystem(conf);
	        if (fileSystem.exists(path)) {
				fileSystem.delete(path, true);
				LOG.info(args[1]+"已存在，删除");
			}
			
			if(job.waitForCompletion(true)){
				LOG.info("success");
			}
	}
}
