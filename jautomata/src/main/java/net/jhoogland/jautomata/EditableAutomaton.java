package net.jhoogland.jautomata;
import java.util.HashMap;

import net.jhoogland.jautomata.semirings.Semiring;

public class EditableAutomaton<L, K> extends HashAutomaton<L, K> 
{
	int stateId;
	int transitionId;
	
	public EditableAutomaton(Semiring<K> semiring) 
	{
		super(new HashMap<Object, BasicState<K>>(), new HashMap<Object, BasicTransition<L, K>>(), semiring);
	}
	
	public int addState(K initialWeight, K finalWeight)
	{
		int sid = stateId;
		addState(sid, initialWeight, finalWeight);
		stateId++;
		return sid;
	}
	
	public int addTransition(Object from, Object to, L label, K weight) 
	{
		int tid = transitionId;		
		addTransition(tid, from, to, label, weight);
		transitionId++;
		return tid;
	}

	public int addTransition(Object from, Object to, L label) 
	{
		return addTransition(from, to, label, semiring().one());		
	}
}
