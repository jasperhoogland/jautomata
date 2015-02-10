package net.jhoogland.jautomata.operations;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import net.jhoogland.jautomata.Automaton;
import net.jhoogland.jautomata.SingleSourceShortestDistancesInterface;
import net.jhoogland.jautomata.semirings.Semiring;

/**
 * <p>
 * This is an implementation of the on-the-fly epsilon removal algorithm described in [1].
 * The result of determinization is an automaton without null-transitions that is equivalent to its operand.
 * </p>   
 * <p>
 * [1] M. Mohri, Generic Epsilon-Removal Algorithm for Weighted Automata. 2000.
 * </p>
 * 
 * @author Jasper Hoogland
 *
 * @param <L>
 * label type
 * 
 * @param <K>
 * the type over which the semiring is defined.
 * (Boolean for regular automata and Double for weighted automata) 
 */

public class EpsilonRemoval<L, K> extends UnaryOperation<L, L, K, K> 
{
	Map<Object, K> finalWeights;
	Map<Object, Collection<Object>> transitionsOut;
	Map<EpsilonRemovalTransition, K> transitionsWeights;
	SingleSourceShortestDistancesInterface<K> shortestDistanceAlgorithm;
	
	public EpsilonRemoval(Automaton<L, K> operand, SingleSourceShortestDistancesInterface<K> shortestDistanceAlgorithm) 
	{
		super(operand, operand.semiring());
		finalWeights = new HashMap<Object, K>();
		transitionsOut = new HashMap<Object, Collection<Object>>();
		transitionsWeights = new HashMap<EpsilonRemovalTransition, K>();
		this.shortestDistanceAlgorithm = shortestDistanceAlgorithm;
	}
	
	public Collection<Object> transitionsOut(Object state) 
	{
		if (! transitionsOut.containsKey(state)) computeProperties(state);
		return this.transitionsOut.get(state);
	}
	
	public K finalWeight(Object state) 
	{
		if (! transitionsOut.containsKey(state)) computeProperties(state);
		return this.finalWeights.get(state);
	}
	
	public K transitionWeight(Object transition) 
	{
		Object previousState = from(transition);
		if (! transitionsOut.containsKey(previousState)) computeProperties(previousState); 
		return transitionsWeights.get(transition);
	}
	
	void computeProperties(Object state)
	{
		EpsilonAutomaton epsilonAutomaton = new EpsilonAutomaton(operand);		
		
		Map<Object, K> shortestDistances = shortestDistanceAlgorithm.computeShortestDistances(epsilonAutomaton, state); 								
		
		Semiring<K> sr = semiring();
		K finalWeight = sr.zero();
		Collection<Object> transitionsOut = new ArrayList<Object>();
		for (Entry<Object, K> e : shortestDistances.entrySet())
		{
			Object q = e.getKey();
			
			finalWeight = sr.add(finalWeight, sr.multiply(e.getValue(), operand.finalWeight(q)));
			for (Object t : operand.transitionsOut(q)) if (operand.label(t) != null)
			{
				EpsilonRemovalTransition transition = new EpsilonRemovalTransition(t, state);
				transitionsOut.add(transition);
				transitionsWeights.put(transition, sr.multiply(e.getValue(), operand.transitionWeight(t)));
			}
		}
		
		this.transitionsOut.put(state, transitionsOut);
		this.finalWeights.put(state, finalWeight);
	}

	public K initialWeight(Object state) 
	{		
		return operand.initialWeight(state);
	}

	public Object from(Object transition) 
	{		
		return ((EpsilonRemovalTransition) transition).previousState;
	}

	public Object to(Object transition) 
	{		
		return operand.to(((EpsilonRemovalTransition) transition).operandTransition);
	}

	public L label(Object transition) 
	{		
		return operand.label(((EpsilonRemovalTransition) transition).operandTransition);
	}
	
	@Override
	public Comparator<Object> topologicalOrder() 
	{		
		return operand.topologicalOrder();
	}
	
	public class EpsilonAutomaton extends UnaryOperation<L, L, K, K>
	{
		public EpsilonAutomaton(Automaton<L, K> operand) 
		{
			super(operand, operand.semiring());		
		}
		
		@Override
		public Collection<Object> transitionsOut(Object state) 
		{		
			Collection<Object> transitionsOut = new ArrayList<Object>();
			for (Object t : operand.transitionsOut(state))
				if (operand.label(t) == null)
					transitionsOut.add(t);
			return transitionsOut;
		}
		
//		@Override
//		public Collection<Object> transitionsOut(Object state, L label) 
//		{		
//			if (label == null) return transitionsOut(state);
//			else return Collections.emptyList();
//		}
	//	
//		@Override
//		public Collection<L> labelsOut(Object state) 
//		{		
//			for (L label : operand.labelsOut(state))
//				if (label == null) return Arrays.asList(null);
//			return Collections.emptyList();
//		}

		public K initialWeight(Object state) 
		{		
			return operand.initialWeight(state);
		}

		public K finalWeight(Object state) 
		{		
			return operand.finalWeight(state);
		}

		public L label(Object transition) 
		{		
			return operand.label(transition);
		}

		public K transitionWeight(Object transition) 
		{		
			return operand.transitionWeight(transition);
		}
	}
	
	public class EpsilonRemovalTransition
	{
		public Object operandTransition;
		public Object previousState;

		public EpsilonRemovalTransition(Object operandTransition, Object previousState) 
		{
			this.operandTransition = operandTransition;
			this.previousState = previousState;
		}
		
		@Override
		public boolean equals(Object obj) 
		{
			if (obj instanceof EpsilonRemoval.EpsilonRemovalTransition)
			{
				@SuppressWarnings("rawtypes")
				EpsilonRemovalTransition other = (EpsilonRemovalTransition) obj;
				return (this.previousState == null ? other.previousState == null : this.previousState.equals(other.previousState)) 
						&& (this.operandTransition == null ? other.operandTransition == null : this.operandTransition.equals(other.operandTransition));
			}
			else return false;
		}
		
		@Override
		public int hashCode() 
		{
			return super.hashCode() * (previousState == null ? 1 : previousState.hashCode());
		}
		
		@Override
		public String toString() 
		{
			return "EpsilonRemovalTransition(" + operandTransition + ", " + previousState + ")";
		}
	}
}
