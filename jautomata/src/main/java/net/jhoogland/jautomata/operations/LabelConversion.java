package net.jhoogland.jautomata.operations;

import java.util.Collection;
import java.util.HashSet;

import net.jhoogland.jautomata.Automaton;

public abstract class LabelConversion<L1, L2, K> extends UnaryOperation<L1, L2, K, K>  
{
	public LabelConversion(Automaton<L1, K> operand) 
	{
		super(operand, operand.semiring());		
	}

	public K initialWeight(Object state) 
	{		
		return operand.initialWeight(state);
	}

	public K finalWeight(Object state) 
	{		
		return operand.finalWeight(state);
	}

	public L2 label(Object transition) 
	{		
		L1 l1 = operand.label(transition);		
		return newLabel(l1);
	}
	
	@Override
	public Collection<Object> transitionsOut(Object state) 
	{		
		return super.transitionsOut(state);
	}
	
	public abstract L2 newLabel(L1 label);
	
	public K transitionWeight(Object transition) 
	{		
		return operand.transitionWeight(transition);
	}
}
