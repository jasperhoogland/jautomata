package net.jhoogland.jautomata.semirings;

import java.util.LinkedList;
import java.util.List;

import net.jhoogland.jautomata.Automaton;
import net.jhoogland.jautomata.Path;

/**
 * A chain of instances of this class specify a path over an automaton and its weight.
 * 
 * @author Jasper Hoogland
 *
 * @param <K>
 * weight type
 */
public class PathWeight<K extends Comparable<K>> implements Comparable<PathWeight<K>> 
{
	public PathWeight<K> previous;
	public Object transition;
	public K weight;
	public Semiring<K> src;
	
	public PathWeight(PathWeight<K> previous, K weight, Semiring<K> src) 
	{
		this.previous = previous;
		this.weight = weight;
		this.src = src;
	}
	
	public PathWeight(PathWeight<K> previous, K weight, Semiring<K> src, Object transition) 
	{
		this(previous, weight, src);
		this.transition = transition; 
	}
	
	public <L> Path<L, K> path(Automaton<L, K> automaton) 
	{
		LinkedList<Object> transitions = new LinkedList<Object>();
		PathWeight<K> cur = this;
		while (cur != null)
		{
			Object t = cur.transition;
			if (t != null) transitions.addFirst(t);
			cur = cur.previous;
		}
		return new Path<L, K>(transitions, weight, automaton);
	}

	public int compareTo(PathWeight<K> o) 
	{		
		PathWeight<K> other = (PathWeight<K>) o;
		return (src.zero().compareTo(src.one())) * this.weight.compareTo(other.weight);
//		return (int) Math.signum(this.weight - other.weight);
	}
	
	@Override
	public boolean equals(Object obj) 
	{
//		PathWeight other = (PathWeight) obj;
//		return this.previous == other.previous && Math.abs(this.weight - other.weight) < 0.001;
		
//		throw new RuntimeException();
		
		PathWeight<K> other = (PathWeight<K>) obj;
		return this.previous == other.previous && this.weight.equals(other.weight);
		
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
