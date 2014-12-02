package net.jhoogland.jautomata;

import java.util.Iterator;
import java.util.List;

/**
 * 
 * Instances of this class contain a path, together with its label and weight. 
 * The path is stored as a list of transitions.
 * 
 * @author Jasper Hoogland
 *
 * @param <L>
 * label type
 * 
 * @param <K>
 * The type of elements of the semiring over which the automaton is defined 
 * (Boolean for regular automata and Double for weighted automata)
 * 
 * @see
 * Automata#bestPaths(Automaton, int)
 * 
 */

public class Path<L, K> implements Iterable<Object>
{
	public List<Object> transitions;
	public K weight;	
	public List<L> label;
	
	public Path(List<Object> transitions, K weight, Automaton<L, K> automaton) 
	{
		this.transitions = transitions;
		this.weight = weight;
		label = Automata.pathLabel(transitions, automaton);
	}

	public Iterator<Object> iterator() 
	{
		return transitions.iterator();
	}
}
