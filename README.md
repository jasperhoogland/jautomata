JAutomata
=========

This is a Java library for (weighted) finite state automata. 
The automata are generic with respect to label type and semiring.
This allows generic algorithms to be performed on any type of (weighted) finite state automata, such as acceptors, transducers, and multi-tape automata.
The library contains algorithms to create automata in Java code and to import them from files.
It contains algorithms to cpmpute shortest paths and string weights.
It also contains methods to manipulate automata, such as intersection, transducer composition, union, label conversion, and semiring conversion.

<h2>Creating Finite State Automata</h2>

New automata can be created in a number of ways.
They can be imported from files or they can be created in an application using Java code.

<h2>Automaton Operations</h2>

This library supports several algorithms on automaton.
This includes the computation of weights of paths and strings, operations such as intersection, union, and conversion of labels and semirings.
