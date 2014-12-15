package net.jhoogland.jautomata;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.Map.Entry;

import net.jhoogland.jautomata.operations.AcceptorIntersection;
import net.jhoogland.jautomata.operations.Operations;
import net.jhoogland.jautomata.operations.SingleInitialStateOperation;
import net.jhoogland.jautomata.queues.DefaultQueueFactory;
import net.jhoogland.jautomata.queues.KTropicalQueueFactory;
import net.jhoogland.jautomata.semirings.PathWeight;
import net.jhoogland.jautomata.semirings.RealSemiring;
import net.jhoogland.jautomata.semirings.Semiring;

/**
 *
 * This class contains static methods for computing properties of automata,
 * creating various types of automata, and performing weight computations on automata.
 * 
 * @author Jasper Hoogland
 *
 */

public class Automata 
{
	/**
	 * 
	 * @return
	 * a {@link Collection} containing all states of the specified automaton.
	 * 
	 */

	public static <L, K> Collection<Object> states(Automaton<L, K> automaton)
	{
		ArrayList<Object> states = new ArrayList<Object>();
		Set<Object> processed = new HashSet<Object>();
		Comparator<Object> order = automaton.topologicalOrder();
		Queue<Object> front = order == null ? new LinkedList<Object>() : new PriorityQueue<Object>(11, order);
		front.addAll(automaton.initialStates());
		processed.addAll(front);
		
		while (! front.isEmpty())
		{
			Object state = front.poll();
			states.add(state);
			for (Object transition : automaton.transitionsOut(state))
			{
				Object next = automaton.nextState(transition);
				if (! processed.contains(next))
				{
					front.add(next);
					processed.add(next);
				}
			}
		}
		return states;
	}
	
	/**
	 * 
	 * @return
	 * a {@link Collection} containing all transitions of the specified automaton.
	 * 
	 */
	
	public static <L, K> Collection<Object> transitions(Automaton<L, K> automaton)
	{
		ArrayList<Object> transitions = new ArrayList<Object>();
		Set<Object> processed = new HashSet<Object>();
		Comparator<Object> order = automaton.topologicalOrder();
		Queue<Object> front = order == null ? new LinkedList<Object>() : new PriorityQueue<Object>(11, order);
		front.addAll(automaton.initialStates());
		processed.addAll(front);
		
		while (! front.isEmpty())
		{
			Object state = front.poll();
			for (Object transition : automaton.transitionsOut(state))
			{
				transitions.add(transition);
				Object next = automaton.nextState(transition);
				if (! processed.contains(next))
				{
					front.add(next);
					processed.add(next);
				}
			}
		}
		return transitions;
	}
	
	/**
	 * 
	 * @return
	 * true if and only if the specified state is an initial state of the specified automaton
	 * 
	 */
	
	public static <L, K> boolean isInitialState(Automaton<L, K> automaton, Object state)
	{
		return ! automaton.semiring().zero().equals(automaton.initialWeight(state));
	}
	
	/**
	 * 
	 * @return
	 * true if and only if the specified state is an final state of the specified automaton
	 * 
	 */
	
	public static <L, K> boolean isFinalState(Automaton<L, K> automaton, Object state)
	{
		return ! automaton.semiring().zero().equals(automaton.finalWeight(state));
	}
	
	/**
	 * 
	 * @return
	 * the label of the specified path as a {@link List} of labels
	 * 
	 */

	public static <L, K> List<L> pathLabel(List<Object> path, Automaton<L, K> automaton)
	{
		ArrayList<L> pathLabel = new ArrayList<L>();
		for (Object transition : path) 
		{
			L label = automaton.label(transition);
			if (label != null) pathLabel.add(label);				
		}				
		return pathLabel;
	}
	
	/**
	 * @return
	 * an automaton that only accepts the empty string
	 */
	
	public static <L, K> SingleStringAutomaton<L, K> emptyStringAutomaton(Semiring<K> semiring)
	{
		return createSingleStringAutomaton(semiring, new ArrayList<L>(0));
	}
	
	/**
	 * @return
	 * an automaton that only accepts the specified string 
	 */
	
