JC = javac
.SUFFIXES: .java .class
.java.class:
        $(JC) $*.java

CLASSES = \
        Game.java \
        State.java \
        Action.java 

MAIN = Game

default: classes

classes: $(CLASSES:.java=.class)

clean:
        $(RM) *.class