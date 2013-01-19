#!/bin/bash

extRefsDir=release/modules/ext

mkdir -p "$extRefsDir" || exit 1;

cd "$extRefsDir"

ln -s ~/bin/Isabelle2012/lib/classes/ext/Pure.jar .
ln -s ~/bin/Isabelle2012/lib/classes/ext/scala-library.jar .
ln -s ~/bin/Isabelle2012/lib/classes/ext/scala-swing.jar .


ln -s ../../../../IsabelleScalaWrapper/dist/libIsaWrapper.jar .
