package net.jhoogland.jautomata.operations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import net.jhoogland.jautomata.Automaton;
import net.jhoogland.jautomata.semirings.KTropicalSemiring;
import net.jhoogland.jautomata.semirings.PathWeight;

/**
 * Conversion of the Boolean, log, and tropical semiring to the k-tropical semiring.
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

public class KTropicalSemiringConversion<K extends Comparable<K>, L> extends SemiringConversion<L, K, List<PathWeight<K>>>
{
	public KTropicalSemiringConversion(Automaton<L, K> operand, int k)
	{
		super(operand, new KTropicalSemiring<K>(k, operand.semiring()));
	}
	
	@Override
	public List<PathWeight<K>> transitionWeight(Object transition) 
	{
		List<PathWeight<K>> weight = convertWeight(operand.transitionWeight(transition));
		if(!weight.isEmpty())
		{
			weight.get(0).transition = transition;
		}
		return weight;
	}
	
	@Override
	public List<PathWeight<K>> convertWeight(K weight)
	{
//		return Arrays.asList(new PathWeight<K>(null, weight, null));
		if (operand.semiring().zero().equals(weight)) return Collections.emptyList();
		else return Arrays.asList(new PathWeight<K>(null, weight, null));
	}
}
