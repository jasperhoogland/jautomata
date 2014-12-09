package net.jhoogland.jautomata.semirings;

/**
 * 
 * Semirings must implement this interface.
 * A semiring is defined by the methods <code>multiply(x1, x2)</code>, <code>add(x1, x2)</code>,
 * <code>one()</code> and <code>zero()</code>.
 * The methods <code>isIdempotent()</code>, <code>isCommutative()</code>, 
 * and <code>isKClosed(int k)</code> specify properties of the semiring.
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

