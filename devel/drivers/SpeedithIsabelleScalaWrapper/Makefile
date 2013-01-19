include ../ScalaProjects.mk

# Specific things:
SOURCES= \
	speedith/mixr/isabelle/NormalForms.scala \
	speedith/mixr/isabelle/Translations.scala
CLASSES=$(SOURCES:%.scala=$(CLASSES_DEST_DIR)/%.class)

JAR_NAME=libSdIsaWrapper.jar

SCALA_CLASSPATH=$(libPure):../IsabelleScalaWrapper/dist/libIsaWrapper.jar:../../../../speedith/devel/Speedith.Core/dist/Speedith.Core.jar:../../../../speedith/devel/Speedith.Core/dist/lib/antlr3-runtime.jar:../../../../speedith/devel/Speedith.Core/dist/lib/Propity.jar


.PHONY : all build clean
all: build

clean: clean-all

build: mkClassDir $(CLASSES) mkJar
