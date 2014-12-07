package net.jhoogland.jautomata;

/**
 * Instances of this class contain information of a transition, such as previous and next state,
 * label, and weight.
 * This class is used by automaton types that explicitly store information of transitions, such as
 * {@link ArrayAutomaton} and {@link HashAutomaton}. 
 * It can also be used by custom automaton types.
 * @author Jasper Hoogland
 *
 * @param <L>
 * @param <K>
 */
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
