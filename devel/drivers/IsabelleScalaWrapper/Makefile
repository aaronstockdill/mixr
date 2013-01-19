include ../ScalaProjects.mk

# Specific things:
SOURCES= \
	mixr/isabelle/pure/lib/TermYXML.scala \
	mixr/isabelle/pure/lib/TermUtils.scala
CLASSES=$(SOURCES:%.scala=$(CLASSES_DEST_DIR)/%.class)

JAR_NAME=libIsaWrapper.jar

SCALA_CLASSPATH=$(libPure)


.PHONY : all build clean
all: build

clean: clean-all

build: mkClassDir $(CLASSES) mkJar