package net.jhoogland.jautomata.operations;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import net.jhoogland.jautomata.AbstractAutomaton;
import net.jhoogland.jautomata.Automaton;
import net.jhoogland.jautomata.ExtendedAutomaton;

/**
 * 
 * Abstract implementation of generic automaton intersection.
 * Acceptor intersection and transducer composition are special cases of generic intersection.
 * They are implemented by {@link AcceptorIntersection} and TransducerComposition, 
 * both of which are subclasses of {@link Intersection}.
 * 
 * @author Jasper Hoogland
 *
 * @param <L1>
 * label type of the first operand
 * 
 * @param <L2>
 * label type of the second operand
 * 
 * @param <L3>
 * label type of the intersection
 * 
 * @param <K>
 * weight type
 * (Boolean for regular automata and Double for weighted automata)
 */

public abstract class Intersection<L1, L2, L3, K> extends AbstractAutomaton<L3, K>  
{
	Automaton<L1, K> operand1;
	Automaton<L2, K> operand2;
	private int extensionType;
		
	public Intersection(Automaton<L1, K> operand1, Automaton<L2, K> operand2) 
	{
		super(operand1.semiring());
		this.operand1 = operand1;
		this.operand2 = operand2;
		this.extensionType = extensionType();
	}
	
	public Collection<Object> initialStates() 
	{
		Collection<Object> initialStates = new ArrayList<Object>();
		for (Object initialState1 : operand1.initialStates())
		{
			for (Object initialState2 : operand2.initialStates())
			{
				initialStates.add(new IntersectionState(initialState1, initialState2, 0));
			}
		}
		return initialStates;
	}

	public Collection<Object> transitionsOut(Object state) 
	{
		IntersectionState s = (IntersectionState) state;
		Collection<Object> transitionsOut = new ArrayList<Object>();
		Map<Object, Collection<Object>> transitionsAByLabel = new HashMap<Object, Collection<Object>>();
		if (extensionType == NONE) for (Object transition1 : operand1.transitionsOut(s.operandState1))
		{
			L1 label = operand1.label(transition1);
			Object labelValue = intersectionLabel1(label);
			if (labelValue != null || s.filterState == 0)
			{
				Collection<Object> ts = transitionsAByLabel.get(labelValue);
				if (ts == null)
				{
					ts = new ArrayList<Object>();
					transitionsAByLabel.put(labelValue, ts);
				}
				ts.add(transition1);
			}
		}
		for (Object transitionB : aIs1() ? operand2.transitionsOut(s.operandState2) : operand1.transitionsOut(s.operandState1))
		{
			Object transition1 = aIs1() ? null : transitionB;
			Object transition2 = aIs1() ? transitionB : null;
			
//			L2 label = operand2.label(transitionB);
			Object labelValue = aIs1() ? 
					intersectionLabel2(operand2.label(transition2)) :
					intersectionLabel1(operand1.label(transition1));
					
			if (labelValue != null || s.filterState == 0)
			{
				Collection<Object> transitionsA = null;
				if (extensionType == NONE) transitionsA = transitionsAByLabel.get(labelValue);
				else if (aIs1()) transitionsA = transitionsOut1(s.operandState1, labelValue);
				else transitionsA = transitionsOut2(s.operandState2, labelValue);
						
				if (transitionsA != null) for (Object transitionA : transitionsA)
				{
					if (aIs1()) transition1 = transitionA;
					else transition2 = transitionA;
					transitionsOut.add(new IntersectionTransition(transition1, transition2, null, null, s.filterState));
				}				
			}
		}
		if (s.filterState == 0 || s.filterState == 1) 
		{
			if (ext1())
				for (Object transition1 : transitionsOut1(s.operandState1, null))
					transitionsOut.add(new IntersectionTransition(transition1, null, null, s.operandState2, s.filterState));
			else
				for (Object transition1 : operand1.transitionsOut(s.operandState1))
					if (intersectionLabel1(operand1.label(transition1)) == null)
						transitionsOut.add(new IntersectionTransition(transition1, null, null, s.operandState2, s.filterState));
		}
		if (s.filterState == 0 || s.filterState == 2) 
		{
			if (ext2())
				for (Object transition2 : transitionsOut2(s.operandState2, null))
			transitionsOut.add(new IntersectionTransition(null, transition2, s.operandState1, null, s.filterState));				
			else 
				for (Object transition2 : operand2.transitionsOut(s.operandState2))
					if (intersectionLabel2(operand2.label(transition2)) == null)
						transitionsOut.add(new IntersectionTransition(null, transition2, s.operandState1, null, s.filterState));
		}
		return transitionsOut;
	}
	
//	Old:
	public Collection<Object> transitionsOutOld(Object state) 
	{
		IntersectionState s = (IntersectionState) state;
		Collection<Object> transitionsOut = new ArrayList<Object>();
		Map<Object, Collection<Object>> transitions1ByLabel = new HashMap<Object, Collection<Object>>();
		for (Object transition1 : operand1.transitionsOut(s.operandState1))
		{
			L1 label = operand1.label(transition1);
			Object labelValue = intersectionLabel1(label);
			if (labelValue != null || s.filterState == 0)
			{
				Collection<Object> ts = transitions1ByLabel.get(labelValue);
				if (ts == null)
				{
					ts = new ArrayList<Object>();
					transitions1ByLabel.put(labelValue, ts);
				}
				ts.add(transition1);
			}
		}
		for (Object transition2 : operand2.transitionsOut(s.operandState2))
		{
			L2 label = operand2.label(transition2);
			Object labelValue = intersectionLabel2(label);
			Collection<Object> transitions1 = transitions1ByLabel.get(labelValue);
			if (transitions1 != null) for (Object transition1 : transitions1)
			{
				transitionsOut.add(new IntersectionTransition(transition1, transition2, null, null, s.filterState));
			}
		}
		if (s.filterState == 0 || s.filterState == 1) for (Object transition1 :  operand1.transitionsOut(s.operandState1))
		{
			if (intersectionLabel1(operand1.label(transition1)) == null) transitionsOut.add(new IntersectionTransition(transition1, null, null, s.operandState2, s.filterState));
		}
		if (s.filterState == 0 || s.filterState == 2) for (Object transition2 : operand2.transitionsOut(s.operandState2))
		{
			if (intersectionLabel2(operand2.label(transition2)) == null) transitionsOut.add(new IntersectionTransition(null, transition2, s.operandState1, null, s.filterState));
		}
		return transitionsOut;
	}
	
