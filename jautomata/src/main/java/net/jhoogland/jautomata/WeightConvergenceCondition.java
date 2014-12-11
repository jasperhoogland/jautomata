package net.jhoogland.jautomata;

/**
 * 
 * Implementations of this interface define weight convergence conditions. 
 * These are used by shortest distance algorithms to determine whether they should terminate.
 * A weight convergence condition specifies whether two consecutive estimations of a weight are close
 * enough to assume the estimations have converged.
 * If so, the <code>converged</code> method yields true for these values.
 * 
 * @author Jasper Hoogland
 *
 * @param <K>
 * weight type
 * 
 */

public interface WeightConvergenceCondition<K> 
{
	/**
	 * @return
	 * true if the computed weights have converged, false otherwise
	 */
	public boolean converged(K w1, K w2);
}
