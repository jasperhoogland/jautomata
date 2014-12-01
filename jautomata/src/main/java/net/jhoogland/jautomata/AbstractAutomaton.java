package net.jhoogland.jautomata;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

import net.jhoogland.jautomata.semirings.Semiring;

/**
 * 
 * Abstract class that implements some basic methods of the {@link Automaton} interface.
 * Most automata are a subclass of {@link AbstractAutomaton}, though this is not required. 
 * 
 * This class provides implementations of labelsOut(state), transitionsOut(state, label), and transitionsOut(state).
 * Subclasses of this class must override either (I) labelsOut(state) and transitionsOut(state, label) 
 * or (II) transitionsOut(state), because this class implements them in terms of each other.
 * 
 * @author Jasper Hoogland
 *
 * @param <L>
 * The label type
 * 
 * @param <K>
 * The type of elements of the semiring over which the automaton is defined 
 * (Boolean for regular automata and Double for weighted automata)
 * 
 */

public abstract class AbstractAutomaton<L, K> implements Automaton<L, K> 
{
	private Semiring<K> semiring;
	private Comparator<Object> topologicalOrder;
	
	/**
	 * 
	 * Creates an instance with the specified {@link Semiring} and topological order.
	 * 
	 */
	
	public AbstractAutomaton(Semiring<K> semiring, Comparator<Object> topologicalOrder) 
	{
		this.semiring = semiring;
		this.topologicalOrder = topologicalOrder;
	}
	
	/**
	 * 
	 * Creates an instance with the specified {@link Semiring}.
	 * 
	 */
	
	public AbstractAutomaton(Semiring<K> semiring)
	{
		this(semiring, null);
	}

	public Collection<L> labelsOut(Object state) 
	{
		Set<L> labelsOut = new HashSet<L>();
		for (Object t : transitionsOut(state)) 
			labelsOut.add(label(t));
		return labelsOut;
	}

	public Collection<Object> transitionsOut(Object state, L label) 
	{
		Collection<Object> transitionsOut = new ArrayList<Object>();
		for (Object t : transitionsOut(state))
		{
			L tLabel = label(t);
			if (label == null && tLabel == null || label != null && tLabel != null && label.equals(tLabel))
				transitionsOut.add(t);
		}
		return transitionsOut;
	}

	public Collection<Object> transitionsOut(Object state) 
	{
		Collection<Object> transitionsOut = new ArrayList<Object>();
		for (L label : labelsOut(state))
			for (Object t : transitionsOut(state, label))
				transitionsOut.add(t);
		return transitionsOut;
	}

	public Semiring<K> semiring() 
	{		
		return semiring;
	}
	
	public Comparator<Object> topologicalOrder() 
	{		
		return topologicalOrder;
	}
}
