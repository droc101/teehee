import javax.imageio.ImageIO;
import javax.swing.JFrame;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class FrameBuffer {

    // It's an image with extra steps!

    public int width;
    public int height;

    Color[] buffer;

    public FrameBuffer(int width, int height) {
        this.width = width;
        this.height = height;
        buffer = new Color[width * height];
        // Initialize the buffer to black
        for(int i = 0; i < buffer.length; i++) {
            buffer[i] = new Color(0, 0, 0);
        }
    }

    public void setPixel(int x, int y, Color c) {
        // Check if the pixel is out of bounds
        if(x < 0 || x >= width || y < 0 || y >= height) {
            return;
        }
        buffer[y * width + x] = c;
    }

    public void clear(Color c) {
        for(int i = 0; i < buffer.length; i++) {
            buffer[i] = c;
        }
    }

    public void drawRect(int x, int y, int w, int h, Color c) {
        for(int i = x; i < x + w; i++) {
            for(int j = y; j < y + h; j++) {
                setPixel(i, j, c);
            }
        }
    }

    public void draw(JFrame frame) {
        // Create a java.awt.image.BufferedImage from the framebuffer
        java.awt.image.BufferedImage img = new java.awt.image.BufferedImage(width, height, java.awt.image.BufferedImage.TYPE_INT_ARGB);
        // Get the image's graphics context
        java.awt.Graphics2D g = img.createGraphics();
        // Draw the framebuffer to the image
        for(int i = 0; i < buffer.length; i++) {
            Color c = buffer[i];
            g.setColor(new java.awt.Color(c.r & 0xFF, c.g & 0xFF, c.b & 0xFF, c.a & 0xFF));
            g.fillRect(i % width, i / width, 1, 1);
        }
        // Draw the image to the frame scaled to the frame's size
        frame.getContentPane().getGraphics().drawImage(img, 0, 0, frame.getContentPane().getWidth(), frame.getContentPane().getHeight(), null);

        // Dispose of the graphics context
        g.dispose();
    }

    public void drawFastVLine(int x, int y, int h, Color color) {
        for(int i = y; i < y + h; i++) {
            setPixel(x, i, color);
        }
    }

    public void BlitSpriteRect(String name, Vector2 screenPos, Vector2 texturePos, Vector2 textureSize) {
        String wallTex = "texture/" + name + ".png";
        // Load the texture using ImageIO
        BufferedImage tex = null;
        try {
            tex = ImageIO.read(new File(wallTex));
        } catch (IOException e) {
            // Load texture/missing.png instead
            try {
                tex = ImageIO.read(new File("texture/missing.png"));
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }

        // Blit the texture to the framebuffer
        for(int i = 0; i < textureSize.x; i++) {
            for(int j = 0; j < textureSize.y; j++) {
                // Ensure the pixel is in bounds using Util.wrap
                int px_x = (int)Util.wrap(texturePos.x + i, 0, tex.getWidth());
                int px_y = (int)Util.wrap(texturePos.y + j, 0, tex.getHeight());
                int pixel = tex.getRGB(px_x, px_y);
                int r = (pixel >> 16) & 0xFF;
                int g = (pixel >> 8) & 0xFF;
                int b = (pixel >> 0) & 0xFF;
                int a = (pixel >> 24) & 0xFF;
                if(a == 0) {
                    continue;
                }
                setPixel((int)screenPos.x + i, (int)screenPos.y + j, new Color(r, g, b));
            }
        }
    }

    public void BlitSprite(String name, Vector2 pos) {
        String wallTex = "texture/" + name + ".png";
        // Load the texture using ImageIO
        BufferedImage tex = null;
        try {
            tex = ImageIO.read(new File(wallTex));
        } catch (IOException e) {
            // Load texture/missing.png instead
            try {
                tex = ImageIO.read(new File("texture/missing.png"));
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }

        // Blit the texture to the framebuffer
        for(int i = 0; i < tex.getWidth(); i++) {
            for(int j = 0; j < tex.getHeight(); j++) {
                int pixel = tex.getRGB(i, j);
                int r = (pixel >> 16) & 0xFF;
                int g = (pixel >> 8) & 0xFF;
                int b = (pixel >> 0) & 0xFF;
                int a = (pixel >> 24) & 0xFF;
                if(a == 0) {
                    continue;
                }
                setPixel((int)pos.x + i, (int)pos.y + j, new Color(r, g, b));
            }
        }
    }

}
