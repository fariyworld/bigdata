package threads;

public class MyThread1 extends Thread {

	@Override
	public void run() {
		for (int i = 0; i < 500000; i++) {
			if (this.interrupted()) {
				
			}
			System.out.println("i="+ (i+1));
		}
	}
}
