package net.jhoogland.jautomata;

import java.util.Collection;

public interface MTLabel<I, L> 
{
	public L tapeLabel(I tape);
	public Collection<I> tapes();
}