	protected static final int NONE = 0;
	protected static final int EXT1 = 1;
	protected static final int EXT2 = 2;
	protected static final int BOTH1 = 3;
	protected static final int BOTH2 = 4;
	
	private boolean aIs1()
	{
		return ! (extensionType == EXT2 || extensionType == BOTH2);
	}
	
	private boolean ext1()
	{
		return extensionType == EXT1 || extensionType >= BOTH1;
	}
	
	private boolean ext2()
	{
		return extensionType == EXT2 || extensionType >= BOTH1;
	}
	
	protected int extensionType()
	{
		return 0;
	}
	
	protected Collection<Object> transitionsOut1(Object state, Object label)
	{
		return null;
	}
	
	protected Collection<Object> transitionsOut2(Object state, Object label)
	{
		return null;
	}
	
	protected abstract Object intersectionLabel1(L1 label);
	protected abstract Object intersectionLabel2(L2 label);
	protected abstract L3 label(L1 label1, L2 label2);

	public K initialWeight(Object state) 
	{		
		IntersectionState s = (IntersectionState) state;
		return s.filterState == 0 ? semiring().multiply(operand1.initialWeight(s.operandState1), operand2.initialWeight(s.operandState2)) : semiring().zero();
	}

	public K finalWeight(Object state) 
	{
		IntersectionState s = (IntersectionState) state;
		return semiring().multiply(operand1.finalWeight(s.operandState1), operand2.finalWeight(s.operandState2));
	}

	public Object from(Object transition) 
	{		
		IntersectionTransition t = (IntersectionTransition) transition;
		Object previousState1 = t.operandTransition1 == null ? t.operandState1 : operand1.from(t.operandTransition1);
		Object previousState2 = t.operandTransition2 == null ? t.operandState2 : operand2.from(t.operandTransition2);
		return new IntersectionState(previousState1, previousState2, t.fromFilterState);
	}

	public Object to(Object transition)
	{
		IntersectionTransition t = (IntersectionTransition) transition;
		Object nextState1 = t.operandTransition1 == null ? t.operandState1 : operand1.to(t.operandTransition1);
		Object nextState2 = t.operandTransition2 == null ? t.operandState2 : operand2.to(t.operandTransition2);
		int filterState = t.operandTransition1 == null ? 2 : (t.operandTransition2 == null ? 1 : 0);
		return new IntersectionState(nextState1, nextState2, filterState);
	}

	public L3 label(Object transition) 
	{		
		IntersectionTransition t = (IntersectionTransition) transition;
		return label(
				t.operandTransition1 == null ? null : operand1.label(t.operandTransition1), 
				t.operandTransition2 == null ? null : operand2.label(t.operandTransition2));
	}

