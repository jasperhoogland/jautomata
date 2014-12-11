package net.jhoogland.jautomata.operations;

import java.util.HashSet;
import java.util.Set;

import net.jhoogland.jautomata.Automata;
import net.jhoogland.jautomata.Automaton;
import net.jhoogland.jautomata.ExactConvergence;
import net.jhoogland.jautomata.MTAutomaton;
import net.jhoogland.jautomata.MTLabel;
import net.jhoogland.jautomata.ReverselyAccessibleAutomaton;
import net.jhoogland.jautomata.SingleSourceShortestDistances;
import net.jhoogland.jautomata.SingleSourceShortestDistancesInterface;
import net.jhoogland.jautomata.TLabel;
import net.jhoogland.jautomata.Transducer;
import net.jhoogland.jautomata.queues.DefaultQueueFactory;
import net.jhoogland.jautomata.semirings.BestPathWeights;
import net.jhoogland.jautomata.semirings.BooleanSemiring;
import net.jhoogland.jautomata.semirings.LogSemiring;
import net.jhoogland.jautomata.semirings.RealSemiring;
import net.jhoogland.jautomata.semirings.Semifield;
import net.jhoogland.jautomata.semirings.Semiring;
import net.jhoogland.jautomata.semirings.TropicalSemiring;

/**
 * 
 * Contains static methods that perform operations on finite state autamata.
 * The return value of an operation method is a new automaton.
 * The same result can be obtained by creating instances of classes that define operations, 
 * but it may be more convenient to create them via the static methods in this class.  
 * 
 * Most of the times, the states and transitions of the resulting automata are not computed 
 * when the operation method is invoked.
 * Instead, they are only computed when needed (on the fly), after invoking methods of the resulting automata,
 * such as initialStates() or transitionsOut(state).
 * 
 * @author Jasper Hoogland
 *
 */

public class Operations 
{
	/**
	 * Computes the intersection of the specified acceptors.
	 * The acceptors must have the same label type. 
	 * The result in an instance of {@link Union}, which itself is an automata.
	 * The states and transitions of the resulting union are created only when needed (on the fly).
	 */
	
	public static <L, K> Automaton<L, K> acceptorIntersection(Automaton<L, K> a1, Automaton<L, K> a2)
	{
		return new AcceptorIntersection<L, K>(a1, a2);
	}

	/**
	 * Computes the composition of the specified transducers.
	 * The output labels of the first transducer must have the same type as the input labels of the second transducer. 
	 * The result in an instance of {@link Union}, which itself is an automata.
	 * The states and transitions of the resulting union are created only when needed (on the fly).
	 */
	
	public static <I, L, O, K> Transducer<I, O, K> transducerComposition(Automaton<TLabel<I, L>, K> a1, Automaton<TLabel<L, O>, K> a2)
	{
		return new TransducerComposition<I, L, O, K>(a1, a2);
	}
	
	public static <I, L, K> MTAutomaton<I, L, K> hashMTIntersection(MTAutomaton<I, L, K> mta1, MTAutomaton<I, L, K> mta2, I intersectionTape1, I intersectiontape2)
	{
		return new HashMTIntersection<I, L, K>(mta1, mta2, intersectionTape1, intersectiontape2);
	}
	
	public static <I, L, K> MTAutomaton<I, L, K> hashMTIntersection(MTAutomaton<I, L, K> mta1, MTAutomaton<I, L, K> mta2, I intersectionTape)
	{
		return new HashMTIntersection<I, L, K>(mta1, mta2, intersectionTape, intersectionTape);
	}
	
	public static <I, L, K> MTAutomaton<I, L, K> hashMTIntersection(MTAutomaton<I, L, K> mta1, MTAutomaton<I, L, K> mta2)
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
	
