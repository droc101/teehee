#ifndef STRUCTURES_H
#define STRUCTURES_H

#define uint unsigned int

typedef struct {
    double x;
    double y;
} Vector2;

typedef struct {
    Vector2 vertA;
    Vector2 vertB;
    char *texture[256];
} Wall;

typedef struct {
    Wall *walls;
    int wallCount;
} Level;

typedef struct {
    uint width;
    uint height;
    uint *pixels;
} Texture;

#endif