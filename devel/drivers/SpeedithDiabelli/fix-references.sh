#!/bin/bash

extRefsDir=release/modules/ext

mkdir -p "$extRefsDir" || exit 1;

cd "$extRefsDir"

ln -s /usr/share/java/commons-cli.jar .
ln -s /usr/share/java/antlr3-runtime.jar .

ln -s ../../../../SpeedithIsabelle/dist/libSdIsaWrapper.jar .

ln -s ../../../../../../../iCircles/devel/iCircles/dist/lib/iCircles.jar .
ln -s ../../../../../../../speedith/devel/Speedith/dist/Speedith.jar .
ln -s ../../../../../../../speedith/devel/Speedith/dist/lib/Speedith.Core.jar .