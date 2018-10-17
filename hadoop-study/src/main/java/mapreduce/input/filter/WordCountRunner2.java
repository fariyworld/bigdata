package mapreduce.input.filter;

import java.io.File;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.FileUtil;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import mapreduce.job.wordcount.WordCountMapper;
import mapreduce.job.wordcount.WordCountReducer;

/**
 * 文件名过滤 - 通配符过滤
 * @author 15257
 *
 */
public class WordCountRunner2 {

	public static void main(String[] args) throws Exception {
		
		Configuration conf = new Configuration();
		
		Job job = Job.getInstance(conf);
		job.setJarByClass(WordCountRunner2.class);
		job.setJobName("wordcount_mapreduce_filter");
		
		job.setMapperClass(WordCountMapper.class);
		job.setReducerClass(WordCountReducer.class);
		
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(IntWritable.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(IntWritable.class);
		
		args = new String[4];
		args[0] = "D:\\Hadoop\\study\\data";
		args[1] = "20180930";
		args[2] = "11";
		args[3] = "D:\\Hadoop\\study\\out";
		
        FileSystem fileSystem = FileSystem.get(conf);
        
        //TODO 文件名过滤 - 通配符过滤  pathPattern
        Path pathPattern = new Path(args[0] + File.separator + args[1] + File.separator +"*");
        FileStatus[] fileStatus = fileSystem.globStatus(pathPattern, new RegexPathFilter("^.*[_]" + args[1] +  args[2] + "\\d+[_].*$", conf));
        
        if(fileStatus.length == 0){
        	System.out.println("没有符合的文件,退出程序...");
        	System.exit(-1);
        }
        
        System.out.println(conf.getLong("fileSize", 0));
        Path[] inputPaths = FileUtil.stat2Paths(fileStatus);
    	FileInputFormat.setInputPaths(job, inputPaths);
		//TODO 删除已存在输出路径
        Path outputPath = new Path(args[3]);
        if (fileSystem.exists(outputPath)) {
			fileSystem.delete(outputPath, true);
			System.out.println(args[3]+"已存在，删除");
		}
		FileOutputFormat.setOutputPath(job, new Path(args[3]));
		FileOutputFormat.setCompressOutput(job, false);
//		FileOutputFormat.setOutputCompressorClass(job, SnappyCodec.class);
		
		//分布式缓存
		job.addCacheFile(new Path("D:\\Hadoop\\study\\data\\cache1.txt").toUri());
		
		if(job.waitForCompletion(true)){
			System.out.println("success");
		}
	}
}
