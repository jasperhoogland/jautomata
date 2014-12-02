package net.jhoogland.jautomata.operations;


import java.util.Collection;
import java.util.Comparator;

import net.jhoogland.jautomata.Automaton;
import net.jhoogland.jautomata.semirings.Semifield;

/**
 * 
 * Reweights an automaton in such a way that the path weights are preserved.
 * 
 * @author Jasper Hoogland
 *
 * @param <L>
 * label type
 * 
 * @param <K>
 * the type over which the semiring is defined
 * 
 */

public abstract class Reweight<L, K> extends UnaryOperation<L, L, K, K> 
{
	public Reweight(Automaton<L, K> operand) 
	{
		super(operand, operand.semiring());
		if (! (operand.semiring() instanceof Semifield)) throw new RuntimeException("Semiring is not a semifield.");
	}
	
	public abstract K potential(Object state);

	@Override
	public Collection<Object> initialStates() 
	{		
		return operand.initialStates();
	}

	@Override
	public Collection<Object> transitionsOut(Object state) 
	{		
		return operand.transitionsOut(state);
	}

	public K initialWeight(Object state) 
	{		
		return semiring().multiply(operand.initialWeight(state), potential(state));
	}

	public K finalWeight(Object state) 
	{
		Semifield<K> sr = (Semifield<K>) semiring();
		return sr.multiply(sr.inverse(potential(state)), operand.finalWeight(state));
	}

	@Override
	public Object previousState(Object transition) 
	{
		return operand.previousState(transition);
	}

	@Override
	public Object nextState(Object transition) 
	{
		return operand.nextState(transition);
	}

	public L label(Object transition) 
	{		
		return operand.label(transition);
	}

	public K transitionWeight(Object transition) 
	{
		Semifield<K> sr = (Semifield<K>) semiring();
		return sr.multiply(sr.inverse(potential(operand.previousState(transition))), 
				sr.multiply(operand.transitionWeight(transition), potential(operand.nextState(transition))));
	}

	@Override
	public Comparator<Object> topologicalOrder() 
	{		
		return operand.topologicalOrder();
	}
}
