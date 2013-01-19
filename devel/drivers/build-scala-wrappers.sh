#!/bin/bash

SCALA_HOME=~/bin/scala-2.9.2

if [[ ! -d "$SCALA_HOME" ]]; then
    echo "Download Scala 2.9 from: http://www.scala-lang.org/downloads/distrib/files/scala-2.9.2.tgz";
    exit 1;
fi

PATH="$PATH:$SCALA_HOME/bin"

libPure=~/bin/Isabelle2012/lib/classes/ext/Pure.jar



################################################################################
## Isabelle Wrapper
##
isaWrapper_sources=(./IsabelleScalaWrapper/src/mixr/isabelle/pure/lib/TermUtils.scala \
    ./IsabelleScalaWrapper/src/mixr/isabelle/pure/lib/TermYXML.scala)
isaWrapper_classDir=./IsabelleScalaWrapper/classes
isaWrapper_distDir=./IsabelleScalaWrapper/dist
isaWrapper_jar=libIsaWrapper.jar
isaWrapper_manifest=./IsabelleScalaWrapper/META-INF/MANIFEST.MF
isaWrapper_libs="$libPure"

echo "Building '$isaWrapper_distDir/$isaWrapper_jar'..."

mkdir -p "$isaWrapper_classDir"
mkdir -p "$isaWrapper_distDir"
scalac -deprecation -d "$isaWrapper_classDir" -classpath "$isaWrapper_libs" "${isaWrapper_sources[@]}"
jar -cfm "$isaWrapper_distDir/$isaWrapper_jar" "$isaWrapper_manifest" -C "$isaWrapper_classDir" "."

echo "Building done."
echo "Can be run with this command:";
cat <<EOF

    scala -classpath "$isaWrapper_libs" "$isaWrapper_distDir/$isaWrapper_jar"

EOF



################################################################################
## Speedith-to-Isabelle Wrapper
##

sdIsaWrapper_sources=(./SpeedithIsabelle/src/speedith/mixr/isabelle/Translations.scala \
    ./SpeedithIsabelle/src/speedith/mixr/isabelle/NormalForms.scala)
sdIsaWrapper_classDir=./SpeedithIsabelle/classes
sdIsaWrapper_distDir=./SpeedithIsabelle/dist
sdIsaWrapper_jar=libSdIsaWrapper.jar
sdIsaWrapper_manifest=./SpeedithIsabelle/META-INF/MANIFEST.MF
sdIsaWrapper_libs="$libPure:$isaWrapper_distDir/$isaWrapper_jar:../../../speedith/devel/Speedith/dist/lib/Speedith.Core.jar:../../../speedith/devel/Speedith/dist/lib/antlr3-runtime.jar"

echo "Building '$sdIsaWrapper_distDir/$sdIsaWrapper_jar'..."

mkdir -p "$sdIsaWrapper_classDir"
mkdir -p "$sdIsaWrapper_distDir"
scalac -deprecation -d "$sdIsaWrapper_classDir" -classpath "$sdIsaWrapper_libs" "${sdIsaWrapper_sources[@]}"
jar -cfm "$sdIsaWrapper_distDir/$sdIsaWrapper_jar" "$sdIsaWrapper_manifest" -C "$sdIsaWrapper_classDir" "."

echo "Building done."
echo "Can be run with this command:";
cat <<EOF

    scala -classpath "$sdIsaWrapper_libs" "$sdIsaWrapper_distDir/$sdIsaWrapper_jar"
EOF