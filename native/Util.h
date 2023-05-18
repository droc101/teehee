#ifndef UTIL_H
#define UTIL_H

#include <stdlib.h>
#include <string.h>
#include "Structures.h"
#include <math.h>

char *getWD();

Vector2 vec2(double x, double y);

double min(double a, double b);

double max(double a, double b);

double Vector2Distance(Vector2 a, Vector2 b);

double WallAngle(Wall wall);

double wrap(double n, double min, double max);

Vector2 RayCast(Vector2 origin, double FromRot, Vector2 vertA, Vector2 vertB);

char RayIntersect(Vector2 origin, double rot, Wall wall);

Vector2 RayIntersection(Vector2 origin, double rot, Wall wall);

#endif