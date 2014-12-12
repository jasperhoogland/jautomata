package net.jhoogland.jautomata.operations;

import net.jhoogland.jautomata.Automaton;

/**
 * Implementation of acceptor intersection.
 * The weight of every string is the product of the weights of that string in the operand acceptors.
 * In the case of unweighted acceptors, the intersection accepts a string if and only if it is accepted
 * by both operands.  
 * 
 * @author Jasper Hoogland
 *
 * @param <L>
 * label type
 * 
 * @param <K>
 * weight type
 * (Boolean for regular automata and Double for weighted automata)
 */
public class AcceptorIntersection<L, K> extends Intersection<L, L, L, K> 
{
	public AcceptorIntersection(Automaton<L, K> operand1, Automaton<L, K> operand2) 
	{
		super(operand1, operand2);		
	}

	@Override
	protected Object intersectionLabel1(L label) 
	{		
		return label;
	}

	@Override
	protected Object intersectionLabel2(L label) 
	{		
		return label;
	}

	@Override
	protected L label(L label1, L label2) 
	{		
		if (label1 != null) return label1;
		else return label2;
	}
}
