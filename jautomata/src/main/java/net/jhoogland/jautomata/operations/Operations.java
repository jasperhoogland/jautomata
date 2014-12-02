package net.jhoogland.jautomata.operations;

import net.jhoogland.jautomata.Automaton;
import net.jhoogland.jautomata.ExactConvergence;
import net.jhoogland.jautomata.SingleSourceShortestDistances;
import net.jhoogland.jautomata.TLabel;
import net.jhoogland.jautomata.Transducer;
import net.jhoogland.jautomata.queues.DefaultQueueFactory;
import net.jhoogland.jautomata.semirings.BestPathWeights;

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

	/**
	 * @return
	 * an instance of {@link SingleInitialStateOperation}, 
	 * which is an automaton with only one initial state, equivalent to the specified automaton.
	 */
	
	public static <L, K> Automaton<L, K> singleInitialState(Automaton<L, K> a)
	{
		return new SingleInitialStateOperation<L, K>(a);
	}
	
	public static <L, K> Automaton<L, BestPathWeights> toKTropicalSemiring(Automaton<L, K> a, int k)
	{
		if (a.semiring().zero().equals(false))
			return new BooleanToKTropical<L>((Automaton<L, Boolean>) a, k);
		else if (a.semiring().zero().equals(0.0))
			return new RealToKTropical<L>((Automaton<L, Double>) a, k);
		else return null;
	}
}
