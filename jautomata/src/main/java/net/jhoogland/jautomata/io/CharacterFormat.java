package net.jhoogland.jautomata.io;

/**
 * 
 * Label format for Character labels.
 * 
 * @author Jasper Hoogland
 *
 */

public class CharacterFormat implements Format<Character> 
{
	public String nullString;
	
	/**
	 * Creates a {@link CharacterFormat} instance with the specified null string.
	 */
	
	public CharacterFormat(String nullString) 
	{
		this.nullString = nullString;
	}
	
	/**
	 * Creates a {@link CharacterFormat} instance with null string <code>--</code>.
	 */
	
	public CharacterFormat()
	{
		this("--");
	}
	
	public String format(Character e) 
	{		
		return e == null ? nullString : e.toString();
	}

	public Character parse(String str) 
	{		
		return str.equals(nullString) ? null : str.charAt(0);
	}
}
