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
 * label type
 * 
 * @param <K>
 * weight type
 * (Boolean for regular automata and Double for weighted automata)
 */
public class BasicTransition<L, K>
{
	private Object from;
	private Object to;
	private L label;
	private K weight;
	
	public BasicTransition(Object previousState, L label, K weight, Object nextState) 
	{
		this.from = previousState;
		this.label = label;
		this.weight = weight;
		this.to = nextState;
	}
	
	public Object from() 
	{
		return from;
	}
	
	public L label() 
	{
		return label;
	}
	
	public K weight() 
	{
		return weight;
	}
	
	public Object to() 
	{
		return to;
	}
}
