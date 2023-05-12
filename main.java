import javax.swing.JFrame;
import java.awt.Dimension;

public class main {
    
    final static int TargetFPS = 30;

    // Dict of key states (WASD SHIFT SPACE TAB)
    enum KeyState {
        W, A, S, D, SHIFT, SPACE, TAB, ESC
    }
    static boolean[] keys = new boolean[KeyState.values().length];


    public static void main(String[] args) {

        //1. Create the frame.
        JFrame frame = new JFrame("GAME AND");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        frame.setLocationRelativeTo(null);
        // Make the window float in i3
        frame.setAlwaysOnTop(true);
        frame.setResizable(false);
        frame.setUndecorated(true);
        frame.setPreferredSize(new Dimension(800, 600));
        frame.pack();
        frame.setVisible(true);
        frame.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                // Check if the key is one of the keys we care about
                switch(evt.getKeyCode()) {
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
                }
            }
        });
        frame.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                // Check if the key is one of the keys we care about
                switch(evt.getKeyCode()) {
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
                }
            }
        });
        
        frame.setSize(640, 480);

        long frameCount = 0;

        Level currentLevel = new Level("test");

        Vector2 playerPos = new Vector2(2.5, 2.5);
        double playerRot = Math.PI * 1.5;

        // Event loop
        while(true) {
            if(keys[KeyState.ESC.ordinal()]) {
                System.exit(0);
            }

            Vector2 OldPos = playerPos.clone();

            if (keys[KeyState.W.ordinal()]) {
                playerPos = playerPos.add(Vector2.fromAngle(playerRot).scale(0.5));
            }
            if (keys[KeyState.S.ordinal()]) {
                playerPos = playerPos.add(Vector2.fromAngle(playerRot).scale(-0.5));
            }

            if (keys[KeyState.A.ordinal()]) {
                playerRot -= 0.1;
            }
            if (keys[KeyState.D.ordinal()]) {
                playerRot += 0.1;
            }

            // Check for collisions
            // Calculate the angle of movement
            double angle = Math.atan2(playerPos.y - OldPos.y, playerPos.x - OldPos.x);

            Ray ray = new Ray(OldPos, angle);
            Double dist = ray.Cast(currentLevel);
            
            if(dist != -1 && dist < 0.5) {
                playerPos = OldPos;
            }

            Dimension size = frame.getSize();
            int width = size.width;
            int height = size.height;
            if(width <= 0 || height <= 0) {
                continue;
            }

            //FrameBuffer fb = new FrameBuffer(width, height);
            FrameBuffer fb = new FrameBuffer(640, 480);

            // Fill the top half of the screen with blue
            //fb.drawRect(0, 0, width, height / 2, new Color(0x0000FF));

            // Fill the bottom half of the screen with green

            RayTracer rt = new RayTracer(currentLevel);
            
            for (int x = 0; x < width; x++) {
                // Fill the top half of the screen with blue
                fb.drawFastVLine(x, 0, fb.height/2, new Color(0,0,255));
                fb.drawFastVLine(x, fb.height/2, fb.height/2, new Color(0,255,0));
                
                rt.RenderCol(fb, playerPos, playerRot, x, height);
                // Fill the bottom half of the screen with green
                
            }

            fb.DrawString(playerPos.toString(), new Vector2(10, 10), new Color(0xFFFFFF));

            fb.draw(frame);
            // try {
            //     Thread.sleep(1000 / TargetFPS);
            // } catch(Exception e) {
            //     e.printStackTrace();
            // }
            frameCount++;
        }

    }
}