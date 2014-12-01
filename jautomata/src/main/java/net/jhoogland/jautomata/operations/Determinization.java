package net.jhoogland.jautomata.operations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import net.jhoogland.jautomata.Automaton;
import net.jhoogland.jautomata.semirings.Semifield;
import net.jhoogland.jautomata.semirings.Semiring;

/**
 * 
 * This is an implementation of the on-the-fly determinization algorithm described in [1]. Instances of this class are equivalent
 * to their operands. The semiring of the automaton is required to be a semifield, i.e. it must have an inverse for multiplication.  
 * 
 * [1] M. Mohri, Finite-State Transducers in Language and Speech Processing. 1997
 * 
 * @author Jasper Hoogland
 *
 * 
 * @param <L>
 * The label type
 * 
 * @param <K>
 * The type of elements of the semiring over which the automaton is defined 
 * (Boolean for regular automata and Double for weighted automata)
 * 
 */

public class Determinization<L, K> extends UnaryOperation<L, L, K, K> 
{
	public Determinization(Automaton<L, K> operand)
	{
		super(operand, operand.semiring());
		if (! (semiring()  instanceof Semifield)) throw new RuntimeException("The semiring of the operand is not a semifield.");
	}

	@Override
	public Collection<Object> initialStates() 
	{
		return Arrays.asList((Object) getInitialState());
	}
	
	public DeterminizationState getInitialState()
	{
		Map<Object, K> remainderWeights = new HashMap<Object, K>();
		for (Object initialOperandState : operand.initialStates())
			remainderWeights.put(initialOperandState, operand.initialWeight(initialOperandState));
		return new DeterminizationState(remainderWeights);		
	}

	@Override
	public Collection<Object> transitionsOut(Object state) 
	{
		Map<L, Collection<Object>> operandTransitionsByLabel = new HashMap<L, Collection<Object>>();
		DeterminizationState s = (DeterminizationState) state;
		for (Entry<Object, K> e : s.remainderWeights.entrySet())
		{
			for (Object operandTransition : operand.transitionsOut(e.getKey()))
			{
				L label = operand.label(operandTransition);
				Collection<Object> operandTransitions = operandTransitionsByLabel.get(label);
				if (operandTransitions == null)
				{
					operandTransitions = new ArrayList<Object>();
					operandTransitionsByLabel.put(label, operandTransitions);
				}
				operandTransitions.add(operandTransition);
			}
		}
		
		// Transition weight is computed in advance and stored in the transition.
		Collection<Object> transitionsOut = new ArrayList<Object>();
		for (Entry<L, Collection<Object>> e : operandTransitionsByLabel.entrySet())
		{
			K transitionWeight = transitionWeight(state, e.getValue());			
			DeterminizationState nextState = nextState(transitionWeight, s, e.getValue());
			transitionsOut.add(new DeterminizationTransition(s, e.getKey(), transitionWeight, nextState));			
		}
		return transitionsOut;
	}
	
	private DeterminizationState nextState(K transitionWeight, DeterminizationState previousState, Collection<Object> operandTransitions) 
	{
		HashMap<Object, K> remainderWeights = new HashMap<Object, K>();
		Semifield<K> sf = (Semifield<K>) semiring();
		for (Object operandTransition : operandTransitions)
		{
			Object nextOperandState = operand.nextState(operandTransition);
			Object previousOperandState = operand.previousState(operandTransition);
			
			K toBeAdded = sf.multiply( 
					sf.multiply(previousState.remainderWeights.get(previousOperandState), operand.transitionWeight(operandTransition)),
					sf.inverse(transitionWeight));
					
			K remainderWeight = remainderWeights.get(nextOperandState);
			if (remainderWeight == null)
			{
				remainderWeights.put(nextOperandState, toBeAdded);
			}
			else
			{
				remainderWeights.put(nextOperandState, sf.add(remainderWeight, toBeAdded));
			}			
		}
		return new DeterminizationState(remainderWeights);
	}

	private K transitionWeight(Object previousState, Collection<Object> operandTransitions)
	{
		Semiring<K> sr = semiring();
		DeterminizationState ps = (DeterminizationState) previousState;
		K weight = sr.zero();
		for (Object operandTransition : operandTransitions)
		{
			weight = sr.add(weight, sr.multiply(ps.remainderWeights.get(operand.previousState(operandTransition)), operand.transitionWeight(operandTransition)));
		}
		return weight;
	}
		
