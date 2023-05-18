#include <jni.h>
#include "Structures.h"
#include <math.h>

Vector2 vec2(double x, double y)
{
    Vector2 v;
    v.x = x;
    v.y = y;
    return v;
}

JNIEXPORT jdoubleArray JNICALL Java_Native_RayCast(JNIEnv *env, jclass, jdouble x1, jdouble y1, jdouble x2, jdouble y2, jdouble fx, jdouble fy, jdouble fr)
{
    // Convert the Java doubles to C doubles
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
        // Construct a Java int array
        jintArray result;
        // Make it empty
        result = (*env)->NewDoubleArray(env, 0);
        // Return it
        return result;
    }

    double t = ((vertA.x - origin.x) * wallVector.y - (vertA.y - origin.y) * wallVector.x) / denominator;
    double u = ((vertA.x - origin.x) * rayVector.y - (vertA.y - origin.y) * rayVector.x) / denominator;

    if (t >= 0 && u >= 0 && u <= 1)
    {
        double intersectionX = origin.x + t * rayVector.x;
        double intersectionY = origin.y + t * rayVector.y;
        // Construct a Java int array
        jintArray result;
        // Make it have 2 elements
        result = (*env)->NewDoubleArray(env, 2);
        // Convert the C doubles to Java ints
        jdouble elements[2] = {(jdouble)intersectionX, (jdouble)intersectionY};
        // Set the Java int array's elements to the C ints
        (*env)->SetDoubleArrayRegion(env, result, 0, 2, elements);
        // Return it
        return result;
    }

    // Construct a Java int array
    jintArray result;
    // Make it empty
    result = (*env)->NewDoubleArray(env, 0);
    // Return it
    return result;
}