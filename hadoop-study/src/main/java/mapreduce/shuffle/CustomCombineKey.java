package mapreduce.shuffle;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.WritableComparable;
import org.apache.log4j.Logger;

import business.entity.Person;

/**
 * 分区、快排、分组、二次排序（默认会调用key里定义的方法）
 *	
 */
public class CustomCombineKey implements WritableComparable<CustomCombineKey> {
	
	private static final Logger LOG = Logger.getLogger(CustomCombineKey.class);
	
	private Text name;
	private IntWritable age;
	private Text info;
	
	public CustomCombineKey() {
		name = new Text();
		age = new IntWritable();
		info = new Text();
	}

	public CustomCombineKey(Text name, IntWritable age, Text info) {
		super();
		this.name = name;
		this.age = age;
		this.info = info;
	}
	
	/**
	 * 序列化 -- 写
	 */
	@Override
	public void write(DataOutput out) throws IOException {
		this.name.write(out);
		this.age.write(out);
		this.info.write(out);
	}
	
	/**
	 * 序列化 -- 读
	 */
	@Override
	public void readFields(DataInput in) throws IOException {
		this.name.readFields(in);
		this.age.readFields(in);
		this.info.readFields(in);
	}
	
	/**
	 * 比较
	 */
	@Override
	public int compareTo(CustomCombineKey other) {
		LOG.info("quickSort() 调用了compareTo......");
		if(!this.age.equals(other.getAge())){
			return this.age.compareTo(other.getAge());
		}else if(!this.info.equals(other.getInfo())){
			return this.info.compareTo(other.getInfo());
		}
		return 0;
	}
	
	@Override
	public int hashCode() {
		LOG.info("HashParatition 调用了hashcode......");
		final int prime = 31;
		int result = 1;
		result = prime * result + ((age == null) ? 0 : age.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		LOG.info("调用了equals......");
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CustomCombineKey other = (CustomCombineKey) obj;
		if (age == null) {
			if (other.age != null)
				return false;
		} else if (!age.equals(other.age))
			return false;
		return true;
	}
	
	public Text getName() {
		return name;
	}

	public void setName(Text name) {
		this.name = name;
	}

	public IntWritable getAge() {
		return age;
	}

	public void setAge(IntWritable age) {
		this.age = age;
	}
	
	public Text getInfo() {
		return info;
	}

	public void setInfo(Text info) {
		this.info = info;
	}

	public CustomCombineKey getKey(Person person){
		this.name.set(person.getName());
		this.age.set(person.getAge());
		return this;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("CustomCombineKey [name=");
		builder.append(name);
		builder.append(", age=");
		builder.append(age);
		builder.append(", info=");
		builder.append(info);
		builder.append("]");
		return builder.toString();
	}
	
	
}
