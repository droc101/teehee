#include <stdlib.h>
#include <string.h>
#include <jni.h>
#include "Structures.h"
#include <math.h>
#include <libpng16/png.h>
#include "Util.h"

#define uint unsigned int
#define BUFFER_WIDTH 320
#define PI 3.14159265358979323846

Texture loadTexture(char **path)
{
    // Path should begin with the working directory
    path = *getWD() + path;
    // Open a PNG file
    FILE *fp = fopen(*path, "rb");
    // If the file doesn't exist, return an empty texture
    if (!fp)
    {
        return loadTexture("textures/missing.png");
    }

    // Read the PNG file
    png_structp png = png_create_read_struct(PNG_LIBPNG_VER_STRING, NULL, NULL, NULL);

    // If the PNG file is invalid, return an empty texture
    if (!png)
    {
        return loadTexture("textures/missing.png");
    }

    // Create a PNG info struct
    png_infop info = png_create_info_struct(png);

    // If the PNG info struct is invalid, return an empty texture
    if (!info)
    {
        return loadTexture("textures/missing.png");
    }

    // If the PNG file is invalid, return an empty texture
    if (setjmp(png_jmpbuf(png)))
    {
        return loadTexture("textures/missing.png");
    }

    // Tell libpng that we've already read the first 8 bytes
    png_init_io(png, fp);

    // Load the pixel data from the PNG file
    png_read_png(png, info, PNG_TRANSFORM_STRIP_16 | PNG_TRANSFORM_PACKING | PNG_TRANSFORM_EXPAND, NULL);

    // Get the width and height of the PNG file
    uint width = png_get_image_width(png, info);
    uint height = png_get_image_height(png, info);

    // Get the pixel data from the PNG file
    png_bytep *rowPointers = png_get_rows(png, info);

    // Create a pixel array
    uint *pixels = malloc(width * height * sizeof(uint));

    // Loop through the pixel data
    for (uint y = 0; y < height; y++)
    {
        // Get the current row
        png_bytep row = rowPointers[y];
        // Loop through the row
        for (uint x = 0; x < width; x++)
        {
            // Get the current pixel
            png_bytep px = &(row[x * 4]);
            // Get the current pixel's color
            uint color = (px[0] << 24) | (px[1] << 16) | (px[2] << 8) | px[3];
            // Set the current pixel in the pixel array
            pixels[y * width + x] = color;
        }
    }

    // Close the PNG file
    fclose(fp);

    // Create a texture
    Texture t;
    t.width = width;
    t.height = height;
    t.pixels = pixels;
    return t;
}

// Yeah yeah this header sucks, blame JNI
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

    Vector2 cast = RayCast(origin, direction, vertA, vertB);

    // Convert the C Vector2 to a Java double array
    jdoubleArray result = (*env)->NewDoubleArray(env, 2);
    jdouble *resultArray = (*env)->GetDoubleArrayElements(env, result, NULL);
    resultArray[0] = cast.x;
    resultArray[1] = cast.y;
    
    // Return the Java double array
    return result;
}

