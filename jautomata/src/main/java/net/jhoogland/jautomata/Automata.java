package net.jhoogland.jautomata;

import java.util.ArrayList;
import java.util.Arrays;
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
import net.jhoogland.jautomata.operations.Union;
import net.jhoogland.jautomata.queues.DefaultQueueFactory;
import net.jhoogland.jautomata.queues.KTropicalQueueFactory;
import net.jhoogland.jautomata.semirings.BestPathWeights;
import net.jhoogland.jautomata.semirings.BooleanSemiring;
import net.jhoogland.jautomata.semirings.KTropicalSemiring;
import net.jhoogland.jautomata.semirings.Semiring;

/**
 *
 * This class contains static methods for creating various types of finite state automata.
 * 
 * @author Jasper Hoogland
 *
 */

public class Automata 
{
	public static void main(String[] args) 
	{
		SinglePathAutomaton<Character, Boolean> a1 = createSinglePathAutomaton(new BooleanSemiring(), "haha");
		SinglePathAutomaton<Character, Boolean> a2 = createSinglePathAutomaton(new BooleanSemiring(), "hallo");
//		for (Object state : states(a1))
//			System.out.println(state);
		
//		SingleSourceShortestDistances<Boolean> sssp = new SingleSourceShortestDistances<Boolean>(new DefaultQueueFactory<Boolean>(), new ExactConvergence<Boolean>());
		
//		System.out.println(sssp.computeShortestDistances(automaton, 0));
		
//		System.out.println(shortestCompleteDistances(automaton, sssp));
		
		Automaton<Character, Boolean> union0 = Operations.determinizeER(Operations.union(a1, a2, Operations.reverse(a1), Operations.reverse(a2)));
		Automaton<Character, Boolean> union = new ArrayAutomaton<Character, Boolean>(union0);
		
		System.out.println(Automata.states(union));
		System.out.println(stringWeight(union, ""));
		System.out.println(stringWeight(union, "tes"));
		System.out.println(":) " + stringWeight(union, "hallo"));
		System.out.println(":) " + stringWeight(union, "haha"));
		System.out.println(stringWeight(union, "hall"));
		
		List<Path<Character, Double>> ps = bestStrings(union, 4);
		
		for (Path<Character, Double> p : ps)
			System.out.println(toString(p.label));
		
		
	}
	
	/**
	 * 
	 * This method returns a Collection containing all states of the specified {@link Automaton}.
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
	
	public static <L, K> boolean isInitialState(Automaton<L, K> automaton, Object state)
	{
		return ! automaton.semiring().zero().equals(automaton.initialWeight(state));
	}
	
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
	
	public static <L, K> List<Path<L, Double>> shortestPaths(Automaton<L, K> automaton, int numPaths, SingleSourceShortestDistances<BestPathWeights> sssd)
	{
		Automaton<L, BestPathWeights> kT = Operations.toKTropicalSemiring(automaton, numPaths);
		
		BestPathWeights w = shortestCompleteDistances(kT, sssd);
		ArrayList<Path<L, Double>> paths = new ArrayList<Path<L, Double>>();
		for (int i = 0; i < w.pathWeights.length; i++) if (w.pathWeights[i].weight != Double.POSITIVE_INFINITY)
		{
			Path<L, Double> path = (Path<L, Double>) w.pathWeights[i].path((net.jhoogland.jautomata.Automaton<L, Double>) automaton);
			path.weight = Math.exp(-path.weight);
			paths.add(path);
		}
		return paths;
	}
	
	public static <L, K> List<Path<L, Double>> shortestPaths(Automaton<L, K> automaton, int numPaths)
	{
		SingleSourceShortestDistances<BestPathWeights> sssd = new SingleSourceShortestDistances<BestPathWeights>(new KTropicalQueueFactory(), new ExactConvergence<BestPathWeights>());
		return shortestPaths(automaton, numPaths, sssd);
	}
	
	public static <L, K> List<Path<L, Double>> bestStrings(Automaton<L, K> automaton, int numPaths, SingleSourceShortestDistances<BestPathWeights> sssd)
	{
		Automaton<L, K> det = Operations.determinizeER(automaton);
		return shortestPaths(det, numPaths, sssd);
	}
	
	public static <L, K> List<Path<L, Double>> bestStrings(Automaton<L, K> automaton, int numPaths)
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
