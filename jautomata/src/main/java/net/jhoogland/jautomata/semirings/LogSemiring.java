package net.jhoogland.jautomata.semirings;

/**
 * 
 * Implementation of the log semiring, 
 * which is a semiring of the set of non-negative real numbers
 * (represented by <code>double</code> values in this implementation).
 * The log semiring is a commutative semifield.
 * It is not idempotent and it is not k-closed for any k.
 * Its multiplication operation is <code>+</code> with identity <code>0</code>,
 * and its addition operation is <code>-log(exp(-x1)+exp(-x2))</code>
 * with identity <code>infinity</code>.
 * The multiplicative inverse is <code>-x</code>.
 * 
 * @author Jasper Hoogland
 *
 */
public class LogSemiring implements Semifield<Double> 
{
	public Double multiply(Double x1, Double x2) 
	{		
		return x1 + x2;
	}

	public Double add(Double x1, Double x2) 
	{		
		return -Math.log(Math.exp(-x1) + Math.exp(-x2));
	}

	public Double one() 
	{		
		return 0.0;
	}

	public Double zero() 
	{		
		return Double.POSITIVE_INFINITY;
	}

	public boolean isIdempotent() 
	{		
		return false;
	}

	public boolean isCommutative() 
	{		
		return true;
	}

	public boolean isKClosed(int k) 
	{		
		return false;
	}

	public Double inverse(Double x) 
	{		
		return -x;
	}
}
