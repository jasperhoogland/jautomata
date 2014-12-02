package net.jhoogland.jautomata.operations;

import net.jhoogland.jautomata.Automaton;
import net.jhoogland.jautomata.TLabel;
import net.jhoogland.jautomata.Transducer;

/**
 * Subclass of {@link LabelConversion} that implements the {@link Transducer} interface.
 * 
 * @author Jasper Hoogland
 *
 * @param <L>
 * label type of the operand automaton
 * 
 * @param <I>
 * input label type of the resulting transducer
 * 
 * @param <O>
 * output label type of the resulting transducer
 * 
 * @param <K>
 * The type of elements of the semiring over which the automaton is defined 
 * (Boolean for regular automata and Double for weighted automata)
 * 
 */

public abstract class TransducerLabelConversion<L, I, O, K> extends LabelConversion<L, TLabel<I, O>, K> implements Transducer<I, O, K> 
{
	public TransducerLabelConversion(Automaton<L, K> operand) 
	{
		super(operand);
	}
}
