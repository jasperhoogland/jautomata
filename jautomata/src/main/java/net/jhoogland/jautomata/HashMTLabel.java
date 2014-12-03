package net.jhoogland.jautomata;

import java.util.Collection;
import java.util.Map;

public class HashMTLabel<I, L> implements MTLabel<I, L> 
{
	Map<I, L> map;
	
	public HashMTLabel(Map<I, L> map) 
	{
		this.map = map;
	}
	
	public L tapeLabel(I tape) 
	{		
		return map == null ? null : map.get(tape);
	}
	
	public Collection<I> tapes() 
	{		
		return map.keySet();
	}
}
