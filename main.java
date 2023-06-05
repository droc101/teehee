import javax.swing.*;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashMap;

public class main {

    final static int TargetFPS = 60;
    public static long messageTime = 0;
    public static String message = "";
    static Input input = new Input();
    public static final int messageTimeMax = 5; // Time (in seconds) to display a message

    public static void Message(String msg) {
        message = msg;
        messageTime = System.currentTimeMillis();
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(NimbusLookAndFeel.class.getName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Load the native helper library
        Util.LoadLibNative();

        // Create and configure the window
        JFrame frame = new JFrame("GAME AND");
        frame.setIconImage(new ImageIcon("texture/ICON.png").getImage());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setUndecorated(false);
        frame.setBackground(java.awt.Color.BLACK);
        frame.setLocationRelativeTo(null);
        frame.setLocation(100,100);
        frame.pack();
        frame.setVisible(true);

        // Add key listeners
        frame.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent evt) {
                input.KeyPressed(evt);
            }
        });
        frame.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent evt) {
                input.KeyReleased(evt);
            }
        });

        // Set the window size
        frame.setSize(1024,768);

        // Create a framebuffer
        FrameBuffer fb = new FrameBuffer(320, 640);

        // Draw the title screen
        Util.DrawString(fb, new Vector2(10,50), "GAME AND");
        Util.DrawString(fb, new Vector2(10,150), "Press SPACE to play");
        Util.DrawString(fb, new Vector2(10, 480-48), "by Droc101");

        // Wait for the user to press space
        while (!input.keys[Input.KeyState.SPACE.ordinal()].pressed) {
            fb.draw(frame);
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // Create the player and level
        Player player = new Player();
        Level currentLevel = new Level("test", player);

        // Forcefully add some entities for testing
        currentLevel.entities.add(new TestEntity(new Vector2(-3,-7.5), 0));
        currentLevel.entities.add(new KeyEntity(new Vector2(-4, 0), 0));
        currentLevel.entities.add(new DoorEntity(new Vector2(6,5.5), 2*Math.PI));
        currentLevel.entities.add(new AmmoEntity(new Vector2(7,-5), 0));

        // Event loop
        while (true) {
            GameIteration(frame, fb, player, currentLevel);
        }

    }

    public static void MessageBox(String message) {
        MessageBox(message, "Message");
    }

    static boolean isKeyJustPressed(Input.KeyState key) {
        return input.keys[key.ordinal()].pressed;
    }


    public static void MessageBox(String message, String title) {
        JOptionPane.showMessageDialog(null, message, title, JOptionPane.INFORMATION_MESSAGE);
    }

    public static void GameIteration(JFrame frame, FrameBuffer fb, Player player, Level currentLevel) {
        input.frame();

        // Get the time at the start of the frame
        long startTime = System.currentTimeMillis();

        if (input.pressed(Input.KeyState.ESC)) {
            System.exit(0);
        }

        Vector2 OldPos = player.position.clone();

        if (input.pressed(Input.KeyState.W)) {
            player.position = player.position.add(Vector2.fromAngle(player.rotation).scale(0.5));
        }
        if (input.pressed(Input.KeyState.S)) {
            player.position = player.position.add(Vector2.fromAngle(player.rotation).scale(-0.5));
        }

        // Q and E for strafing
        if (input.pressed(Input.KeyState.Q)) {
            player.position = player.position.add(Vector2.fromAngle(player.rotation + Math.PI / 2).scale(-0.5));
        }
        if (input.pressed(Input.KeyState.E)) {
            player.position = player.position.add(Vector2.fromAngle(player.rotation + Math.PI / 2).scale(0.5));
        }

        if (input.justPressed(Input.KeyState.SPACE) && player.ammo > 0) {
            player.ammo -= 1;
            Ray ray = new Ray(player.position, player.rotation);
            Double dist = ray.Cast(currentLevel);
            Entity hit = ray.HitscanEntities(currentLevel);
            if (hit != null) {
                // Check if the distance to the entity is less than the distance to the wall
                if (dist == null || Vector2.Distance(hit.position, player.position) < dist) {
                    hit.onHit();
                }
            }
        }


        if (input.pressed(Input.KeyState.A)) {
            player.rotation -= 0.1;
        }
        if (input.pressed(Input.KeyState.D)) {
            player.rotation += 0.1;
        }

        player.rotation = player.rotation % (Math.PI * 2);

        // Check for collisions
        // Calculate the angle of movement
        double angle = Math.atan2(player.position.y - OldPos.y, player.position.x - OldPos.x);

        Ray ray = new Ray(OldPos, angle);
        Double dist = ray.Cast(currentLevel);

        if (dist != -1 && dist < 0.5) {
            player.position = OldPos;
        }

        // Check for entities that should be freed
        for (int i = 0; i < currentLevel.entities.size(); i++) {
            if (currentLevel.entities.get(i).freeNextFrame) {
                currentLevel.entities.remove(i);
                i--;
            }
        }

        // Update entities
        for (Entity e : currentLevel.entities) {
            e.update(player);
        }

        //FrameBuffer fb = new FrameBuffer(width, height);


        RayTracer rt = new RayTracer(currentLevel, fb.width);

        for (int x = 0; x < fb.width; x++) {
            // Fill the top half of the screen with blue
            //fb.drawFastVLine(x, 0, fb.height / 2, new Color(43, 36, 29));
            //fb.drawFastVLine(x, fb.height / 2, fb.height / 2, new Color(13, 6, 0));

            rt.RenderCol(fb, player.position, player.rotation, x, fb.height - 100, currentLevel.findSector(player.position));
            rt.RenderColPass2(fb, player.position, player.rotation, x, fb.height - 100);
        }

        fb.BlitSprite("barrel", new Vector2(fb.width / 2 - 64, fb.height - 356));

        // Draw a rect on the last 100px of the screen
        fb.drawRect(0, fb.height - 100, fb.width, 100, new Color(40, 25, 25));

        Util.DrawString(fb, new Vector2(30, fb.height-90), "HP");
        Util.DrawInt(fb, new Vector2(65, fb.height-90), player.health);

        Util.DrawString(fb, new Vector2(10, fb.height-60), "FPS");
        Util.DrawInt(fb, new Vector2(65, fb.height-60), (int) (1000 / (System.currentTimeMillis() - startTime)));

        Util.DrawString(fb, new Vector2(150, fb.height-90), "AMMO");
        Util.DrawInt(fb, new Vector2(220, fb.height-90), player.ammo);

        Util.DrawString(fb, new Vector2(150, fb.height-60), "KEYS");
        Util.DrawInt(fb, new Vector2(220, fb.height-60), player.keys);

        // Check if a message should be displayed on screen
        int time = (int) (System.currentTimeMillis() - messageTime);
        if (time < messageTimeMax*1000) {
            Util.DrawString(fb, new Vector2(10, 10), message);
        }

        if (player.health <= 0) {
            player.health = 0;
            Util.DrawString(fb, new Vector2(10, 50), "YOU DIED");
            Util.DrawString(fb, new Vector2(10, 100), "Press ESC to exit");
            fb.draw(frame);

            while (!input.pressed(Input.KeyState.ESC)) {
                // Wait for the next frame
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.exit(0);

        }

        //Message("POS " + player.position.toString());

        fb.draw(frame);

        // Get the time at the end of the frame
        long endTime = System.currentTimeMillis();

        // Calculate the time it took to render the frame
        long frameTime = endTime - startTime;

        // Check if the frame took too long to render
        if (frameTime > 1000 / TargetFPS) {
            //System.out.println("Frame took too long to render: " + frameTime + "ms");
        }

        // Wait for the next frame
        try {
            Thread.sleep(Math.max(0, (1000 / TargetFPS) - frameTime));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}