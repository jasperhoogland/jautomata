package net.jhoogland.jautomata;

/**
 * 
 * Implements an input/output label pair for transducers.
 * 
 *  
 * @author Jasper Hoogland
 *
 */

public class TLabel<I, O> 
{
	public I iLabel;
	public O oLabel;
	
	/**
	 * 
	 * Creates a transducer input/output label pair with the specified input and output labels.
	 * 
	 * @param iLabel
	 * @param oLabel
	 */
	
	public TLabel(I iLabel, O oLabel) 
	{
		this.iLabel = iLabel;
		this.oLabel = oLabel;
	}
	
	/**
	 * 
	 * @return
	 * the input label
	 * 
	 */
	
	public I in() 
	{
		return iLabel;
	}
	
	/**
	 * 
	 * @return
	 * the output label
	 */
	
	public O out() 
	{
		return oLabel;
	}
	
	@Override
	public boolean equals(Object obj) 
	{		
		if (obj instanceof TLabel)
		{
			TLabel<I, O> other = (TLabel<I, O>) obj;
			return equals(this.iLabel, other.iLabel) && equals(this.oLabel, other.oLabel);
		}
		else return false;
	}
	
	private boolean equals(Object l1, Object l2)
	{
		return l1 == null ? l2 == null : l1.equals(l2);
	}
	
	@Override
	public int hashCode() 
	{		
		return hashCode(iLabel) + hashCode(oLabel);
	}

	private int hashCode(Object l) 
	{		
		return l == null ? 0 : l.hashCode();
	}
}
