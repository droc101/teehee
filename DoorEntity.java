public class DoorEntity extends Entity {
    public DoorEntity(Vector2 position, double rotation) {
        super(position, rotation);
        length = 2.0;
        texture = "cross";
        doCollide = true;
    }

    boolean unlocked = false;
    int openframes = 0;

    public void update(Player player) {
        if (isColliding(player.position) && !unlocked) {
            if (player.keys > 0) {
                player.keys -= 1;
                main.Message("Door Unlocked");
                unlocked = true;
            } else {
                main.Message("you need a key");
            }
        } else if (unlocked) {
            // Move the door slightly to the left obeying the rotation
            position = position.add(Vector2.fromAngle(rotation).scale(-0.1));
            openframes += 1;

            if (openframes > 25) {
                freeNextFrame = true;
            }
        }
    }

    public void onHit() {

    }
}
