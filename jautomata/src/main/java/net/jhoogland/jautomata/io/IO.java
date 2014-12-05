package net.jhoogland.jautomata.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import net.jhoogland.jautomata.AbstractAutomaton;
import net.jhoogland.jautomata.Automaton;
import net.jhoogland.jautomata.BasicState;
import net.jhoogland.jautomata.BasicTransition;
import net.jhoogland.jautomata.HashAutomaton;
import net.jhoogland.jautomata.semirings.BooleanSemiring;
import net.jhoogland.jautomata.semirings.RealSemiring;
import net.jhoogland.jautomata.semirings.Semiring;

public class IO 
{
	public static <L, K> Automaton<L, K> loadAcceptor(File file, String format, Semiring<K> semiring, Format<L> labelFormat) throws IOException
	{
		if (format.toLowerCase().equals("att")) return loadAcceptorATT(file, semiring, labelFormat);
		else return null;
	}

	public static <K> Automaton<Character, K> loadAcceptor(File file, Semiring<K> semiring, String format) throws IOException
	{
		return loadAcceptor(file, format, semiring, new CharacterFormat());
	}

	public static <L> Automaton<L, Boolean> loadUnweightedAcceptor(File file, String format, Format<L> labelFormat) throws IOException
	{
		return loadAcceptor(file, format, new BooleanSemiring(), labelFormat);
	}

	public static Automaton<Character, Boolean> loadUnweightedAcceptor(File file, String format) throws IOException
	{
		return loadUnweightedAcceptor(file, format, new CharacterFormat());		
	}
	
	public static <L> Automaton<L, Double> loadWeightedAcceptor(File file, String format, Format<L> labelFormat) throws IOException
	{
		return loadAcceptor(file, format, new RealSemiring(), labelFormat);
	}

	public static Automaton<Character, Double> loadWeightedAcceptor(File file, String format) throws IOException
	{
		return loadWeightedAcceptor(file, format, new CharacterFormat());		
	}
	
	private static <L, K> Automaton<L, K> loadAcceptorATT(File file, Semiring<K> semiring, Format<L> labelFormat) throws IOException
	{
		BufferedReader br = new BufferedReader(new FileReader(file));
		String line = br.readLine();
		LoadedAutomaton<L, K> la = new LoadedAutomaton<L, K>(semiring);
		while (line != null)
		{
			String[] fields = line.split(" ");
			if (fields.length > 2)
			{
				Object weight = fields.length == 3 ? semiring.one() : Double.parseDouble(fields[3]);
				L label = labelFormat.parse(fields[2]);
				String from = fields[0];
				String to = fields[1];
				la.addTransition(from, label, (K) weight, to);
				if (la.initialWeights.isEmpty())
					la.addInitialState(from, semiring.one());
			}
			else
			{
				String state = fields[0];
				Object weight = fields.length == 1 ? semiring.one() : Double.parseDouble(fields[1]);
				la.addFinalState(state, (K) weight);
			}
			line = br.readLine();
		}		
		br.close();
		return new HashAutomaton<L, K>(la);
	}
	
	static class LoadedAutomaton<L, K> extends AbstractAutomaton<L, K>
	{
		Map<String, K> initialWeights;
		Map<String, K> finalWeights;		
		Map<String, Collection<Object>> transitionsOut; 
		
		public LoadedAutomaton(Semiring<K> semiring) 
		{
			super(semiring);
			finalWeights = new HashMap<String, K>();
			initialWeights = new HashMap<String, K>();
			transitionsOut = new HashMap<String, Collection<Object>>();
		}
		
		void addTransition(String from, L label, K weight, String to)
		{
			Collection<Object> ts = transitionsOut.get(from);
			if (ts == null)
			{
				ts = new ArrayList<Object>();
				transitionsOut.put(from, ts);
			}
			ts.add(new BasicTransition<L, K>(from, label, weight, to));
		}
		
		void addInitialState(String state, K weight)
		{
			initialWeights.put(state, weight);
		}

		void addFinalState(String state, K weight)
		{
			finalWeights.put(state, weight);
		}

		public Collection<Object> initialStates() 
		{			
			Collection<Object> initialStates = new ArrayList<Object>();
			initialStates.addAll(initialWeights.keySet());
			return initialStates;
		}
		
		@Override
		public Collection<Object> transitionsOut(Object state) 
		{			
			Collection<Object> ts = transitionsOut.get(state);
			return ts == null ? Collections.emptyList() : ts;
		}

		public K initialWeight(Object state) 
		{			
			K w = initialWeights.get(state);
			return w == null ? semiring().zero() : w;		
		}

		public K finalWeight(Object state) 
		{			
			K w = finalWeights.get(state);
			return w == null ? semiring().zero() : w;
		}

		public Object previousState(Object transition) 
		{			
			BasicTransition<L, K> t = (BasicTransition<L, K>) transition;
			return t.previousState();
		}

		public Object nextState(Object transition) 
		{
			BasicTransition<L, K> t = (BasicTransition<L, K>) transition;
			return t.nextState();
		}

		public L label(Object transition) 
		{
			BasicTransition<L, K> t = (BasicTransition<L, K>) transition;
			return t.label();
		}

		public K transitionWeight(Object transition) 
		{
			BasicTransition<L, K> t = (BasicTransition<L, K>) transition;
			return t.weight();
		}
		
	}
}
