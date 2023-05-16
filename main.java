import javax.swing.*;
import java.awt.Dimension;

public class main {

    final static int TargetFPS = 30;

    // Dict of key states
    enum KeyState {
        W, A, S, D, SHIFT, SPACE, TAB, ESC, Q, E
    }

    static boolean[] keys = new boolean[KeyState.values().length];


    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(javax.swing.plaf.nimbus.NimbusLookAndFeel.class.getName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        //MessageBox("Controls:\nWS: move\nAD: Rotate\nQE: Strafe", "Controls");

        //1. Create the frame.
        JFrame frame = new JFrame("GAME AND");

        frame.setIconImage(new ImageIcon("texture/ICON.png").getImage());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setAlwaysOnTop(true);
        frame.setResizable(false);
        //frame.setUndecorated(true);
        frame.setBackground(java.awt.Color.BLACK);
        frame.setLocationRelativeTo(null);
        frame.setLocation(100,100);
        frame.pack();
        frame.setVisible(true);
        frame.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                // Check if the key is one of the keys we care about
                switch (evt.getKeyCode()) {
                    case java.awt.event.KeyEvent.VK_W:
                        keys[KeyState.W.ordinal()] = true;
                        break;
                    case java.awt.event.KeyEvent.VK_A:
                        keys[KeyState.A.ordinal()] = true;
                        break;
                    case java.awt.event.KeyEvent.VK_S:
                        keys[KeyState.S.ordinal()] = true;
                        break;
                    case java.awt.event.KeyEvent.VK_D:
                        keys[KeyState.D.ordinal()] = true;
                        break;
                    case java.awt.event.KeyEvent.VK_SHIFT:
                        keys[KeyState.SHIFT.ordinal()] = true;
                        break;
                    case java.awt.event.KeyEvent.VK_SPACE:
                        keys[KeyState.SPACE.ordinal()] = true;
                        break;
                    case java.awt.event.KeyEvent.VK_TAB:
                        keys[KeyState.TAB.ordinal()] = true;
                        break;
                    case java.awt.event.KeyEvent.VK_ESCAPE:
                        keys[KeyState.ESC.ordinal()] = true;
                        break;
                    case java.awt.event.KeyEvent.VK_Q:
                        keys[KeyState.Q.ordinal()] = true;
                        break;
                    case java.awt.event.KeyEvent.VK_E:
                        keys[KeyState.E.ordinal()] = true;
                        break;
                }
            }
        });
        frame.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                // Check if the key is one of the keys we care about
                switch (evt.getKeyCode()) {
                    case java.awt.event.KeyEvent.VK_W:
                        keys[KeyState.W.ordinal()] = false;
                        break;
                    case java.awt.event.KeyEvent.VK_A:
                        keys[KeyState.A.ordinal()] = false;
                        break;
                    case java.awt.event.KeyEvent.VK_S:
                        keys[KeyState.S.ordinal()] = false;
                        break;
                    case java.awt.event.KeyEvent.VK_D:
                        keys[KeyState.D.ordinal()] = false;
                        break;
                    case java.awt.event.KeyEvent.VK_SHIFT:
                        keys[KeyState.SHIFT.ordinal()] = false;
                        break;
                    case java.awt.event.KeyEvent.VK_SPACE:
                        keys[KeyState.SPACE.ordinal()] = false;
                        break;
                    case java.awt.event.KeyEvent.VK_TAB:
                        keys[KeyState.TAB.ordinal()] = false;
                        break;
                    case java.awt.event.KeyEvent.VK_ESCAPE:
                        keys[KeyState.ESC.ordinal()] = false;
                        break;
                    case java.awt.event.KeyEvent.VK_Q:
                        keys[KeyState.Q.ordinal()] = false;
                        break;
                    case java.awt.event.KeyEvent.VK_E:
                        keys[KeyState.E.ordinal()] = false;
                        break;
                }
            }
        });

        frame.setSize(1024,768);

        long frameCount = 0;

        Level currentLevel = new Level("test");

        currentLevel.entities.add(new TestEntity(new Vector2(5,5), 0));

        Vector2 playerPos = new Vector2(2.5, 2.5);
        double playerRot = Math.PI * 1.5;
        int playerHealth = 100;
        final int playerMaxHealth = 100;
        FrameBuffer fb = new FrameBuffer(320, 240);

        // Event loop
        while (true) {

            // Get the time at the start of the frame
            long startTime = System.currentTimeMillis();

            if (keys[KeyState.ESC.ordinal()]) {
                System.exit(0);
            }

            Vector2 OldPos = playerPos.clone();

            if (keys[KeyState.W.ordinal()]) {
                playerPos = playerPos.add(Vector2.fromAngle(playerRot).scale(0.5));
            }
            if (keys[KeyState.S.ordinal()]) {
                playerPos = playerPos.add(Vector2.fromAngle(playerRot).scale(-0.5));
            }

            // Q and E for strafing
            if (keys[KeyState.Q.ordinal()]) {
                playerPos = playerPos.add(Vector2.fromAngle(playerRot + Math.PI / 2).scale(-0.5));
            }
            if (keys[KeyState.E.ordinal()]) {
                playerPos = playerPos.add(Vector2.fromAngle(playerRot + Math.PI / 2).scale(0.5));
            }


            if (keys[KeyState.A.ordinal()]) {
                playerRot -= 0.1;
            }
            if (keys[KeyState.D.ordinal()]) {
                playerRot += 0.1;
            }

            playerRot = playerRot % (Math.PI * 2);

            // Check for collisions
            // Calculate the angle of movement
            double angle = Math.atan2(playerPos.y - OldPos.y, playerPos.x - OldPos.x);

            Ray ray = new Ray(OldPos, angle);
            Double dist = ray.Cast(currentLevel);

            if (dist != -1 && dist < 0.5) {
                playerPos = OldPos;
            }

            // Update entities
            for (Entity e : currentLevel.entities) {
                e.update(playerPos);
            }

            //FrameBuffer fb = new FrameBuffer(width, height);


            RayTracer rt = new RayTracer(currentLevel);

            for (int x = 0; x < fb.width; x++) {
                // Fill the top half of the screen with blue
                fb.drawFastVLine(x, 0, fb.height / 2, new Color(43, 36, 29));
                fb.drawFastVLine(x, fb.height / 2, fb.height / 2, new Color(13, 6, 0));

                rt.RenderCol(fb, playerPos, playerRot, x, fb.height);

            }

            // Draw the player health bar in the top left corner
            // Calculate the width of the health bar
            int barWidth = (int) (((double) playerHealth / playerMaxHealth) * 200);

            // Draw the black background of the health bar
            fb.drawRect(10, 10, 200, 10, new Color(0,0,0));
            // Draw the red foreground of the health bar
            fb.drawRect(10, 10, barWidth, 10, new Color(255,0,0));

            fb.draw(frame);

            // Get the time at the end of the frame
            long endTime = System.currentTimeMillis();

            // Calculate the time it took to render the frame
            long frameTime = endTime - startTime;

            // Check if the frame took too long to render
            if (frameTime > 1000 / TargetFPS) {
                System.out.println("Frame took too long to render: " + frameTime + "ms");
            }

            // Wait for the next frame
            try {
                Thread.sleep(Math.max(0, (1000 / TargetFPS) - frameTime));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            frameCount++;
        }

    }

    public static void MessageBox(String message) {
        MessageBox(message, "Message");
    }


    public static void MessageBox(String message, String title) {
        JOptionPane.showMessageDialog(null, message, title, JOptionPane.INFORMATION_MESSAGE);
    }
}