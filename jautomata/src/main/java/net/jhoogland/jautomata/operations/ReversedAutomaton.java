package net.jhoogland.jautomata.operations;

import java.util.Collection;

import net.jhoogland.jautomata.ReverselyAccessibleAutomaton;

public class ReversedAutomaton<L, K> extends UnaryOperation<L, L, K, K> implements ReverselyAccessibleAutomaton<L, K> 
{
	public ReversedAutomaton(ReverselyAccessibleAutomaton<L, K> operand) 
	{
		super(operand, operand.semiring());
	}
	
	private ReverselyAccessibleAutomaton<L, K> operand()
	{
		return (ReverselyAccessibleAutomaton<L, K>) operand;
	}
	
	@Override
	public Collection<Object> initialStates() 
	{		
		return operand().finalStates();
	}
	
	public Collection<Object> finalStates() 
	{		
		return operand().initialStates();
	}
	
	@Override
	public Collection<Object> transitionsOut(Object state) 
	{		
		return operand().transitionsIn(state);
	}
	
	public Object previousState(Object transition) 
	{		
		return operand().nextState(transition);
	}

	public Object nextState(Object transition) 
	{		
		return operand().previousState(transition);
	}	

	public K initialWeight(Object state) 
	{		
		return operand().finalWeight(state);
	}

	public K finalWeight(Object state) 
	{		
		return operand().initialWeight(state);
	}

	public L label(Object transition) 
	{		
		return operand.label(transition);
	}

	public K transitionWeight(Object transition) 
	{		
		return operand.transitionWeight(transition);
	}

	public Collection<Object> transitionsIn(Object state) 
	{		
		return operand.transitionsOut(state);
	}
}
