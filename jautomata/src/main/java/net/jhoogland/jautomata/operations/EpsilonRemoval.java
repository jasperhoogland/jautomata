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
 * 
 * This is an implementation of the on-the-fly epsilon removal algorithm described in [1]. Instances of this class are equivalent
 * to their operands.   
 * 
 * [1] M. Mohri, Generic Epsilon-Removal Algorithm for Weighted Automata. 2000.

 * 
 * @author Jasper Hoogland
 *
 * @param <S>
 * state type
 * 
 * @param <T>
 * transition type
 * 
 * @param <L>
 * label type
 * 
 * @param <K>
 * the type over which the semiring is defined. 
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
		Object previousState = previousState(transition);
		if (! transitionsOut.containsKey(previousState)) computeProperties(previousState); 
		return transitionsWeights.get(transition);
	}
	
	void computeProperties(Object state)
	{
		EpsilonAutomaton<L, K> epsilonAutomaton = new EpsilonAutomaton<L, K>(operand);		
		
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

	public Object previousState(Object transition) 
	{		
		return ((EpsilonRemovalTransition) transition).previousState;
	}

	public Object nextState(Object transition) 
	{		
		return operand.nextState(((EpsilonRemovalTransition) transition).operandTransition);
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
			return "(" + operandTransition + ", " + previousState + ")";
		}
	}
}
