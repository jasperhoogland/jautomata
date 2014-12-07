package net.jhoogland.jautomata.semirings;

import net.jhoogland.jautomata.operations.Determinization;

/**
 * 
 * Semifields must implement this interface. 
 * A semifield is a semiring with a multiplicative inverse. 
 * The real semiring and the tropical semiring are examples of semifields. 
 * The multiplicative inverse is used by the {@link Determinization} operation. 
 * 
 * @author Jasper Hoogland
 *
 * @param <K>
 * the type of the elements of the semifield
 */

public interface Semifield<K> extends Semiring<K> 
{
	/**
	 * @return
	 * the multiplicative inverse of the specified weight
	 */
	
	K inverse(K x);
}
