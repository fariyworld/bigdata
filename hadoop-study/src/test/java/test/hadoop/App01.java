package test.hadoop;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.IntWritable;
import org.junit.Test;

public class App01 {

	@Test
	public void getCodec(){
		String type = "a";
		
		switch (type) {
		case "a": {
			System.out.println("a");
//			return;
//			break;
		}
		case "b": {
			System.out.println("b");
//			break;
		}
		default: {
			System.out.println("default");
			break;
		}
		}
	}
	
	
	@Test
	public void test02(){
		Configuration conf = new Configuration();
		conf.set("mme_sep", "\\|");
		String string = "a|\\N|c";
		String sep = conf.get("mme_sep");
		String[] split = string.split(sep, -1);
		System.out.println(split[0]);
		System.out.println(split.length);
		
		System.out.println("\\N".equals(split[1]));
		IntWritable intWritable = new IntWritable(1);
		System.out.println(intWritable.hashCode());
		
		
	}
}
