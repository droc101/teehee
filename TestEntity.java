public class TestEntity extends Entity {
    public TestEntity(Vector2 position, double rotation) {
        super(position, rotation);
        length = 2.0;
        texture = "demon";
    }

    public void update(Vector2 playerPos) {
        rotation += 0.01;

        // Check if the entity is close to the player
        if (Vector2.Distance(position, playerPos) < 2.0) {
            // Move the entity away from the player
            position.x += (position.x - playerPos.x) * 0.5;
            position.y += (position.y - playerPos.y) * 0.5;
        }
    }
}
