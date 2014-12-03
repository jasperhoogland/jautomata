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
	public static <I, L, K> Automaton<L, K> toAcceptor(Automaton<MTLabel<I, L>, K> mtAutomaton, final I tape)
	{
		return new LabelConversion<MTLabel<I, L>, L, K>(mtAutomaton) 
		{
			@Override
			public L newLabel(MTLabel<I, L> label) 
			{
				return label == null ? null : label.tapeLabel(tape);
			}
		};
	}
	
	public static <I, L, K> Transducer<L, L, K> toTransducer(Automaton<MTLabel<I, L>, K> mtAutomaton, final I inputTape, final I outputTape)
	{
		return new TransducerLabelConversion<MTLabel<I, L>, L, L, K>(mtAutomaton) 
		{
			@Override
			public TLabel<L, L> newLabel(MTLabel<I, L> label)
			{				
				return label == null ? null : new TLabel<L, L>(label.tapeLabel(inputTape), label.tapeLabel(outputTape));
			}
		};
	}	
	
	public static <I, L, K> MTAutomaton<I, L, K> acceptorToHashMT(Automaton<L, K> acceptor, final I tape)
	{
		return new MTLabelConversion<L, I, L, K>(acceptor, Arrays.asList(tape))
		{
			@Override
			public MTLabel<I, L> newLabel(L label) 
			{
				Map<I, L> mtLabel = new HashMap<I, L>();
				mtLabel.put(tape, label);
				return new HashMTLabel<I, L>(mtLabel);
			}
		}; 
	}
	
	public static <I, L, K> MTAutomaton<I, L, K> transducerToHashMT(Automaton<TLabel<L, L>, K> transducer, final I tape1, final I tape2)
	{
		return new MTLabelConversion<TLabel<L, L>, I, L, K>(transducer, Arrays.asList(tape1, tape2))
		{
			@Override
			public MTLabel<I, L> newLabel(TLabel<L, L> label) 
			{
				Map<I, L> mtLabel = new HashMap<I, L>();
				if (label != null)
				{
					mtLabel.put(tape1, label.in());
					mtLabel.put(tape2, label.out());					
				}
				return new HashMTLabel<I, L>(mtLabel);
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
		
	public static <I, L, K> MTAutomaton<I, L, K> projectHashMT(Automaton<MTLabel<I, L>, K> mta, final Collection<I> tapes)
	{
		return new MTLabelConversion<MTLabel<I, L>, I, L, K>(mta, tapes)
		{
			@Override
			public MTLabel<I, L> newLabel(MTLabel<I, L> label) 
			{				
				Map<I, L> projection = new HashMap<I, L>();
				if (label != null)
					for (I tape : tapes) projection.put(tape, label.tapeLabel(tape));															
				return new HashMTLabel<I, L>(projection);
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
