public class TestEntity extends Entity {

    boolean pc = false;

    int health = 5;

    public TestEntity(Vector2 position, double rotation) {
        super(position, rotation);
        length = 2.0;
        texture = "demon";
    }

    public void update(Player player) {
        rotation += 0.01;
        position = position.add(Vector2.fromAngle(rotation).scale(0.01));

        if (isColliding(player.position) && !pc) {
            player.health -= 5;
            // Knock the player back a bit
            player.position = player.position.add(Vector2.fromAngle(rotation).scale(-0.1));
            pc = true;
        }
        if (!isColliding(player.position)) {
            pc = false;
        }
    }

    public void onHit() {
        health -= 1;
        if (health <= 0) {
            freeNextFrame = true;
        }
    }
}
