package net.jhoogland.jautomata;

import java.util.Collection;

/**
 * <p>
 * Automata classes that implement this interface provide methods to iterate over an automaton 
 * in the reverse direction, starting at the final states via incoming transitions. 
 * Normal automata (that do not implement this interface) only provide methods to visit states and transitions
 * in the forward direction, starting at the initial states via outgoing transitions.
 * In order to make an automaton reversely accessible, the methods finalStates() and transitionsIn(state) must
 * be implemented.
 * </p>
 * <p>
 * The method isReverselyAccessible() yields true if an instance is indeed reversely accessible.
 * Some classes that implement this interface are only reversely accessible if certain conditions are met.
 * For example, an automaton class that performs a unary operation on an operand automaton may only be
 * reversely accessible if the operand is reversely accessible as well.
 * </p> 
 * 
 * @author Jasper Hoogland
 *
 * @param <L>
 * label type
 * 
 * @param <K>
 * weight type 
 * (Boolean for regular automata and Double for weighted automata)
 */

public interface ReverselyAccessibleAutomaton<L, K> extends Automaton<L, K>
{
	/**
	 * 
	 * @return
	 * Collection of initial states
	 * 
	 */
	
	public Collection<Object> finalStates();
	
	
	/**
	 * 
	 * @return
	 * Collection of incoming transitions from the specified state
	 * 
	 */
	
	public Collection<Object> transitionsIn(Object state);
	
	/**
	 * @return
	 * <code>true</code> if the implementation is indeed reversely accessible, <code>false</code> otherwise
	 */
	
	public boolean isReverselyAccessible();
}
