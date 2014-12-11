package net.jhoogland.jautomata;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
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

import net.jhoogland.jautomata.io.CharacterFormat;
import net.jhoogland.jautomata.io.AcceptorIO;
import net.jhoogland.jautomata.io.TransducerIO;
import net.jhoogland.jautomata.operations.AcceptorIntersection;
import net.jhoogland.jautomata.operations.Operations;
import net.jhoogland.jautomata.operations.SingleInitialStateOperation;
import net.jhoogland.jautomata.operations.Union;
import net.jhoogland.jautomata.queues.DefaultQueueFactory;
import net.jhoogland.jautomata.queues.KTropicalQueueFactory;
import net.jhoogland.jautomata.semirings.BestPathWeights;
import net.jhoogland.jautomata.semirings.BooleanSemiring;
import net.jhoogland.jautomata.semirings.KTropicalSemiring;
import net.jhoogland.jautomata.semirings.RealSemiring;
import net.jhoogland.jautomata.semirings.Semiring;
import static net.jhoogland.jautomata.operations.Operations.concat;
import static net.jhoogland.jautomata.operations.Operations.union;

/**
 *
 * This class contains static methods for computing properties of automata 
 * and creating various types of automata.
 * 
 * @author Jasper Hoogland
 *
 */

public class Automata 
{
	public static void main(String[] args) throws IOException 
	{
		SinglePathAutomaton<Character, Double> a1 = createSinglePathAutomaton(new RealSemiring(), "a");
		SinglePathAutomaton<Character, Double> a2 = createSinglePathAutomaton(new RealSemiring(), "b");
		Automaton<Character, Double> complex = Operations.epsilonRemoval(Operations.singleInitialState(Operations.weightedClosure(Operations.weightedUnion(a1, a2), 0.6)));
		List<Path<Character, Double>> ps = bestStrings(complex, 5);
		for (Path<Character, Double> p : ps)
			System.out.println(p.weight + ": " + toString(p.label));
	}

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
	 * the label of the specified path
	 * 
	 */

	public static <L, K> List<L> pathLabel(Iterable<Object> path, Automaton<L, K> automaton)
	{
		ArrayList<L> pathLabel = new ArrayList<L>();
		for (Object transition : path) 
		{
			L label = automaton.label(transition);
			if (label != null) pathLabel.add(label);				
		}				
		return pathLabel;
	}
	
	public static <L, K> SinglePathAutomaton<L, K> emptyStringAutomaton(Semiring<K> semiring)
	{
		return createSinglePathAutomaton(semiring, new ArrayList<L>(0));
	}
		
	public static <L, K> SinglePathAutomaton<L, K> createSinglePathAutomaton(Semiring<K> semiring, List<L> list)
	{
		return new SinglePathAutomaton<L, K>(semiring, list);
	}
	
	public static <K> SinglePathAutomaton<Character, K> createSinglePathAutomaton(Semiring<K> semiring, String str)
	{
		return createSinglePathAutomaton(semiring, toCharacterList(str));
	}
	
	public static <K> K stringWeight(Automaton<Character, K> automaton, String str)
	{
		return stringWeight(automaton, toCharacterList(str));
	}
	
	public static <L, K> K stringWeight(Automaton<L, K> automaton, List<L> string)
	{
		SingleSourceShortestDistances<K> sssd = new SingleSourceShortestDistances<K>(new DefaultQueueFactory<K>(), new ExactConvergence<K>());
		return stringWeight(automaton, sssd, string);
	}
	
	public static <S, T, L, K> K stringWeight(Automaton<L, K> automaton, SingleSourceShortestDistances<K> sssd, List<L> string)
	{
		SinglePathAutomaton<L, K> stringAutomaton = new SinglePathAutomaton<L, K>(automaton.semiring(), string);
		AcceptorIntersection<L, K> intersection = new AcceptorIntersection<L, K>(automaton, stringAutomaton);
		return shortestCompleteDistances(intersection, sssd);
	}
	
	public static <L, K extends Comparable<K>> List<Path<L, K>> shortestPaths(Automaton<L, K> automaton, int numPaths, SingleSourceShortestDistances<BestPathWeights<K>> sssd)
	{
		Semiring<K> sr = automaton.semiring();
		if (sr.zero().equals(0.0))
			automaton = (Automaton<L, K>) Operations.realToTropicalSemiring((Automaton<L, Double>) automaton);
		
		
		Automaton<L, BestPathWeights<K>> kT = Operations.toKTropicalSemiring(automaton, numPaths);
		
		BestPathWeights<K> w = shortestCompleteDistances(kT, sssd);
		ArrayList<Path<L, K>> paths = new ArrayList<Path<L, K>>();
		for (int i = 0; i < w.pathWeights.length; i++) if (! w.pathWeights[i].weight.equals(automaton.semiring().zero()))
		{
			Path<L, K> path = w.pathWeights[i].path(automaton);
			if (sr.zero().equals(0.0))
			{	
				Double nw = Math.exp(- (Double) path.weight);
				path.weight = (K) nw;
			}
			paths.add(path);
		}
		return paths;
	}
	
	public static <L, K extends Comparable<K>> List<Path<L, K>> shortestPaths(Automaton<L, K> automaton, int numPaths)
	{
		SingleSourceShortestDistances<BestPathWeights<K>> sssd = new SingleSourceShortestDistances<BestPathWeights<K>>(new KTropicalQueueFactory<K>(), new ExactConvergence<BestPathWeights<K>>());
		return shortestPaths(automaton, numPaths, sssd);
	}
	
	public static <L, K extends Comparable<K>> List<Path<L, K>> bestStrings(Automaton<L, K> automaton, int numPaths, SingleSourceShortestDistances<BestPathWeights<K>> sssd)
	{
		Automaton<L, K> det = Operations.determinizeER(automaton);
		return shortestPaths(det, numPaths, sssd);
	}
	
	public static <L, K extends Comparable<K>> List<Path<L, K>> bestStrings(Automaton<L, K> automaton, int numPaths)
	{
		Automaton<L, K> det = Operations.determinizeER(automaton);
		return shortestPaths(det, numPaths);
	}
	
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
	
	public static <L, K> Map<Object, K> shortestDistancesToFinalStates(Automaton<L, K> automaton, SingleSourceShortestDistancesInterface<K> sssd)
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

	public static <L, K> K shortestCompleteDistances(Automaton<L, K> automaton, SingleSourceShortestDistances<K> shortestDistanceAlgorithm)
	{
		Semiring<K> sr = automaton.semiring();
		Map<Object, K> shortestDistances = shortestDistancesFromInitialStates(automaton, shortestDistanceAlgorithm);
		K weight = sr.zero();
		for (Entry<Object, K> e : shortestDistances.entrySet()) if (isFinalState(automaton, e.getKey()))
		{
			weight = sr.add(weight, sr.multiply(e.getValue(), automaton.finalWeight(e.getKey())));
		}
		return weight;
	}
	
	public static List<Character> toCharacterList(String str)
	{
		List<Character> characterList = new ArrayList<Character>(str.length());
		for (int i = 0; i < str.length(); i++)
			characterList.add(str.charAt(i));
		return characterList;
	}
	
	public static String toString(List<Character> characterList)
	{
		String str = "";
		for (Character c : characterList)
			if (c != null)
				str += c;
		return str;
	}
	
	
}
