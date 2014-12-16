package net.jhoogland.jautomata.operations;

import net.jhoogland.jautomata.EditableAutomaton;
import net.jhoogland.jautomata.TLabel;
import net.jhoogland.jautomata.Transducer;
import net.jhoogland.jautomata.semirings.Semiring;

public class EditableTransducer<I, O, K> extends EditableAutomaton<TLabel<I, O>, K> implements Transducer<I, O, K> 
{
	public EditableTransducer(Semiring<K> semiring) 
	{
		super(semiring);		
	}
		
	public int addTransition(int from, int to, I iLabel, O oLabel, K weight) 
	{
		return addTransition(from, to, new TLabel<I, O>(iLabel, oLabel), weight);
	}
}
