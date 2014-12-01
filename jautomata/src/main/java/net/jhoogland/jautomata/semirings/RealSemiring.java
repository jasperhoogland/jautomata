package net.jhoogland.jautomata.semirings;

/**
 * 
 * An implementation of the real semiring. 
 * The real semiring is defined over the set of real numbers, which are represented by 
 * <tt>double</tt> values in this implementation. 
 * Summation is the addition operation and product is the mulitiplication 
 * operation. 
 * 0 is the addition identity and 1 is the multiplication identity. 
 * The real semiring is commutative (x * y = y * x). 
 * It is not idempotent (x + x != x)
 * and it is not k-closed for any k.
 * 
 * @author Jasper Hoogland
 * 
 */

public class RealSemiring implements Semifield<Double>
{
	public Double add(Double x1, Double x2) 
	{		
		return x1 + x2;
	}

	public Double zero() 
	{
		return 0.0;
	}

	public Double one() 
	{
		return 1.0;
	}

	public Double multiply(Double x1, Double x2) 
	{
		return x1 * x2;
	}
	
	public Double inverse(Double x) 
	{		
		return 1 / x;
	}
	
	public boolean equals(Object arg0) 
	{
		return arg0 instanceof RealSemiring;
	}

	public boolean isIdempotent() 
	{
		return false;
	}
	
	/**
	 * 
	 * @return
	 * <tt>true</tt>
	 */
	
	public boolean isCommutative() 
	{
		return true;
	}

	/**
	 * 
	 * The real semiring is not k-closed for any k.
	 */
	
	public boolean isKClosed(int k) 
	{
		return false;
	}

	public Double multiplicativeInverse(Double x) 
	{
		return 1.0 / x;
	}
}
