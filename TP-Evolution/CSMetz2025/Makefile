
sources := $(wildcard src/fr/csmetz/*.java)
objects := $(patsubst %.java,%.class,$(wildcard src/fr/csmetz/*.java))

jar: fr/csmetz/CSMetz.class
	@echo $(objects)
	jar cvf csmetz.jar fr

fr/csmetz/BinaryToBCD.class: src/fr/csmetz/BinaryToBCD.java
	javac -cp logisim-evolution-3.9.0-all.jar:. -d . $<

fr/csmetz/GraphicCard.class: src/fr/csmetz/GraphicCard.java
	javac -cp logisim-evolution-3.9.0-all.jar:. -d . $<

fr/csmetz/CSMetz.class: src/fr/csmetz/CSMetz.java fr/csmetz/UAL.class fr/csmetz/BinaryToBCD.class fr/csmetz/GraphicCard.class
	javac -cp logisim-evolution-3.9.0-all.jar:. -d . $<
