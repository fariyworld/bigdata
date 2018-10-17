package mapreduce.input.filter;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import mapreduce.job.wordcount.WordCountMapper;
import mapreduce.job.wordcount.WordCountReducer;

/**
 * 缺失文件过滤
 * @author 15257
 *
 */
public class WordCountRunner1 {

	public static void main(String[] args) throws Exception {
		
		Configuration conf = new Configuration();
		
		Job job = Job.getInstance(conf);
		job.setJarByClass(WordCountRunner1.class);
		job.setJobName("wordcount_mapreduce_filter");
		
		job.setMapperClass(WordCountMapper.class);
		job.setReducerClass(WordCountReducer.class);
		
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(IntWritable.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(IntWritable.class);
		
		args = new String[2];
		args[0] = "D:/BONC/wordcount.txt";
		args[1] = "D:/BONC/wordcount_out";
        FileSystem fileSystem = FileSystem.get(conf);
        
        //TODO 缺失文件过滤
        Path inputPath = new Path(args[0]);
		if(fileSystem.exists(inputPath)){
			FileInputFormat.addInputPath(job, inputPath);
		}
		
		//TODO 删除已存在输出路径
        Path outputPath = new Path(args[1]);
        if (fileSystem.exists(outputPath)) {
			fileSystem.delete(outputPath, true);
			System.out.println(args[1]+"已存在，删除");
		}
		FileOutputFormat.setOutputPath(job, new Path(args[1]));
		FileOutputFormat.setCompressOutput(job, false);
//		FileOutputFormat.setOutputCompressorClass(job, SnappyCodec.class);
		
		if(job.waitForCompletion(true)){
			System.out.println("success");
		}
	}
}
