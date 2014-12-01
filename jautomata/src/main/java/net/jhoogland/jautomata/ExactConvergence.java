package net.jhoogland.jautomata;

public class ExactConvergence<K> implements WeightConvergenceCondition<K> 
{
	public boolean converged(K w1, K w2) 
	{		
		return w1.equals(w2);
	}
}