Wall *GetWallsFromJni(jobject sector, JNIEnv *env) {
        // Convert the java sector to a C sector
    jclass sectorClass = (*env)->GetObjectClass(env, sector);
    // Read "ArrayList<Wall> walls" from the sector
    jfieldID wallsField = (*env)->GetFieldID(env, sectorClass, "walls", "Ljava/util/ArrayList;");
    // Convert the java ArrayList into a C array and length
    jobject walls = (*env)->GetObjectField(env, sector, wallsField);
    jclass wallsClass = (*env)->GetObjectClass(env, walls);
    jmethodID wallsToArray = (*env)->GetMethodID(env, wallsClass, "toArray", "()[Ljava/lang/Object;");
    jobjectArray wallsArray = (*env)->CallObjectMethod(env, walls, wallsToArray);
    jsize wallsLength = (*env)->GetArrayLength(env, wallsArray);
    // Convert the java ArrayList into a C array and length
    Wall *wallsC = malloc(wallsLength * sizeof(Wall));
    // Loop through the java ArrayList and convert to a C Wall
    for (int i = 0; i < wallsLength; i++) {
        // Get the current java Wall
        jobject jwall = (*env)->GetObjectArrayElement(env, wallsArray, i);
        Wall wall;
        // Read Vector2 vertA from the java Wall
        jclass wallClass = (*env)->GetObjectClass(env, jwall);
        jfieldID vertAField = (*env)->GetFieldID(env, wallClass, "vertA", "LVector2;");
        jobject vertA = (*env)->GetObjectField(env, jwall, vertAField);
        jclass vertAClass = (*env)->GetObjectClass(env, vertA);
        jfieldID vertAXField = (*env)->GetFieldID(env, vertAClass, "x", "D");
        jfieldID vertAYField = (*env)->GetFieldID(env, vertAClass, "y", "D");
        wall.vertA = vec2((*env)->GetDoubleField(env, vertA, vertAXField), (*env)->GetDoubleField(env, vertA, vertAYField));
        // Read Vector2 vertB from the java Wall
        jfieldID vertBField = (*env)->GetFieldID(env, wallClass, "vertB", "LVector2;");
        jobject vertB = (*env)->GetObjectField(env, jwall, vertBField);
        jclass vertBClass = (*env)->GetObjectClass(env, vertB);
        jfieldID vertBXField = (*env)->GetFieldID(env, vertBClass, "x", "D");
        jfieldID vertBYField = (*env)->GetFieldID(env, vertBClass, "y", "D");
        wall.vertB = vec2((*env)->GetDoubleField(env, vertB, vertBXField), (*env)->GetDoubleField(env, vertB, vertBYField));
        // Read String texture from the java Wall
        jfieldID textureField = (*env)->GetFieldID(env, wallClass, "texture", "Ljava/lang/String;");
        jstring texture = (*env)->GetObjectField(env, jwall, textureField);
        // Convert the java String to a C string (char *)
        const char *textureC = (*env)->GetStringUTFChars(env, texture, NULL);
        // Copy the C string to the C Wall
        strncpy(&wall.texture, textureC, 256);
        // Add the C Wall to the C array
        wallsC[i] = wall;
    }
    return wallsC;
}

int getWallCountFromJni(jobject sector, JNIEnv *env) {
        // Convert the java sector to a C sector
    jclass sectorClass = (*env)->GetObjectClass(env, sector);
    // Read "ArrayList<Wall> walls" from the sector
    jfieldID wallsField = (*env)->GetFieldID(env, sectorClass, "walls", "Ljava/util/ArrayList;");
    // Convert the java ArrayList into a C array and length
    jobject walls = (*env)->GetObjectField(env, sector, wallsField);
    jclass wallsClass = (*env)->GetObjectClass(env, walls);
    jmethodID wallsToArray = (*env)->GetMethodID(env, wallsClass, "toArray", "()[Ljava/lang/Object;");
    jobjectArray wallsArray = (*env)->CallObjectMethod(env, walls, wallsToArray);
    jsize wallsLength = (*env)->GetArrayLength(env, wallsArray);
    return wallsLength;
}

