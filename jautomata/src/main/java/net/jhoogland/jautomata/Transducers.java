package net.jhoogland.jautomata;

import java.util.List;

import net.jhoogland.jautomata.operations.Concatenation;
import net.jhoogland.jautomata.operations.LabelConversion;
import net.jhoogland.jautomata.operations.Operations;
import net.jhoogland.jautomata.operations.TransducerLabelConversion;

/**
*
* This class contains static methods for finite state transducers.
* 
* @author Jasper Hoogland
*
*/

public class Transducers 
{
	public static <I, O, K> Automaton<O, K> apply(Automaton<TLabel<I, O>, K> transducer, List<I> string)
	{		
		Automaton<TLabel<I, I>, K> t = identity(Automata.createSinglePathAutomaton(transducer.semiring(), string));
		return outputAcceptor(Operations.transducerComposition(t, transducer));
	}
	
	public static <O, K> Automaton<O, K> apply(Automaton<TLabel<Character, O>, K> transducer, String string)
	{
		return apply(transducer, Automata.toCharacterList(string));
	}
	
	public static <I, O, K> Automaton<I, K> inputAcceptor(Automaton<TLabel<I, O>, K> transducer)
	{
		return new LabelConversion<TLabel<I, O>, I, K>(transducer)
		{
			@Override
			public I newLabel(TLabel<I, O> label) 
			{
				return label == null ? null : label.in();
			}
		};
	}
	
	public static <I, O, K> Automaton<O, K> outputAcceptor(Automaton<TLabel<I, O>, K> transducer)
	{
		return new LabelConversion<TLabel<I, O>, O, K>(transducer)
		{
			@Override
			public O newLabel(TLabel<I, O> label) 
			{
				return label == null ? null : label.out();
			}
		};
	}
	
	public static <I, O, K> Transducer<I, O, K> swap(Automaton<TLabel<O, I>, K> transducer)
	{
		return new TransducerLabelConversion<TLabel<O, I>, I, O, K>(transducer)
		{
			@Override
			public TLabel<I, O> newLabel(TLabel<O, I> label) 
			{
				if (label == null) return null;
				else return new TLabel<I, O>(label.out(), label.in());
			}
		};
	}
	
	public static <L, K> Transducer<L, L, K> identity(Automaton<L, K> acceptor)
	{
		return new TransducerLabelConversion<L, L, L, K>(acceptor) 
		{
			@Override
			public TLabel<L, L> newLabel(L label) 
			{
				if (label == null) return null;
				else return new TLabel<L, L>(label, label);
			}
		};
	}
	
	public static <I, O, K> Transducer<I, O, K> inputTransducer(Automaton<I, K> acceptor)
	{
		return new TransducerLabelConversion<I, I, O, K>(acceptor)
		{
			@Override
			public TLabel<I, O> newLabel(I label) 
			{
				return new TLabel<I, O>(label, null);
			}
		};
	}
	
	public static <I, O, K> Transducer<I, O, K> outputTransducer(Automaton<O, K> acceptor)
	{
		return new TransducerLabelConversion<O, I, O, K>(acceptor)
		{
			@Override
			public TLabel<I, O> newLabel(O label) 
			{
				return new TLabel<I, O>(null, label);
			}
		};
	}
	
	public static <I, O, K> Transducer<I, O, K> concat(final Automaton<TLabel<I, O>, K>... operands)
	{
		class TransducerConcatenation extends Concatenation<TLabel<I, O>, K> implements Transducer<I, O, K>
		{
			public TransducerConcatenation() 
			{
				super(operands);
			}
		}		
		return new TransducerConcatenation();
	}
}
