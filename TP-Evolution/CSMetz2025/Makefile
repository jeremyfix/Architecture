
sources := $(wildcard src/fr/csmetz/*.java)
objects := $(patsubst %.java,%.class,$(wildcard src/fr/csmetz/*.java))

jar: $(objects)
	@echo $(objects)
	jar cvf csmetz.jar fr

%.class: %.java
	javac -cp logisim-evolution-3.9.0-all.jar:. -d . $<
