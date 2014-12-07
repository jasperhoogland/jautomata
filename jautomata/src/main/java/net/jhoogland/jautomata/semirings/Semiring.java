package net.jhoogland.jautomata.semirings;

/**
 * 
 * Semirings must implement this interface.
 * A semiring is defined by the methods <tt>multiply(x1, x2)</tt>, <tt>add(x1, x2)</tt>,
 * <tt>one()</tt> and <tt>zero()</tt>.
 * The methods <tt>isIdempotent()</tt>, <tt>isCommutative()</tt>, 
 * and <tt>isKClosed(int k)</tt> specify properties of the semiring.
 * 
 * @author Jasper Hoogland
 *
 * @param <K>
 * the type of the elements of the semiring.
 */

public interface Semiring<K>
{
	// Definition
	
	/**
	 * @return
	 * the product of the specified weights
	 * 
	 */
	
	public K multiply(K x1, K x2);
	
	/**
	 * 
	 * @return
	 * the sum of the specified weights
	 * 
	 */
	
	public K add(K x1, K x2);
	
	/**
	 * @return
	 * the multiplication identity of this semiring
	 * 
	 */
	
	public K one();
	
	/**
	 * @return
	 * the addition identity of this semiring
	 */
	
	public K zero();
	
	// Properties
	public boolean isIdempotent();
	public boolean isCommutative();
	public boolean isKClosed(int k);
}

