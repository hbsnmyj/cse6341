JAVAC = javac
JAVAFLAGS = -g

.SUFFIXES: .java .class

.java.class:
	$(JAVAC) $(JAVAFLAGS) $*.java

CLASSES = \
					$(wildcard scanner/*.java)\
					$(wildcard ast/*.java)\
					$(wildcard parser/*.java)

default: classes

classes: $(CLASSES:.java=.class)

clean:
	$(RM) scanner/*.class
	$(RM) ast/*.class
	$(RM) parser/*.class
