package net.jhoogland.jautomata.operations;

import net.jhoogland.jautomata.Automaton;

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
