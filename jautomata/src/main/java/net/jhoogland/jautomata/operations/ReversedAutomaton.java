package net.jhoogland.jautomata.operations;

import java.util.Collection;

import net.jhoogland.jautomata.ReverselyAccessibleAutomaton;

/**
 * 
 * Implementation of automaton reversal. 
 * The operation reverses the direction of all transitions, and swaps the initial and final weights.
 * The result is an automaton that accepts the reversed strings that are accepted by the operand. 
 * 
 * This operation requires the operand automaton to be reversely accessible, 
 * i.e. it must implement the {@link ReverselyAccessibleAutomaton} interface 
 * and the isReverselyAccessible() method must yield <code>true</code>.
 * 
 * @author Jasper Hoogland
 *
 * @see
 * ReverselyAccessibleAutomaton
 * 
 * @param <L>
 * label type
 * 
 * @param <K>
 * weight type 
 * (Boolean for regular automata and Double for weighted automata)
 */

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
	
	public Object from(Object transition) 
	{		
		return operand().to(transition);
	}

	public Object to(Object transition) 
	{		
		return operand().from(transition);
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
	
	public boolean isReverselyAccessible() 
	{		
		return true;
	}
}
