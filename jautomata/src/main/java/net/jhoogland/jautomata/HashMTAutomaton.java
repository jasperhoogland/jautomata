package net.jhoogland.jautomata;

import java.util.Collection;

public class HashMTAutomaton<T, L, K> extends HashAutomaton<MTLabel<T, L>, K> implements MTAutomaton<T, L, K> 
{
	Collection<T> tapes;

	public HashMTAutomaton(MTAutomaton<T, L, K> src) 
	{
		super(src);
		this.tapes = src.tapes();
	}

	public Collection<T> tapes() 
	{		
		return tapes;
	}
}
