package net.jhoogland.jautomata;

import java.util.HashMap;
import java.util.Map;

import net.jhoogland.jautomata.semirings.Semiring;

/**
 * 
 * Implementation of {@link Automaton} that is backed up by {@link HashMap}s that contain information of
 * states and transitions.
 * This class is the transducer version of {@link HashAutomaton}, of which it is a subclass.
 * 
 * @author Jasper Hoogland
 *
 * @param <I>
 * input label type
 * 
 * @param <O>
 * output label type
 * 
 * @param <K>
 * weight type
 */

public class HashTransducer<I, O, K> extends HashAutomaton<TLabel<I, O>, K> implements Transducer<I, O, K>
{
	public HashTransducer(Automaton<TLabel<I, O>, K> src) 
	{
		super(src);
	}
	
	public HashTransducer(Map<Object, BasicState<K>> states, Map<Object, BasicTransition<TLabel<I, O>, K>> transitions, Semiring<K> semiring) 
	{
		super(states, transitions, semiring);		
	}
}
