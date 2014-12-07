package net.jhoogland.jautomata.queues;

import java.util.Map;
import java.util.Queue;

import net.jhoogland.jautomata.Automaton;

/**
 * 
 * Implementations of this class are used by shortest distance algorithms to create state queues.
 * State queues store states that need to be processed by the algorithm.
 * The most efficient queue discipline depends on the type of automaton.
 * Each time a shortest distance algorithm is run, a state queue is created by calling the
 * <code>createQueue</code> method of a {@link QueueFactory} instance.
 * 
 * @author Jasper Hoogland
 *
 * @param <K>
 * weight type
 */

public interface QueueFactory<K>
{
	public <L> Queue<Object> createQueue(Automaton<L, K> automaton, Map<Object, K> weightMap);
}
