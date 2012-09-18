#!/bin/bash

extRefsDir=release/modules/ext

mkdir -p "$extRefsDir" || exit 1;

cd "$extRefsDir"

ln -s ../../../../Propity/dist/Propity.jar .