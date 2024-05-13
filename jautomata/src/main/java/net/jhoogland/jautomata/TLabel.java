package net.jhoogland.jautomata;

import java.util.Objects;

/**
 * Implements an input/output label pair for transducers.
 *  
 * @author Jasper Hoogland
 *
 * @param <I>
 * input label type
 * 
 * @param <O>
 * output label type
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
		if (obj == this)
		{
			return true;
		}
		if (!(obj instanceof TLabel))
		{
			return false;
		}
		TLabel<?, ?> other = (TLabel<?, ?>) obj;
		return Objects.equals(this.iLabel, other.iLabel) && Objects.equals(this.oLabel, other.oLabel);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(iLabel, oLabel);
	}
}