	public static <L, K> Automaton<L, K> epsilonRemoval(Automaton<L, K> operand)
	{
		return new EpsilonRemoval<L, K>(operand, new SingleSourceShortestDistances<K>(new DefaultQueueFactory<K>(), new ExactConvergence<K>()));
	}
	
	public static <L, K> Automaton<L, K> determinize(Automaton<L, K> operand)
	{
		return new Determinization<L, K>(operand);
	}

	public static <L, K> Automaton<L, K> determinizeER(Automaton<L, K> operand)
	{
		return determinize(epsilonRemoval(operand));
	}
	
	public static <L, K> Automaton<L, K> push(Automaton<L, K> operand, SingleSourceShortestDistancesInterface<K> sssd)
	{
		return new Push<L, K>(operand, sssd);
	}

	public static <L, K> Automaton<L, K> push(Automaton<L, K> operand)
	{
		return push(operand, new SingleSourceShortestDistances<K>(new DefaultQueueFactory<K>(), new ExactConvergence<K>()));
	}

	public static <L, K> Automaton<L, K> reverse(ReverselyAccessibleAutomaton<L, K> operand)
	{
		return new ReversedAutomaton<L, K>(operand);
	}

	

	/**
	 * Computes the union of the specified automata.
	 * The automata must have the same label type.
	 * The result in an instance of {@link Union}, which itself is an automata.
	 * The states and transitions of the resulting union are created only when needed (on the fly).
	 */
	
	public static <L, K> Automaton<L, K> union(Automaton<L, K>... operands)
	{
		return new Union<L, K>(operands);
	}
	
	public static <L> Automaton<L, Double> weightedUnion(Automaton<L, Double>[] operands, final double[] weights)
	{
		return new Union<L, Double>(operands)
		{
			@Override
			public Double initialWeight(Object state) 
			{				
				UnionElement s = (Union<L, Double>.UnionElement) state;
				return operands[s.index].semiring().multiply(weights[s.index], super.initialWeight(state));
			}
		};
	}
	
	public static <L> Automaton<L, Double> weightedUnion(Automaton<L, Double>... operands)
	{
		double[] weights = new double[operands.length];
		Semifield<Double> sf = (Semifield<Double>) operands[0].semiring();
		double l = sf.one().equals(1.0) ? operands.length : -Math.log(operands.length);
		double p = sf.inverse(l);
		for (int i = 0; i < weights.length; i++) weights[i] = p;
		return weightedUnion(operands, weights);
	}
	
	public static <L, K> Automaton<L, K> concat(Automaton<L, K>... operands)
	{
		return new Concatenation<L, K>(operands);
	}
	
	/**
	 *  
	 * @return
	 * the Kleene closure of the specified automaton
	 * 
	 */

	public static <L, K> Automaton<L, K> closure(Automaton<L, K> operand)
	{
		return new Closure<L, K>(operand);
	}

	public static <L, K> Automaton<L, Double> weightedClosure(Automaton<L, Double> operand, final double lambda)
	{
		return new Closure<L, Double>(operand)
		{
			@Override
			public Double initialWeight(Object state) 
			{				
				return lambda * super.initialWeight(state);
			}
			
			@Override
			public Double transitionWeight(Object transition) 
			{
				Closure<L, K>.Transition t = (Closure<L, K>.Transition) transition;
				if (t.opState != null && t.fromInitialState)
					return (1 - lambda) * super.transitionWeight(transition);
				else 
					return super.transitionWeight(transition);
			}
		};
	}

	public static <L, K> Automaton<L, K> kleenePlus(Automaton<L, K> operand)
	{
		return union(operand, closure(operand));
	}
	
	public static <L> Automaton<L, Double> weightedKleenePlus(Automaton<L, Double> operand, double lambda)
	{
		return weightedUnion(new Automaton[] { operand, closure(operand) }, new double[] { lambda, 1 - lambda } );
	}
	
	public static <L, K> Automaton<L, K> optional(Automaton<L, K> operand)
	{
		return union(operand, (Automaton<L, K>) Automata.emptyStringAutomaton(operand.semiring()));
	}

