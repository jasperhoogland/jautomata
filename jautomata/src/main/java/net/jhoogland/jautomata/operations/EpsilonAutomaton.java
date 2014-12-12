package net.jhoogland.jautomata.operations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import net.jhoogland.jautomata.Automaton;

public class EpsilonAutomaton<L, K> extends UnaryOperation<L, L, K, K>
{
	public EpsilonAutomaton(Automaton<L, K> operand) 
	{
		super(operand, operand.semiring());		
	}
	
	@Override
	public Collection<Object> transitionsOut(Object state) 
	{		
		Collection<Object> transitionsOut = new ArrayList<Object>();
		for (Object t : operand.transitionsOut(state))
			if (operand.label(t) == null)
				transitionsOut.add(t);
		return transitionsOut;
	}
	
//	@Override
//	public Collection<Object> transitionsOut(Object state, L label) 
//	{		
//		if (label == null) return transitionsOut(state);
//		else return Collections.emptyList();
//	}
//	
//	@Override
//	public Collection<L> labelsOut(Object state) 
//	{		
//		for (L label : operand.labelsOut(state))
//			if (label == null) return Arrays.asList(null);
//		return Collections.emptyList();
//	}

	public K initialWeight(Object state) 
	{		
		return operand.initialWeight(state);
	}

	public K finalWeight(Object state) 
	{		
		return operand.finalWeight(state);
	}

	public L label(Object transition) 
	{		
		return operand.label(transition);
	}

	public K transitionWeight(Object transition) 
	{		
		return operand.transitionWeight(transition);
	}
}
