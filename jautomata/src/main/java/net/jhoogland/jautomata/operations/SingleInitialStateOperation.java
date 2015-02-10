package net.jhoogland.jautomata.operations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Collection;

import net.jhoogland.jautomata.Automaton;


/**
 * 
 * This operation creates an automaton that is equivalent to the operand automaton, but has only 
 * one initial state. 
 * The initial state of the new automaton has an epsilon transition to every 
 * initial state of the original automaton. 
 * The weight of each of these transitions is the original initial weight.   
 * 
 * @author Jasper Hoogland
 *
 * @param <L>
 * label type
 * 
 * @param <K>
 * weight type
 * (Boolean for regular automata and Double for weighted automata)
 */

public class SingleInitialStateOperation<L, K> extends UnaryOperation<L, L, K, K> 
{
	public SingleInitialStateOperation(Automaton<L, K> operand) 
	{
		super(operand, operand.semiring());
	}

	@SuppressWarnings("unchecked")
	@Override
	public Collection<Object> initialStates() 
	{		
		return Arrays.asList(initialState());
	}

	public Object initialState() 
	{		
		return new SISState();
	}

	@Override
	public Collection<Object> transitionsOut(Object state) 
	{
//		// this versions depends on the "General" project:
//		if (state.initialState) return new CollectionMapper<S, SISTransition<S, T>>(operand.initialStates(), new Function<S, SISTransition<S, T>>()
//		{
//			@Override
//			public SISTransition<S, T> apply(S item) 
//			{				
//				return new SISTransition<S, T>(null, item, true);
//			}
//		
//		});
//		return new CollectionMapper<T, SISTransition<S, T>>(operand.transitionsOut((S) state.operandState), new Function<T, SISTransition<S, T>>()
//		{
//			@Override
//			public SISTransition<S, T> apply(T item) 
//			{				
//				return new SISTransition<S, T>(item, null, false);
//			}			
//		});
		Collection<Object> transitionsOut = new ArrayList<Object>();
		SISState s = (SISState) state;
		if (s.initialState)
		{
			for (Object iState : operand.initialStates())
			{
				transitionsOut.add(new SISTransition(null, iState, true));
			}
		}
		else
		{
			for (Object operandTransition : operand.transitionsOut(s.operandState))
			{
				transitionsOut.add(new SISTransition(operandTransition, null, false));
			}
		}
		return transitionsOut;
	}

	public K initialWeight(Object state) 
	{
		SISState s = (SISState) state;
		if (s.initialState) return semiring().one();
		else return semiring().zero();
	}

	public K finalWeight(Object state) 
	{
		SISState s = (SISState) state;
		if (s.initialState) return semiring().zero();
		else return operand.finalWeight(s.operandState);
	}

	@Override
	public Object from(Object transition) 
	{
		SISTransition t = (SISTransition) transition;
		if (t.fromInitialState) return initialState();
		else return new SISState(operand.from(t.operandTransition));
	}

	@Override
	public Object to(Object transition) 
	{
		SISTransition t = (SISTransition) transition;
		if (t.fromInitialState) return new SISState(t.operandInitialState);
		else return new SISState(operand.to(t.operandTransition));
	}

	public L label(Object transition) 
	{	
		SISTransition t = (SISTransition) transition;
		if (t.fromInitialState) return null;
		else return operand.label(t.operandTransition);
	}

	public K transitionWeight(Object transition) 
	{
		SISTransition t = (SISTransition) transition;
		if (t.fromInitialState) return operand.initialWeight(t.operandInitialState);
		else return operand.transitionWeight(t.operandTransition);
	}
	
	@Override
	public Comparator<Object> topologicalOrder() 
	{		
		final Comparator<Object> operandTopologicalOrder = operand.topologicalOrder();		
		return operandTopologicalOrder == null ? null : new Comparator<Object>()
		{
			public int compare(Object o1, Object o2) 
			{
				SISState s1 = (SISState) o1;
				SISState s2 = (SISState) o2;
				if (s1.initialState && s2.initialState) return 0;
				else if (s1.initialState) return -1;
				else if (s2.initialState) return 1;
				else return operandTopologicalOrder.compare(s1.operandState, s2.operandState);
			}			
		};
	}

	public class SISState
	{
		public Object operandState;
		public boolean initialState;

		public SISState(Object operandState) 
		{
			this.operandState = operandState;
			this.initialState = false;
		}
		
		public SISState() 
		{
			this.initialState = true;
		}
		
		@Override
		public boolean equals(Object obj) 
		{
			if (obj == null) return false;
			if (obj instanceof SingleInitialStateOperation.SISState)
			{
				SISState other = (SISState) obj; 
				if (this.initialState) return other.initialState;
				else return ! other.initialState && operandState.equals(other.operandState);								
			}
			else return false;
		}
		
		@Override
		public int hashCode() 
		{		
			return initialState ? 1 : 1 + operandState.hashCode();
		}
		
		@Override
		public String toString() 
		{			
			return "SISState(" + operandState + ", " + this.initialState + ")";
		}
	}
	
	public class SISTransition 
	{
		public Object operandTransition;
		public Object operandInitialState;
		public boolean fromInitialState;
		
		public SISTransition(Object operandTransition, Object operandInitialState, boolean fromInitialState) 
		{
			this.operandTransition = operandTransition;
			this.operandInitialState = operandInitialState;
			this.fromInitialState = fromInitialState;
		}
		
		@Override
		public String toString() 
		{			
			return "SISTransition(" + operandTransition + ", " + operandInitialState + ", " + fromInitialState + ")";
		}
	}
}
