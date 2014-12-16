package net.jhoogland.jautomata.operations;

import java.util.Collection;

import net.jhoogland.jautomata.Automaton;
import net.jhoogland.jautomata.MTAutomaton;
import net.jhoogland.jautomata.MTLabel;

/**
 * Abstract class for label conversion operations for multi-tape automata.
 * 
 * @author Jasper Hoogland
 *
 * @param <L1>
 * operand tape label type
 * 
 * @param <I>
 * tape type
 * 
 * @param <L2>
 * operation label type
 * 
 * @param <K>
 * weight type
 * (Boolean for regular automata and Double for weighted automata)
 */

public abstract class MTLabelConversion<L1, I, L2, K> extends LabelConversion<L1, MTLabel<I, L2>, K> implements MTAutomaton<I, L2, K>
{
	Collection<I> tapes;
	
	public MTLabelConversion(Automaton<L1, K> operand, Collection<I> tapes) 
	{
		super(operand);
		this.tapes = tapes;
	}
	
	public Collection<I> tapes() 
	{		
		return tapes;
	}
}
