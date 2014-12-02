package net.jhoogland.jautomata.queues;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

import net.jhoogland.jautomata.Automata;
import net.jhoogland.jautomata.Automaton;
import net.jhoogland.jautomata.semirings.BestPathWeights;
import net.jhoogland.jautomata.semirings.KTropicalSemiring;

public class KTropicalQueueFactory implements QueueFactory<BestPathWeights>
{

	public <L> Queue<Object> createQueue(final Automaton<L, BestPathWeights> automaton, final Map<Object, BestPathWeights> weightMap) 
	{
//		if (automaton.topologicalOrder() != null) return new TopologicalQueueFactory<BestPathWeights<Object>>().createQueue(automaton, weightMap);
		final HashMap<Object, Integer> numExtractions = new HashMap<Object, Integer>();
		return new PriorityQueue<Object>(32, new Comparator<Object>()
		{
			public int compare(Object o1, Object o2) 
			{				
				return (int) Math.signum(mu(o1) - mu(o2));
			}
			
			private double mu(Object state)
			{
				BestPathWeights w = weightMap.get(state);
				Integer n = numExtractions.get(state);
				if (n == null) n = 0;
				int k = n < w.pathWeights.length ? n : w.pathWeights.length - 1;
				return w.pathWeights[k].weight;
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
