public class AmmoEntity extends Entity {

    // It gives you a key. That's it.

    public AmmoEntity(Vector2 position, double rotation) {
        super(position, rotation);
        length = 2.0;
        texture = "iq";
    }

    public void update(Player player) {
        rotation += 0.1;
        if (isColliding(player.position)) {
            main.Message("You got ammo");
            player.ammo += 10;
            freeNextFrame = true;
        }
    }
}
