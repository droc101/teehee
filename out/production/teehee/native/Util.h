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

double wrap(double n, double min, double max);

#endif