package net.jhoogland.jautomata.queues;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

import net.jhoogland.jautomata.Automata;
import net.jhoogland.jautomata.Automaton;
import net.jhoogland.jautomata.semirings.KTropicalSemiring;
import net.jhoogland.jautomata.semirings.PathWeight;

/**
 * 
 * Implementation of a {@link QueueFactory} that creates queues for the n shortest paths algorithm.
 * 
 * @author Jasper Hoogland
 *
 */

public class KTropicalQueueFactory<K extends Comparable<K>> implements QueueFactory<List<PathWeight<K>>>
{

	public <L> Queue<Object> createQueue(final Automaton<L, List<PathWeight<K>>> automaton, final Map<Object, List<PathWeight<K>>> weightMap) 
	{
//		if (automaton.topologicalOrder() != null) return new TopologicalQueueFactory<BestPathWeights<Object>>().createQueue(automaton, weightMap);
		final HashMap<Object, Integer> numExtractions = new HashMap<Object, Integer>();
		final KTropicalSemiring<K> sr = (KTropicalSemiring<K>) automaton.semiring(); 
		return new PriorityQueue<Object>(32, new Comparator<Object>()
		{
			public int compare(Object o1, Object o2) 
			{				
				return mu(o1).compareTo(mu(o2));
			}
			
			private K mu(Object state)
			{
				List<PathWeight<K>> w = weightMap.get(state);
				Integer n = numExtractions.get(state);
				if (n == null) n = 0;
				int k = n < sr.k ? n : sr.k - 1;
				return w.get(k).weight;
			}
			
		})
		{
			@Override
			public Object poll() 
			{				
				Object s = super.poll();
				if (s != null) 
				{
					Integer n = numExtractions.get(s);
					if (n == null) n = 0;
					n++;
					numExtractions.put(s, n);
					if (automaton.semiring() instanceof KTropicalSemiring)
					{
						KTropicalSemiring sr = (KTropicalSemiring) automaton.semiring();  
						if (sr.k == 1 && Automata.isFinalState(automaton, s)) 
						{
//							System.out.println("queue clear " + size());
							clear();
						}
					}
				}
				return s;
			}
		};
	}



}
