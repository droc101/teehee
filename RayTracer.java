import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class RayTracer {
    Level level;

    public RayTracer(Level level) {
        this.level = level;
    }

    public void RenderCol(FrameBuffer buffer, Vector2 fromPos, double fromRot, int col, int screenH, Sector s) {
        RenderCol(buffer, fromPos, fromRot, col, screenH, 0.0, 0, s);
    }

    public void RenderCol(FrameBuffer buffer, Vector2 FromPos, double FromRot, int col, int screenH, double inWallDist, int rc, Sector s) {

        if (s == null) {
            System.err.println("Sector is null! Defaulting to 0");
            s = level.sectors.get(0); // Fall back to sector 0 if out of bounds or something
        }

        // Get the angle of the column
        // FOV is 90 degrees
        double angle = Math.atan2(col - buffer.width / 2, buffer.width / 2) + FromRot;

        ArrayList<Wall> walls = new ArrayList<Wall>();
        // Copy level.walls to walls
        for (Wall wall : s.walls) {
            walls.add(wall);
        }
        // Loop over each entity
        for (Entity entity : level.entities) {
            // Check if the entity is a wall
            walls.add(entity.makeWall());
        }

        // Create a ray
        Ray ray = new Ray(FromPos, angle);
        // Loop over each wall until a wall is hit

        // Look over each wall and get the closest one (accounting for the minimum distance)
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

        if (closestWall.isPortal) {
            // Don't draw backside of portal
            if (closestWall.portalSector == s) {
                // Recursively call for the next wall
                RenderCol(buffer, FromPos, FromRot, col, screenH, closestDist, rc + 1, closestWall.portalSector);
                return;
            }


            RenderCol(buffer, FromPos, FromRot, col, screenH, closestDist+1, rc + 1, closestWall.portalSector);
            // Calculate the y position of portal sector's floor on the screen (remember, position is 2D)
            // Get the distance from the ray origin to the intersection point
            Vector2 intersection = ray.Intersection(closestWall);
            double distance = Vector2.Distance(FromPos, intersection) * Math.cos(angle - FromRot);

            // Get the height of the wall on screen
            double height = screenH / distance;

            // Get the y position of the wall
            double y = (buffer.height - height) / 2;

            // Offset the Y position based on the sector's floor height and the wall's distance
            double y2 = y + (-closestWall.portalSector.floorHeight) * (screenH / distance);

            // Offset the height based on the sector's ceiling height and the wall's distance
            double height2 = height + (closestWall.portalSector.ceilingHeight - closestWall.portalSector.floorHeight) * (screenH / distance);

            //buffer.drawFastVLine(col, (int) (y2+height2), (int) (((y2 + height2) - y) + height), new Color(0, 0, 255));
            return;
        }

        // Find which sector the wall is in
        Sector sector = null;
        for (Sector sr : level.sectors) {
            if (sr.containsWall(closestWall)) {
                sector = sr;
                break;
            }
        }

        if (sector == null) {
            Sector fakeSector = new Sector();
            fakeSector.walls.add(closestWall);
            sector = fakeSector;
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
        texCol *= (wallLength / ((sector.ceilingHeight - sector.floorHeight)*2));
        texCol %= texW;

        // texCol = 0;

        // Get the distance from the ray origin to the intersection point
        double distance = Vector2.Distance(FromPos, intersection) * Math.cos(angle - FromRot);

        // Get the height of the wall on screen
        double height = screenH / distance;

        // Get the y position of the wall
        double y = (buffer.height - height) / 2;

        // Calcuate the shade of the wall based on the camera angle and wall normal
        double shade = Math.abs(Math.cos((FromRot + (1.5 * Math.PI)) - closestWall.getAngle()));

        // Multiply by the sector's light level
        shade *= sector.lightLevel;

        // Make sure the shade is between 40 and 255
        shade = Math.max(0.4, Math.min(1, shade));


        // Calculate the color of the wall
        int r = (int) (255 * shade);
        int g = (int) (255 * shade);
        int b = (int) (255 * shade);
        Color color = new Color(r, g, b);

        // Backup the original y position and height
        double origY = y;
        double origHeight = height;
        // Offset the Y position based on the sector's floor height and the wall's distance
        y += (-sector.floorHeight) * (screenH / distance);

        // Offset the height based on the sector's ceiling height and the wall's distance
        height += (sector.ceilingHeight - sector.floorHeight) * (screenH / distance);

        // Fill the original y position and height with the floor color
        for (int i = 0; i < origHeight; i++) {
            // Check if the Y pixel is on screen
            if (origY + i < 0 || origY + i >= buffer.height) continue;
            buffer.setPixel(col, (int) (origY + i), new Color(255,0,255));
        }

        // Draw the wall using its texture and shade
        for (int i = 0; i < height; i++) {
            if (closestWall.isPortal) {
                buffer.setPixel(col, (int) y + i, color);
                continue;
            }
            // Check if the Y pixel is on screen
            if (y + i < 0 || y + i >= buffer.height) continue;
            // Get the y position of the texture
            int texY = (int) (i / height * texH);
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

    }
}
