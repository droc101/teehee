# teehee
This is my AP CSA final project. It's an old school FPS written in (mostly) java.
This branch is for improvements I am making after the project was due. The original project is on the 'main' branch.

Uses Java 22.

### File Structure
/ = Main java code
/native = Native helper library (C)
/texture = All textures and sprites
/levels = Level files

### Compiling
Tested on Linux x64 and Windows 11
run `make native` to compile the native library
run `make java` to compile the java classes
NOTE: To compile for windows, you'll need to cd into native and run `make -f Makefile.WIN64` (still compiling on linux tho)

Chances are you'll need to edit the makefile in the native folder to point to your JDK header locations.

### Windows?
Yes, but good luck compiling it.