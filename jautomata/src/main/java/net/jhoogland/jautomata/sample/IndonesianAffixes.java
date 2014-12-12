package net.jhoogland.jautomata.sample;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;

import net.jhoogland.jautomata.Automata;
import net.jhoogland.jautomata.Automaton;
import net.jhoogland.jautomata.EditableAutomaton;
import net.jhoogland.jautomata.Path;
import net.jhoogland.jautomata.Transducer;
import net.jhoogland.jautomata.Transducers;
import net.jhoogland.jautomata.io.TransducerIO;
import net.jhoogland.jautomata.operations.Operations;
import net.jhoogland.jautomata.semirings.BooleanSemiring;

public class IndonesianAffixes 
{
	public static void main(String[] args) throws UnsupportedEncodingException, IOException 
	{
		Transducer<Character, Character, Boolean> meWords = meWordsTransducer();
		Automaton<Character, Boolean> r = Transducers.apply(meWords, "tulis");
		Automaton<Character, Double> r2 = Operations.toWeightedAutomaton(r);
		
		List ps = Automata.bestStrings(r, 30);
		
		for (Path p : (List<Path>) ps)
			System.out.println(p.weight + ": " + Automata.toString(p.label));
		
//		System.out.println(Automata.stringWeight(r, "menulis"));
	}
	
	public static Transducer<Character, Character, Boolean> meWordsTransducer()
	{
		InputStream is = IndonesianAffixes.class.getResourceAsStream("indonesian-prefix-1.txt");
		
		try {
			Transducer<Character, Character, Boolean> ip1 = TransducerIO.readUnweighted(new InputStreamReader(is, "UTF-8"));
			Transducer<Character, Character, Boolean> pfMe = Transducers.outputTransducer(Automata.createSingleStringAutomaton(new BooleanSemiring(), "me"));			
			Transducer<Character, Character, Boolean> stem = stemTransducer();
			
			return Transducers.concat(pfMe, ip1, stem);	
		} catch (UnsupportedEncodingException e) 
		{
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public static Transducer<Character, Character, Boolean> stemTransducer()
	{
		EditableAutomaton<Character, Boolean> stemAcceptor = new EditableAutomaton<Character, Boolean>(new BooleanSemiring());
		int s = stemAcceptor.addState(true, true);
		for (char c = 'a'; c <= 'z'; c++)
			stemAcceptor.addTransition(s, s, c);
		
		return Transducers.identity(stemAcceptor);
	}
}
