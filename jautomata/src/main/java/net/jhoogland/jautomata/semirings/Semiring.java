package net.jhoogland.jautomata.semirings;

/**
 * 
 * Classes that represent semirings implement this interface.
 * 
 * @author Jasper Hoogland
 *
 * @param <K>
 */

public interface Semiring<K>
{
	// Definition
	
	/**
	 * 
	 * Defines the multiplication operation for this semiring.
	 * 
	 * @param x1
	 * the first operand of the multiplication operation
	 * 
	 * @param x2
	 * the second operand of the multiplication operation
	 * 
	 * @return
	 * the product of the specified weights
	 * 
	 */
	
	public K multiply(K x1, K x2);
	
	/**
	 * 
	 * Defines the addition operation for this semiring.
	 * 
	 * @param x1
	 * the first operand of the addition operation
	 * 
	 * @param x2
	 * the second operand of the addition operation
	 * 
	 * @return
	 * the sum of the specified weights
	 * 
	 */
	
	public K add(K x1, K x2);
	
	/**
	 * 
	 * Defines the multiplication identity of this semiring. For any <tt>a</tt> of type <tt>K</tt> it holds that 
	 * <tt>multiply(multiplicationId(), a)</tt>=<tt>multiply(a, multiplicationId())</tt>=<tt>a</tt>.
	 * 
	 * @return
	 * the multiplication identity of this semiring
	 * 
	 */
	
	public K one();
	
	/**
	 * 
	 * Defines the addition identity of this semiring. For any <tt>a</tt> of type <tt>K</tt> it must  that 
	 * <tt>add(additionId(), a)</tt>=<tt>add(a, additionId())</tt>=<tt>a</tt>.
	 * 
	 * @return
	 * the addition identity of this semiring
	 * 
	 */
	
	public K zero();
	
	// Properties
	public boolean isIdempotent();
	public boolean isCommutative();
	public boolean isKClosed(int k);
}

