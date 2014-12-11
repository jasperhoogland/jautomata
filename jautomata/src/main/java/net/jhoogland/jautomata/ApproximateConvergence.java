package net.jhoogland.jautomata;

/**
 * 
 * Weight convergence condition for <code>Double</code> values that yields true if and only if 
 * the previous and current weights are approximately the same, i.e. the difference is within a specified
 * bound.
 * 
 * @author Jasper Hoogland
 */

public class ApproximateConvergence implements  WeightConvergenceCondition<Double>
{
	double bound;
	
	/**
	 * Constructs an {@link ApproximateConvergence} instance with the specified bound.
	 */
	
	public ApproximateConvergence(double bound) 
	{
		this.bound = bound;
	}

	/**
	 * Constructs an {@link ApproximateConvergence} instance with default bound <code>0.00001</code>.
	 */
	
	public ApproximateConvergence() 
	{
		this(0.00001);
	}

	public boolean converged(Double w1, Double w2) 
	{
		return Math.abs(w1 - w2) <= bound;
	}
}
