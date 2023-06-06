import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public class Sector {

    public ArrayList<Wall> walls = new ArrayList<Wall>();


    public Sector() {

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
