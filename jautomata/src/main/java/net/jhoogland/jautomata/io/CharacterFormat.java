package net.jhoogland.jautomata.io;

public class CharacterFormat implements Format<Character> 
{
	public String format(Character e) 
	{		
		return e == null ? "--" : e.toString();
	}

	public Character parse(String str) 
	{		
		return str.equals("--") ? null : str.charAt(0);
	}
}
