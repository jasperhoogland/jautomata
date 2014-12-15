package net.jhoogland.jautomata;

import java.util.Collection;
import java.util.Comparator;

import net.jhoogland.jautomata.semirings.Semiring;

/**
 * 
 * All automata (acceptors, transducers, multi-tape automata, and their weighted variants) implement this interface.
 * The methods <code>initialStates()</code>, <code>transitionsOut(state)</code>,
 * <code>initialWeight(state)</code>, <code>finalWeight(state)</code>, <code>previousState(transition)</code>, 
 * <code>nextState(transition)</code>, <code>label(transition)</code>, 
 * and <code>transitionWeight(transition)</code> define the automaton.
 * The method <code>semiring()</code> returns the {@link Semiring} over which the automaton is defined.
 * The method <code>topologicalOrder()</code> makes it possible to specify a topological order on the automaton states, if it exists.
 * Some algorithms are faster if a topological order is specified. 
 * 
 * @author Jasper Hoogland
 *
 * @param <L>
 * label type
 * 
 * @param <K>
 * weight type 
 * (Boolean for regular automata and Double for weighted automata)
 * 
 */

public interface Automaton<L, K>
{
	/**
	 * 
	 * @return
	 * Collection of initial states
	 * 
	 */
	
	public Collection<Object> initialStates();
	
	
//	/**
//	 * 
//	 * @return
//	 * Collection of labels of outgoing transitions from the specified state
//	 * 
//	 */
//	
//	public Collection<L> labelsOut(Object state);
//	
//	
//	/**
//	 * 
//	 * @return
//	 * Collection of outgoing transitions with the specified label from the specified
//	 * 
//	 */
//	
//	public Collection<Object> transitionsOut(Object state, L label);
	
	
	/**
	 * 
	 * @return
	 * Collection of outgoing transitions from the specified
	 * 
	 */
	
	public Collection<Object> transitionsOut(Object state);
	
	
	/**
	 * 
	 * @return
	 * The initial weight of the specified state if it is an initial state, 
	 * returns semiring().zero() otherwise. 
	 * 
	 */
	
	public K initialWeight(Object state);

	
	/**
	 * 
	 * @return
	 * the final weight of the specified state if it is an final state, 
	 * semiring().zero() otherwise
	 * 
	 */
	
	public K finalWeight(Object state);
	
	
	/**
	 * 
	 * @return
	 * the state from which the specified transition is an outgoing transition 
	 * 
	 */
	
	public Object from(Object transition);
	
	
	/**
	 * 
	 * @return
	 * the state to which the specified transition is an incoming transition 
	 * 
	 */
	
	public Object to(Object transition);
	
	/**
	 * 
	 * @return
	 * the label of the specified transition
	 * 
	 */
	
	public L label(Object transition);
	
	
	/**
	 * @return
	 * the weight of the specified transition
	 */
	
	public K transitionWeight(Object transition);
	
	
	/**
	 * @return
	 * the {@link Semiring} over which this automaton is defined
	 * 
	 */
	
	public Semiring<K> semiring();
	
	
	/**
	 * 
	 * @return
	 * a topological order over the states of this automaton if one is known, 
	 * <code>null</code> otherwise
	 * 
	 */
	public Comparator<Object> topologicalOrder();
}
