import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public class Sector {
    public double floorHeight = 0;
    public double ceilingHeight = 1;
    public double lightLevel = 1.0;

    public String floorTexture = "null";
    public String ceilingTexture = "null";

    public ArrayList<Wall> walls = new ArrayList<Wall>();


    public Sector() {

    }

    public Sector(double floorHeight, double ceilingHeight, double lightLevel, String floorTexture, String ceilingTexture, Wall[] walls) {
        this.floorHeight = floorHeight;
        this.ceilingHeight = ceilingHeight;
        this.lightLevel = lightLevel;
        this.floorTexture = floorTexture;
        this.ceilingTexture = ceilingTexture;
        this.walls = (ArrayList<Wall>) Arrays.stream(walls).toList();
        // Check if the sector is a closed shape
        if (!isClosed()) {
            throw new IllegalArgumentException("Sector is not a closed shape");
        }
    }

    boolean isClosed() {
        // Check if each wall shares at least one point with the next wall
        for (int i = 0; i < walls.size(); i++) {
            Wall wall = walls.get(i);
            Wall nextWall = walls.get((i + 1) % walls.size());
            if (!wall.containsPoint(nextWall.vertA) && !wall.containsPoint(nextWall.vertB)) {
                return false;
            }
        }
        return true;
    }

    public boolean containsWall(Wall wall) {
        return walls.contains(wall);
    }

    public boolean containsPoint(Vector2 point) {
        // Create a polygon from the sector walls
        Polygon polygon = new Polygon();
        for (Wall wall : walls) {
            polygon.addPoint((int) wall.vertA.x, (int) wall.vertA.y);
        }
        return polygon.contains(point.x, point.y);

    }
}
