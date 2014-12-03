package net.jhoogland.jautomata;

import java.util.ArrayList;
import java.util.Collection;

public class ArrayMTLabel<L> implements MTLabel<Integer, L> 
{
	L[] tLabels;
	
	public ArrayMTLabel(L... tLabels) 
	{
		this.tLabels = tLabels;
	}

	public L tapeLabel(Integer tape) 
	{		
		return tLabels == null ? null : tLabels[tape];
	}
	
	public Collection<Integer> tapes() 
	{
		Collection<Integer> tapes = new ArrayList<Integer>();
		for (int i = 0; i < tapes.size(); i++) tapes.add(i);
		return tapes;
	}
}
