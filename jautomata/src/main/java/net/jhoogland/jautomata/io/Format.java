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
	/**
	 * @return
	 * the formatted {@link String} for the specified label according to this label format
	 */
	
	public String format(L e);

	/**
	 * @return
	 * the parsed label for the specified label string according to this label format
	 */
	
	public L parse(String str);
}
