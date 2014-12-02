package net.jhoogland.jautomata.operations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import net.jhoogland.jautomata.Automata;
import net.jhoogland.jautomata.Automaton;

public class KleeneStar<L, K> extends UnaryOperation<L, L, K, K> 
{
	public KleeneStar(Automaton<L, K> operand)
	{
		super(operand, operand.semiring());
	}

	public Collection<Object> initialStates() 
	{		
		return Arrays.asList(initialState());
	}
	
	@Override
	public Collection<Object> transitionsOut(Object state) 
	{		
		State s = (State) state;
		Collection<Object> transitionsOut = new ArrayList<Object>();
		if (s.opState == null)
		{
			for (Object ios : operand.initialStates())
				transitionsOut.add(new Transition(ios, true));
		}
		else
		{
			for (Object t : operand.transitionsOut(s.opState))
				transitionsOut.add(new Transition(t));
			if (Automata.isFinalState(operand, s.opState))
				transitionsOut.add(new Transition(s.opState, false));
		}
		
		return transitionsOut;
	}

	public K initialWeight(Object state)
	{		
		return getOpState(state) == null ? semiring().one() : semiring().zero();
	}

	public K finalWeight(Object state) 
	{		
		return getOpState(state) == null ? semiring().one() : semiring().zero();
	}

	public Object previousState(Object transition) 
	{		
		Transition t = (Transition) transition;
		if (t.inOperand()) return new State(operand.previousState(t.opState));
		else if (t.fromInitialState) return initialState();
		else return new State(t.opState);
	}

	private Object initialState()
	{
		return new State(null);
	}

	public Object nextState(Object transition) 
	{
		Transition t = (Transition) transition;
		if (t.inOperand()) return new State(operand.nextState(t.opState));
		else if (t.fromInitialState) return new State(t.opState);
		else return initialState();
	}

	public L label(Object transition) 
	{
		Transition t = (Transition) transition;
		if (t.inOperand()) return operand.label(t.opState);
		else if (t.fromInitialState) return null;
		else return null;
	}

	public K transitionWeight(Object transition) 
	{
		Transition t = (Transition) transition;
		if (t.inOperand()) return operand.label(t.opState);
		else if (t.fromInitialState) return null;
		else return null;
	}
	
	private Object getOpState(Object state)
	{
		return ((State) state).opState;
	}
	
	class State
	{
		Object opState;
		
		public State(Object opState) 
		{
			this.opState = opState;
		}
	}
	
	class Transition
	{
		Object opTransition;
		Object opState;
		boolean fromInitialState;
		
		public Transition(Object opTransition) 
		{
			this.opTransition = opTransition;
		}
		
		public Transition(Object opState, boolean fromInitialState) 
		{
			this.opState = opState;
			this.fromInitialState = fromInitialState;
		}
		
		boolean inOperand()
		{
			return opTransition != null;
		}
	}
}
