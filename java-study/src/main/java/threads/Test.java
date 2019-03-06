package threads;

public class Test {

	public static void main(String[] args) {
		System.out.println("start main线程");
		try {
//			ThreadGroup group = new ThreadGroup("test");
//			Thread myThread1 = new Thread(group, new MyThread1(), "myThread1");
			MyThread1 myThread1 = new MyThread1();
			myThread1.start();
			Thread.sleep(1000);
			myThread1.interrupt();
//			System.out.println("myThread1线程是否停止1？ = " + myThread1.isInterrupted());
//			System.out.println("myThread1线程是否停止2？ = " + myThread1.isInterrupted());
//			System.out.println("myThread1线程是否停止1？ = " + myThread1.interrupted());
//			System.out.println("myThread1线程是否停止2？ = " + myThread1.interrupted());
//			Thread.currentThread().interrupt();
//			System.out.println("当前线程是否停止1？ = " + Thread.interrupted());
//			System.out.println("当前线程是否停止2？ = " + Thread.interrupted());
		} catch (Exception e) {
			System.out.println("main try catch");			
			e.printStackTrace();
		}
		System.out.println("end main线程");
	}

}
