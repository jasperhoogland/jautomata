package net.jhoogland.jautomata.operations;

import java.util.ArrayList;
import java.util.Collection;

import net.jhoogland.jautomata.AbstractAutomaton;
import net.jhoogland.jautomata.Automata;
import net.jhoogland.jautomata.Automaton;

public class Concatenation<L, K> extends AbstractAutomaton<L, K> 
{
	Automaton<L, K>[] operands;
	
	public Concatenation(Automaton<L, K>... operands) 
	{
		super(operands[0].semiring());
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
		if (s.type == 0)
		{
			for (Object t : operands[s.index].transitionsOut(s.value))
				transitionsOut.add(new ConcatenationElement(2, s.index, t));
			if (Automata.isFinalState(operands[s.index], s.value)
					&& s.index + 1 < operands.length)
				transitionsOut.add(new ConcatenationElement(3, s.index, s.value));
		}
		else if (s.type == 1 && s.index + 1 < operands.length)
		{
			for (Object ios : operands[s.index + 1].initialStates())
				transitionsOut.add(new ConcatenationElement(4, s.index, ios));
				
		}		
		return transitionsOut;
	}

	public K initialWeight(Object state) 
	{
		ConcatenationElement s = (ConcatenationElement) state;		
		return s.type == 0 && s.index == 0 ? operands[0].initialWeight(s.value) : semiring().zero();
	}

	public K finalWeight(Object state) 
	{
		ConcatenationElement s = (ConcatenationElement) state;		
		return s.type == 0 && s.index + 1 == operands.length
				? operands[s.index].finalWeight(s.value) 
				: semiring().zero();
	}

	public Object previousState(Object transition) {
		// TODO Auto-generated method stub
		return null;
	}

	public Object nextState(Object transition) {
		// TODO Auto-generated method stub
		return null;
	}

	public L label(Object transition) {
		// TODO Auto-generated method stub
		return null;
	}

	public K transitionWeight(Object transition) {
		// TODO Auto-generated method stub
		return null;
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
