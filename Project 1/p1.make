JC = javac
.SUFFIXES: .java .class
.java.class:
        $(JC) $*.java

CLASSES = \
        Action.java \
        Outcome.java \
        State.java \
        Game.java \
        Connect.java

MAIN = Connect

default: classes

classes: $(CLASSES:.java=.class)

clean:
        $(RM) *.class