JNIEXPORT jintArray JNICALL Java_Native_render_1col(JNIEnv *env, jclass, jdoubleArray fromPos, jdouble fromRot, jint col, jint screenH, jdouble minWallDist, jint rc, jobject sector) {
    Wall *walls = GetWallsFromJni(sector, env);
    int wallCount = getWallCountFromJni(sector, env);
    
    // Convert fromPos to a C Vector2
    Vector2 FromPos = vec2((*env)->GetDoubleArrayElements(env, fromPos, NULL)[0], (*env)->GetDoubleArrayElements(env, fromPos, NULL)[1]);

    // Construct a Java int array
    jintArray resulta;
    // Make it screenH elements long
    resulta = (*env)->NewIntArray(env, screenH);
    // Get the array's elements
    jint *result = (*env)->GetIntArrayElements(env, resulta, NULL);

    // Load the wall's texture
    Texture texture = loadTexture(walls[col].texture);

        // Get the angle of the column
        // FOV is 90 degrees
    double angle = atan2(col - BUFFER_WIDTH / 2, BUFFER_WIDTH / 2) + fromRot;
        // Loop over each wall until a wall is hit

        // Look over each wall and get the closest one (accounting for the minimum distance)
        Wall closestWall;
        double closestDist = 999999;
        for (int wi = 0; wi < wallCount; wi++) {
            // Check if the ray intersects the wall
            if (RayIntersect(FromPos, angle, walls[wi])) {
                // Get the distance to the wall
                double dist = Vector2Distance(FromPos, RayIntersection(FromPos, fromRot, walls[wi]));
                // Check if the distance is less than the closest distance
                if (dist < closestDist && dist > minWallDist) {
                    // Set the closest wall to the current wall
                    closestWall = walls[wi];
                    // Set the closest distance to the current distance
                    closestDist = dist;
                }
            }
        }


        // Get the height of the texture
        int texH = texture.height;
        // Get the width of the texture
        int texW = texture.width;

        // Get the intersection point
        Vector2 intersection = RayIntersection(FromPos, angle, closestWall);

        // Get the length of the wall
        double wallLength = Vector2Distance(closestWall.vertA, closestWall.vertB);

        // Get the local x position of the intersection point
        double localX = Vector2Distance(closestWall.vertA, intersection);

        double texCol = localX / wallLength * texW;

        texCol *= (wallLength / ((1 - 0)*2));

        // texCol = 0;

        // Get the distance from the ray origin to the intersection point
        double distance = Vector2Distance(FromPos, intersection) * cos(angle - fromRot);

        // Get the height of the wall on screen
        double height = screenH / distance;

        // Get the y position of the wall
        double y = (screenH - height) / 2;

        // Calcuate the shade of the wall based on the camera angle and wall normal
        double shade = abs(cos((fromRot + (1.5 * PI)) - WallAngle(closestWall)));

        // Light should be affected by distance but not by a factor of more than half
        shade = shade * (1 - (distance / (BUFFER_WIDTH / 2)));

        // Multiply by the sector's light level
        // shade *= 1.0;

        // Make sure the shade is between 40 and 255
        shade = max(0.4, min(1, shade));

        // Apply fake banding to the shade
        shade = floor(shade * 16) / 16;


        // Calculate the color of the wall
        int r = (int) (255 * shade);
        int g = (int) (255 * shade);
        int b = (int) (255 * shade);
        uint color = (r << 16) | (g << 8) | b;

        // Backup the original y position and height
        double origY = y;
        double origHeight = height;

        

        // Draw the wall using its texture and shade
        for (int i = 0; i < height; i++) {
            // Check if the Y pixel is on screen
            if (y + i < 0 || y + i >= screenH) continue;
            // Get the y position of the texture
            int texY = (int) (i / height * texH);

            // Make sure the texture Y position is between 0 and the texture height
            texY = (int) wrap(texY, 0, texH - 1);
            texCol = (int)wrap(texCol, 0, texW - 1);

            // Get the color of the pixel in the texture
            int texColor = texture.pixels[(texture.width * (int)texY) + (int)texCol];
            // Get the color of the pixel in the texture
            int texR = (texColor >> 16) & 0xFF;
            int texG = (texColor >> 8) & 0xFF;
            int texB = (texColor >> 0) & 0xFF;
            // Calculate the color of the pixel on the wall
            r = (int) (texR * shade);
            g = (int) (texG * shade);
            b = (int) (texB * shade);
            color = (r << 16) | (g << 8) | b;

            // Draw the pixel into the outout jIntArray
            result[(int) (y + i)] = color;
        }

        


        // Get the floor color (placeholder lime green)
        r = 200;
        g = 200;
        b = 200;

        // Calculate the floor color
        color = (r << 16) | (g << 8) | b;

        // Draw the floor
        for (int i = 0; i < screenH - (y + height); i++) {

            // Check if the Y pixel is on screen
            if (y + height + i < 0 || y + height + i >= screenH) continue;
            // Draw the pixel
            //buffer.setPixel(col, (int) (y + height + i), color);
            result[(int) (y + height + i)] = color;
        }
        
        for (int i = 0; i < screenH - (y + height); i++) {
            // Check if the Y pixel is on screen
            if (y + height + i < 0 || y + height + i >= screenH) continue;
            // Draw the pixel
            result[(int) (y + height + i)] = 0x00FF00;
        }


    // Return it
    return resulta;
}