package mapreduce.job.decodeparquet;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Iterator;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.parquet.example.data.Group;
import org.apache.parquet.example.data.simple.NanoTime;
import org.apache.parquet.hadoop.ParquetInputFormat;
import org.apache.parquet.hadoop.api.DelegatingReadSupport;
import org.apache.parquet.hadoop.api.InitContext;
import org.apache.parquet.hadoop.example.GroupReadSupport;
import org.apache.parquet.io.api.Binary;

import jodd.datetime.JDateTime;
/**
 * 解析parquet文件
 * @author 15257
 *
 */
public class DecodeParquetApp {
	
	public static class WordCountMap extends Mapper<Void, Group, LongWritable, Text> {
		protected void map(Void key, Group value, Mapper<Void, Group, LongWritable, Text>.Context context) throws IOException, InterruptedException {
			try {
				Binary start_time = value.getInt96("start_time", 0);
				long timestamp = getTimestamp(start_time);
				String imsi = value.getString("imsi",0);
				String mme_group_id = value.getString("mme_group_id",0);
				String mme_code = value.getString("mme_code",0);
				String ue_s1ap_id = value.getString("ue_s1ap_id",0);
				String src_eci = value.getString("src_eci",0);
				context.write(new LongWritable(1),new Text(timestamp+","+imsi+","+mme_group_id+","+mme_code+","+ue_s1ap_id+","+src_eci));
			} catch (Exception e) {
				return;
			}
		}
	}
	
	public static class WordCountReduce extends Reducer<LongWritable, Text, LongWritable, Text> {
		public void reduce(LongWritable key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
		    Iterator<Text> iterator = values.iterator();
		    while(iterator.hasNext()){
		        context.write(key,iterator.next());
		    }
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
	
	public static void main(String[] args) throws Exception {
		Configuration conf = new Configuration();
//		String readSchema = "";
//		conf.set(ReadSupport.PARQUET_READ_SCHEMA, readSchema);
		 
        Job job = Job.getInstance(conf);
        job.setJarByClass(DecodeParquetApp.class);
        job.setJobName("parquet");

        String in = args[0];
        String  out = args[1];
        
        job.setMapperClass(WordCountMap.class);
        job.setInputFormatClass(ParquetInputFormat.class);
        ParquetInputFormat.setReadSupportClass(job, MyReadSupport.class);
        ParquetInputFormat.addInputPath(job, new Path(in));
        
        job.setReducerClass(WordCountReduce.class);
        job.setOutputFormatClass(TextOutputFormat.class);
        FileOutputFormat.setOutputPath(job, new Path(out));
        
        //判断output文件夹是否存在，如果存在则删除
        Path path = new Path(out);
        //根据path找到这个文件
        FileSystem fileSystem = path.getFileSystem(conf);
        if (fileSystem.exists(path)) {
            fileSystem.delete(path, true);
            System.out.println(out+"已存在，删除");
        }
 
        job.waitForCompletion(true);


	}
	
	public static final long NANOS_PER_SECOND = 1000000000;
	public static final long SECONDS_PER_MINUTE = 60;
	public static final long MINUTES_PER_HOUR = 60;
	
	/**
	 * parquet时间戳转换long
	 * @param time
	 * @return
	 */
	public static long getTimestamp(Binary time){
		NanoTime nanoTime = NanoTime.fromBinary(time);
		int julianDay = nanoTime.getJulianDay();
		long timeOfDayNanos = nanoTime.getTimeOfDayNanos();
		JDateTime jDateTime = new JDateTime((double) julianDay);
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, jDateTime.getYear());
		calendar.set(Calendar.MONTH, jDateTime.getMonth() - 1); // java calender index starting at 1.
		calendar.set(Calendar.DAY_OF_MONTH, jDateTime.getDay());
		long remainder = timeOfDayNanos;
		int hour = (int) (remainder / (NANOS_PER_SECOND * SECONDS_PER_MINUTE * MINUTES_PER_HOUR));
		remainder = remainder % (NANOS_PER_SECOND * SECONDS_PER_MINUTE * MINUTES_PER_HOUR);
		int minutes = (int) (remainder / (NANOS_PER_SECOND * SECONDS_PER_MINUTE));
		remainder = remainder % (NANOS_PER_SECOND * SECONDS_PER_MINUTE);
		int seconds = (int) (remainder / (NANOS_PER_SECOND));
		long nanos = remainder % NANOS_PER_SECOND;
		calendar.set(Calendar.HOUR_OF_DAY, hour);
		calendar.set(Calendar.MINUTE, minutes);
		calendar.set(Calendar.SECOND, seconds);
		Timestamp ts = new Timestamp(calendar.getTimeInMillis());
		ts.setNanos((int) nanos);
		return ts.getTime();
	}


}
