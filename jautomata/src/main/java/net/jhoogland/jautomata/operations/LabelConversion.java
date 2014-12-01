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
	public Collection<L2> labelsOut(Object state) 
	{
		Collection<L1> ls1 = operand.labelsOut(state);
		Collection<L2> ls2 = new HashSet<L2>();
		for (L1 l1 : ls1) ls2.add(newLabel(l1));
		return ls2;
	}
	
	@Override
	public Collection<Object> transitionsOut(Object state, L2 label) 
	{		
		return operand.transitionsOut(state, oldLabel(label));
	}
	
	public abstract L2 newLabel(L1 label);
	public abstract L1 oldLabel(L2 label);

	public K transitionWeight(Object transition) 
	{		
		return operand.transitionWeight(transition);
	}
}
