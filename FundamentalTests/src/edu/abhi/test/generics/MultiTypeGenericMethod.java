/**
 * 
 */
package edu.abhi.test.generics;

/**
 * @author abhis
 *
 */
public class MultiTypeGenericMethod {
	
	public <A,B> void print(A a, B b) {
		System.out.println(a + " " + b);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String x = "The number is: ";
		Integer y = 34;
		
		MultiTypeGenericMethod obj = new MultiTypeGenericMethod();
		obj.print(x, y);
	}

}
