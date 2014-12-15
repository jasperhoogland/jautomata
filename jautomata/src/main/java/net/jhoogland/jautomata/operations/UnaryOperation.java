package net.jhoogland.jautomata.operations;

import java.util.Collection;

import net.jhoogland.jautomata.AbstractAutomaton;
import net.jhoogland.jautomata.Automaton;
import net.jhoogland.jautomata.semirings.Semiring;

/**
 * 
 * Abstract class for unary operations on automata.
 * 
 * @author Jasper Hoogland
 *
 * @param <L1>
 * operand label type
 * 
 * @param <L2>
 * operation label type
 * 
 * @param <K1>
 * operand weight type
 * (Boolean for regular automata and Double for weighted automata)
 * 
 * @param <K2>
 * operation weight type
 * (Boolean for regular automata and Double for weighted automata)
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
	
	public Object from(Object transition) 
	{		
		return operand.from(transition);
	}

	public Object to(Object transition) 
	{		
		return operand.to(transition);
	}	
}
