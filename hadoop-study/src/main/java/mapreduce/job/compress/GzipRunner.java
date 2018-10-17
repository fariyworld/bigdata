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
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.log4j.Logger;

public class GzipRunner {
	
	private static final Logger LOG = Logger.getLogger(GzipRunner.class);

	public static class GzipMapper extends Mapper<LongWritable, Text, NullWritable, Text>{
		
		NullWritable outkey = NullWritable.get();

		@Override
		protected void map(LongWritable key, Text value, Mapper<LongWritable, Text, NullWritable, Text>.Context context)
				throws IOException, InterruptedException {
			System.out.println(key);
			System.out.println(value);
			context.write(outkey, new Text("1"));
		}
		
		
	}
	
	public static class GzipReducer extends Reducer<NullWritable, Text, NullWritable, Text>{

		NullWritable outkey = NullWritable.get();
		
		@Override
		protected void reduce(NullWritable key, Iterable<Text> values,
				Reducer<NullWritable, Text, NullWritable, Text>.Context context) throws IOException, InterruptedException {

			context.write(outkey, new Text());
		}
		
		
	}
	
	
	public static void main(String[] args) throws Exception {
		
		
		Configuration conf = new Configuration();
		Job job = Job.getInstance(conf);
		job.setJarByClass(GzipRunner.class);
		job.setJobName("compress_gzip_more");
		job.setMapperClass(GzipMapper.class);
		job.setReducerClass(GzipReducer.class);
		
		job.setMapOutputKeyClass(NullWritable.class);
		job.setMapOutputValueClass(Text.class);
		job.setOutputKeyClass(NullWritable.class);
		job.setOutputValueClass(Text.class);
		
		FileInputFormat.setInputPaths(job, new Path("D:\\BONC\\Shaanxi"));
		FileOutputFormat.setOutputPath(job, new Path("D:\\tmp\\uncompress\\out"));
		FileOutputFormat.setCompressOutput(job, false);
		
		
		 Path path = new Path("D:\\tmp\\uncompress\\out");
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
