package net.jhoogland.jautomata.operations;

import java.util.Arrays;
import java.util.List;

import net.jhoogland.jautomata.Automaton;
import net.jhoogland.jautomata.semirings.KTropicalSemiring;
import net.jhoogland.jautomata.semirings.PathWeight;

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
		weight.get(0).transition = transition;
		return weight;
	}
	
	@Override
	public List<PathWeight<K>> convertWeight(K weight)
	{
//		PathWeight<K>[] p = new PathWeight[((KTropicalSemiring<K>) semiring()).k];
//		p[0] = new PathWeight<K>(null, weight, null);
////		p[0] = new PathWeight<K>(null, convert(weight), null);
//		for (int i = 1; i < p.length; i++) p[i] = new PathWeight<K>(null, operand.semiring().zero(), null);
		return Arrays.asList(new PathWeight<K>(null, weight, null));
		
	}
	
//	public abstract K convert(K weight);
}
