LIBRARY = libnative.so # Output file
C_FILES = $(wildcard *.c) # Input C files
H_FILES = $(wildcard *.h) # Input H files
O_FILES = $(C_FILES:.c=.o) # Output O files
CC = gcc # Compiler
CFLAGS = -Winline -Os -fdata-sections -ffunction-sections -fno-stack-protector -Wl,-z,max-page-size=0x1000 -s -nostdlib -fpic -I$(JAVA_HOME)/include -I$(JAVA_HOME)/include/linux -I/usr/lib/jvm/java-20-jdk/include -I/usr/lib/jvm/java-20-jdk/include/linux # Compiler flags
LD = gcc # Linker
LDFLAGS = -shared -Wl,--gc-sections -Wl,-z,max-page-size=0x1000 -Wl,-z,norelro # Linker flags
%.o: %.c $(H_FILES) # Compile C files
	$(CC) $(CFLAGS) -c $< -o $@
$(LIBRARY): $(O_FILES) # Link O files
	$(LD) $(LDFLAGS) -o $@ $^
clean:
	rm -f $(O_FILES) # Remove O files
cleanall: clean
	rm -f $(LIBRARY) # Remove output file