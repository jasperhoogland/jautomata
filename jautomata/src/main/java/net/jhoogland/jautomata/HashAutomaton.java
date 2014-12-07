package net.jhoogland.jautomata;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import net.jhoogland.jautomata.semirings.Semiring;

/**
 * Implementation of {@link Automaton} that is backed up by {@link HashMap}s that contain information of
 * states and transitions.
 * 
 * @author Jasper Hoogland
 *
 * @param <L>
 * label type
 * 
 * @param <K>
 * weight type
 */

public class HashAutomaton<L, K> extends ExplicitAutomaton<L, K> 
{
	private Map<Object, BasicState<K>> states;
	private Map<Object, BasicTransition<L, K>> transitions;
	
	public HashAutomaton(Map<Object, BasicState<K>> states, Map<Object, BasicTransition<L, K>> transitions, Semiring<K> semiring) 
	{
		super(semiring);		
		this.states = states;
		this.transitions = transitions;
		initialStates = new ArrayList<Object>();
		finalStates = new ArrayList<Object>();
		K zero = semiring.zero();
		for (Entry<Object, BasicState<K>> e : states.entrySet())
		{
			BasicState<K> stateData = e.getValue();
			Object state = e.getKey();
			if (! stateData.initialWeight().equals(zero)) initialStates.add(state);
			if (! stateData.finalWeight().equals(zero)) finalStates.add(state);			
		}
	}
	
	public HashAutomaton(Automaton<L, K> src) 
	{
		super(src.semiring());
		initialStates = new ArrayList<Object>();
		finalStates = new ArrayList<Object>();
		Collection<Object> states = Automata.states(src);
		this.states = new HashMap<Object, BasicState<K>>();
		K zero = semiring().zero();
//		Map<Object, Integer> stateMap = new HashMap<Object, Integer>();
		for (Object state : states)
		{
			K initialWeight = src.initialWeight(state);
			K finalWeight = src.finalWeight(state);
			Collection<Object> transitionsOut = new ArrayList<Object>();
			Collection<Object> transitionsIn = new ArrayList<Object>();
			BasicState<K> stateData = new BasicState<K>(initialWeight, finalWeight, transitionsOut, transitionsIn);
			this.states.put(state, stateData);
			if (! stateData.initialWeight().equals(zero)) initialStates.add(state);
			if (! stateData.finalWeight().equals(zero)) finalStates.add(state);
		}
		Collection<Object> transitions = Automata.transitions(src);
		this.transitions = new HashMap<Object, BasicTransition<L,K>>();
		
		for (Object transition : transitions)
		{
			Object prevSrcState = src.previousState(transition);
			Object nextSrcState = src.nextState(transition);
			L label = src.label(transition);
			K weight = src.transitionWeight(transition);
			this.states.get(prevSrcState).transitionsOut().add(transition);
			this.states.get(nextSrcState).transitionsIn().add(transition);
			this.transitions.put(transition, new BasicTransition<L, K>(prevSrcState, label, weight, nextSrcState));
		}
	}

	protected BasicState<K> getState(Object state)
	{
		return states.get(state);
	}

	protected BasicTransition<L, K> getTransition(Object transition)
	{
		return transitions.get(transition);
	}
}
