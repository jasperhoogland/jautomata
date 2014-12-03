package net.jhoogland.jautomata.operations;

import java.util.Collection;

import net.jhoogland.jautomata.Automaton;
import net.jhoogland.jautomata.MTAutomaton;
import net.jhoogland.jautomata.MTLabel;

public abstract class MTLabelConversion<L1, I, L2, K> extends LabelConversion<L1, MTLabel<I, L2>, K> implements MTAutomaton<I, L2, K>
{
	Collection<I> tapes;
	
	public MTLabelConversion(Automaton<L1, K> operand, Collection<I> tapes) 
	{
		super(operand);
	}
	
	public Collection<I> tapes() 
	{		
		return tapes;
	}
}
