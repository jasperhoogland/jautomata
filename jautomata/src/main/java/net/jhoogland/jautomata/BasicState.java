package net.jhoogland.jautomata;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Instances of this class contain information of a state, such as initial weight, final weight,
 * and outgoing and incoming transitions.
 * This class is used by automaton types that explicitly store information of states, such as
 * {@link ArrayAutomaton} and {@link HashAutomaton}. 
 * It can also be used by custom automaton types.
 * 
 * @author Jasper Hoogland
 *
 * @param <K>
 * weight type
 * 
 */

public class BasicState<K> 
{
	private Collection<Object> transitionsOut;
	private Collection<Object> transitionsIn;
	private K initialWeight;
	private K finalWeight;
	
	public BasicState(K initialWeight, K finalWeight, Collection<Object> transitionsOut, Collection<Object> transitionsIn) 
	{
		this.initialWeight = initialWeight;
		this.finalWeight = finalWeight;
		this.transitionsOut = transitionsOut;
		this.transitionsIn = transitionsIn;
	}
	
	public BasicState(K initialWeight, K finalWeight) 
	{
		this(initialWeight, finalWeight, new ArrayList<Object>(), new ArrayList<Object>());
	}
	
	/**
	 * @return
	 * the initial weight of a state
	 */
	public K initialWeight() 
	{
		return initialWeight;
	}
	
	/**
	 * @return
	 * the final weight of a state
	 */
	public K finalWeight() 
	{
		return finalWeight;
	}
	
	/**
	 * @return
	 * the outgoing transitions of a state
	 */
	public Collection<Object> transitionsOut() 
	{
		return transitionsOut;
	}
	
	/**
	 * @return
	 * the incoming transitions of a state
	 */
	public Collection<Object> transitionsIn() 
	{
		return transitionsIn;
	}
}
