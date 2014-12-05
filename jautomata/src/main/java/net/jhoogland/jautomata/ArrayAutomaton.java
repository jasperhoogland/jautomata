package net.jhoogland.jautomata;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import net.jhoogland.jautomata.semirings.Semiring;

/**
 * 
 * Implementation of {@link Automaton} that is backed up by arrays of states and transitions.
 * Each state and transition is identified by an integer, 
 * and all information (outgoing transitions of states, next and previous states of transitions, labels, weights, etc)
 * is explicitly stored in arrays.
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

public class ArrayAutomaton<L, K> extends ExplicitAutomaton<L, K>
{
	private BasicState<K>[] states;
	private BasicTransition<L, K>[] transitions;
	
	public ArrayAutomaton(BasicState<K>[] states, BasicTransition<L, K>[] transitions, Semiring<K> semiring) 
	{
		super(semiring);		
		this.states = states;
		this.transitions = transitions;
		initialStates = new ArrayList<Object>();
		finalStates = new ArrayList<Object>();
		K zero = semiring.zero();
		for (int i = 0; i < states.length; i++)
		{
			BasicState<K> state = states[i];
			if (! state.initialWeight().equals(zero)) initialStates.add(i);
			if (! state.finalWeight().equals(zero)) finalStates.add(i);			
		}
	}
	
	public ArrayAutomaton(Automaton<L, K> src) 
	{
		super(src.semiring());
		initialStates = new ArrayList<Object>();
		finalStates = new ArrayList<Object>();
		Collection<Object> states = Automata.states(src);
		this.states = new BasicState[states.size()];
		K zero = semiring().zero();
		Map<Object, Integer> stateMap = new HashMap<Object, Integer>();
		int s = 0;
		for (Object state : states)
		{
			K initialWeight = src.initialWeight(state);
			K finalWeight = src.finalWeight(state);
			Collection<Object> transitionsOut = new ArrayList<Object>();
			Collection<Object> transitionsIn = new ArrayList<Object>();
			this.states[s] = new BasicState<K>(initialWeight, finalWeight, transitionsOut, transitionsIn);
			if (! this.states[s].initialWeight().equals(zero)) initialStates.add(s);
			if (! this.states[s].finalWeight().equals(zero)) finalStates.add(s);
			stateMap.put(state, s);
			s++;
		}
		Collection<Object> transitions = Automata.transitions(src);
		this.transitions = new BasicTransition[transitions.size()];
		
		int t = 0;
		for (Object transition : transitions)
		{
			Object prevSrcState = src.previousState(transition);
			Object nextSrcState = src.nextState(transition);
			int previousState = stateMap.get(prevSrcState);
			int nextState = stateMap.get(nextSrcState);
			L label = src.label(transition);
			K weight = src.transitionWeight(transition);
			this.states[previousState].transitionsOut().add(t);
			this.states[nextState].transitionsIn().add(t);
			this.transitions[t] = new BasicTransition<L, K>(previousState, label, weight, nextState);
			t++;
		}
	}

	protected BasicState<K> getState(Object state)
	{
		return states[((Integer) state).intValue()];
	}

	protected BasicTransition<L, K> getTransition(Object state)
	{
		return transitions[((Integer) state).intValue()];
	}
}
