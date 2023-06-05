public class KeyEntity extends Entity {

    // It gives you a key. That's it.

    public KeyEntity(Vector2 position, double rotation) {
        super(position, rotation);
        length = 2.0;
        texture = "key";
    }

    public void update(Player player) {
        rotation += 0.1;
        if (isColliding(player.position)) {
            main.Message("You got a key");
            player.keys += 1;
            freeNextFrame = true;
        }
    }
}
