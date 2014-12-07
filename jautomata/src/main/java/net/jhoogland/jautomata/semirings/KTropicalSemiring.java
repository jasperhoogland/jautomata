package net.jhoogland.jautomata.semirings;

import java.util.Arrays;

/**
 * 
 * This semiring is used by the shortest distance algorithm to determine the n shortest distances to a state. 
 * If the storePath field has value true, then the paths are stored that led to the n shortest distances. 
 * 
 * @author Jasper Hoogland
 *
 */

public class KTropicalSemiring implements Semiring<BestPathWeights> 
{
	public int k;
	public boolean storePath;
	
	public KTropicalSemiring(int k)
	{
		this(k, true);
	}
	
	public KTropicalSemiring(int k, boolean storePath) 
	{
		this.k = k;
		this.storePath = storePath;
	}

	public BestPathWeights multiply(BestPathWeights x1, BestPathWeights x2) 
	{
		PathWeight[] x = new PathWeight[k * k];
		int p = 0;
		for (int i = 0; i < k; i++) for (int j = 0; j < k; j++, p++)
		{
			x[p] = new PathWeight(storePath ? x1.pathWeights[i] : null, x1.pathWeights[i].weight + x2.pathWeights[j].weight, storePath ? x2.pathWeights[j].transition : null);
		}
		Arrays.sort(x);
		PathWeight[] y = new PathWeight[k];
		System.arraycopy(x, 0, y, 0, k);
		return new BestPathWeights(y);		
	}

	public BestPathWeights add(BestPathWeights x1, BestPathWeights x2) 
	{
		PathWeight[] x = new PathWeight[2 * k];
		System.arraycopy(x1.pathWeights, 0, x, 0, k);
		System.arraycopy(x2.pathWeights, 0, x, k, k);
		Arrays.sort(x);
		PathWeight[] y = new PathWeight[k];
		System.arraycopy(x, 0, y, 0, k);
		return new BestPathWeights(y);
	}

	public BestPathWeights one() 
	{
		PathWeight[] mi = new PathWeight[k];
		mi[0] = new PathWeight(null, 0.0);
		for (int i = 1; i < k; i++) mi[i] = new PathWeight(null, Double.POSITIVE_INFINITY);
		return new BestPathWeights(mi);
	}

	public BestPathWeights zero() 
	{
		PathWeight[] mi = new PathWeight[k];
		for (int i = 0; i < k; i++) mi[i] = new PathWeight(null, Double.POSITIVE_INFINITY);
		return new BestPathWeights(mi);
	}

	public boolean isIdempotent() 
	{		
		return k < 2;
	}

	public boolean isCommutative() 
	{		
		return ! storePath;
	}

	public boolean isKClosed(int k)
	{		
		return k >= this.k - 1;
	}
	
}
