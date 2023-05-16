import java.util.HashMap;
import java.util.Map;

public class Ray {
    public Vector2 origin;
    public double direction;

    private Map<Wall, Vector2> intersections = new HashMap<Wall, Vector2>();

    public Ray(Vector2 origin, double direction) {
        this.origin = origin;
        this.direction = direction;
    }

    public boolean Intersects(Wall wall) {
        Vector2 intersection = Intersection(wall);
        return intersection != null;
    }

    public double Cast(Level level) {
        Wall closestWall = null;
        double closestDist = Double.MAX_VALUE;
        for (Wall wall : level.walls) {
            //System.out.println("rtgb");
            Vector2 intersection = Intersection(wall);
            if (intersection != null) {
                double distance = Vector2.Distance(origin, intersection);
                if (distance < closestDist) {
                    closestWall = wall;
                    closestDist = distance;
                }
            }
        }
        // If no wall was hit, return -1
        if (closestWall != null) {
            return closestDist;
        }
        return -1;
    }

    public Vector2 Intersection(Wall wall) {
        if (intersections.containsKey(wall)) {
            return intersections.get(wall);
        }
        Vector2 wallVector = new Vector2(wall.vertB.x - wall.vertA.x, wall.vertB.y - wall.vertA.y);
        Vector2 rayVector = new Vector2(Math.cos(direction), Math.sin(direction));

        double denominator = (rayVector.x * wallVector.y) - (rayVector.y * wallVector.x);
        if (denominator == 0) {
            return null; // Rays are parallel, no intersection
        }

        double t = ((wall.vertA.x - origin.x) * wallVector.y - (wall.vertA.y - origin.y) * wallVector.x) / denominator;
        double u = ((wall.vertA.x - origin.x) * rayVector.y - (wall.vertA.y - origin.y) * rayVector.x) / denominator;

        if (t >= 0 && u >= 0 && u <= 1) {
            double intersectionX = origin.x + t * rayVector.x;
            double intersectionY = origin.y + t * rayVector.y;
            Vector2 intersection = new Vector2(intersectionX, intersectionY);
            intersections.put(wall, intersection);
            return intersection;
        }

        return null; // No intersection
    }
}
