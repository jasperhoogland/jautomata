package net.jhoogland.jautomata;

/**
 * 
 * Implementations of this interface define conditions that specify
 * whether the value of a weight has converged given two consecutive values.
 * Weight convergence conditions are used by shortest distance algorithms to determine whether they should terminate. 
 * 
 * @author Jasper Hoogland
 *
 * @param <K>
 * the type of the elements of the semiring
 * 
 */

public interface WeightConvergenceCondition<K> 
{
	/**
	 * 
	 * Checks whether the value of a weight has converged given two consecutive values. 
	 * 
	 * @param w1
	 * the old weight
	 * 
	 * @param w2
	 * the new weight
	 * 
	 * @return
	 * true if the computed weights have converged, false otherwise
	 * 
	 */
	public boolean converged(K w1, K w2);
}
