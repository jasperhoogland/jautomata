package net.jhoogland.jautomata;

import java.util.Collection;

public interface ReverselyAccessibleAutomaton<L, K> extends Automaton<L, K>
{
	/**
	 * 
	 * @return
	 * Collection of initial states
	 * 
	 */
	
	public Collection<Object> finalStates();
	
	
	/**
	 * 
	 * @return
	 * Collection of incoming transitions from the specified
	 * 
	 */
	
	public Collection<Object> transitionsIn(Object state);
	
	public boolean isReverselyAccessible();
}
