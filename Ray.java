public class Ray {
    public Vector2 origin;
    public double direction;

    public Ray(Vector2 origin, double direction) {
        this.origin = origin;
        this.direction = direction;
    }

    public boolean Intersects(Wall wall) {
        Vector2 intersection = Intersection(wall);
        return intersection != null;
    }

    public double Cast(Level level) {
        for (Wall wall : level.walls) {
            Vector2 intersection = Intersection(wall);
            if (intersection != null) {
                return Vector2.Distance(origin, intersection);
            }
        }
        return -1;
    }

    public Vector2 Intersection(Wall wall) {
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
            return new Vector2(intersectionX, intersectionY);
        }

        return null; // No intersection
    }
}
