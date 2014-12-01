package net.jhoogland.jautomata.semirings;

/**
 * The boolean semiring. This semiring is used for unweighted automata.
 * 
 * @author Jasper Hoogland
 *
 */

public class BooleanSemiring implements Semifield<Boolean> 
{
	// Definition
	
	public Boolean multiply(Boolean x1, Boolean x2) 
	{		
		return x1 && x2;
	}

	public Boolean add(Boolean x1, Boolean x2) 
	{		
		return x1 || x2;
	}

	public Boolean one() 
	{		
		return true;
	}

	public Boolean zero() 
	{		
		return false;
	}
	
	public Boolean inverse(Boolean x) 
	{		
		return true;
	}
	
	// Properties

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
