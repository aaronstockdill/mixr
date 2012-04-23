# Diabelli

_Diabelli_ is a framework for integrating multiple sentential and diagrammatic theorem provers into one heterogeneous reasoning environment.

The primary motivation for the Diabelli framework was to integrate diagrammatic and sentential theorem provers to enable heterogeneous reasoning. However, the Diabelli framework is extremely flexible and allows integration and communication between any type of theorem provers&mdash;as long as there exists a translation between them.

Diabelli is a conglomeration of modules built for the [NetBeans Platform](http://netbeans.org/features/platform/). It integrates well with [I3P](http://www-pu.informatik.uni-tuebingen.de/i3p/) (an interactive interface for the Isabelle prover).

This project is related to (but not dependent on) [Speedith](https://gitorious.org/speedith) (a diagrammatic theorem prover for spider diagrams).

Speedith and [Isabelle](http://www.cl.cam.ac.uk/research/hvg/Isabelle/) (through I3P) are the first supported theorem provers to be integrated into the Diabelli framework. It is thus possible to use spider diagrams while doing proofs in Isabelle. Isabelle formulae are automatically translated to spider diagrams if possible. The user can then reason on the spider diagram and then return the result of reasoning with the spider-diagrammatic representation back to Isabelle.

## Licence

Diabelli is an open source project. It's sources are freely available under the [MIT License](http://en.wikipedia.org/wiki/Mit_license).

The full text of the licence is in the following file:

>   `LICENSE`
