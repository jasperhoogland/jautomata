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
		return out(label);
	}

	@Override
	protected Object intersectionLabel2(TLabel<L, O> label) 
	{		
		return in(label);
	}

	@Override
	protected TLabel<I, O> label(TLabel<I, L> label1, TLabel<L, O> label2) 
	{		
		return new TLabel<I, O>(in(label1), out(label2));
	}
	
	private <I1, O1> I1 in(TLabel<I1, O1> label)
	{
		return label == null ? null : label.in();
	}

	private <I1, O1> O1 out(TLabel<I1, O1> label)
	{
		return label == null ? null : label.out();
	}
}
