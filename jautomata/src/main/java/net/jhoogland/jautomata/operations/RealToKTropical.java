package net.jhoogland.jautomata.operations;

import net.jhoogland.jautomata.Automata;
import net.jhoogland.jautomata.Automaton;
import net.jhoogland.jautomata.semirings.BestPathWeights;
import net.jhoogland.jautomata.semirings.KTropicalSemiring;
import net.jhoogland.jautomata.semirings.PathWeight;

/**
 * 
 * This operation creates an automaton associated with a k-tropical semiring based on an automaton associated with a
 * real semiring. 
 * This semiring is used by n shortest paths algorithms, such as {@link Automata#bestPaths(Automaton, int)} and
 * {@link Automata#bestStrings(Automaton, int)}.
 * The states, transitions, and labels are not modified. 
 * The {@link #semiring()} method returns an instance of {@link KTropicalSemiring}.
 * 
 * @author Jasper Hoogland
 *
 * @param <L>
 * label type
 * 
 */

public class RealToKTropical<L>  extends SemiringConversion<L, Double, BestPathWeights> 
{
	public RealToKTropical(Automaton<L, Double> operand, int k)
	{
		super(operand, new KTropicalSemiring(k));
	}
	
	public BestPathWeights transitionWeight(Object transition) 
	{
		BestPathWeights weight = convertWeight(operand.transitionWeight(transition));
		weight.pathWeights[0].transition = transition;
		return weight;
	}
	
	public BestPathWeights convertWeight(Double weight)
	{
		PathWeight[] p = new PathWeight[((KTropicalSemiring) semiring()).k];
		p[0] = new PathWeight(null, - Math.log(weight), null);
		for (int i = 1; i < p.length; i++) p[i] = new PathWeight(null, Double.POSITIVE_INFINITY, null);
		return new BestPathWeights(p);
	}
}
