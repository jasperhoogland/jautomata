package net.jhoogland.jautomata.operations;


import java.util.Map;

import net.jhoogland.jautomata.Automata;
import net.jhoogland.jautomata.Automaton;
import net.jhoogland.jautomata.ReverselyAccessibleAutomaton;
import net.jhoogland.jautomata.SingleSourceShortestDistancesInterface;

/**
 *
 * Reweights the specified automaton using the the generic shortest distance to the final states as potential function. 
 * This class is an implementation of the weight pushing algorithm described in [1].
 * 
 * [1] M. Mohri, M. Riley. A Weight Pushing Algorithm for Large Vocabulary Speech Recognition
 * 
 * @author Jasper Hoogland
 *
 * @param <L>
 * label type
 * 
 * @param <K>
 * The type of elements of the semiring over which the automaton is defined 
 * (Boolean for regular automata and Double for weighted automata)
 * 
 */

public class Push<L, K> extends Reweight<L, K> 
{
	Map<Object, K> distancesToFinalStates;
	
	public Push(ReverselyAccessibleAutomaton<L, K> operand, SingleSourceShortestDistancesInterface<K> sssd) 
	{
		super(operand);
		distancesToFinalStates = Automata.shortestDistancesToFinalStates(operand, sssd);		
	}

	@Override
	public K potential(Object state) 
	{		
		return distancesToFinalStates.get(state);
	}
}
