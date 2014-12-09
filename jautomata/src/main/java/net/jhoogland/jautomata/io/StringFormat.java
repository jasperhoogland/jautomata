package net.jhoogland.jautomata.io;

public class StringFormat implements Format<String> 
{
	String nullString;
	
	public StringFormat(String nullString) 
	{		
		this.nullString = nullString;
	}
	
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
