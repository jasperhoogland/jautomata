package net.jhoogland.jautomata.operations;

import net.jhoogland.jautomata.Automaton;
import net.jhoogland.jautomata.semirings.BestPathWeights;
import net.jhoogland.jautomata.semirings.KTropicalSemiring;
import net.jhoogland.jautomata.semirings.PathWeight;

public abstract class KTropicalSemiringConversion<K, L> extends SemiringConversion<L, K, BestPathWeights>
{
	public KTropicalSemiringConversion(Automaton<L, K> operand, int k)
	{
		super(operand, new KTropicalSemiring(k));
	}
	
	@Override
	public BestPathWeights transitionWeight(Object transition) 
	{
		BestPathWeights weight = convertWeight(operand.transitionWeight(transition));
		weight.pathWeights[0].transition = transition;
		return weight;
	}
	
	@Override
	public BestPathWeights convertWeight(K weight)
	{
		PathWeight[] p = new PathWeight[((KTropicalSemiring) semiring()).k];
		p[0] = new PathWeight(null, convert(weight), null);
		for (int i = 1; i < p.length; i++) p[i] = new PathWeight(null, Double.POSITIVE_INFINITY, null);
		return new BestPathWeights(p);
	}
	
	public abstract double convert(K weight);
}
