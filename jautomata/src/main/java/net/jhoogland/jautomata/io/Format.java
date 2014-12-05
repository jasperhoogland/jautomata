package net.jhoogland.jautomata.io;

public interface Format<T> 
{
	public String format(T e);
	public T parse(String str);
}
