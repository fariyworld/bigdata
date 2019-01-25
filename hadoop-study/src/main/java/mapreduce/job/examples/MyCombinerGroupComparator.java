package mapreduce.job.examples;

import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyCombinerGroupComparator extends WritableComparator {

	private static final Logger LOGGER = LoggerFactory.getLogger(MyCombinerGroupComparator.class);

	public MyCombinerGroupComparator() {
		super(TestKey.class, true);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public int compare(WritableComparable a, WritableComparable b) {
		LOGGER.info("调用了MyCombinerGroupComparator()....................");
		try {
			Thread.sleep(1000L);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		TestKey key1 = (TestKey) a;
		TestKey key2 = (TestKey) b;
		
		if (!key1.getImsi().equals(key2.getImsi())) {
			return key1.getImsi().compareTo(key2.getImsi());
		} else if (!key1.getMmeS1apid().equals(key2.getMmeS1apid())) {
			return key1.getMmeS1apid().compareTo(key2.getMmeS1apid());
		} 
		return 0;
	}

	
	
}