include ../ScalaProjects.mk

# Specific things:
SOURCES= \
	diabelli/isabelle/pure/lib/TermUtils.scala \
	diabelli/isabelle/pure/lib/TermYXML.scala
CLASSES=$(SOURCES:%.scala=$(CLASSES_DEST_DIR)/%.class)

JAR_NAME=libIsaWrapper.jar

SCALA_CLASSPATH=$(libPure)


.PHONY : all build clean
all: build

clean: cleanClasses

build: mkClassDir $(CLASSES) mkJar