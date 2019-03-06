package threads;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MutilThreadOpsRedis implements Runnable {

	private static final Logger LOGGER = LoggerFactory.getLogger(MutilThreadOpsRedis.class);
	
	@Override
	public void run() {
		Test04.test1();
	}
}