	public static <L, K> SingleStringAutomaton<L, K> createSingleStringAutomaton(Semiring<K> semiring, List<L> list)
	{
		return new SingleStringAutomaton<L, K>(semiring, list);
	}
	
	/**
	 * @return
	 * an automaton that only accepts the specified string
	 */
	
	public static <K> SingleStringAutomaton<Character, K> createSingleStringAutomaton(Semiring<K> semiring, String str)
	{
		return createSingleStringAutomaton(semiring, toCharacterList(str));
	}
	
	/**
	 * @return
	 * the weight of the specified string
	 */
	
	public static <K> K stringWeight(Automaton<Character, K> automaton, String str)
	{
		return stringWeight(automaton, toCharacterList(str));
	}
	
	/**
	 * @return
	 * the weight of the specified string
	 */
	
	public static <L, K> K stringWeight(Automaton<L, K> automaton, List<L> string)
	{
		SingleSourceShortestDistances<K> sssd = new SingleSourceShortestDistances<K>(new DefaultQueueFactory<K>(), new ExactConvergence<K>());
		return stringWeight(automaton, sssd, string);
	}
	
	/**
	 * @return
	 * the weight of the specified string computed by the specified shortest distance algorithm.
	 */
	
	public static <S, T, L, K> K stringWeight(Automaton<L, K> automaton, SingleSourceShortestDistances<K> sssd, List<L> string)
	{
		SingleStringAutomaton<L, K> stringAutomaton = new SingleStringAutomaton<L, K>(automaton.semiring(), string);
		AcceptorIntersection<L, K> intersection = new AcceptorIntersection<L, K>(automaton, stringAutomaton);
		return shortestCompleteDistances(intersection, sssd);
	}
	
	/**
	 * @return
	 * a list with the specified number of shortest paths computed by the specified shortest distance algorithm. 
	 */
	
	public static <L, K extends Comparable<K>> List<Path<L, K>> shortestPaths(Automaton<L, K> automaton, int numPaths, SingleSourceShortestDistances<List<PathWeight<K>>> sssd)
	{
		Semiring<K> sr = automaton.semiring();
		if (sr.zero().equals(0.0))
			automaton = (Automaton<L, K>) Operations.realToTropicalSemiring((Automaton<L, Double>) automaton);
		Automaton<L, List<PathWeight<K>>> kT = Operations.toKTropicalSemiring(automaton, numPaths);
		List<PathWeight<K>> w = shortestCompleteDistances(kT, sssd);
		ArrayList<Path<L, K>> paths = new ArrayList<Path<L, K>>();
		for (PathWeight<K> pw : w) if (! pw.weight.equals(automaton.semiring().zero()))
		{
			Path<L, K> path = pw.path(automaton);
			if (sr.zero().equals(0.0))
			{	
				Double nw = Math.exp(- (Double) path.weight);
				path.weight = (K) nw;
			}
			paths.add(path);
		}
		return paths;
	}
	
	/**
	 * @return
	 * a list with the specified number of shortest paths computed by the specified shortest distance algorithm. 
	 */

	public static <L, K extends Comparable<K>> List<Path<L, K>> shortestPaths(Automaton<L, K> automaton, int numPaths)
	{
		SingleSourceShortestDistances<List<PathWeight<K>>> sssd = new SingleSourceShortestDistances<List<PathWeight<K>>>(new KTropicalQueueFactory<K>(), new ExactConvergence<List<PathWeight<K>>>());
		return shortestPaths(automaton, numPaths, sssd);
	}
	
	/**
	 * @return
	 * a list with the specified number of best strings computed by the specified shortest distance algorithm
	 */
	
	public static <L, K extends Comparable<K>> List<Path<L, K>> bestStrings(Automaton<L, K> automaton, int numPaths, SingleSourceShortestDistances<List<PathWeight<K>>> sssd)
	{
		Automaton<L, K> det = Operations.determinizeER(automaton);
		return shortestPaths(det, numPaths, sssd);
	}

	/**
	 * @return
	 * a list with the specified number of best strings
	 */
	
	public static <L, K extends Comparable<K>> List<Path<L, K>> bestStrings(Automaton<L, K> automaton, int numPaths)
	{
		Automaton<L, K> det = Operations.determinizeER(automaton);
		return shortestPaths(det, numPaths);
	}
	
