package net.jhoogland.jautomata.queues;

import java.util.Comparator;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

import net.jhoogland.jautomata.Automaton;

public class TopologicalQueueFactory<K> implements QueueFactory<K> 
{
	public <L> Queue<Object> createQueue(Automaton<L, K> automaton, Map<Object, K> weightMap) 
	{		
		Comparator<Object> topologicalOrder = automaton.topologicalOrder();
		if (topologicalOrder == null) return null;
		return new PriorityQueue<Object>(32, topologicalOrder);
	}
}
