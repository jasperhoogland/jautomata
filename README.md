JAutomata
=========

This is a Java library for (weighted) finite state automata. 
The automata are generic with respect to label type and semiring.
This allows generic algorithms to be performed on any type of (weighted) finite state automata, such as acceptors, transducers, and multi-tape automata.
The library contains algorithms to create automata in Java code and to import them from files.
It contains algorithms to compute properties of automata, including
<ul>
<li>string weights</li>
<li><i>n</i> shortest paths</li>
<li><i>n</i> best strings</li>
</ul>
It also contains methods to manipulate automata, such as
<ul>
<li>intersection</li>
<li>transducer composition</li>
<li>epsilon removal</li>
<li>determinization</li>
<li>union</li>
<li>label conversion</li>
<li>semiring conversion</li>
</ul>

<h2>Creating Finite State Automata</h2>

New automata can be created in a number of ways.
They can be imported from files or they can be created in an application using Java code.

<h2>Automaton Operations</h2>

This library supports several algorithms on automaton.
This includes the computation of weights of paths and strings, operations such as intersection, union, and conversion of labels and semirings.
