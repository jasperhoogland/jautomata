package net.jhoogland.jautomata.semirings;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * 
 * This semiring is used by the shortest distance algorithm to determine the n shortest distances to a state. 
 * If the storePath field has value <code>true</code>, then the paths are stored that led to the n shortest distances.\
 *   
 * 
 * @author Jasper Hoogland
 *
 */

public class KTropicalSemiring<K extends Comparable<K>> implements Semiring<List<PathWeight<K>>> 
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

	public List<PathWeight<K>> multiply(List<PathWeight<K>> x1, List<PathWeight<K>> x2) 
	{
		List<PathWeight<K>> sorted = new ArrayList<PathWeight<K>>();
		for (PathWeight<K> pw1 : x1) for (PathWeight<K> pw2 : x2)
		{
			if (storePath)
			{
				sorted.add(new PathWeight<K>(pw1, src.multiply(pw1.weight, pw2.weight), src, pw2.transition));
			}
			else
			{
				sorted.add(new PathWeight<K>(null, src.multiply(pw1.weight, pw2.weight), src, null));
			}
		}
		Collections.sort(sorted);
		List<PathWeight<K>> sum = new ArrayList<PathWeight<K>>();
		Iterator<PathWeight<K>> it = sorted.iterator();
		while (sum.size() < k && it.hasNext())
			sum.add(it.next());
		return sum;	
	}

	public List<PathWeight<K>> add(List<PathWeight<K>> x1, List<PathWeight<K>> x2) 
	{
		List<PathWeight<K>> sorted = new ArrayList<PathWeight<K>>(x1);
		sorted.addAll(x2);
		Collections.sort(sorted);
		List<PathWeight<K>> sum = new ArrayList<PathWeight<K>>();
		Iterator<PathWeight<K>> it = sorted.iterator();
		while (sum.size() < k && it.hasNext())
			sum.add(it.next());
		return sum;
	}

	public List<PathWeight<K>> one() 
	{
		PathWeight<K> pw = new PathWeight<K>(null, src.one(), src);		
		return Arrays.asList(pw);
	}

	public List<PathWeight<K>> zero() 
	{
		return Collections.emptyList();
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
