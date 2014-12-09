package net.jhoogland.jautomata.semirings;

/**
 * 
 * Implementation of the semiring of non-negative real numbers
 * (represented by <code>double</code> values in this implementation).
 * This is a commutative semifield. 
 * It is not idempotent and it is not k-closed for any k.
 * Its multiplication operation is <code>*</code> with identity <code>1</code>,
 * and its addition operation is <code>+</code> with identity <code>0</code>,
 * The multiplicative inverse is <code>1 / x</code>.
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
