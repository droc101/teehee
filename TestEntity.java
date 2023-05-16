public class TestEntity extends Entity {
    public TestEntity(Vector2 position, double rotation) {
        super(position, rotation);
        length = 2.0;
        texture = "demon";
    }

    public void update(Vector2 playerPos) {
        // Look at the player
        //rotation = Math.atan2(playerPos.y - position.y, playerPos.x - position.x);
    }
}
