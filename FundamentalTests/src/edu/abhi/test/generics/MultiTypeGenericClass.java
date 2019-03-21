/**
 * 
 */
package edu.abhi.test.generics;

/**
 * @author abhis
 *
 */
public class MultiTypeGenericClass<A,B> {
	
	public void print(A a, B b) {
		System.out.println(a + " " + b);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String x = "The number is: ";
		Integer y = 33;
		
		MultiTypeGenericClass<String, Integer> obj = new MultiTypeGenericClass<>();
		obj.print(x, y);
	}

}
