#include <stdlib.h>
#include "Structures.h"
#include <math.h>

#define PI 3.14159265358979323846 // Pie is yummy

Vector2 vec2(double x, double y) // Create a Vector2
{
    Vector2 v;
    v.x = x;
    v.y = y;
    return v;
}

double min(double a, double b) // Get the minimum of two numbers
{
    return a < b ? a : b;
}

double max(double a, double b) // Get the maximum of two numbers
{
    return a > b ? a : b;
}

double Vector2Distance(Vector2 a, Vector2 b) // Get the distance between two Vector2s
{
    double dx = a.x - b.x;
    double dy = a.y - b.y;
    return sqrt(dx * dx + dy * dy);
}

double wrap(double n, double min, double max) // Wrap a number between two numbers
{
    double d = max - min;
    return n == max ? n : fmod(n - min, d) + min;
}
