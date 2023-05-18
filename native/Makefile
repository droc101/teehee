# Make all C and H files in this directory into a shared library named "libnative.so"
# This is the library that will be loaded by the Java code.
# The library will be placed in the "lib" directory of the project.

# The name of the library to create
LIBRARY = libnative.so

# The C files to compile
C_FILES = $(wildcard *.c)

# The H files to include
H_FILES = $(wildcard *.h)

# The object files to create
O_FILES = $(C_FILES:.c=.o)

# The compiler to use
CC = gcc

# The flags to pass to the compiler
CFLAGS = -Wall -fpic -I$(JAVA_HOME)/include -I$(JAVA_HOME)/include/linux -I/usr/lib/jvm/java-20-jdk/include -I/usr/lib/jvm/java-20-jdk/include/linux

# The linker to use
LD = gcc

# The flags to pass to the linker
LDFLAGS = -shared

# Compile the C files into object files
%.o: %.c $(H_FILES)
	$(CC) $(CFLAGS) -c $< -o $@

# Link the object files into a shared library
$(LIBRARY): $(O_FILES)
	$(LD) $(LDFLAGS) -o $@ $^

# Remove the object files
clean:
	rm -f $(O_FILES)

# Remove the shared library
cleanall: clean
	rm -f $(LIBRARY)