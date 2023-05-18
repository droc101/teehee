#include <jni.h>

JNIEXPORT void JNICALL Java_Util_test(JNIEnv *, jclass) {
    printf("Hello from C!\n");
}