# Some common things:
SCALA_HOME  =~/bin/scala-2.9.2
SCALAC = $(SCALA_HOME)/bin/scalac
SCALA = $(SCALA_HOME)/bin/scala
SCALAC_FLAGS=
SCALAC_OPTS=
SCALA_CLASSPATH=
CLASSES_DEST_DIR=classes
SRC_DIR=src
DIST_DIR=dist
CLASSES=
MANIFEST_FILE=META-INF/MANIFEST.MF

libPure =$$HOME/bin/Isabelle2012/lib/classes/ext/Pure.jar

$(CLASSES_DEST_DIR)/%.class: $(SRC_DIR)/%.scala
	$(SCALAC) -d $(CLASSES_DEST_DIR) $(SCALAC_FLAGS) $(SCALAC_OPTS) -classpath $(CLASSES_DEST_DIR):$(SCALA_CLASSPATH) $<

.PHONY : mkClassDir cleanClasses mkJar run cleanJar clean-all
mkClassDir:
	@mkdir -p $(CLASSES_DEST_DIR)

clean-all: cleanClasses cleanJar

cleanClasses:
	$(RM) -R $(CLASSES_DEST_DIR)

cleanJar:
	$(RM) -R $(DIST_DIR)

mkJar:
	@mkdir -p $(DIST_DIR)
	jar -cfm $(DIST_DIR)/$(JAR_NAME) $(MANIFEST_FILE) -C $(CLASSES_DEST_DIR) "."

run: build
	$(SCALA) -classpath $(SCALA_CLASSPATH) $(DIST_DIR)/$(JAR_NAME)