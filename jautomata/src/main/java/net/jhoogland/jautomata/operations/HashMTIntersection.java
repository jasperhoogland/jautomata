package net.jhoogland.jautomata.operations;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.jhoogland.jautomata.HashMTLabel;
import net.jhoogland.jautomata.MTAutomaton;
import net.jhoogland.jautomata.MTLabel;

public class HashMTIntersection<I, L, K> extends MTIntersection<I, L, K> 
{
	public HashMTIntersection(MTAutomaton<I, L, K> operand1, MTAutomaton<I, L, K> operand2, I intersectionTape1, I intersectionTape2) 
	{
		super(operand1, operand2, merge(operand1.tapes(), operand2.tapes()), intersectionTape1, intersectionTape2);		
	}
	
	private static <I> Collection<I> merge(Collection<I> tapes1, Collection<I> tapes2)
	{
		Set<I> tapes = new HashSet<I>(tapes1);
		tapes.addAll(tapes2);
		return tapes;
	}

	@Override
	protected MTLabel<I, L> label(MTLabel<I, L> label1, MTLabel<I, L> label2) 
	{
		Map<I, L> map = new HashMap<I, L>();
		if (label1 != null) 
			for (I tape : label1.tapes()) 
				map.put(tape, label1.tapeLabel(tape));
		if (label2 != null) 
			for (I tape : label2.tapes()) 
				map.put(tape, label2.tapeLabel(tape));
		return new HashMTLabel<I, L>(map);
	}
}
