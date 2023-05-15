public class Entity {
    public Vector2 position = new Vector2(0, 0);
    public double rotation = 0;

    public double length = 1.0;
    public String texture = "null";

    public Entity() {

    }

    public Entity(Vector2 position, double rotation) {
        this.position = position;
        this.rotation = rotation;
    }

    public void update() {

    }

    public Wall makeWall() {
        // Subtract length / 2 from the position according to the rotation
        Vector2 pos = new Vector2(position.x - (length / 2) * Math.cos(rotation),
                position.y - (length / 2) * Math.sin(rotation));
        // Add length / 2 to the position according to the rotation
        Vector2 pos2 = new Vector2(position.x + (length / 2) * Math.cos(rotation),
                position.y + (length / 2) * Math.sin(rotation));

        return new Wall(pos, pos2, texture);
    }
}
