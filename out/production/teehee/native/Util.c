#include <stdlib.h>
#include <string.h>
#include "Structures.h"
#include <math.h>
#include <unistd.h>

#define PI 3.14159265358979323846

Vector2 vec2(double x, double y)
{
    Vector2 v;
    v.x = x;
    v.y = y;
    return v;
}

double min(double a, double b)
{
    return a < b ? a : b;
}

double max(double a, double b)
{
    return a > b ? a : b;
}

double Vector2Distance(Vector2 a, Vector2 b)
{
    double dx = a.x - b.x;
    double dy = a.y - b.y;
    return sqrt(dx * dx + dy * dy);
}

double wrap(double n, double min, double max)
{
    double d = max - min;
    return n == max ? n : fmod(n - min, d) + min;
}
