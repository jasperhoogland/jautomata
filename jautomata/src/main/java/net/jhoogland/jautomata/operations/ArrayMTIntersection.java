package net.jhoogland.jautomata.operations;

import java.util.ArrayList;
import java.util.Collection;

import net.jhoogland.jautomata.ArrayMTLabel;
import net.jhoogland.jautomata.MTAutomaton;
import net.jhoogland.jautomata.MTLabel;

/**
 * Intersection of array-based multi-tape automata for a single intersection tape.
 * 
 * @author Jasper Hoogland
 *
 * @param <L>
 * tape label type
 * 
 * @param <K>
 * weight type
 * (Boolean for regular automata and Double for weighted automata)
 */

public class ArrayMTIntersection<L, K> extends MTIntersection<Integer, L, K> 
{
	public ArrayMTIntersection(MTAutomaton<Integer, L, K> operand1, MTAutomaton<Integer, L, K> operand2, int intersectionTape1, int intersectionTape2) 
	{
		super(operand1, operand2, createTapes(operand1.tapes().size() + operand2.tapes().size()), intersectionTape1, intersectionTape2);
	}

	@Override
	protected MTLabel<Integer, L> label(MTLabel<Integer, L> label1, MTLabel<Integer, L> label2) 
	{		
		int m = label1 == null ? 0 : label1.tapes().size();
		int n = label2 == null ? 0 : label2.tapes().size();
		L[] tLabels = (L[]) new Object[m + n];
		for (int i = 0; i < m; i++)
			tLabels[i] = label1.tapeLabel(i);
		for (int i = 0; i < n; i++)
			tLabels[m + i] = label2.tapeLabel(i);		
		return new ArrayMTLabel<L>(tLabels);
	}
	
	private static Collection<Integer> createTapes(int n) 
	{
		Collection<Integer> tapes = new ArrayList<Integer>(n);
		for (int i = 0; i < n; i++) tapes.add(i);
		return tapes;
	}
}
