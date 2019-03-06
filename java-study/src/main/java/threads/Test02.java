package threads;

import org.apache.hadoop.conf.Configuration;

public class Test02 {

	public static void main(String[] args) {
		System.out.println("start main线程");
		Configuration conf = new Configuration();
		conf.set("ADDR_ARRAY", "118.25.229.83:6379");
		conf.setBoolean("isCluster", false);
		
	
	}
}
