package net.jhoogland.jautomata;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Implementation of a multi-tape label type.
 * Tape labels are stored in a {@link HashMap}.
 * 
 * @author Jasper Hoogland
 *
 * @param <T>
 * tape type
 * 
 * @param <L>
 * tape label type
 */
public class HashMTLabel<T, L> implements MTLabel<T, L> 
{
	Map<T, L> map;
	
	public HashMTLabel(Map<T, L> map) 
	{
		this.map = map;
	}
	
	public L tapeLabel(T tape) 
	{		
		return map == null ? null : map.get(tape);
	}
	
	public Collection<T> tapes() 
	{		
		return map.keySet();
	}
}
