package net.jhoogland.jautomata.semirings;

import net.jhoogland.jautomata.operations.Determinization;

/**
 * 
 * Semirings that are also a semifield implement this interface. A semifield is a semiring with
 * a multiplicative inverse. The real semiring and the tropical semiring are examples of 
 * semifields. The multiplicative inverse is used by the {@link Determinization} algorithm. 
 * 
 * @author Jasper Hoogland
 *
 * @param <K>
 * the type over which the semiring is defined.
 */

public interface Semifield<K> extends Semiring<K> 
{
	/**
	 * Computes the multiplicative inverse of a weight.
	 * 
	 * @param x
	 * the weight of which the inverse is to be returned
	 * 
	 * @return
	 * the multiplicative inverse of the specified weight
	 */
	
	K inverse(K x);
}
