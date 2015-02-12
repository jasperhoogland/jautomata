package net.jhoogland.jautomata.operations;

import java.util.Collection;

import net.jhoogland.jautomata.Automaton;
import net.jhoogland.jautomata.ExtendedTransducer;
import net.jhoogland.jautomata.TLabel;
import net.jhoogland.jautomata.Transducer;

/**
 * Transducer composition of two transducers.
 * The output of the first transducer is mapped to the input of the second transducer.
 * The result is a transducer with the input of the first transducer mapped to the output of the second transducer.
 * Transducer composition is a special case of intersection. 
 * 
 * @author Jasper Hoogland
 *
 * @param <I>
 * input label type
 * 
 * @param <L>
 * intersection label type
 * 
 * @param <O>
 * output label type
 * 
 * @param <K>
 * weight type
 * (Boolean for regular automata and Double for weighted automata)
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
	
	@Override
	protected int extensionType() 
	{		
		if (operand1 instanceof ExtendedTransducer)
		{
			if (operand2 instanceof ExtendedTransducer)
				return BOTH1;
			else 
				return EXT1;
		}
		else
		{
			if (operand2 instanceof ExtendedTransducer)
				return EXT2;
			else 
				return NONE;
		}		
	}
	
	@Override
	protected Collection<Object> transitionsOut1(Object state, Object label) 
	{		
		ExtendedTransducer<I, L, K> o1 = (ExtendedTransducer<I, L, K>) operand1;
		return o1.transitionsOutI(state, (I) label);
	}
	
	@Override
	protected Collection<Object> transitionsOut2(Object state, Object label) 
	{		
		ExtendedTransducer<L, O, K> o2 = (ExtendedTransducer<L, O, K>) operand2;
		return o2.transitionsOutO(state, (O) label);
	}
}
