package net.jhoogland.jautomata;

import java.util.Collection;


public interface MTAutomaton<I, L, K> extends Automaton<MTLabel<I, L>, K> 
{
	public Collection<I> tapes();
}
