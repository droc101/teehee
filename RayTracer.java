import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class RayTracer {

    // Renders the scene using raycasting

    double[] depth_buffer;


    Level level;

    public RayTracer(Level level, int width) {
        this.level = level;
        depth_buffer = new double[width];
    }

    public void RenderCol(FrameBuffer buffer, Vector2 fromPos, double fromRot, int col, int screenH, Sector s) {
        RenderCol(buffer, fromPos, fromRot, col, screenH, 0.0, 0, s);
    }

    public void RenderCol(FrameBuffer buffer, Vector2 FromPos, double FromRot, int col, int screenH, double inWallDist, int rc, Sector s) {

        if (s == null) {
            s = level.sectors.get(0); // Fall back to sector 0 if out of bounds or something
        }
        double angle = Math.atan2(col - buffer.width / 2, buffer.width / 2) + FromRot;

        ArrayList<Wall> walls = new ArrayList<Wall>();
        for (Wall wall : s.walls) {
            walls.add(wall);
        }

        Ray ray = new Ray(FromPos, angle);
        Wall closestWall = null;
        double closestDist = Double.MAX_VALUE;
        for (Wall wall : walls) {
            // Check if the ray intersects the wall
            if (ray.Intersects(wall)) {
                // Get the distance to the wall
                double dist = Vector2.Distance(FromPos, ray.Intersection(wall));
                // Check if the distance is less than the closest distance
                if (dist < closestDist && dist > inWallDist) {
                    // Set the closest wall to the current wall
                    closestWall = wall;
                    // Set the closest distance to the current distance
                    closestDist = dist;
                }
            }
        }

        if (closestWall == null) {
            return;
        }

        // Get the wall's texture
        String wallTex = "texture/" + closestWall.texture + ".png";
        // Load the texture using ImageIO
        BufferedImage tex = null;
        try {
            tex = ImageIO.read(new File(wallTex));
        } catch (IOException e) {
            // Load texture/missing.png instead
            try {
                tex = ImageIO.read(new File("texture/missing.png"));
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }

        // Get the height of the texture
        int texH = tex.getHeight();
        // Get the width of the texture
        int texW = tex.getWidth();

        // Get the intersection point
        Vector2 intersection = ray.Intersection(closestWall);

        // Add this column to the depth buffer
        depth_buffer[col] = closestDist;

        // Get the length of the wall
        double wallLength = Vector2.Distance(closestWall.vertA, closestWall.vertB);

        // Get the local x position of the intersection point
        double localX = Vector2.Distance(closestWall.vertA, intersection);

        double texCol = localX / wallLength * texW;

        texCol *= (wallLength / 2);
        texCol %= texW;

        // Get the distance from the ray origin to the intersection point
        double distance = Vector2.Distance(FromPos, intersection) * Math.cos(angle - FromRot);

        // Get the height of the wall on screen
        double height = screenH / distance;

        // Get the y position of the wall
        double y = (screenH - height) / 2;

        // Calcuate the shade of the wall based on the camera angle and wall normal
        double shade = Math.abs(Math.cos((FromRot + (1.5 * Math.PI)) - closestWall.getAngle()));

        // Light should be affected by distance but not by a factor of more than half
        shade = shade * (1 - (distance / (buffer.width / 2)));

        // Make sure the shade is between 40 and 255
        shade = Math.max(0.4, Math.min(1, shade));

        // Apply fake banding to the shade
        shade = Math.floor(shade * 16) / 16;


        // Calculate the color of the wall
        int r = (int) (255 * shade);
        int g = (int) (255 * shade);
        int b = (int) (255 * shade);
        Color color = new Color(r, g, b);

        // Draw the wall using its texture and shade
        for (int i = 0; i < height; i++) {
            // Check if the Y pixel is on screen
            if (y + i < 0 || y + i >= screenH) continue;
            // Get the y position of the texture
            int texY = (int) (i / height * texH);

            // Make sure the texture Y position is between 0 and the texture height
            texY = (int) Util.wrap(texY, 0, texH - 1);
            texCol = (int)Util.wrap(texCol, 0, texW - 1);

            // Get the color of the pixel in the texture
            //System.out.println(texCol + " " + texY);
            int texColor = tex.getRGB((int) texCol, texY);
            // Get the color of the pixel in the texture
            int texR = (texColor >> 16) & 0xFF;
            int texG = (texColor >> 8) & 0xFF;
            int texB = (texColor >> 0) & 0xFF;
            // Calculate the color of the pixel on the wall
            r = (int) (texR * shade);
            g = (int) (texG * shade);
            b = (int) (texB * shade);
            color = new Color(r, g, b);

            // Draw the pixel
            buffer.setPixel(col, (int) y + i, color);
        }

        // Draw the floor
        buffer.drawFastVLine(col, (int) (y + height), (int) (screenH - (y + height)), new Color(200, 200, 200));
        buffer.drawFastVLine(col, 0, (int) y, new Color(180, 180, 180));

        // Convert the depth to a grayscale color
        int depthColor = (int) (255 * (closestDist / (buffer.width / 2)));
        // Make sure the depth color is between 0 and 255
        depthColor = (int) Util.wrap(depthColor, 0, 255);
        // Set the top 20 pixels to the depth color
        //buffer.drawFastVLine(col, 0, 20, new Color(depthColor, depthColor, depthColor));

    }

    public void RenderColPass2(FrameBuffer buffer, Vector2 FromPos, double FromRot, int col, int screenH) {
        double angle = Math.atan2(col - buffer.width / 2, buffer.width / 2) + FromRot;

        ArrayList<Wall> walls = new ArrayList<Wall>();

        for (Entity entity : level.entities) {
            walls.add(entity.makeWall());
        }

        Ray ray = new Ray(FromPos, angle);
        Wall closestWall = null;
        double closestDist = Double.MAX_VALUE;
        for (Wall wall : walls) {
            // Check if the ray intersects the wall
            if (ray.Intersects(wall)) {
                // Get the distance to the wall
                double dist = Vector2.Distance(FromPos, ray.Intersection(wall));
                if (dist < closestDist) {
                    // Set the closest wall to the current wall
                    closestWall = wall;
                    // Set the closest distance to the current distance
                    closestDist = dist;
                }
            }
        }

        if (closestWall == null) {
            return;
        }

        // Check the depth buffer
        if (depth_buffer[col] < closestDist) {
            return;
        }

        // Get the wall's texture
        String wallTex = "texture/" + closestWall.texture + ".png";
        // Load the texture using ImageIO
        BufferedImage tex = null;
        try {
            tex = ImageIO.read(new File(wallTex));
        } catch (IOException e) {
            // Load texture/missing.png instead
            try {
                tex = ImageIO.read(new File("texture/missing.png"));
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }

        // Get the height of the texture
        int texH = tex.getHeight();
        // Get the width of the texture
        int texW = tex.getWidth();

        // Get the intersection point
        Vector2 intersection = ray.Intersection(closestWall);

        // Get the length of the wall
        double wallLength = Vector2.Distance(closestWall.vertA, closestWall.vertB);

        // Get the local x position of the intersection point
        double localX = Vector2.Distance(closestWall.vertA, intersection);

        double texCol = localX / wallLength * texW;

        texCol *= (wallLength / 2);
        texCol %= texW;

        // Get the distance from the ray origin to the intersection point
        double distance = Vector2.Distance(FromPos, intersection) * Math.cos(angle - FromRot);

        // Get the height of the wall on screen
        double height = screenH / distance;

        // Get the y position of the wall
        double y = (screenH - height) / 2;

        // Calcuate the shade of the wall based on the camera angle and wall normal
        double shade = Math.abs(Math.cos((FromRot + (1.5 * Math.PI)) - closestWall.getAngle()));

        // Light should be affected by distance but not by a factor of more than half
        shade = shade * (1 - (distance / (buffer.width / 2)));

        // Make sure the shade is between 40 and 255
        shade = Math.max(0.4, Math.min(1, shade));

        // Apply fake banding to the shade
        shade = Math.floor(shade * 16) / 16;


        // Calculate the color of the wall
        int r = (int) (255 * shade);
        int g = (int) (255 * shade);
        int b = (int) (255 * shade);
        Color color = new Color(r, g, b);


        // Draw the wall using its texture and shade
        for (int i = 0; i < height; i++) {
            // Check if the Y pixel is on screen
            if (y + i < 0 || y + i >= screenH) continue;
            // Get the y position of the texture
            int texY = (int) (i / height * texH);

            // Make sure the texture Y position is between 0 and the texture height
            texY = (int) Util.wrap(texY, 0, texH - 1);
            texCol = (int)Util.wrap(texCol, 0, texW - 1);

            // Get the color of the pixel in the texture
            //System.out.println(texCol + " " + texY);
            int texColor = tex.getRGB((int) texCol, texY);
            // Get the color of the pixel in the texture
            int texR = (texColor >> 16) & 0xFF;
            int texG = (texColor >> 8) & 0xFF;
            int texB = (texColor >> 0) & 0xFF;
            int texA = (texColor >> 24) & 0xFF;
            // Calculate the color of the pixel on the wall
            r = (int) (texR * shade);
            g = (int) (texG * shade);
            b = (int) (texB * shade);
            color = new Color(r, g, b);

            if (texA == 0) {
                continue;
            }

            // Draw the pixel
            buffer.setPixel(col, (int) y + i, color);
        }

        // Draw the floor
        buffer.drawFastVLine(col, (int) (y + height), (int) (screenH - (y + height)), new Color(200, 200, 200));
        buffer.drawFastVLine(col, 0, (int) y, new Color(180, 180, 180));

        // Convert the depth to a grayscale color
        int depthColor = (int) (255 * (closestDist / (buffer.width / 2)));
        // Make sure the depth color is between 0 and 255
        depthColor = (int) Util.wrap(depthColor, 0, 255);
        // Set the top 20 pixels to the depth color
        //buffer.drawFastVLine(col, 0, 20, new Color(depthColor, depthColor, depthColor));

    }
}
