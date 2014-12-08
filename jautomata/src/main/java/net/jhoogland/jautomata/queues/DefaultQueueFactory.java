package net.jhoogland.jautomata.queues;

import java.util.Map;
import java.util.Queue;

import net.jhoogland.jautomata.Automaton;

/**
 * 
 * This queue factory creates a topological state queue for automata that provide 
 * a topological order and a shortest-first queue for all other automata. 
 * 
 * @author Jasper Hoogland
 *
 * @param <K>
 * weight type
 */

public class DefaultQueueFactory<K> implements QueueFactory<K> 
{
	public <L> Queue<Object> createQueue(Automaton<L, K> automaton, Map<Object, K> weightMap) 
	{
		if (automaton.topologicalOrder() == null) 
		{
			return new ShortestFirstQueueFactory<K>().createQueue(automaton, weightMap);
		}
		else return new TopologicalQueueFactory<K>().createQueue(automaton, weightMap);
	}
}
