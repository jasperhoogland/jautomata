package net.jhoogland.jautomata.operations;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;

import net.jhoogland.jautomata.AbstractAutomaton;
import net.jhoogland.jautomata.Automata;
import net.jhoogland.jautomata.Automaton;

public class Concatenation<L, K> extends AbstractAutomaton<L, K> 
{
	Automaton<L, K>[] operands;
	boolean hasTopologicalOrder;
	
	private static int STATE_IN_OPERAND = 0;
	private static int STATE_CONNECTION = 1;
	private static int TRANSITION_IN_OPERAND = 2;
	private static int TRANSITION_TO_CONSTATE = 3;
	private static int TRANSITION_FROM_CONSTATE = 4;
	
	public Concatenation(Automaton<L, K>... operands) 
	{
		super(operands[0].semiring());
		this.operands = operands;
		hasTopologicalOrder = true;
		for (Automaton<L, K> operand : operands)
		{
			if (operand.topologicalOrder() == null)
			{
				hasTopologicalOrder = false;
				break;
			}
		}
	}

	public Collection<Object> initialStates() 
	{
		Collection<Object> initialStates = new ArrayList<Object>();
		for (Object ios : operands[0].initialStates())
			initialStates.add(new ConcatenationElement(0, 0, ios));
		return initialStates;
	}
	
	@Override
	public Collection<Object> transitionsOut(Object state) 
	{
		Collection<Object> transitionsOut = new ArrayList<Object>();
		ConcatenationElement s = (ConcatenationElement) state;
		if (s.type == STATE_IN_OPERAND)
		{
			for (Object t : operands[s.index].transitionsOut(s.value))
				transitionsOut.add(new ConcatenationElement(TRANSITION_IN_OPERAND, s.index, t));
			if (Automata.isFinalState(operands[s.index], s.value)
					&& s.index + 1 < operands.length)
				transitionsOut.add(new ConcatenationElement(TRANSITION_TO_CONSTATE, s.index, s.value));
		}
		else if (s.type == STATE_CONNECTION && s.index + 1 < operands.length)
		{
			for (Object ios : operands[s.index + 1].initialStates())
				transitionsOut.add(new ConcatenationElement(TRANSITION_FROM_CONSTATE, s.index, ios));
				
		}		
		return transitionsOut;
	}

	public K initialWeight(Object state) 
	{
		ConcatenationElement s = (ConcatenationElement) state;		
		return s.type == STATE_IN_OPERAND && s.index == 0 ? operands[0].initialWeight(s.value) : semiring().zero();
	}

	public K finalWeight(Object state) 
	{
		ConcatenationElement s = (ConcatenationElement) state;		
		return s.type == STATE_IN_OPERAND && s.index + 1 == operands.length
				? operands[s.index].finalWeight(s.value) 
				: semiring().zero();
	}

	public Object previousState(Object transition)
	{
		ConcatenationElement t = (ConcatenationElement) transition;
		if (t.type == TRANSITION_IN_OPERAND) 
			return new ConcatenationElement(STATE_IN_OPERAND, t.index, operands[t.index].previousState(t.value));
		else if (t.type == TRANSITION_TO_CONSTATE)
			return new ConcatenationElement(STATE_IN_OPERAND, t.index, t.value);
		else if (t.type == TRANSITION_FROM_CONSTATE)
			return new ConcatenationElement(STATE_CONNECTION, t.index, null);
		return null;
	}

	public Object nextState(Object transition) 
	{
		ConcatenationElement t = (ConcatenationElement) transition;
		if (t.type == TRANSITION_IN_OPERAND) 
			return new ConcatenationElement(STATE_IN_OPERAND, t.index, operands[t.index].nextState(t.value));
		else if (t.type == TRANSITION_TO_CONSTATE)
			return new ConcatenationElement(STATE_CONNECTION, t.index, null);			
		else if (t.type == TRANSITION_FROM_CONSTATE)
			return new ConcatenationElement(STATE_IN_OPERAND, t.index + 1, t.value);			
		return null;
	}

	public L label(Object transition) 
	{
		ConcatenationElement t = (ConcatenationElement) transition;
		if (t.type == TRANSITION_IN_OPERAND) 
			return operands[t.index].label(t.value);
		else if (t.type == TRANSITION_TO_CONSTATE || t.type == TRANSITION_FROM_CONSTATE)
			return null;			
		return null;				
	}

	public K transitionWeight(Object transition) 
	{
		ConcatenationElement t = (ConcatenationElement) transition;
		if (t.type == TRANSITION_IN_OPERAND) 
			return operands[t.index].transitionWeight(t.value);
		else if (t.type == TRANSITION_TO_CONSTATE)
			return operands[t.index].finalWeight(t.value);
		else if (t.type == TRANSITION_FROM_CONSTATE)
			return operands[t.index + 1].initialWeight(t.value);
		return null;				
	}
	
	@Override
	public Comparator<Object> topologicalOrder() 
	{
		if (hasTopologicalOrder)
		{
			return new Comparator<Object>() 
			{
				public int compare(Object arg0, Object arg1) 
				{
					ConcatenationElement s1 = (ConcatenationElement) arg0;
					ConcatenationElement s2 = (ConcatenationElement) arg1;
					int idxDiff = s1.index - s2.index;
					if (idxDiff != 0) return (int) Math.signum(idxDiff);
					else
					{
						int typeDiff = s1.type - s2.type;
						if (typeDiff != 0) return (int) Math.signum(typeDiff);
						else if (s1.type == 0)
							return operands[s1.index].topologicalOrder().compare(s1.value, s2.value);							
						else return 0;
					}					
				}				
			};
		}
		else return null;
	}
	
	class ConcatenationElement
	{
		public int type; // 0 state in operand, 1 connection state
			// 2 transition in operand, 3 to connection state, 4 from connection state
		public int index;
		public Object value;
		
		public ConcatenationElement(int inOperand, int index, Object value) 
		{
			this.index = index;
			this.value = value;
			this.type = inOperand;
		}
		
		@Override
		public String toString() 
		{			
			return "<" + type + ", " + index + ", " + value.toString() + ">";
		}
		
		@Override
		public boolean equals(Object obj) 
		{			
			if (obj == null || ! (obj instanceof Concatenation.ConcatenationElement)) 
				return false;
			ConcatenationElement other = (ConcatenationElement) obj;			
			return this.type == other.type
					&& this.index == other.index 
					&& (this.value == null ? other.value == null : this.value.equals(other.value));
		}
		
		@Override
		public int hashCode() 
		{			
			return type + index + (value == null ? 0 : value.hashCode());
		}
	}	
}
