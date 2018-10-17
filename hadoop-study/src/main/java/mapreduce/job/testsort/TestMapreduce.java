package mapreduce.job.testsort;

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

import business.entity.Person;
import mapreduce.shuffle.CustomCombineKey;

public class TestMapreduce {

	public static class TestMapper extends Mapper<LongWritable, Text, CustomCombineKey, Text> {
		
		private static final Logger LOG = Logger.getLogger(TestMapper.class);

		Person personTmp = new Person();
		CustomCombineKey groupKey = new CustomCombineKey();
		Text outValue = new Text();
		
		@Override
		protected void map(LongWritable key, Text value,
				Mapper<LongWritable, Text, CustomCombineKey, Text>.Context context)
				throws IOException, InterruptedException {
			
			String lineTxt = value.toString();
			LOG.info(lineTxt);
			personTmp = personTmp.stringTo(lineTxt);
			outValue.set(lineTxt);
			context.write(groupKey.getKey(personTmp), outValue);
		}
	}
	
	
	public static class TestReduce extends Reducer<CustomCombineKey, Text, NullWritable, Text>{
		
		private static final Logger LOG = Logger.getLogger(TestReduce.class);

		@Override
		protected void reduce(CustomCombineKey key, Iterable<Text> values,
				Reducer<CustomCombineKey, Text, NullWritable, Text>.Context context)
				throws IOException, InterruptedException {
			
			LOG.info(key);
			for (Text text : values) {
				LOG.info(text.toString());
			}
		}
	}
	
	
	public static void main(String[] args) throws Exception {
		
		Configuration conf = new Configuration();
		Job job = Job.getInstance(conf);
		job.setJobName("test_mapreduce");
		job.setJarByClass(TestMapreduce.class);
		job.setMapperClass(TestMapper.class);
		job.setReducerClass(TestReduce.class);
		job.setMapOutputKeyClass(CustomCombineKey.class);
		job.setMapOutputValueClass(Text.class);
		job.setOutputKeyClass(NullWritable.class);
		job.setOutputValueClass(Text.class);
		
//		job.setPartitionerClass(cls);
		
		FileInputFormat.setInputPaths(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));
		FileOutputFormat.setCompressOutput(job, false);
		
		Path path = new Path(args[1]);
        FileSystem fileSystem = path.getFileSystem(conf);
        if (fileSystem.exists(path)) {
			fileSystem.delete(path, true);
			System.out.println(args[1]+"已存在，删除");
		}
		
		if(job.waitForCompletion(true)){
			System.out.println("success");
		}
	}
}
