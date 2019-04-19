/**
 * 
 */
package edu.abhi.test;

/**
 * @author abhis
 *
 */
public class TestClass {

	/**
	 * @param args
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws InterruptedException {
		
		for(int i=0; i<=10; i++) {
		main1();
		
		main2();
		}
		//Thread.sleep(500);
	
	}

	private static void main1() throws InterruptedException {
		Thread.sleep(2000);
		
	}

	private static void main2() throws InterruptedException {
		Thread.sleep(1000);
		
	}
	
}
