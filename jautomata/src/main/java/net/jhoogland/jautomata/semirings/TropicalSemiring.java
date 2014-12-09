package net.jhoogland.jautomata.semirings;

/**
 * 
 * Implementation of the tropical semiring.
 * This is a commutative semifield over the set of nonnegative real numbers and infinity
 * It is idempotent and k-closed for any k > 0.
 * Its multiplication operation is + with identity 0,
 * and its addition operation is min(x1, x2) with identity infinity.
 * The multiplicative inverse is -x.
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
