package net.jhoogland.jautomata.semirings;

/**
 * 
 * An implementation of the real semiring. 
 * The real semiring is defined over the set of real numbers, which are represented by 
 * <code>double</code> values in this implementation. 
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

	/**
	 * @return
	 * <code>false</code>
	 */
	public boolean isIdempotent() 
	{
		return false;
	}
	
	/**
	 * 
	 * @return
	 * <code>true</code>
	 */
	
	public boolean isCommutative() 
	{
		return true;
	}

	/**
	 * 
	 * @return
	 * <code>false</code>
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
