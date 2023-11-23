package net.jhoogland.jautomata.io;

/**
 * Label format for String labels.
 * 
 * @author Jasper Hoogland
 *
 */

public class StringFormat implements Format<String> 
{
	String nullString;
	
	/**
	 * Creates a {@link StringFormat} instance with the specified null string.
	 */
	
	public StringFormat(String nullString) 
	{		
		this.nullString = nullString;
	}	
	
	/**
	 * Creates a {@link StringFormat} instance with null string <code>-</code>.
	 */

	public StringFormat() 
	{
		this("-");
	}
	
	public String format(String e) 
	{
		return e == null ? nullString : e;
	}

	public String parse(String str) 
	{		
		return str.equals(nullString) ? null : str;
	}
}
