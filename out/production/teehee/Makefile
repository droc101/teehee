LOGFILES = $(wildcard *.log)
CLASSFILES = $(wildcard *.class)
SRCFILES = $(wildcard *.java)

clean:
	rm -f $(LOGFILES)
	rm -f $(CLASSFILES)

cleanall: clean

headers:
	javac -h native/ Native.java
	rm -f Native.class

native: headers
	make -C native

java:
	javac $(SRCFILES)

all: native

run: native java
	java main