	/**
	 * 
	 * Computes for each state <code>s</code> the shortest distance from the initial states to <code>s</code>.
	 * 
	 * @param automaton
	 * the automaton to perform the operation on 
	 * 
	 * @param sssd
	 * the shortest distance algorithm to use
	 * 
	 * @return
	 * a map that assigns to each state the shortest distance from the initial states to that state
	 */
	
	public static <L, K> Map<Object, K> shortestDistancesFromInitialStates(Automaton<L, K> automaton, SingleSourceShortestDistancesInterface<K> sssd)
	{
		SingleInitialStateOperation<L, K> sisAutomaton = new SingleInitialStateOperation<L, K>(automaton);		
		Map<Object, K> sisMap = sssd.computeShortestDistances(sisAutomaton, sisAutomaton.initialState());
		
		HashMap<Object, K> sdMap = new HashMap<Object, K>();
		for (Entry<Object, K> e : sisMap.entrySet()) 
		{	
			SingleInitialStateOperation<L, K>.SISState s = (SingleInitialStateOperation<L, K>.SISState) e.getKey();
			if (s.operandState != null) 
				sdMap.put(s.operandState, e.getValue());
		}
		
		return sdMap;
	}
	
	/**
	 * Computes for each state <code>s</code> the shortest distance from <code>s</code> to the final states.
	 * The automaton is required to be reversely accessible.
	 * 
	 * @param automaton
	 * the automaton to perform the operation on 
	 * 
	 * @param sssd
	 * the shortest distance algorithm to use
	 * 
	 * @return
	 * a map that assigns to each state the shortest distance from that state to the final states 
	 */
	
	public static <L, K> Map<Object, K> shortestDistancesToFinalStates(ReverselyAccessibleAutomaton<L, K> automaton, SingleSourceShortestDistancesInterface<K> sssd)
	{
		Automaton<L, K> rev = Operations.reverse((ReverselyAccessibleAutomaton<L, K>) automaton);
		SingleInitialStateOperation<L, K> sisAutomaton = new SingleInitialStateOperation<L, K>(rev);		
		Map<Object, K> sisMap = sssd.computeShortestDistances(sisAutomaton, sisAutomaton.initialState());
		
		HashMap<Object, K> sdMap = new HashMap<Object, K>();
		for (Entry<Object, K> e : sisMap.entrySet()) 
		{	
			SingleInitialStateOperation<L, K>.SISState s = (SingleInitialStateOperation<L, K>.SISState) e.getKey();
			if (s.operandState != null) 
				sdMap.put(s.operandState, e.getValue());
		}
		
		return sdMap;
	}
	
	
	/**
	 * Computes the shortest distance from the initial states to the final states.
	 * 
	 * @param automaton
	 * the automaton to perform the operation on 
	 * 
	 * @param sssd
	 * the shortest distance algorithm to use
	 * 
	 * @return
	 * the shortest distance from the initial states to the final states 
	 */
	
	public static <L, K> K shortestCompleteDistances(Automaton<L, K> automaton, SingleSourceShortestDistances<K> sssd)
	{
		Semiring<K> sr = automaton.semiring();
		Map<Object, K> shortestDistances = shortestDistancesFromInitialStates(automaton, sssd);
		K weight = sr.zero();
		for (Entry<Object, K> e : shortestDistances.entrySet()) if (isFinalState(automaton, e.getKey()))
		{
			weight = sr.add(weight, sr.multiply(e.getValue(), automaton.finalWeight(e.getKey())));
		}
		return weight;
	}
	
	/**
	 * @return
	 * the {@link List} of {@link Character}s of the specified {@link String}  
	 */
	
	public static List<Character> toCharacterList(String str)
	{
		List<Character> characterList = new ArrayList<Character>(str.length());
		for (int i = 0; i < str.length(); i++)
			characterList.add(str.charAt(i));
		return characterList;
	}
	
	/**
	 * @return
	 * the {@link String} of the specified {@link List} of {@link Character}s
	 */
	
	public static String toString(List<Character> characterList)
	{
		String str = "";
		for (Character c : characterList)
			if (c != null)
				str += c;
		return str;
	}
	
	
}
