package net.jhoogland.jautomata;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.jhoogland.jautomata.operations.LabelConversion;
import net.jhoogland.jautomata.operations.TransducerLabelConversion;
import net.jhoogland.jautomata.operations.MTLabelConversion;

/**
 * 
 * Class with static methods to perform operations on multi-tape automata.
 * 
 * @author Jasper Hoogland
 *
 */

public class MTAutomata 
{
	public static <T, L, K> Automaton<L, K> toAcceptor(Automaton<MTLabel<T, L>, K> mtAutomaton, final T tape)
	{
		return new LabelConversion<MTLabel<T, L>, L, K>(mtAutomaton) 
		{
			@Override
			public L newLabel(MTLabel<T, L> label) 
			{
				return label == null ? null : label.tapeLabel(tape);
			}
		};
	}
	
	public static <T, L, K> Transducer<L, L, K> toTransducer(Automaton<MTLabel<T, L>, K> mtAutomaton, final T inputTape, final T outputTape)
	{
		return new TransducerLabelConversion<MTLabel<T, L>, L, L, K>(mtAutomaton) 
		{
			@Override
			public TLabel<L, L> newLabel(MTLabel<T, L> label)
			{				
				return label == null ? null : new TLabel<L, L>(label.tapeLabel(inputTape), label.tapeLabel(outputTape));
			}
		};
	}	
	
	public static <T, L, K> MTAutomaton<T, L, K> acceptorToHashMT(Automaton<L, K> acceptor, final T tape)
	{
		return new MTLabelConversion<L, T, L, K>(acceptor, Arrays.asList(tape))
		{
			@Override
			public MTLabel<T, L> newLabel(L label) 
			{
				Map<T, L> mtLabel = new HashMap<T, L>();
				mtLabel.put(tape, label);
				return new HashMTLabel<T, L>(mtLabel);
			}
		}; 
	}
	
	public static <T, L, K> MTAutomaton<T, L, K> transducerToHashMT(Automaton<TLabel<L, L>, K> transducer, final T tape1, final T tape2)
	{
		return new MTLabelConversion<TLabel<L, L>, T, L, K>(transducer, Arrays.asList(tape1, tape2))
		{
			@Override
			public MTLabel<T, L> newLabel(TLabel<L, L> label) 
			{
				Map<T, L> mtLabel = new HashMap<T, L>();
				if (label != null)
				{
					mtLabel.put(tape1, label.in());
					mtLabel.put(tape2, label.out());					
				}
				return new HashMTLabel<T, L>(mtLabel);
			}			
		}; 
	}


	public static <L, K> MTAutomaton<Integer, L, K> acceptorArrayMT(Automaton<L, K> acceptor)
	{
		return new MTLabelConversion<L, Integer, L, K>(acceptor, Arrays.asList(0))
		{
			@Override
			public MTLabel<Integer, L> newLabel(L label) 
			{
				return new ArrayMTLabel<L>(label);
			}
		}; 
	}
	
	public static <L, K> MTAutomaton<Integer, L, K> transducerToArrayMT(Automaton<TLabel<L, L>, K> transducer)
	{
		return new MTLabelConversion<TLabel<L, L>, Integer, L, K>(transducer, Arrays.asList(0, 1))
		{
			@Override
			public MTLabel<Integer, L> newLabel(TLabel<L, L> label) 
			{
				return label == null ? null : new ArrayMTLabel<L>(label.in(), label.out());
			}
		}; 
	}
		
	public static <T, L, K> MTAutomaton<T, L, K> projectHashMT(Automaton<MTLabel<T, L>, K> mta, final Collection<T> tapes)
	{
		return new MTLabelConversion<MTLabel<T, L>, T, L, K>(mta, tapes)
		{
			@Override
			public MTLabel<T, L> newLabel(MTLabel<T, L> label) 
			{				
				Map<T, L> projection = new HashMap<T, L>();
				if (label != null)
					for (T tape : tapes) projection.put(tape, label.tapeLabel(tape));															
				return new HashMTLabel<T, L>(projection);
			}
		}; 
	}
	
	public static <L, K> MTAutomaton<Integer, L, K> projectArrayMT(Automaton<MTLabel<Integer, L>, K> mta, final List<Integer> tapes)
	{
		return new MTLabelConversion<MTLabel<Integer, L>, Integer, L, K>(mta, tapes)
		{
			@Override
			public MTLabel<Integer, L> newLabel(MTLabel<Integer, L> label) 
			{		
				L[] labels = (L[]) new Object[tapes.size()];
				if (label != null)
				{
					int i = 0;
					for (Integer index : tapes)
					{
						labels[i++] = label.tapeLabel(index);					
					}					
				}
				return new ArrayMTLabel<L>(labels);
			}
		}; 
	}
}
