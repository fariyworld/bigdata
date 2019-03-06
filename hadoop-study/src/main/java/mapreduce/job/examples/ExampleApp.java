package mapreduce.job.examples;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.FileUtil;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.CombineTextInputFormat;
import org.apache.hadoop.mapreduce.lib.input.MultipleInputs;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.MultipleOutputs;
import org.apache.parquet.example.data.Group;
import org.apache.parquet.hadoop.ParquetInputFormat;
import org.apache.parquet.hadoop.api.DelegatingReadSupport;
import org.apache.parquet.hadoop.api.InitContext;
import org.apache.parquet.hadoop.example.GroupReadSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import mapreduce.input.filter.RegexPathFilter;
import mapreduce.output.format.RedisOutPutFormat;

public class ExampleApp {

	private static final Logger LOGGER = LoggerFactory.getLogger(ExampleApp.class);
	
	public static void main(String[] args) {
//		LOGGER.info("replace new class");
//		System.exit(-1);
		try {
			// hadoop集群和应用程序配置
			Configuration conf = new Configuration();
			
			//TODO 读取应用程序的配置并放到conf对象中,方便在map/reduce端获取配置
//			String prefix = "D:\\Workspaces\\eclipse_4.5_workspace\\study\\hadoop-study\\data\\";
//			conf.setBoolean("local", true);
//			conf.set("outpath", prefix + "out");

			String prefix = "/test/data/";
			conf.setBoolean("local", false);
			conf.set("outpath", "/test/out");

			conf.set("queuename", "default");
			conf.set("jobName", "test");
			conf.set("cahceFile", prefix + "cache.txt");
			conf.set("MR_FORMAT", "txt");
			conf.set("mrinput", prefix + "persons.txt");
//			conf.set("mrRegex", "^.*[_]2018093011\\d+[_].*$");
			conf.setLong("splitSize", 128L);
			conf.set("mmeinput", prefix + "info");
			conf.set("MME_FORMAT", "txt");
			conf.setInt("reduceNum", 2);
//			conf.set("ADDR_ARRAY", "118.25.229.83:6379");
//			conf.setInt("REDIS_INDEX", 1);
			conf.set("ADDR_ARRAY", "172.27.0.13:12301;172.27.0.13:12302;172.27.0.13:12303");
//			conf.setBoolean("isCluster", false);
			conf.setBoolean("isCluster", true);
			conf.set("AUTH", "liuye0425@+.");
			
			//TODO 设置MapReduce运行配置
			// 队列名
			String queuename = conf.get("queuename");
			if (StringUtils.isNotBlank(queuename)) {
				conf.set("mapreduce.job.queuename", queuename);
			}
			// 支持递归子目录作为输入
			conf.set("mapreduce.input.fileinputformat.input.dir.recursive", "true");
			// 初始化Job
			Job job = Job.getInstance(conf);
			// 设置主类
			job.setJarByClass(ExampleApp.class);
			// 设置作业名称
			job.setJobName(conf.get("jobName", "ExampleApp"));
			// 缓存
			job.addCacheFile(new Path(conf.get("cahceFile")).toUri());
			
			// HDFS文件系统
			FileSystem fileSystem = FileSystem.get(conf);
			
			
			/*************   MR   ****************/		
			
			String mrinput = conf.get("mrinput");
			//TODO MR文件名过滤 - 正则过滤 数据量统计
			Path mrPath = new Path(mrinput);//正则 2019011701
			FileStatus[] mrFileStatus = fileSystem.globStatus(mrPath, new RegexPathFilter(conf, conf.get("mrRegex", "^.*$"), "mr"));
			
			if (mrFileStatus.length == 0) {
				LOGGER.info("mr没有匹配的数据");
				System.exit(-1);
			}
			Path[] mrInputPaths = FileUtil.stat2Paths(mrFileStatus);
			// 输入文件格式 xml/压缩/文本/parquet
			switch (conf.get("MR_FORMAT", "txt")) {
			case "txt": {
				for (Path path : mrInputPaths) {
					MultipleInputs.addInputPath(job, path, CombineTextInputFormat.class, MrTextMapper.class);
				}
				CombineTextInputFormat.setMaxInputSplitSize(job, (conf.getLong("splitSize", 128L) *1024 * 1024));
				break;
			}
			case "xml": {
				
				break;
			}
			case "compress": break;
			case "multicompress": break;
			case "parquet": {
				// 小文件问题
				ParquetInputFormat.setReadSupportClass(job, MyReadSupport.class);
				break;
			}
			default:
				break;
			}
			
			/*************   MME   ****************/			
			
			String mmeinput = conf.get("mmeinput");
			//TODO MME文件名过滤 - 正则过滤 数据量统计
			Path mmePath = new Path(mmeinput);//正则 2019011701
			FileStatus[] mmeFileStatus = fileSystem.globStatus(mmePath, new RegexPathFilter(conf, conf.get("mmeRegex", "^.*$"), "mme"));
			
			if (mmeFileStatus.length == 0) {
				LOGGER.info("mme没有匹配的数据");
				System.exit(-1);
			}
			Path[] mmeInputPaths = FileUtil.stat2Paths(mmeFileStatus);
			// 输入文件格式 xml/压缩/文本/parquet
			switch (conf.get("MME_FORMAT", "txt")) {
			case "txt": {
				for (Path path : mmeInputPaths) {
					MultipleInputs.addInputPath(job, path, CombineTextInputFormat.class, MmeTextMapper.class);
				}
				CombineTextInputFormat.setMaxInputSplitSize(job, (conf.getLong("splitSize", 128L) *1024 * 1024));
				break;
			}
			case "xml": {
				
				break;
			}
			case "compress": break;
			case "multicompress": break;
			case "parquet": {
				// 小文件问题
				
				ParquetInputFormat.setReadSupportClass(job, MyReadSupport.class);
				break;
			}
			default:
				break;
			}
			
			/*************   Combine   ****************/		
			
			job.setCombinerClass(MyCombiner.class);
			job.setCombinerKeyGroupingComparatorClass(MyCombinerGroupComparator.class);
			
			/*************   Reducer   ****************/		
			
			
			// 本job使用的reducer的类
			job.setReducerClass(TestReducer.class);
			
			// 设置自定义分区策略
			Class partitionerClass = Class.forName("mapreduce.job.examples.MyPartitioner");
			job.setPartitionerClass(partitionerClass);
			
			// 设置自定义分组策略
			Class groupComparatorClass = Class.forName("mapreduce.job.examples.MyGroupingComparator");
			job.setGroupingComparatorClass(groupComparatorClass);
			
			// 设置自定义二次排序策略
			Class sortComparatorClass = Class.forName("mapreduce.job.examples.MySortComparator");
			job.setSortComparatorClass(sortComparatorClass);
//			
			
			/*************   Out   ****************/	
			
			// 指定处理结果的输出数据存放路径
			String outpath = conf.get("outpath");
			Path outputPath = new Path(outpath);
			if (fileSystem.exists(outputPath)) {
				fileSystem.delete(outputPath, true);
				LOGGER.info("{} 输出路径存在，已删除！", outpath);
			}
			job.setOutputFormatClass(RedisOutPutFormat.class);
			FileOutputFormat.setOutputPath(job, outputPath);
			// 将reduce输出文件不压缩
			FileOutputFormat.setCompressOutput(job, false);

			// 指定mapper的输出数据kv类型
			job.setMapOutputKeyClass(TestKey.class);
			job.setMapOutputValueClass(Text.class);

			// 指定reduce的输出数据kv类型
			job.setOutputKeyClass(NullWritable.class);
			job.setOutputValueClass(Text.class);
			
			// 设置reducer个数为1
			job.setNumReduceTasks(conf.getInt("reduceNum", 1));
			
			if (job.waitForCompletion(true)) {
				// 作业完成后日志统计  计数器
				LOGGER.info("SUCCESS");
			}
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			// TODO: handle finally clause
		}
	}
	
	public static final class MyReadSupport extends DelegatingReadSupport<Group> {
		public MyReadSupport() {
		    super(new GroupReadSupport());
		}
		
		@Override
		public org.apache.parquet.hadoop.api.ReadSupport.ReadContext init(InitContext context) {
		    return super.init(context);
		}
	}
}
