package net.jhoogland.jautomata;

import java.util.ArrayList;
import java.util.Collection;


/**
 *
 * Implementation of a multi-tape label type.
 * Tapes are indicated by indices (starting at zero) and tape labels are stored in an array.
 * 
 * @author Jasper Hoogland
 *
 * @param <L>
 * label type
 * 
 * @see
 * MTAutomaton
 */

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
