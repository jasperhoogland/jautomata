package net.jhoogland.jautomata.queues;

import java.util.Map;
import java.util.Queue;

import net.jhoogland.jautomata.Automaton;

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
