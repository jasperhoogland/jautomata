package net.jhoogland.jautomata;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import net.jhoogland.jautomata.operations.HashMTIntersection;
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
	/**
	 * @return
	 * an acceptor that is the projection of the specified multi-tape automaton on the
	 * specified tape   
	 */
	
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
	
	/**
	 * @return
	 * a transducer that is the projection of the specified multi-tape automaton on the
	 * specified tapes   
	 */
	
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
	
	/**
	 * @return
	 * a multi-tape automaton with {@link HashMTLabel}s that has one tape and is equivalent to the specified acceptor.  
	 */
	
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
	
	/**
	 * @return
	 * a multi-tape automaton with {@link HashMTLabel}s that has two tapes and is equivalent to the specified transducer.
	 */
	
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

	/**
	 * @return
	 * a multi-tape automaton with {@link ArrayMTLabel}s that has one tape and is equivalent to the specified acceptor.  
	 */
	
	public static <L, K> MTAutomaton<Integer, L, K> acceptorToArrayMT(Automaton<L, K> acceptor)
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
	
	/**
	 * @return
	 * a multi-tape automaton with {@link ArrayMTLabel}s that has two tapes and is equivalent to the specified transducer.
	 */
	
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
	
	/**
	 * @return
	 * a multi-tape automaton with {@link HashMTLabel}s that is a projection of the specified multi-tape automaton to the specified tapes 
	 */
		
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
	
	/**
	 * @return
	 * a multi-tape automaton with {@link ArrayMTLabel}s that is a projection of the specified multi-tape automaton to the specified tapes 
	 */
		
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
	
	/**
	 * @return
	 * the intersection of {@link HashMap}-based multi-tape automata. 
	 */
	
	public static <I, L, K> MTAutomaton<I, L, K> hashMTIntersection(MTAutomaton<I, L, K> mta1, MTAutomaton<I, L, K> mta2, I intersectionTape1, I intersectiontape2)
	{
		return new HashMTIntersection<I, L, K>(mta1, mta2, intersectionTape1, intersectiontape2);
	}
	
	public static <I, L, K> MTAutomaton<I, L, K> hashMTIntersection(MTAutomaton<I, L, K> mta1, MTAutomaton<I, L, K> mta2, I intersectionTape)
	{
		return new HashMTIntersection<I, L, K>(mta1, mta2, intersectionTape, intersectionTape);
	}
	
	public static <I, L, K> MTAutomaton<I, L, K> hashMTIntersection(MTAutomaton<I, L, K>... mtas)
	{
		if (mtas.length == 1) return mtas[0];
		MTAutomaton<I, L, K> intsct = binaryHashMTIntersection(mtas[0], mtas[1]);
		for (int i = 2; i < mtas.length; i++)
		{
			intsct = binaryHashMTIntersection(intsct, mtas[i]);
		}
		return intsct;
	}
	
	public static <I, L, K> MTAutomaton<I, L, K> explicitHashMTIntersection(MTAutomaton<I, L, K>... mtas)
	{
		if (mtas.length == 1) return mtas[0];
		MTAutomaton<I, L, K> intsct = new HashMTAutomaton<I, L, K>(binaryHashMTIntersection(mtas[0], mtas[1]));
		for (int i = 2; i < mtas.length; i++)
		{
			intsct = new HashMTAutomaton<I, L, K>(binaryHashMTIntersection(intsct, mtas[i]));
		}
		return intsct;
	}
	
	private static <I, L, K> MTAutomaton<I, L, K> binaryHashMTIntersection(MTAutomaton<I, L, K> mta1, MTAutomaton<I, L, K> mta2)
	{
		Set<I> tapes = new HashSet<I>(mta1.tapes());
		tapes.retainAll(mta2.tapes());
		if (tapes.size() == 1)
		{
			I intersectionTape = tapes.iterator().next();			
			return new HashMTIntersection<I, L, K>(mta1, mta2, intersectionTape, intersectionTape);
		}
		else if (tapes.isEmpty()) throw new RuntimeException("Operands have no overlapping tapes.");		
		else throw new RuntimeException("Operands have more than one overlapping tape.");
	}
	
	public static <T, K> Map<T, String> toStrings(Path<MTLabel<T, Character>, K> path)
	{
		Map<T, String> strings = new HashMap<T, String>();
		for (MTLabel<T, Character> label : path.label)
			if (label != null)
			{
				for (T tape : label.tapes())
				{
					Character tapeLabel = label.tapeLabel(tape);
					if (tapeLabel != null)
					{
						String pathTapeLabel = strings.get(tape);
						if (pathTapeLabel == null) pathTapeLabel = "";
						pathTapeLabel += tapeLabel;
						strings.put(tape, pathTapeLabel);
					}
				}
			}
		return strings;
	}
}
