package net.jhoogland.jautomata.io;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;

import net.jhoogland.jautomata.ArrayAutomaton;
import net.jhoogland.jautomata.Automata;
import net.jhoogland.jautomata.Automaton;
import net.jhoogland.jautomata.HashTransducer;
import net.jhoogland.jautomata.ReverselyAccessibleAutomaton;
import net.jhoogland.jautomata.TLabel;
import net.jhoogland.jautomata.Transducer;
import net.jhoogland.jautomata.io.AcceptorIO.LoadedAutomaton;
import net.jhoogland.jautomata.operations.Operations;
import net.jhoogland.jautomata.semirings.BooleanSemiring;
import net.jhoogland.jautomata.semirings.RealSemiring;
import net.jhoogland.jautomata.semirings.Semiring;

/**
 * This class contains static methods to load and save (weighted) transducers.
 * 
 * @author Jasper Hoogland
 */

public class TransducerIO 
{
	public static <I, O, K> Transducer<I, O, K> read(Reader reader, Semiring<K> semiring, Format<I> inputLabelFormat, Format<O> outputLabelFormat) throws IOException
	{
		return readATT(reader, semiring, inputLabelFormat, outputLabelFormat);
	}

	public static <K> Transducer<Character, Character, K> read(Reader reader, Semiring<K> semiring) throws IOException
	{
		return read(reader, semiring, new CharacterFormat(), new CharacterFormat());
	}

	public static <I, O> Transducer<I, O, Boolean> readUnweighted(Reader reader, Format<I> inputLabelFormat, Format<O> outputLabelFormat) throws IOException
	{
		return read(reader, new BooleanSemiring(), inputLabelFormat, outputLabelFormat);
	}

	public static Transducer<Character, Character, Boolean> readUnweighted(Reader reader, String format) throws IOException
	{
		return readUnweighted(reader, new CharacterFormat(), new CharacterFormat());		
	}
	
	public static <I, O> Transducer<I, O, Double> readWeighted(Reader reader, Format<I> inputLabelFormat, Format<O> outputLabelFormat) throws IOException
	{
		return read(reader, new RealSemiring(), inputLabelFormat, outputLabelFormat);
	}

	public static Transducer<Character, Character, Double> readWeighted(Reader reader, String format) throws IOException
	{
		return readWeighted(reader, new CharacterFormat(), new CharacterFormat());		
	}
	
	public static <I, O, K> void write(Automaton<TLabel<I, O>, K> automaton, Writer writer, Format<I> inputLabelFormat, Format<O> outputLabelFormat) throws FileNotFoundException
	{
		ReverselyAccessibleAutomaton<TLabel<I, O>, K> a = new ArrayAutomaton<TLabel<I, O>, K>(automaton.initialStates().size() > 1 ? Operations.singleInitialState(automaton) : automaton);	
		PrintWriter pw = writer instanceof PrintWriter ? (PrintWriter) writer : new PrintWriter(writer);
		K one = automaton.semiring().one();
		for (Object t : Automata.transitions(a))
		{
			String from = a.previousState(t).toString();
			String iLabel = inputLabelFormat.format(a.label(t).in());
			String oLabel = outputLabelFormat.format(a.label(t).out());
			K weight = a.transitionWeight(t);
			String weightStr = weight.equals(one) ? "" : " " + weight;
			String to = a.nextState(t).toString();
			pw.println(from + " " + to + " " + iLabel + " " + oLabel + weightStr);
		}
		for (Object s : a.finalStates())
		{
			K weight = a.finalWeight(s);
			pw.println(s + (weight.equals(one) ? "" : " " + weight));
		}		
		pw.close();
	}
	
	public static <K> void write(Automaton<TLabel<Character, Character>, K> automaton, Writer writer, String format) throws FileNotFoundException
	{
		write(automaton, writer, new CharacterFormat(), new CharacterFormat());
	}
	
	private static <I, O, K> Transducer<I, O, K> readATT(Reader reader, Semiring<K> semiring, Format<I> inputLabelFormat, Format<O> outputLabelFormat) throws IOException
	{
		BufferedReader br = reader instanceof BufferedReader ? (BufferedReader) reader : new BufferedReader(reader);
		String line = br.readLine();
		LoadedAutomaton<TLabel<I, O>, K> la = new LoadedAutomaton<TLabel<I, O>, K>(semiring);
		while (line != null)
		{
			String[] fields = line.split(" ");
			if (fields.length > 3)
			{
				Object weight = fields.length == 4 ? semiring.one() : Double.parseDouble(fields[4]);
				I iLabel = inputLabelFormat.parse(fields[2]);
				O oLabel = outputLabelFormat.parse(fields[3]);
				String from = fields[0];
				String to = fields[1];
				la.addTransition(from, new TLabel<I, O>(iLabel, oLabel), (K) weight, to);
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
		return new HashTransducer<I, O, K>(la);
	}
}
