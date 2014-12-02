package net.jhoogland.jautomata;

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
}
