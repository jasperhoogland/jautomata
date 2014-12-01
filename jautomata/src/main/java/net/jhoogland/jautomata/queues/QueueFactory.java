package net.jhoogland.jautomata.queues;

import java.util.Map;
import java.util.Queue;

import net.jhoogland.jautomata.Automaton;

public interface QueueFactory<K>
{
	public <L> Queue<Object> createQueue(Automaton<L, K> automaton, Map<Object, K> weightMap);
}
