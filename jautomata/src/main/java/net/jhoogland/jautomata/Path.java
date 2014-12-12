package net.jhoogland.jautomata;

import java.util.Iterator;
import java.util.List;

/**
 * 
 * Instances of this class contain a path, its label and its weight. 
 * The path is stored as a list of transitions,
 * and the path label is stored as a list of transition labels.
 * 
 * @author Jasper Hoogland
 *
 * @param <L>
 * label type
 * 
 * @param <K>
 * weight type 
 * (Boolean for regular automata and Double for weighted automata)
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
