package net.jhoogland.jautomata;

import java.util.HashSet;
import java.util.Set;

public class Validation 
{
	/**
	 * 
	 * Checks if the <code>transitionsOutI</code> and <code>transitionsOutO</code> methods are compatible with the <code>transitionsOut</code> method.
	 * 
	 */
	
	public static <I, O, K> void validateExtendedAutomaton(ExtendedTransducer<I, O, K> a, boolean checkReverse)
	{
		Set<I> inputLabels = new HashSet<I>();
		Set<O> outputLabels = new HashSet<O>();
		for (Object s : Automata.states(a))
		{
			for (Object t : a.transitionsOut(s))
			{
				TLabel<I, O> l = a.label(t);
				I li = l.in();
				O lo = l.out();
				if (! a.transitionsOutI(s, li).contains(t))
				{
					System.err.println("Transition returned by transitionsOut(state), but not by transitionsOutI(state, inputLabel): " + t);
					System.err.println("State: " + s );
					System.err.println("Input label: " + li);
					System.err.println("Transition: " + t);
					throw new RuntimeException();
				}
				if (! a.transitionsOutO(s, lo).contains(t))
				{
					System.err.println("Transition returned by transitionsOut(state), but not by transitionsOutO(state, inputLabel): " + t);
					System.err.println("State: " + s );
					System.err.println("Output label: " + lo);
					System.err.println("Transition: " + t);
					throw new RuntimeException();
				}
				if (checkReverse)
				{
					inputLabels.add(li);
					outputLabels.add(lo);					
				}
			}
		}
		if (! checkReverse) return;
		for (Object s : Automata.states(a))
		{
			for (I li : inputLabels) for (Object t : a.transitionsOutI(s, li))
			{
				if (! new HashSet<Object>(a.transitionsOut(s)).contains(t))
				{
					System.err.println("Transition returned by transitionsOutI(state, inputLabel), but not by transitionsOut(state):");
					System.err.println("State: " + s );
					System.err.println("Input label: " + li);
					System.err.println("Transition: " + t);
					throw new RuntimeException();
				}
			}
			for (O lo : outputLabels) for (Object t : a.transitionsOutO(s, lo))
			{
				if (! new HashSet<Object>(a.transitionsOut(s)).contains(t))
				{
					System.err.println("Transition returned by transitionsOutO(state, outputLabel), but not by transitionsOut(state): " + t);
					System.err.println("State: " + s );
					System.err.println("Output label: " + lo);
					System.err.println("Transition: " + t);
					throw new RuntimeException();
				}
			}
		}
	}
}
