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

    public void update(Vector2 playerPos) {

    }

    public boolean isColliding(Vector2 pos) {
        // Calculate the distance between the entity and the point
        double dist = Vector2.Distance(position, pos);
        // Check if the distance is less than the length of the entity
        if (dist < length) {
            return true;
        }
        return false;
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
