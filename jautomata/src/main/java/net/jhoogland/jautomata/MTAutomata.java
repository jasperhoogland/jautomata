package net.jhoogland.jautomata;

import java.util.HashMap;
import java.util.Map;

import net.jhoogland.jautomata.operations.LabelConversion;
import net.jhoogland.jautomata.operations.TransducerLabelConversion;
import net.jhoogland.jautomata.operations.MTLabelConversion;

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
		return new MTLabelConversion<L, I, L, K>(acceptor)
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
		return new MTLabelConversion<TLabel<L, L>, I, L, K>(transducer)
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
		return new MTLabelConversion<L, Integer, L, K>(acceptor)
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
		return new MTLabelConversion<TLabel<L, L>, Integer, L, K>(transducer)
		{
			@Override
			public MTLabel<Integer, L> newLabel(TLabel<L, L> label) 
			{
				return label == null ? null : new ArrayMTLabel<L>(label.in(), label.out());
			}
		}; 
	}
}
