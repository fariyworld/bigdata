package test.parquet;

import java.sql.Timestamp;
import java.util.Calendar;

import org.apache.hadoop.fs.Path;
import org.apache.parquet.example.data.Group;
import org.apache.parquet.example.data.simple.NanoTime;
import org.apache.parquet.hadoop.ParquetReader;
import org.apache.parquet.hadoop.example.GroupReadSupport;
import org.apache.parquet.io.api.Binary;

import jodd.datetime.JDateTime;

public class ParquetReadTest {

	public static final long NANOS_PER_SECOND = 1_000_000_000;
	public static final long SECONDS_PER_MINUTE = 60;
	public static final long MINUTES_PER_HOUR = 60;

	public static void main(String[] args) throws Exception {
		parquetReader("D:\\BONC\\Shanxi\\data\\parquet\\002186_0.0");
	}

	private static void parquetReader(String inPath) throws Exception {
		GroupReadSupport readSupport = new GroupReadSupport();
		ParquetReader<Group> reader = ParquetReader.builder(readSupport, new Path(inPath)).build();
		Group line = null;
		while ((line = reader.read()) != null) {
			System.out.println(getTimestamp(line.getInt96("start_time", 0)));
			return;
		}
	}

	public static long getTimestamp(Binary time) {
		System.out.println(time);
		NanoTime nanoTime = NanoTime.fromBinary(time);
		System.out.println(nanoTime);
		int julianDay = nanoTime.getJulianDay();
		long timeOfDayNanos = nanoTime.getTimeOfDayNanos();
		JDateTime jDateTime = new JDateTime((double) julianDay);
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, jDateTime.getYear());
		// java calender index starting at 1.
		calendar.set(Calendar.MONTH, jDateTime.getMonth() - 1);
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
		System.out.println(ts);
		return ts.getTime();
	}
}
