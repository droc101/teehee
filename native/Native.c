#include <stdlib.h> // C Standard Library
#include <jni.h> // Java JNI Library
#include "Structures.h" // Structures (Vector2)
#include "Util.h" // Utility functions

// Yeah yeah this header sucks, blame JNI
JNIEXPORT jdoubleArray JNICALL Java_Native_RayCast(JNIEnv *env, jclass, jdouble x1, jdouble y1, jdouble x2, jdouble y2, jdouble fx, jdouble fy, jdouble fr)
{
    double dx1 = (double)x1;
    double dy1 = (double)y1;
    double dx2 = (double)x2;
    double dy2 = (double)y2;
    double dfx = (double)fx;
    double dfy = (double)fy;
    double direction = (double)fr;
    Vector2 vertA = vec2(dx1, dy1);
    Vector2 vertB = vec2(dx2, dy2);
    Vector2 origin = vec2(dfx, dfy);
    Vector2 wallVector = vec2(vertB.x - vertA.x, vertB.y - vertA.y);
    Vector2 rayVector = vec2(cos(direction), sin(direction));
    double denominator = (rayVector.x * wallVector.y) - (rayVector.y * wallVector.x);
    if (denominator == 0)
    {
        jintArray result;
        result = (*env)->NewDoubleArray(env, 0);
        return result;
    }
    double t = ((vertA.x - origin.x) * wallVector.y - (vertA.y - origin.y) * wallVector.x) / denominator;
    double u = ((vertA.x - origin.x) * rayVector.y - (vertA.y - origin.y) * rayVector.x) / denominator;
    if (t >= 0 && u >= 0 && u <= 1)
    {
        double intersectionX = origin.x + t * rayVector.x;
        double intersectionY = origin.y + t * rayVector.y;
        jintArray result;
        result = (*env)->NewDoubleArray(env, 2);
        jdouble elements[2] = {(jdouble)intersectionX, (jdouble)intersectionY};
        (*env)->SetDoubleArrayRegion(env, result, 0, 2, elements);
        return result;
    }
    jintArray result;
    result = (*env)->NewDoubleArray(env, 0);
    return result;
}
