package net.jhoogland.jautomata;

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
}
