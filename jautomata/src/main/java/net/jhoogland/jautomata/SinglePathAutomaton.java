package net.jhoogland.jautomata;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import net.jhoogland.jautomata.semirings.Semiring;

/**
 * 
 * 
 * @author Jasper Hoogland
 *
 * @param <L>
 * @param <K>
 */

public class SinglePathAutomaton<L, K> extends AbstractAutomaton<L, K> implements ReverselyAccessibleAutomaton<L, K>  
{
	List<L> list;

	public SinglePathAutomaton(Semiring<K> semiring, List<L> list) 
	{
		super(semiring, new IntegerComparator());		
		this.list = list;
	}

	public Collection<Object> initialStates() 
	{		
		return Arrays.asList((Object) 0);
	}

	public K initialWeight(Object state)
	{
		if (state instanceof Integer)
		{
			Integer stateInt = (Integer) state;
			return stateInt != null && stateInt.intValue() == 0 ? semiring().one() : semiring().zero();
		}
		return semiring().zero();
	}

	public K finalWeight(Object state) 
	{
		if (state instanceof Integer)
		{
			Integer stateInt = (Integer) state;
			return stateInt != null && stateInt.intValue() == list.size() ? semiring().one() : semiring().zero();
		}
		return semiring().zero();
	}
	
	@Override
	public Collection<Object> transitionsOut(Object state) 
	{		
		if (state != null && state instanceof Integer)
		{
			Integer stateInt = (Integer) state;
			return stateInt >= 0 && stateInt < list.size() ? Arrays.asList(state) : Collections.emptyList(); 
		}
		return Collections.emptyList();
	}

	public Object previousState(Object transition) 
	{
		if (transition instanceof Integer)
		{
			Integer transitionInt = (Integer) transition;
			return transitionInt != null ? transitionInt.intValue() : null;
		}
		return null;
	}

	public Object nextState(Object transition) 
	{
		if (transition instanceof Integer)
		{
			Integer transitionInt = (Integer) transition;
			return transitionInt != null ? transitionInt.intValue() + 1 : null;
		}
		return null;
	}

	public L label(Object transition) 
	{
		if (transition instanceof Integer)
		{
			Integer transitionInt = (Integer) transition;
			return transitionInt != null ? list.get(transitionInt.intValue()) : null;
		}
		return null;
	}

	public K transitionWeight(Object transition) 
	{
		if (transition instanceof Integer)
		{
			Integer transitionInt = (Integer) transition;
			return transitionInt != null && 
					transitionInt.intValue() >= 0 && 
					transitionInt.intValue() < list.size() ? 
							semiring().one() : semiring().zero();
		}
		return semiring().zero();
	}

	public Collection<Object> finalStates() 
	{		
		return Arrays.asList((Object) list.size());
	}

	public Collection<Object> transitionsIn(Object state) 
	{		
		if (state != null && state instanceof Integer)
		{
			Integer stateInt = (Integer) state;
			return stateInt >= 1 && stateInt <= list.size() ? Arrays.asList((Object) (stateInt - 1)) : Collections.emptyList(); 
		}
		return Collections.emptyList();
	}
}
