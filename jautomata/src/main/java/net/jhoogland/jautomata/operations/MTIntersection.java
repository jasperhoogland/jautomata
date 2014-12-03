package net.jhoogland.jautomata.operations;

import java.util.Collection;

import net.jhoogland.jautomata.Automaton;
import net.jhoogland.jautomata.MTAutomaton;
import net.jhoogland.jautomata.MTLabel;

public abstract class MTIntersection<I, L, K> extends Intersection<MTLabel<I, L>, MTLabel<I, L>, MTLabel<I, L>, K> implements MTAutomaton<I, L, K> 
{
	Collection<I> tapes;
	
	I intersectionTape1;
	I intersectionTape2;
	
	public MTIntersection(Automaton<MTLabel<I, L>, K> operand1, Automaton<MTLabel<I, L>, K> operand2, Collection<I> tapes, I intersectionTape1, I intersectionTape2) 
	{
		super(operand1, operand2);
		this.tapes = tapes;
		this.intersectionTape1 = intersectionTape1;
		this.intersectionTape2 = intersectionTape2;
	}

	@Override
	protected Object intersectionLabel1(MTLabel<I, L> label) 
	{		
		return label == null ? null : label.tapeLabel(intersectionTape1);
	}

	@Override
	protected Object intersectionLabel2(MTLabel<I, L> label) 
	{
		return label == null ? null : label.tapeLabel(intersectionTape1);
	}
	
	public Collection<I> tapes() 
	{		
		return tapes;
	}
}
