package net.jhoogland.jautomata;

public class AATransition<L, K>
{
	private int previousState;
	private int nextState;
	private L label;
	private K weight;
	
	public AATransition(int previousState, L label, K weight, int nextState) 
	{
		this.previousState = previousState;
		this.label = label;
		this.weight = weight;
		this.nextState = nextState;
	}
	
	public int previousState() 
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
	
	public int nextState() 
	{
		return nextState;
	}
}
