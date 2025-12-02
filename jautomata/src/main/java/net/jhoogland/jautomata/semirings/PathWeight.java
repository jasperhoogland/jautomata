package net.jhoogland.jautomata.semirings;

import java.util.*;

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
		if (obj == this)
		{
			return true;
		}
		if (!(obj instanceof PathWeight))
		{
			return false;
		}
		PathWeight<?> other = (PathWeight<?>) obj;
		if (!this.weight.equals(other.weight))
		{
			return false;
		}
		PathWeight<?> thisPrevious = this.previous;
		PathWeight<?> otherPrevious = other.previous;
		while (true)
		{
			if (thisPrevious == otherPrevious)
			{
				return true;
			}
			if (thisPrevious == null || otherPrevious == null)
			{
				return false;
			}
			if (!thisPrevious.weight.equals(otherPrevious.weight))
			{
				return false;
			}
			thisPrevious = thisPrevious.previous;
			otherPrevious = otherPrevious.previous;
		}
	}

	@Override
	public int hashCode()
	{
		int hash = weight.hashCode();
		PathWeight<?> checkPrevious = previous;
		while (checkPrevious != null)
		{
			hash = 31 * hash + previous.weight.hashCode();
			checkPrevious = checkPrevious.previous;
		}
		return hash;
	}

	@Override
	public String toString()
	{
		return getClass().getSimpleName() + "[transition=" + transition + ", weight=" + weight + ", previous=" + previous + "]";
	}
}
