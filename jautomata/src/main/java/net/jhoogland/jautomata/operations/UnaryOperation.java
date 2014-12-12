package net.jhoogland.jautomata.operations;

import java.util.Collection;

import net.jhoogland.jautomata.AbstractAutomaton;
import net.jhoogland.jautomata.Automaton;
import net.jhoogland.jautomata.semirings.Semiring;

/**
 * 
 * @author Jasper Hoogland
 *
 * @param <L1>
 * Label type of the operand
 * 
 * @param <L2>
 * Label type of the resulting automaton
 * 
 * @param <K1>
 * 
 * 
 * @param <K2>
 */

public abstract class UnaryOperation<L1, L2, K1, K2> extends AbstractAutomaton<L2, K2> 
{
	Automaton<L1, K1> operand;
	
	public UnaryOperation(Automaton<L1, K1> operand, Semiring<K2> semiring) 
	{
		super(semiring, operand.topologicalOrder());
		this.operand = operand;
	}

	public Collection<Object> initialStates() 
	{
		return operand.initialStates();
	}
	
	public Collection<Object> transitionsOut(Object state) 
	{		
		return operand.transitionsOut(state);
	}
	
	public Object previousState(Object transition) 
	{		
		return operand.previousState(transition);
	}

	public Object nextState(Object transition) 
	{		
		return operand.nextState(transition);
	}	
}
