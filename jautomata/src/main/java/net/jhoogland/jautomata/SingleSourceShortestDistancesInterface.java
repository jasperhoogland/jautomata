package net.jhoogland.jautomata;

import java.util.Map;

/**
 *
 * Implemented by single source shortest distance algorithms.
 * 
 * @author Jasper Hoogland
 *
 * @param <K>
 * weight type
 * (Boolean for regular automata and Double for weighted automata)
 **/

public interface SingleSourceShortestDistancesInterface<K>
{
	public <L> Map<Object, K> computeShortestDistances(Automaton<L, K> automaton, Object sourceState);
}
