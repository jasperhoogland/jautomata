package net.jhoogland.jautomata;

import java.util.Collection;

/**
 * Multi-tape automaton types must implement this interface.  
 * 
 * @author Jasper Hoogland
 *
 * @param <T>
 * tape type
 * 
 * @param <L>
 * tape label type
 * 
 * @param <K>
 * weight type
 */

public interface MTAutomaton<T, L, K> extends Automaton<MTLabel<T, L>, K> 
{
	/**
	 * @return
	 * a {@link Collection} containing all tapes of this multi-tape automaton.
	 */
	public Collection<T> tapes();
}
