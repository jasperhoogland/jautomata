package net.jhoogland.jautomata.semirings;

/**
 * 
 * Implementation of the log semiring.
 * 
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
