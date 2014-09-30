JC = javac
JAVA = java
.SUFFIXES: .java .class
.java.class:
	$(JC) $*.java

TRAINFILE = training_set.csv
TESTFILE = test-all.csv
VALIDATIONFILE = validation.csv
OUTPUTFILE = predictions.txt


CLASSES = \
	Recommender.java

classes: $(CLASSES:.java=.class)

default: classes

test: classes
	$(JAVA) Recommender $(TRAINFILE) $(TESTFILE) $(OUTPUTFILE)

validation: classes
	$(JAVA) Recommender $(TRAINFILE) $(VALIDATIONFILE) $(OUTPUTFILE)

clean: 
	$(RM) *.class
