package net.jhoogland.jautomata.semirings;

/**
 * 
 * An implementation of the tropical semiring. 
 * The tropical semiring is defined over the set of real numbers (and positive 
 * infinity), which are represented by <tt>double</tt> values in this implementation. 
 * <tt>min</tt> is the addition operation 
 * and summation is the multiplication operation. 
 * Positive infinity is the addition identity and 0 is the multiplication identity. 
 * The tropical semiring is commutative (x + y = y + x), idempotent (min(x, x) = x),
 * and k-closed for all k.
 * 
 * @author Jasper Hoogland
 *
 */

public class TropicalSemiring implements Semifield<Double>
{	
	public Double add(Double x1, Double x2) 
	{
		return Math.min(x1, x2);
	}

	public Double zero() 
	{
		return Double.POSITIVE_INFINITY;
	}

	public Double one() 
	{
		return 0.0;
	}

	public Double multiply(Double x1, Double x2) 
	{
		return x1 + x2;
	}
	
	public Double inverse(Double x) 
	{		
		return -x;
	}
	
	@Override
	public boolean equals(Object obj) 
	{
		return obj instanceof TropicalSemiring;
	}

	public boolean isIdempotent() 
	{
		return true;
	}

	public boolean isCommutative() 
	{
		return true;
	}

	public boolean isKClosed(int k) 
	{
		return true;
	}
}