	public K initialWeight(Object state) 
	{		
		return getInitialState().equals(state) ? semiring().one() : semiring().zero();
	}

	public K finalWeight(Object state) 
	{
		Semiring<K> sr = semiring();
		K finalWeight = sr.zero();
		DeterminizationState s = (DeterminizationState) state;
		for (Entry<Object, K> e : s.remainderWeights.entrySet())
			finalWeight = sr.add(finalWeight, sr.multiply(e.getValue(), operand.finalWeight(e.getKey())));
		return finalWeight;
	}

	public Object previousState(Object transition) 
	{
		return ((DeterminizationTransition) transition).previousState;
	}

	public Object nextState(Object transition) 
	{	
		// debug:
		DeterminizationTransition t = (DeterminizationTransition) transition;
		if (t.nextState == null) throw new RuntimeException();
		return t.nextState;
	}

	public L label(Object transition) 
	{		
		return ((DeterminizationTransition) transition).label;
	}

	public K transitionWeight(Object transition) 
	{		
		return ((DeterminizationTransition) transition).weight;
	}

	@Override
	public Comparator<Object> topologicalOrder() 
	{
		final Comparator<Object> operandTopologicalOrder = operand.topologicalOrder();
		return operandTopologicalOrder == null ? null : new Comparator<Object>()
		{
			public int compare(Object o1, Object o2) 
			{
				DeterminizationState s1 = (DeterminizationState) o1;
				DeterminizationState s2 = (DeterminizationState) o2;
				if (s1.equals(s2)) return 0;
				else if (prec(s1.remainderWeights.keySet(), s2.remainderWeights.keySet())) return -1;
				else if (prec(s2.remainderWeights.keySet(), s1.remainderWeights.keySet())) return 1;
				else return 0;
			}
			
			private boolean prec(Set<Object> states1, Set<Object> states2)
			{
				for (Object state2 : states2)
				{
					boolean precStateFound = false;
					for (Object state1 : states1)
					{
						if (operandTopologicalOrder.compare(state1, state2) < 0)
						{
							precStateFound = true;
							break;
						}
					}
					if (! precStateFound) return false;
				}
				return true;
			}
		};
	}
	
	public class DeterminizationState 
	{
		Map<Object, K> remainderWeights;
		
		public DeterminizationState(Map<Object, K> remainderWeights) 
		{
			this.remainderWeights = remainderWeights;
		}
		
		@SuppressWarnings("rawtypes")
		@Override
		public boolean equals(Object obj) 
		{
			if (obj instanceof Determinization.DeterminizationState) 
			{
				DeterminizationState other = (DeterminizationState) obj; 
				return remainderWeights.equals(other.remainderWeights); 
			}
			else return false;
		}
		
		@Override
		public int hashCode() 
		{
			return remainderWeights.hashCode();
		}
		
		@Override
		public String toString() 
		{		
			return "DeterminizationState(" + remainderWeights + ")";
		}
	}
	
	public class DeterminizationTransition 
	{
		DeterminizationState previousState;
		L label;
		K weight;
		DeterminizationState nextState;
			
		public DeterminizationTransition(DeterminizationState previousState, L label, K weight, DeterminizationState nextState) 
		{
			this.previousState = previousState;
			this.label = label;
			this.weight = weight;
			this.nextState = nextState;
		}
		
		@Override
		public boolean equals(Object obj) 
		{
			if (obj instanceof Determinization.DeterminizationTransition)
			{
				@SuppressWarnings("unchecked")
				DeterminizationTransition other = (DeterminizationTransition) obj;
				return this.previousState.equals(other.previousState)
						&& this.nextState.equals(other.nextState)
						&& labelEquals(this.label, other.label)
						&& this.weight.equals(other.weight);
			}
			else return false;
		}
		
		private boolean labelEquals(L label1, L label2)
		{
			return label1 == null && label2 == null || label1 != null && label2 != null && label1.equals(label2);
		}
		
		@Override
		public int hashCode() 
		{		
			return previousState.hashCode() + nextState.hashCode() + weight.hashCode() + (label == null ? 0 : label.hashCode());
		}
		
		@Override
		public String toString() 
		{		
			return this.getClass().getSimpleName() + "(" + previousState + ", " + label + ", " + weight + " " + nextState + ")";
		}
	}
}
