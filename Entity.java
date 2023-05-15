public class Entity {
    public Vector2 position = new Vector2(0, 0);
    public double rotation = 0;

    public double length = 1.0;
    public String texture = "null";

    public Entity(Vector2 position, double rotation) {
        this.position = position;
        this.rotation = rotation;
    }

    public void update() {

    }
}
