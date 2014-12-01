package net.jhoogland.jautomata;

import java.util.Map;

/**
 *
 * Implementations of this class represent single-source shortest distance algorithms.
 * 
 * @author Jasper Hoogland
 *
 * @param <S>
 * state type
 * 
 * @param <K>
 * the type over which the semiring is defined 
 * 
 **/

public interface SingleSourceShortestDistancesInterface<K>
{
	public <L> Map<Object, K> computeShortestDistances(Automaton<L, K> automaton, Object sourceState);
}
