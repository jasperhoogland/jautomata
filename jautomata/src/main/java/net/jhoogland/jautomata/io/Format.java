package net.jhoogland.jautomata.io;

/**
 * 
 * Implementations of this interface specify how transition labels must be parsed and formatted by IO methods.
 * 
 * @author Jasper Hoogland
 *
 * @param <L>
 * label type
 */

public interface Format<L> 
{
	public String format(L e);
	public L parse(String str);
}
