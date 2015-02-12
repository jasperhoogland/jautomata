package net.jhoogland.jautomata;

import java.util.Collection;

/**
 * 
 * Automata that implement this interface provide a way to efficiently compute intersections
 * of automata with a large number of transitions per state.
 * 
 * 
 * @author Jasper Hoogland
 *
 * @param <L>
 * @param <K>
 */

public interface ExtendedAutomaton<L, K> extends Automaton<L, K> 
{
	public int maxNumTransitions(Object state);
	public Collection<Object> transitionsOut(Object state, L label);
}
