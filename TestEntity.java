public class TestEntity extends Entity {
    public TestEntity(Vector2 position, double rotation) {
        super(position, rotation);
        length = 2.0;
        texture = "demon";
    }

    public void update(Vector2 playerPos) {
        rotation += 0.01;
        position = position.add(Vector2.fromAngle(rotation).scale(0.01));
    }
}
