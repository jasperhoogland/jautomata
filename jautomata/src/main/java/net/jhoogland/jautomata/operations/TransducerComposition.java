package net.jhoogland.jautomata.operations;

import net.jhoogland.jautomata.Automaton;
import net.jhoogland.jautomata.TLabel;
import net.jhoogland.jautomata.Transducer;

/**
 * 
 * 
 * @author Jasper Hoogland
 *
 * @param <I>
 * @param <L>
 * @param <O>
 * @param <K>
 */

public class TransducerComposition<I, L, O, K> extends Intersection<TLabel<I, L>, TLabel<L, O>, TLabel<I, O>, K> implements Transducer<I, O, K>
{
	public TransducerComposition(Automaton<TLabel<I, L>, K> operand1, Automaton<TLabel<L, O>, K> operand2) 
	{
		super(operand1, operand2);		
	}

	@Override
	protected Object intersectionLabel1(TLabel<I, L> label) 
	{		
		return label.out();
	}

	@Override
	protected Object intersectionLabel2(TLabel<L, O> label) 
	{		
		return label.in();
	}

	@Override
	protected TLabel<I, O> label(TLabel<I, L> label1, TLabel<L, O> label2) 
	{		
		return new TLabel<I, O>(label1.in(), label2.out());
	}
}
