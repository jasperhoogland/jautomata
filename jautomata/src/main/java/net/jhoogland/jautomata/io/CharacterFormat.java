package net.jhoogland.jautomata.io;

public class CharacterFormat implements Format<Character> 
{
	public String nullString;
	
	public CharacterFormat(String nullString) 
	{
		this.nullString = nullString;
	}
	
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
