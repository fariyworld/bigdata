package business.entity;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.Serializable;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.WritableComparable;

public class DateTemperaturePair implements Serializable, WritableComparable<DateTemperaturePair> {
	
	private static final long serialVersionUID = -8948445804778528466L;
	private Text yearMonth;//2018-01
	private Text day;//10
	private DoubleWritable temperature;//-23.3
	
	public DateTemperaturePair() {
		this.yearMonth = new Text();
		this.day = new Text();
		this.temperature = new DoubleWritable();
	}

	public DateTemperaturePair(Text yearMonth, Text day, DoubleWritable temperature) {
		this.yearMonth = yearMonth;
		this.day = day;
		this.temperature = temperature;
	}

	@Override
	public void write(DataOutput out) throws IOException {
		this.yearMonth.write(out);
		this.day.write(out);
		this.temperature.write(out);
	}

	@Override
	public void readFields(DataInput in) throws IOException {
		this.yearMonth.readFields(in);
		this.day.readFields(in);
		this.temperature.readFields(in);
	}

	@Override
	public int compareTo(DateTemperaturePair other) {
		int compareValue = this.yearMonth.compareTo(other.yearMonth);
		if (compareValue == 0) {
			compareValue = this.temperature.compareTo(other.temperature);
		}
		return compareValue;//升序
//		return -compareValue;//降序
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
//		result = prime * result + ((day == null) ? 0 : day.hashCode());
//		result = prime * result + ((temperature == null) ? 0 : temperature.hashCode());
		result = prime * result + ((yearMonth == null) ? 0 : yearMonth.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DateTemperaturePair other = (DateTemperaturePair) obj;
		if (day == null) {
			if (other.day != null)
				return false;
		} else if (!day.equals(other.day))
			return false;
		if (temperature == null) {
			if (other.temperature != null)
				return false;
		} else if (!temperature.equals(other.temperature))
			return false;
		if (yearMonth == null) {
			if (other.yearMonth != null)
				return false;
		} else if (!yearMonth.equals(other.yearMonth))
			return false;
		return true;
	}

	public Text getYearMonth() {
		return yearMonth;
	}

	public void setYearMonth(Text yearMonth) {
		this.yearMonth = yearMonth;
	}

	public Text getDay() {
		return day;
	}

	public void setDay(Text day) {
		this.day = day;
	}

	public DoubleWritable getTemperature() {
		return temperature;
	}

	public void setTemperature(DoubleWritable temperature) {
		this.temperature = temperature;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(yearMonth);
		builder.append(",");
		builder.append(day);
		builder.append(",");
		builder.append(temperature);
		return builder.toString();
	}
	
	public void reset(){
		yearMonth = new Text();
		day = new Text();
		temperature = new DoubleWritable();
	}
}
