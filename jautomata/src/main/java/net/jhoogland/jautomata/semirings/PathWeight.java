package net.jhoogland.jautomata.semirings;

import java.util.LinkedList;
import java.util.List;

import net.jhoogland.jautomata.Automaton;
import net.jhoogland.jautomata.Path;

/**
 * 
 * A chain of instances of this class specify a path over an automaton and its weight.
 * 
 * @author Jasper Hoogland
 *
 */

public class PathWeight implements Comparable<PathWeight> 
{
	public PathWeight previous;
	public Object transition;
	public double weight;
	
	public PathWeight(PathWeight previous, double weight) 
	{
		this.previous = previous;
		this.weight = weight;
	}
	
	public PathWeight(PathWeight previous, double weight, Object transition) 
	{
		this(previous, weight);
		this.transition = transition; 
	}
	
	public <L> Path<L, Double> path(Automaton<L, Double> automaton) 
	{
		LinkedList<Object> transitions = new LinkedList<Object>();
		PathWeight cur = this;
		while (cur != null)
		{
			Object t = cur.transition;
			if (t != null) transitions.addFirst(t);
			cur = cur.previous;
		}
		return new Path<L, Double>(transitions, weight, automaton);
	}

	public int compareTo(PathWeight o) 
	{		
		PathWeight other = (PathWeight) o;
		return (int) Math.signum(this.weight - other.weight);
	}
	
	@Override
	public boolean equals(Object obj) 
	{
		PathWeight other = (PathWeight) obj;
		return this.previous == other.previous && Math.abs(this.weight - other.weight) < 0.001;
	}
	
	boolean equals(Object o1, Object o2)
	{
		if (o1 == null) return o2 == null;
		else if (o2 == null) return false;
		else return o1.equals(o2);
	}
	
	@Override
	public String toString() 
	{		
		return getClass().getSimpleName() + "(" + transition + ", " + weight + ")";
	}
}
