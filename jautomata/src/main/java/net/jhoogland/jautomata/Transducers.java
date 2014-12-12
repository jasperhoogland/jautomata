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
	/**
	 * Applies a string to the input tape of the specified transducer and returns the resulting acceptor
	 * of the output labels.  
	 */
	
	public static <I, O, K> Automaton<O, K> apply(Automaton<TLabel<I, O>, K> transducer, List<I> string)
	{		
		Automaton<TLabel<I, I>, K> t = identity(Automata.createSingleStringAutomaton(transducer.semiring(), string));
		return outputAcceptor(Operations.transducerComposition(t, transducer));
	}
	
	/**
	 * Applies a string to the input tape of the specified transducer and returns the resulting acceptor
	 * of the output labels.  
	 */
	
	public static <O, K> Automaton<O, K> apply(Automaton<TLabel<Character, O>, K> transducer, String string)
	{
		return apply(transducer, Automata.toCharacterList(string));
	}
	
	/**
	 * @return
	 * the acceptor that is the projection of the specified transducer on the input tape 
	 */
	
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
	
	/**
	 * @return
	 * the acceptor that is the projection of the specified transducer on the output tape 
	 */
	
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
	
	/**
	 * @return
	 * a transducer that swaps the input and output tapes of the specified transducer  
	 */
	
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
	
	/**
	 * @return
	 * a transducer with the input and output tapes identical to the specified acceptor
	 */
	
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
	
	/**
	 * @return
	 * a transducer with the input tape identical to the specified acceptor and the empty string on the output tape
	 */	
	
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
	
	/**
	 * @return
	 * a transducer with the output tape identical to the specified acceptor and the empty string on the input tape
	 */	
	
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
	
	/**
	 * @return
	 * a transducer that concatenates the specified transducers
	 */
	
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
