package net.jhoogland.jautomata.operations;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;

import net.jhoogland.jautomata.AbstractAutomaton;
import net.jhoogland.jautomata.Automaton;

/**
 * 
 * Implementation of the union of a set of automata (its operands).
 * A string is accepted by the union if and only if it is accepted by one of its operands.
 * The weight of a string is the minimum string weight of its operands (in case of the tropical semiring)
 * or the sum of the string weights of its operands (in case of the probability semiring).
 * 
 * The union has a topological order if all operands have one. 
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

public class Union<L, K> extends AbstractAutomaton<L, K> 
{
	public Automaton<L, K>[] operands;

	/**
	 * 
	 * Creates the union of the specified automata.
	 * The semiring is taken from the first operand.
	 * 
	 * @param operands
	 */
	
	public Union(Automaton<L, K>... operands) 
	{
		super(operands[0].semiring());
		this.operands = (Automaton<L, K>[]) operands;
	}

	public Collection<Object> initialStates() 
	{
		Collection<Object> initialStates = new ArrayList<Object>();
		for (int i = 0; i < operands.length; i++)
		{
			for (Object initialOperandState : operands[i].initialStates())
			{
				initialStates.add(new UnionElement(i, initialOperandState));
			}
		}
		return initialStates;
	}
	
//	@Override
//	public Collection<L> labelsOut(Object state) 
//	{
//		UnionElement s = (UnionElement) state;
//		return operands[s.index].labelsOut(s.value);
//	}
//	
//	@Override
//	public Collection<Object> transitionsOut(Object state, L label) 
//	{
//		Collection<Object> transitionsOut = new ArrayList<Object>();
//		UnionElement s = (UnionElement) state;
//		for (Object operandTransition : operands[s.index].transitionsOut(s.value, label))
//		{
//			transitionsOut.add(new UnionElement(s.index, operandTransition));
//		}
//		return transitionsOut;
//	}

	public Collection<Object> transitionsOut(Object state) 
	{
		Collection<Object> transitionsOut = new ArrayList<Object>();
		UnionElement s = (UnionElement) state;
		for (Object operandTransition : operands[s.index].transitionsOut(s.value))
		{
			transitionsOut.add(new UnionElement(s.index, operandTransition));
		}
		return transitionsOut;
	}
	
	public K initialWeight(Object state) 
	{		
		UnionElement s = (UnionElement) state; 
		return operands[s.index].initialWeight(s.value);
	}

	public K finalWeight(Object state) 
	{		
		UnionElement s = (UnionElement) state; 
		return operands[s.index].finalWeight(s.value);
	}

	public Object from(Object transition) 
	{		
		UnionElement t = (UnionElement) transition; 
		return new UnionElement(t.index, operands[t.index].from(t.value));
	}

	public Object to(Object transition) 
	{		
		UnionElement t = (UnionElement) transition; 
		return new UnionElement(t.index, operands[t.index].to(t.value));
	}

	public L label(Object transition) 
	{
		UnionElement t = (UnionElement) transition;
		return operands[t.index].label(t.value);
	}

	public K transitionWeight(Object transition) 
	{
		UnionElement t = (UnionElement) transition;
		return operands[t.index].transitionWeight(t.value);
	}

	@Override
	public Comparator<Object> topologicalOrder() 
	{		
		@SuppressWarnings("unchecked")
		final Comparator<Object>[] topologicalOrders = new Comparator[operands.length];
		for (int i = 0; i < operands.length; i++) 
		{
			topologicalOrders[i] = operands[i].topologicalOrder();
			if (topologicalOrders[i] == null) return null;
		}
		return new Comparator<Object>()
		{
			public int compare(Object o1, Object o2) 
			{
				UnionElement s1 = (UnionElement) o1;
				UnionElement s2 = (UnionElement) o2;
				int operandComp = (int) Math.signum(s1.index - s2.index);
				if (operandComp != 0) return operandComp;
				else return topologicalOrders[s1.index].compare(s1.value, s2.value);				
			}			
		};
	}
	
	class UnionElement
	{
		public int index;
		public Object value;
		
		public UnionElement(int index, Object value) 
		{
			this.index = index;
			this.value = value;
		}
		
		@Override
		public String toString() 
		{			
			return "<" + index + ", " + value.toString() + ">";
		}
		
		@Override
		public boolean equals(Object obj) 
		{			
			if (obj == null || ! (obj instanceof Union.UnionElement)) return false;
			UnionElement other = (UnionElement) obj;			
			return this.index == other.index && (this.value == null ? other.value == null : this.value.equals(other.value));
		}
		
		@Override
		public int hashCode() 
		{			
			return index + (value == null ? 0 : value.hashCode());
		}
	}	
}
