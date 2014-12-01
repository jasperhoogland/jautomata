package net.jhoogland.jautomata.queues;

import java.util.Comparator;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

import net.jhoogland.jautomata.Automaton;

public class ShortestFirstQueueFactory<K> implements QueueFactory<K> 
{
	public Comparator<K> weightComparator;
	
	public ShortestFirstQueueFactory() 
	{
		this.weightComparator = new Comparator<K>() 
		{
			
			public int compare(K o1, K o2) 
			{
				if (o1 instanceof Comparable)
				{
					@SuppressWarnings("unchecked")
					Comparable<K> w1 = (Comparable<K>) o1;
					return w1.compareTo(o2);
				}
				throw new RuntimeException();
			}			
		};
	}
	
	public ShortestFirstQueueFactory(Comparator<K> weightComparator)
	{
		this.weightComparator = weightComparator;
	}
	
	
	public <L> Queue<Object> createQueue(Automaton<L, K> automaton, final Map<Object, K> weightMap) 
	{
		return new PriorityQueue<Object>(11, new Comparator<Object>()
		{
			
			public int compare(Object o1, Object o2) 
			{
				K w1 = weightMap.get(o1);
				K w2 = weightMap.get(o2);
				if (w1 == null && w2 == null) return 0;
				else if (w1 == null) return 1;
				else if (w2 == null) return -1;
				else return weightComparator.compare(w1, w2);
			}
		});
	}
}
