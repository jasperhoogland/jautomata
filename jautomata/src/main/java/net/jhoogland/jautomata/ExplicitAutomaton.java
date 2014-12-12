package net.jhoogland.jautomata;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import net.jhoogland.jautomata.semirings.Semiring;

/**
 * Subclasses of this abstract class implement automaton types that explicitly store information of states 
 * (initial weight, final weight, incoming/outgoing transitions) and transitions 
 * (previous/next state, label, weight).    
 * 
 * @author Jasper Hoogland
 *
 * @param <L>
 * label type
 * 
 * @param <K>
 * weight type
 */
public abstract class ExplicitAutomaton<L, K> extends AbstractAutomaton<L, K>  implements ReverselyAccessibleAutomaton<L, K>
{
	protected Collection<Object> initialStates;
	protected Collection<Object> finalStates;	
	
	public ExplicitAutomaton(Semiring<K> semiring) 
	{
		super(semiring);
	}

	public Collection<Object> initialStates() 
	{		
		return initialStates;
	}

	public K initialWeight(Object state) 
	{		
		return getState(state).initialWeight();
	}

	public K finalWeight(Object state) 
	{		
		return getState(state).finalWeight();
	}
	
	public Collection<Object> transitionsOut(Object state)
	{		
		return getState(state).transitionsOut();
	}

	public Object previousState(Object transition) 
	{
		return getTransition(transition).previousState();
	}

	public Object nextState(Object transition) 
	{		
		return getTransition(transition).nextState();
	}

	public L label(Object transition) 
	{
		return getTransition(transition).label();
	}

	public K transitionWeight(Object transition)
	{		
		return getTransition(transition).weight();
	}
	
	public Collection<Object> finalStates() 
	{
		return finalStates;
	}
	
	public boolean isReverselyAccessible() 
	{		
		return true;
	}

	public Collection<Object> transitionsIn(Object state)
	{		
		return getState(state).transitionsIn();
	}

	/**
	 * @return
	 * An instance of {@link BasicState} that contains all information on the specified state
	 */
	protected abstract BasicState<K> getState(Object state);
	
	/**
	 * @return
	 * An instance of {@link BasicTransition} that contains all information on the specified transition
	 */
	protected abstract BasicTransition<L, K> getTransition(Object state);
}