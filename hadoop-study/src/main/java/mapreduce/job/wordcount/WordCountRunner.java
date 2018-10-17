package mapreduce.job.wordcount;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.log4j.Logger;

public class WordCountRunner {
	
	private static final Logger LOG = Logger.getLogger(WordCountRunner.class);

	public static void main(String[] args) throws Exception {
		
		Configuration conf = new Configuration();
		
		Job job = Job.getInstance(conf);
		job.setJarByClass(WordCountRunner.class);
		job.setJobName("wordcount_mapreduce");
		
		job.setMapperClass(WordCountMapper.class);
		job.setReducerClass(WordCountReducer.class);
		
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(IntWritable.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(IntWritable.class);
		
//		args = new String[2];
//		args[0] = "D:/BONC/wordcount.txt";
//		args[1] = "D:/BONC/wordcount_out";
		FileInputFormat.setInputPaths(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));
		FileOutputFormat.setCompressOutput(job, false);
//		FileOutputFormat.setOutputCompressorClass(job, SnappyCodec.class);
		
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
