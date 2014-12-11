package net.jhoogland.jautomata.operations;

import net.jhoogland.jautomata.Automaton;
import net.jhoogland.jautomata.semirings.BestPathWeights;
import net.jhoogland.jautomata.semirings.KTropicalSemiring;
import net.jhoogland.jautomata.semirings.PathWeight;

public class KTropicalSemiringConversion<K extends Comparable<K>, L> extends SemiringConversion<L, K, BestPathWeights<K>>
{
	public KTropicalSemiringConversion(Automaton<L, K> operand, int k)
	{
		super(operand, new KTropicalSemiring<K>(k, operand.semiring()));
	}
	
	@Override
	public BestPathWeights<K> transitionWeight(Object transition) 
	{
		BestPathWeights<K> weight = convertWeight(operand.transitionWeight(transition));
		weight.pathWeights[0].transition = transition;
		return weight;
	}
	
	@Override
	public BestPathWeights<K> convertWeight(K weight)
	{
		PathWeight<K>[] p = new PathWeight[((KTropicalSemiring<K>) semiring()).k];
		p[0] = new PathWeight<K>(null, weight, null);
//		p[0] = new PathWeight<K>(null, convert(weight), null);
		for (int i = 1; i < p.length; i++) p[i] = new PathWeight<K>(null, operand.semiring().zero(), null);
		return new BestPathWeights<K>(p);
	}
	
//	public abstract K convert(K weight);
}
