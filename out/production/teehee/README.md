# teehee
This is my AP CSA final project. It's an old school FPS written in (mostly) java.

### File Structure
/ = Main java code
/native = Native helper library (C)
/texture = All textures and sprites
/levels = Level files

### Compiling
Tested on Linux x64
run `make native` to compile the native library
run `make java` to compile the java classes

Chances are you'll need to edit the makefile in the native folder to point to your JDK header locations.

### Windows?
You'll need to edit the library loader in Util.java and the makefile to output to a DLL. That might work, but I'm not testing it.