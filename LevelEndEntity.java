public class LevelEndEntity extends Entity {

    public LevelEndEntity(Vector2 position, double rotation) {
        super(position, rotation);
        length = 2.0;
        texture = "BLOB2";
    }

    public void update(Player player) {
        player.triggerWin = isColliding(player.position);
        if (player.triggerWin) {
            freeNextFrame = true;
        }
    }

}
