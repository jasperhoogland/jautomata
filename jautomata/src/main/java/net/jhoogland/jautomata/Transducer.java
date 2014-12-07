package net.jhoogland.jautomata;

/**
 * 
 * This interface is optionally implemented by transducers.
 * The only purpose of this interface is to make the type specification of transducers shorter, 
 * because it allows users to avoid the explicit use of {@link TLabel}.  
 * All operations in this library that return transducers implement this interface.
 * All operations in this library that are performed on transducers, though, do not require this interface to be implemented,
 * provided that the transducers have the appropriate label type.
 * 
 * 
 * @author Jasper Hoogland
 *
 * @param <I>
 * The output label type
 * 
 * @param <O>
 * The input label type
 * 
 * @param <K>
 * weight type 
 * (Boolean for regular automata and Double for weighted automata)
 */

public interface Transducer<I, O, K> extends Automaton<TLabel<I, O>, K> 
{
}
