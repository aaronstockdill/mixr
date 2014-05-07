# MixR

_MixR_ is a framework for integrating multiple sentential and diagrammatic theorem provers, as well as informal tools from arbitrary domains (such as image processing, natural language processing, amongst others) into one heterogeneous reasoning system.

MixR is a conglomeration of modules built for the [NetBeans Platform](http://netbeans.org/features/platform/). It integrates well with [I3P](http://www-pu.informatik.uni-tuebingen.de/i3p/) (an interactive interface for the Isabelle prover).

This project is related to (but not dependent on) [Speedith](https://gitorious.org/speedith) (a diagrammatic theorem prover for spider diagrams).

Speedith and [Isabelle](http://www.cl.cam.ac.uk/research/hvg/Isabelle/) (through I3P) are the first supported theorem provers to be integrated into the MixR framework--resulting in the _Diabelli Heterogeneous Reasoning System_. With Diabelli it is possible to use spider diagrams while performing proofs in Isabelle. Isabelle formulae are automatically translated to spider diagrams if possible. The user can then reason on the spider diagram and then return the result of reasoning with the spider-diagrammatic representation back to Isabelle.

We provide two further examples of heterogeneous reasoning with MixR:

1.   NatLang: demonstrates how reasoning with natural languages can be performed in a general-purpose sentential and formal theorem prover (in this case, Isabelle).
2.   PicProc: demonstrates how one can reason about bitmap images within Isabelle.

Both of the examples above use the novel concept of _placeholders_ to insert natural language sentences and images into Isabelle's formulae.

## Installation procedure

1.  Download the NetBeans IDE with Java SE support from this location (currently we support NetBeans 7.3):

        https://netbeans.org/downloads/

2.  Clone this repository repository:

        git clone https://github.com/urbas/mixr.git

3.  This is enough just to build the MixR framework (without any plugins!). This means that none of the examples (Isabelle, Speedith, NatLang, PicProc, and TPTP will work).

    To build MixR framework (MixR core), open the `devel/MixRFramework` project in NetBeans.

    To build Diabelli (with Isabelle, Speedith, NatLang, PicProc, and TPTP support), proceed as follows:

4.  Download and unpack Isabelle 2012  into the `~/bin/Isabelle2012` folder. Isabelle 2012 can be downloaded from:

        https://bitbucket.org/urbas/i3p

5.  Install Maven:

        http://maven.apache.org/download.cgi

6.  Clone the Speedith and iCircles repositories (follow their installation instructions from README.md):

        git clone https://github.com/urbas/speedith.git
        git clone https://github.com/urbas/iCircles.git

    The `Speedith` and `iCircles` folders must be siblings to the `mixr` folder.

7.  Go to the `mixr` folder and run the following:

      mvn install

8.  Clone I3P from here:

        http://pu.inf.uni-tuebingen.de/i3p/

9.  Open the `Diabelli` project (`devel/Diabelli`) within NetBeans and make sure all dependencies are resolved. Try to build and run.

    Feel free to contact developers in case the build fails.

## Licence

MixR is an open source project. It's sources are freely available under the [MIT License](http://en.wikipedia.org/wiki/Mit_license).

The full text of the licence is in the following file:

>   `<repository root>/LICENSE`
