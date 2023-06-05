import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public class Sector {

    public ArrayList<Wall> walls = new ArrayList<Wall>();


    public Sector() {

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
