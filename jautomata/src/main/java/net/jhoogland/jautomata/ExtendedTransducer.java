package net.jhoogland.jautomata;

import java.util.Collection;

public interface ExtendedTransducer<I, O, K> extends Transducer<I, O, K> 
{
	public Collection<Object> transitionsOutI(Object state, I label);
	public Collection<Object> transitionsOutO(Object state, O label);
}
