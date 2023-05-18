#include <stdlib.h>
#include <string.h>
#include "Structures.h"
#include <math.h>
#include <unistd.h>

char *getWD() {
    char *wd = malloc(1024);
    getcwd(wd, 1024);
    return wd;
}

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

double WallAngle(Wall wall) {
    return atan2(wall.vertB.y - wall.vertA.y, wall.vertB.x - wall.vertA.x);
}

double wrap(double n, double min, double max)
{
    double d = max - min;
    return n == max ? n : fmod(n - min, d) + min;
}

Vector2 RayCast(Vector2 origin, double FromRot, Vector2 vertA, Vector2 vertB) {
    Vector2 wallVector = vec2(vertB.x - vertA.x, vertB.y - vertA.y);
    Vector2 rayVector = vec2(cos(FromRot), sin(FromRot));

    double denominator = (rayVector.x * wallVector.y) - (rayVector.y * wallVector.x);
    if (denominator == 0)
    {
        // Construct a vector2 with NaN values
        Vector2 result;
        result.x = NAN;
        result.y = NAN;
        // Return it
        return result;
    }

    double t = ((vertA.x - origin.x) * wallVector.y - (vertA.y - origin.y) * wallVector.x) / denominator;
    double u = ((vertA.x - origin.x) * rayVector.y - (vertA.y - origin.y) * rayVector.x) / denominator;

    if (t >= 0 && u >= 0 && u <= 1)
    {
        double intersectionX = origin.x + t * rayVector.x;
        double intersectionY = origin.y + t * rayVector.y;
        // Convert it to a Vector2 and return it
        return vec2(intersectionX, intersectionY);
    }

    // Return a NaN vector2
    Vector2 result;
    result.x = NAN;
    result.y = NAN;
    return result;
}

char RayIntersect(Vector2 origin, double rot, Wall wall) {
    return RayCast(origin, rot, wall.vertA, wall.vertB).x != NAN;
}

Vector2 RayIntersection(Vector2 origin, double rot, Wall wall) {
    return RayCast(origin, rot, wall.vertA, wall.vertB);
}