PROJECTS=IsabelleScalaWrapper SpeedithIsabelle

.PHONY : all clean test-all
all:
	@for d in $(PROJECTS); do (cd $$d; $(MAKE) $@); done

clean:
	@for d in $(PROJECTS); do (cd $$d; $(MAKE) $@); done

run-all:
	@for d in $(PROJECTS); do (cd $$d; $(MAKE) run); done

spisa-run:
	@(cd SpeedithIsabelle; $(MAKE) run);