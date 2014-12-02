package net.jhoogland.jautomata.operations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;

import net.jhoogland.jautomata.Automata;
import net.jhoogland.jautomata.Automaton;

/**
 * 
 * Implementation of the Kleene closure.
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

public class Closure<L, K> extends UnaryOperation<L, L, K, K> 
{
	public Closure(Automaton<L, K> operand)
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
		if (t.inOperand()) return new State(operand.previousState(t.opTransition));
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
		if (t.inOperand()) return new State(operand.nextState(t.opTransition));
		else if (t.fromInitialState) return new State(t.opState);
		else return initialState();
	}

	public L label(Object transition) 
	{
		Transition t = (Transition) transition;
		if (t.inOperand()) return operand.label(t.opTransition);
		else if (t.fromInitialState) return null;
		else return null;
	}

	public K transitionWeight(Object transition) 
	{
		Transition t = (Transition) transition;
		if (t.inOperand()) return operand.transitionWeight(t.opTransition);
		else if (t.fromInitialState) return operand.initialWeight(t.opState);
		else return operand.finalWeight(t.opState);
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
			if (opState instanceof Closure.State)
				throw new RuntimeException();
		}
		
		@Override
		public boolean equals(Object obj) 
		{			
			State other = (State) obj;
			return this.opState == null ? other.opState == null : this.opState.equals(other.opState);
		}
		
		@Override
		public int hashCode() 
		{			
			return this.opState == null ? 0 : this.opState.hashCode();
		}
		
		@Override
		public String toString() 
		{			
			return this.opState == null ? "<i>" : "<" + this.opState.toString() + ">";
		}
	}
	
	@Override
	public Comparator<Object> topologicalOrder() 
	{		
		return null;
	}
	
	class Transition
	{
		Object opTransition;
		Object opState;
		boolean fromInitialState;
		
		public Transition(Object opTransition) 
		{
			this.opTransition = opTransition;
			if (opTransition instanceof Closure.Transition)
				throw new RuntimeException();
		}
		
		public Transition(Object opState, boolean fromInitialState) 
		{
			this.opState = opState;
			this.fromInitialState = fromInitialState;
			if (opState instanceof Closure.State)
				throw new RuntimeException();
		}
		
		boolean inOperand()
		{
			return opTransition != null;
		}
		
		
		@Override
		public boolean equals(Object obj) 
		{			
			Transition other = (Transition) obj;
			return (this.opState == null ? other.opState == null : this.opState.equals(other.opState))
					&& (this.opTransition == null ? other.opTransition == null : this.opTransition.equals(other.opTransition))
					&& this.fromInitialState == other.fromInitialState;
		}
		
		@Override
		public int hashCode() 
		{			
			return (this.opState == null ? 0 : this.opState.hashCode())
					+ (this.opTransition == null ? 0 : this.opTransition.hashCode());
		}
		
		@Override
		public String toString() 
		{			
			if (opTransition == null)
			{
				if (fromInitialState) return "<i> -> " + opState.toString();
				else return opState.toString() + " -> <i>";				
			}
			else return "<" + opTransition + ">";
		}
	}
}
