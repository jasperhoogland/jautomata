package net.jhoogland.jautomata.semirings;

/**
 * Implementation of the Boolean semiring, 
 * which is a semiring of the Boolean values <code>true</code> and <code>false</code>.
 * Shortest distance algorithms use this semiring to compute whether unweighted automata have complete paths.
 * The Boolean semiring is a commutative semifield.
 * It is idempotent and it k-closed for any k > 0.
 * Its multiplication operation is <code>and</code> with identity <code>true</code>,
 * and its addition operation is <code>or</code> 
 * with identity <code>false</code>.
 * The multiplicative inverse is <code>true</code>.
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