	public static <L, K> Automaton<L, K> weightedOptional(Automaton<L, K> operand, double weightOperand, double weightEmptyString)
	{
		return weightedUnion(new Automaton[] { operand, (Automaton<L, K>) Automata.emptyStringAutomaton(operand.semiring()) }, new double[] { weightOperand, weightEmptyString} );
	}

	public static <L, K> Automaton<L, K> weightedOptional(Automaton<L, K> operand, double weightOperand)
	{
		return weightedOptional(operand, weightOperand, 1.0 - weightOperand);
	}

	/**
	 * @return
	 * an instance of {@link SingleInitialStateOperation}, 
	 * which is an automaton with only one initial state, equivalent to the specified automaton.
	 */
	
	public static <L, K> Automaton<L, K> singleInitialState(Automaton<L, K> a)
	{
		return new SingleInitialStateOperation<L, K>(a);
	}
	
	public static <L> Automaton<L, Double> toWeightedAutomaton(Automaton<L, Boolean> a)
	{
		final RealSemiring sr = new RealSemiring();
		return new SemiringConversion<L, Boolean, Double>(a, sr) 
		{
			@Override
			public Double convertWeight(Boolean weight) 
			{				
				return weight ? sr.one() : sr.zero();
			}
		};
	}
	
	public static <L> Automaton<L, Boolean> toUnweightedAutomaton(Automaton<L, Double> a)
	{
		final Semiring<Double> sr = a.semiring();
		return new SemiringConversion<L, Double, Boolean>(a, new BooleanSemiring()) 
		{
			@Override
			public Boolean convertWeight(Double weight) 
			{				
				return ! sr.zero().equals(weight);
			}
		};
	}
	
	public static <L> Automaton<L, Double> logToRealSemiring(Automaton<L, Double> a)
	{
		return new SemiringConversion<L, Double, Double>(a, new RealSemiring())
		{
			@Override
			public Double convertWeight(Double weight) 
			{
				return Math.exp(-weight);
			}			
		};		
	}
	
	public static <L> Automaton<L, Double> realToLogSemiring(Automaton<L, Double> a)
	{
		return realToLogSemiring(a, new LogSemiring());
	}
	
	public static <L> Automaton<L, Double> realToTropicalSemiring(Automaton<L, Double> a)
	{
		return realToLogSemiring(a, new TropicalSemiring());
	}
	
	private static <L> Automaton<L, Double> realToLogSemiring(Automaton<L, Double> a, Semiring<Double> semiring)
	{
		return new SemiringConversion<L, Double, Double>(a, semiring)
		{
			@Override
			public Double convertWeight(Double weight) 
			{
				return -Math.log(weight);
			}			
		};		
	}
	
	public static <L, K extends Comparable<K>> Automaton<L, BestPathWeights<K>> toKTropicalSemiring(Automaton<L, K> a, int k)
	{
//		if (a.semiring().zero().equals(false))
//			return new KTropicalSemiringConversion<Boolean, L>((Automaton<L, Boolean>) a, k)
//			{
//				@Override
//				public double convert(Boolean weight) 
//				{
//					return weight ? 0.0 : Double.POSITIVE_INFINITY;
//				}
//				
//			};
//		else if (a.semiring().zero().equals(0.0))
//			return new KTropicalSemiringConversion<Double, L>((Automaton<L, Double>) a, k)
//			{
//				@Override
//				public double convert(Double weight) 
//				{					
//					return -Math.log(weight);
//				}
//			};
//		else return null;
		return new KTropicalSemiringConversion<K, L>((Automaton<L, K>) a, k)
//		{
//			@Override
//			public K convert(K weight) 
//			{
//				return weight ? 0.0 : Double.POSITIVE_INFINITY;
//			}
//			
//		}
		;
	}
}
