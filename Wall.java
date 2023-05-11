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


}
