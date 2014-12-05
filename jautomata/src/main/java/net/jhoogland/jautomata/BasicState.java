package net.jhoogland.jautomata;

import java.util.Collection;

public class BasicState<K> 
{
	private Collection<Object> transitionsOut;
	private Collection<Object> transitionsIn;
	private K initialWeight;
	private K finalWeight;
	
	public BasicState(K initialWeight, K finalWeight, Collection<Object> transitionsOut, Collection<Object> transitionsIn) 
	{
		this.initialWeight = initialWeight;
		this.finalWeight = finalWeight;
		this.transitionsOut = transitionsOut;
		this.transitionsIn = transitionsIn;
	}
	
	public K initialWeight() 
	{
		return initialWeight;
	}
	
	public K finalWeight() 
	{
		return finalWeight;
	}
	
	public Collection<Object> transitionsOut() 
	{
		return transitionsOut;
	}
	
	public Collection<Object> transitionsIn() 
	{
		return transitionsIn;
	}
}
