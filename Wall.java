public class Wall {
    public Vector2 vertA;
    public Vector2 vertB;
    public String texture;

    public Wall(Vector2 vertA, Vector2 vertB, String texture) {
        this.vertA = vertA;
        this.vertB = vertB;
        this.texture = texture;
    }

    public Wall(Vector2 vertA, Vector2 vertB) {
        this.vertA = vertA;
        this.vertB = vertB;
        this.texture = "default";
    }

    public Vector2 getNormal() {
        return new Vector2(vertB.y - vertA.y, vertA.x - vertB.x);
    }

    public Vector2 getMidpoint() {
        return new Vector2((vertA.x + vertB.x) / 2, (vertA.y + vertB.y) / 2);
    }

    public double getLength() {
        return Math.sqrt(Math.pow(vertB.x - vertA.x, 2) + Math.pow(vertB.y - vertA.y, 2));
    }

    public double getAngle() {
        return Math.atan2(vertB.y - vertA.y, vertB.x - vertA.x);
    }

    public Vector2 GetCenter() {
        return new Vector2((vertA.x + vertB.x) / 2, (vertA.y + vertB.y) / 2);
    }

    public double DistanceTo(Vector2 point) {
        Vector2 wallVector = new Vector2(vertB.x - vertA.x, vertB.y - vertA.y);
        Vector2 pointVector = new Vector2(point.x - vertA.x, point.y - vertA.y);

        double denominator = (wallVector.x * wallVector.x) + (wallVector.y * wallVector.y);
        if (denominator == 0) {
            return -1; // Wall has no length, no distance
        }

        double t = (pointVector.x * wallVector.x + pointVector.y * wallVector.y) / denominator;

        if (t >= 0 && t <= 1) {
            double intersectionX = vertA.x + t * wallVector.x;
            double intersectionY = vertA.y + t * wallVector.y;
            return Vector2.Distance(point, new Vector2(intersectionX, intersectionY));
        }

        return -1; // No intersection
    }

    public double AngleTo(Vector2 pos) {
        return Math.atan2(pos.y - vertA.y, pos.x - vertA.x) - getAngle();
    }

    // Check if the wall is facing the player
    public boolean IsFacing(double angle) {
        // Must be within 135 degrees of the wall's angle
        return Math.abs(angle - getAngle()) < Math.PI * 0.75;

    }


}
