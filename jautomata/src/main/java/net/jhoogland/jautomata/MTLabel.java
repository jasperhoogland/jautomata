package net.jhoogland.jautomata;

import java.util.Collection;

/**
 * 
 * The label type of multi-tape automata.
 * Multi-tape label types must implement this interface.
 * 
 * @author Jasper Hoogland
 *
 * @param <T>
 * tape type
 * 
 * @param <L>
 * tape label type
 * 
 */

public interface MTLabel<T, L> 
{
	/**
	 * @return
	 * the tape label of the specified tape
	 */
	
	public L tapeLabel(T tape);
	
	/**
	 * @return
	 * set of tapes of this multi-tape label
	 */
	
	public Collection<T> tapes();
}