	public K transitionWeight(Object transition) 
	{	
		IntersectionTransition t = (IntersectionTransition) transition;
		if (t.operandTransition1 == null) return operand2.transitionWeight(t.operandTransition2);
		else
		{
			K weight = operand1.transitionWeight(t.operandTransition1);
			if (t.operandTransition2 != null) weight = semiring().multiply(weight, operand2.transitionWeight(t.operandTransition2));
			return weight;
		}				
	}

//	@Override
//	public Collection<I> tapes() 
//	{
//		HashSet<I> tapes = new HashSet<I>(operand1.tapes());
//		tapes.addAll(operand2.tapes());
//		return tapes;
//	}

	
	public Comparator<Object> topologicalOrder() 
	{
		final Comparator<Object> operand1TopologicalOrder = operand1.topologicalOrder();
		final Comparator<Object> operand2TopologicalOrder = operand2.topologicalOrder();
		return operand1TopologicalOrder == null && operand2TopologicalOrder == null ? null :
			new Comparator<Object>()
			{
				public int compare(Object o1, Object o2) 
				{
					IntersectionState s1 = (IntersectionState) o1;
					IntersectionState s2 = (IntersectionState) o2;
					int comp = operand1TopologicalOrder == null ? 0 : operand1TopologicalOrder.compare(s1.operandState1, s2.operandState1);
					if (comp == 0) comp = operand2TopologicalOrder == null ? 0 : operand2TopologicalOrder.compare(s1.operandState2, s2.operandState2);
					if (comp == 0 && s1.filterState != s2.filterState) 
					{
						if (s1.filterState == 0) comp = -1;
						else if (s2.filterState == 0) comp = 1;
						else if (s1.filterState == 1) comp = -1;
						else comp = 1;
					}
					return comp;
				}			
			};
	}
	
	class IntersectionState
	{
		public Object operandState1; 
		public Object operandState2;
		public int filterState;
		
		public IntersectionState(Object operandState1, Object operandState2, int filterState)
		{
			this.operandState1 = operandState1;
			this.operandState2 = operandState2;
			this.filterState = filterState;
			
			// debug:
			if (operandState1 == null) throw new RuntimeException();
			if (operandState2 == null) throw new RuntimeException();
		}
		
		@Override
		public boolean equals(Object obj) 
		{		
			if (obj instanceof Intersection.IntersectionState)
			{
				IntersectionState other = (IntersectionState) obj; 
				return this.operandState1.equals(other.operandState1) && this.operandState2.equals(other.operandState2)
						&& this.filterState == other.filterState;
			}
			return false;
		}
		
		@Override
		public int hashCode() 
		{		
			return 3 * (operandState1.hashCode() + operandState2.hashCode()) + filterState;
		}
		
		@Override
		public String toString() 
		{
			return "IntersectionState(" + operandState1 + ", " + operandState2 + ", " + filterState + ")";
		}
	}

	class IntersectionTransition 
	{
		public Object operandTransition1;
		public Object operandTransition2;
		public int fromFilterState;
		
		public Object operandState1;
		public Object operandState2;
		
		public IntersectionTransition(Object operandTransition1, Object operandTransition2, Object operandState1, Object operandState2, int fromFilterState) 
		{
			this.operandTransition1 = operandTransition1;
			this.operandTransition2 = operandTransition2;
			this.operandState1 = operandState1;
			this.operandState2 = operandState2;
			this.fromFilterState = fromFilterState;
		}
		
		@Override
		public boolean equals(Object obj) 
		{
			if (obj instanceof Intersection.IntersectionTransition)
			{
				IntersectionTransition other = (IntersectionTransition) obj;
				return equals(this.operandState1, other.operandState1) &&
						equals(this.operandState2, other.operandState2) &&
						equals(this.operandTransition1, other.operandTransition1) &&
						equals(this.operandTransition2, other.operandTransition2) &&
						this.fromFilterState == other.fromFilterState;
						
			}
			else return false;
		}
		
		public <E> boolean equals (E e1, E e2)
		{
			return e1 == null && e2 == null || e1 != null && e2 != null && e1.equals(e2);
		}
		
		@Override
		public int hashCode() 
		{
			int hashCode = 0;
			if (operandTransition1 != null) hashCode += operandTransition1.hashCode();
			if (operandTransition2 != null) hashCode += operandTransition2.hashCode();
			if (operandState1 != null) hashCode += operandState1.hashCode();
			if (operandState2 != null) hashCode += operandState2.hashCode();
			hashCode *= 3;
			hashCode += fromFilterState;
			return hashCode;
		}
		
		@Override
		public String toString() 
		{		
			return "IntersectionTransition(" + operandTransition1 + ", " + operandState1 + ", " + operandTransition2 + ", " + operandState2 + ", " + fromFilterState + ")";
		}
	}

}
