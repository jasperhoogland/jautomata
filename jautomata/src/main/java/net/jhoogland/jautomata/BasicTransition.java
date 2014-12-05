package net.jhoogland.jautomata;

public class BasicTransition<L, K>
{
	private Object previousState;
	private Object nextState;
	private L label;
	private K weight;
	
	public BasicTransition(Object previousState, L label, K weight, Object nextState) 
	{
		this.previousState = previousState;
		this.label = label;
		this.weight = weight;
		this.nextState = nextState;
	}
	
	public Object previousState() 
	{
		return previousState;
	}
	
	public L label() 
	{
		return label;
	}
	
	public K weight() 
	{
		return weight;
	}
	
	public Object nextState() 
	{
		return nextState;
	}
}
