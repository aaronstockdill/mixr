include ../ScalaProjects.mk

# Specific things:
SOURCES= \
	speedith/diabelli/isabelle/NormalForms.scala \
	speedith/diabelli/isabelle/Translations.scala
CLASSES=$(SOURCES:%.scala=$(CLASSES_DEST_DIR)/%.class)

JAR_NAME=libSdIsaWrapper.jar

SCALA_CLASSPATH=$(libPure):../IsabelleScalaWrapper/dist/libIsaWrapper.jar:../../../../speedith/devel/Speedith/dist/lib/Speedith.Core.jar:../../../../speedith/devel/Speedith/dist/lib/antlr3-runtime.jar


.PHONY : all build clean
all: build

clean: cleanClasses

build: mkClassDir $(CLASSES) mkJar