package net.jhoogland.jautomata.semirings;

import java.util.Arrays;

/**
 * 
 * This semiring is used by the shortest distance algorithm to determine the n shortest distances to a state. 
 * If the storePath field has value <code>true</code>, then the paths are stored that led to the n shortest distances.\
 *   
 * 
 * @author Jasper Hoogland
 *
 */

public class KTropicalSemiring<K extends Comparable<K>> implements Semiring<BestPathWeights<K>> 
{
	public int k;
	public boolean storePath;
	public Semiring<K> src;
	
	public KTropicalSemiring(int k, Semiring<K> src)
	{
		this(k, true, src);
	}
	
	public KTropicalSemiring(int k, boolean storePath, Semiring<K> src) 
	{
		this.k = k;
		this.storePath = storePath;
		this.src = src;
	}

	public BestPathWeights<K> multiply(BestPathWeights<K> x1, BestPathWeights<K> x2) 
	{
		PathWeight<K>[] x = new PathWeight[k * k];
		int p = 0;
		for (int i = 0; i < k; i++) for (int j = 0; j < k; j++, p++)
		{
			x[p] = new PathWeight<K>(storePath ? x1.pathWeights[i] : null, src.multiply(x1.pathWeights[i].weight, x2.pathWeights[j].weight), src, storePath ? x2.pathWeights[j].transition : null);
		}
		Arrays.sort(x);
		PathWeight<K>[] y = new PathWeight[k];
		System.arraycopy(x, 0, y, 0, k);
		return new BestPathWeights<K>(y);		
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
//		mi[0] = new PathWeight(null, 0.0);
		mi[0] = new PathWeight(null, src.one(), src);
		for (int i = 1; i < k; i++) mi[i] = new PathWeight(null, src.zero(), src);
		return new BestPathWeights(mi);
	}

	public BestPathWeights zero() 
	{
		PathWeight[] mi = new PathWeight[k];
		for (int i = 0; i < k; i++) mi[i] = new PathWeight(null, src.zero(), src);
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
