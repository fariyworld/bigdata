package mapreduce.job.testsort;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import business.entity.DateTemperaturePair;

/**
 * 按key值排序
 * @author 15257
 *
 */
public class TestMapreduce2 {

	public static class TestMapper extends Mapper<LongWritable, Text, DateTemperaturePair, DateTemperaturePair> {
		
		private static final Logger LOG = LoggerFactory.getLogger(TestMapper.class);
		
		@Override
		protected void map(LongWritable key, Text value,
				Mapper<LongWritable, Text, DateTemperaturePair, DateTemperaturePair>.Context context)
				throws IOException, InterruptedException {
			
			String lineTxt = value.toString();
			DateTemperaturePair outValue = new DateTemperaturePair();
			String[] paramArray = lineTxt.split(",", -1);
			outValue.setYearMonth(new Text(paramArray[0].concat("-").concat(paramArray[1])));
			outValue.setDay(new Text(paramArray[2]));
			outValue.setTemperature(new DoubleWritable(Double.valueOf(paramArray[3])));
			context.write(outValue, outValue);
		}
	}
	
	
	public static class TestReduce extends Reducer<DateTemperaturePair, DateTemperaturePair, NullWritable, Text>{
		
		private static final Logger LOG = LoggerFactory.getLogger(TestReduce.class);
		StringBuilder sorted = new StringBuilder();
		NullWritable outkey = NullWritable.get();
		private Text outValue = new Text();
		private int count = 0;
		@Override
		protected void reduce(DateTemperaturePair key, Iterable<DateTemperaturePair> values,
				Reducer<DateTemperaturePair, DateTemperaturePair, NullWritable, Text>.Context context)
				throws IOException, InterruptedException {
			LOG.info("第 {} 次调用reduce()", ++count);
			sorted.delete(0, sorted.length());
			LOG.info("key: [{}]", key.getYearMonth().toString());
			sorted.append(key.getYearMonth().toString());
			sorted.append("\t");
			for (DateTemperaturePair value : values) {
				LOG.info(value.getTemperature().toString());
				sorted.append(value.getTemperature().toString());
				sorted.append(",");
			}
			sorted.delete(sorted.length()-1, sorted.length());
			outValue.set(sorted.toString());
			LOG.info(outValue.toString());
			context.write(outkey, outValue);
			LOG.info("一组values迭代结束");
		}
	}
	
	/**
	 * 分组
	 *
	 */
	public static class DateTemperaturePairGroupComparator extends WritableComparator {

		public DateTemperaturePairGroupComparator() {
			super(DateTemperaturePair.class, true);
		}

		@Override
		public int compare(WritableComparable a, WritableComparable b) {
			DateTemperaturePair pair1 = (DateTemperaturePair) a;
			DateTemperaturePair pair2 = (DateTemperaturePair) b;
			return pair1.getYearMonth().compareTo(pair2.getYearMonth());
		}
	}
	
	public static void main(String[] args) throws Exception {
		
		Configuration conf = new Configuration();
		Job job = Job.getInstance(conf);
		job.setJobName("test_mapreduce");
		job.setJarByClass(TestMapreduce2.class);
		job.setMapperClass(TestMapper.class);
		job.setReducerClass(TestReduce.class);
		job.setMapOutputKeyClass(DateTemperaturePair.class);
		job.setMapOutputValueClass(DateTemperaturePair.class);
		job.setOutputKeyClass(NullWritable.class);
		job.setOutputValueClass(Text.class);
		job.setGroupingComparatorClass(DateTemperaturePairGroupComparator.class);
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
