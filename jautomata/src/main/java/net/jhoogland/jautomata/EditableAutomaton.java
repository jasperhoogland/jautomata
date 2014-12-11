package net.jhoogland.jautomata;
import java.util.HashMap;

import net.jhoogland.jautomata.semirings.Semiring;

/**
 * 
 * An automaton type that can be edited using the <code>addState</code> and 
 * <code>addTransition</code> methods.
 * <code>addState</code> returns the state Id for the added state, 
 * which can be passed to the <code>addTransition</code> to specify the previous and next
 * state of the transition.
 * 
 * @author Jasper Hoogland
 *
 * @param <L>
 * label type
 * 
 * @param <K>
 * weight type
 */

public class EditableAutomaton<L, K> extends HashAutomaton<L, K> 
{
	int stateId;
	int transitionId;
	
	public EditableAutomaton(Semiring<K> semiring) 
	{
		super(new HashMap<Object, BasicState<K>>(), new HashMap<Object, BasicTransition<L, K>>(), semiring);
	}
	
	public int addState(K initialWeight, K finalWeight)
	{
		int sid = stateId;
		addState(sid, initialWeight, finalWeight);
		stateId++;
		return sid;
	}
	
	public int addTransition(int from, int to, L label, K weight) 
	{
		int tid = transitionId;		
		addTransition(tid, from, to, label, weight);
		transitionId++;
		return tid;
	}

	public int addTransition(int from, int to, L label) 
	{
		return addTransition(from, to, label, semiring().one());		
	}
}
