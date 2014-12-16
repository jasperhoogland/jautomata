package net.jhoogland.jautomata.sample;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;

import net.jhoogland.jautomata.Automata;
import net.jhoogland.jautomata.Automaton;
import net.jhoogland.jautomata.EditableAutomaton;
import net.jhoogland.jautomata.MTAutomata;
import net.jhoogland.jautomata.MTAutomaton;
import net.jhoogland.jautomata.Path;
import net.jhoogland.jautomata.SingleStringAutomaton;
import net.jhoogland.jautomata.Transducer;
import net.jhoogland.jautomata.Transducers;
import net.jhoogland.jautomata.io.TransducerIO;
import net.jhoogland.jautomata.operations.EditableTransducer;
import net.jhoogland.jautomata.operations.Operations;
import net.jhoogland.jautomata.semirings.BooleanSemiring;

public class IndonesianAffixes 
{
	public static void main(String[] args) throws UnsupportedEncodingException, IOException 
	{
		MTAutomaton<String, Character, Boolean> mMTA = morphologyMTA();
		
		Automaton<Character, Boolean> acceptor = 
				Automata.createMultipleStringsAutomaton(new BooleanSemiring(), 
					"rasa", "guna", "kata", "tulis", "putus", "selamat", "baik", "ada");
		
		MTAutomaton<String, Character, Boolean> words = MTAutomata.acceptorToHashMT(acceptor, "stem");		
		MTAutomaton<String, Character, Boolean> wordForms = MTAutomata.hashMTIntersection(mMTA, words);
		List ps = Automata.bestStrings(wordForms, 30);
		
		for (Path p : (List<Path>) ps)
			System.out.println(p.weight + ": " + MTAutomata.toStrings(p));
	}
	
	public static MTAutomaton<String, Character, Boolean> morphologyMTA()
	{
//		MTAutomaton<String, Character, Boolean> mtaMe = MTAutomata.transducerToHashMT(meWordsTransducer(), "stem", "me-");
		MTAutomaton<String, Character, Boolean> mtaMeKan = MTAutomata.transducerToHashMT(meKanWordsTransducer(), "stem", "me-kan");
//		MTAutomaton<String, Character, Boolean> mtaDiKan = MTAutomata.transducerToHashMT(diKanWordsTransducer(), "stem", "di-kan");
		MTAutomaton<String, Character, Boolean> mtaPeAn = MTAutomata.transducerToHashMT(peAnWordsTransducer(), "stem", "pe-an");
		MTAutomaton<String, Character, Boolean> mtaBer = MTAutomata.transducerToHashMT(berWordsTransducer(), "stem", "ber-");
		MTAutomaton<String, Character, Boolean> mtaPerAn = MTAutomata.transducerToHashMT(perAnWordsTransducer(), "stem", "per-an");
		MTAutomaton<String, Character, Boolean> mMTA = 
				MTAutomata.hashMTIntersection(mtaMeKan, mtaPeAn, mtaBer, mtaPerAn);
		return mMTA;
	}
	
	public static Transducer<Character, Character, Boolean> meWordsTransducer()
	{
		InputStream is = IndonesianAffixes.class.getResourceAsStream("indonesian-prefix-1.txt");
		
		try {			
			Transducer<Character, Character, Boolean> pfMe = affixTransducer("me");
			Transducer<Character, Character, Boolean> ip1 = TransducerIO.readUnweighted(new InputStreamReader(is, "UTF-8"));
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
	
	public static Transducer<Character, Character, Boolean> peAnWordsTransducer()
	{
		InputStream is = IndonesianAffixes.class.getResourceAsStream("indonesian-prefix-1.txt");
		
		try {
			Transducer<Character, Character, Boolean> pfPe = affixTransducer("pe");			
			Transducer<Character, Character, Boolean> ip1 = TransducerIO.readUnweighted(new InputStreamReader(is, "UTF-8"));
			Transducer<Character, Character, Boolean> stem = stemTransducer();
			Transducer<Character, Character, Boolean> sfAn = affixTransducer("an");
			
			return Transducers.concat(pfPe, ip1, stem, sfAn);	
		} catch (UnsupportedEncodingException e) 
		{
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public static Transducer<Character, Character, Boolean> meKanWordsTransducer()
	{
		InputStream is = IndonesianAffixes.class.getResourceAsStream("indonesian-prefix-1.txt");
		
		try {
			Transducer<Character, Character, Boolean> pfMe = affixTransducer("me");			
			Transducer<Character, Character, Boolean> ip1 = TransducerIO.readUnweighted(new InputStreamReader(is, "UTF-8"));
			Transducer<Character, Character, Boolean> stem = stemTransducer();
			Transducer<Character, Character, Boolean> sfAn = affixTransducer("kan");
			
			return Transducers.concat(pfMe, ip1, stem, sfAn);	
		} catch (UnsupportedEncodingException e) 
		{
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public static Transducer<Character, Character, Boolean> diKanWordsTransducer()
	{
		Transducer<Character, Character, Boolean> pfDi = affixTransducer("di");			
		Transducer<Character, Character, Boolean> stem = stemTransducer();
		Transducer<Character, Character, Boolean> sfAn = affixTransducer("kan");
		
		return Transducers.concat(pfDi, stem, sfAn);	
	}
	
	public static Transducer<Character, Character, Boolean> berWordsTransducer()
	{
		Transducer<Character, Character, Boolean> pfBe = affixTransducer("be");
		Transducer<Character, Character, Boolean> pfR = rPrefixTransducer();
		Transducer<Character, Character, Boolean> stem = stemTransducer();
		
		return Transducers.concat(pfBe, pfR, stem);	
	}
	
	public static Transducer<Character, Character, Boolean> perAnWordsTransducer()
	{
		Transducer<Character, Character, Boolean> pfPe = affixTransducer("pe");
		Transducer<Character, Character, Boolean> pfR = rPrefixTransducer();
		Transducer<Character, Character, Boolean> stem = stemTransducer();
		Transducer<Character, Character, Boolean> sfAn = affixTransducer("an");
		
		return Transducers.concat(pfPe, pfR, stem, sfAn);	
	}
	
	public static Transducer<Character, Character, Boolean> stemTransducer()
	{
		EditableAutomaton<Character, Boolean> stemAcceptor = new EditableAutomaton<Character, Boolean>(new BooleanSemiring());
		int s = stemAcceptor.addState(true, true);
		for (char c = 'a'; c <= 'z'; c++)
			stemAcceptor.addTransition(s, s, c);
		
		return Transducers.identity(stemAcceptor);
	}
	
	public static Transducer<Character, Character, Boolean> affixTransducer(String affix)
	{
		return Transducers.outputTransducer(Automata.createSingleStringAutomaton(new BooleanSemiring(), affix));
	}
	
	public static Transducer<Character, Character, Boolean> rPrefixTransducer()
	{
		EditableTransducer<Character, Character, Boolean> t = new EditableTransducer<Character, Character, Boolean>(new BooleanSemiring());
		int s1 = t.addState(true, false);
		int s2 = t.addState(false, false);
		int s3 = t.addState(false, true);
		t.addTransition(s1, s3, 'r', 'r', true);
		t.addTransition(s1, s2, null, 'r', true);
		for (char c : new char[] {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 's', 't', 'u', 'w', 'y', 'z'})
			t.addTransition(s2, s3, c, c, true);
		return t;
	}
}
