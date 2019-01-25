package mapreduce.job.examples;

import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MySortComparator extends WritableComparator {

	private static final Logger LOGGER = LoggerFactory.getLogger(MySortComparator.class);

	public MySortComparator() {
		super(TestKey.class, true);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public int compare(WritableComparable a, WritableComparable b) {
		LOGGER.info("调用了MySortComparator()....................");
		try {
			Thread.sleep(1000L);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		TestKey key1 = (TestKey) a;
		TestKey key2 = (TestKey) b;
		
		if (!key1.getEci().equals(key2.getEci())) {
			return key1.getEci().compareTo(key2.getEci());
		} else if (!key1.getMmeS1apid().equals(key2.getMmeS1apid())) {
			return key1.getMmeS1apid().compareTo(key2.getMmeS1apid());
		} else if (!key1.getTimestamp().equals(key2.getTimestamp())) {
			return key1.getTimestamp().compareTo(key2.getTimestamp());
		}
		return 0;
	}

	
	
}
