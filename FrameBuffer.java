import javax.imageio.ImageIO;
import javax.swing.JFrame;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class FrameBuffer {
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

    public Color getPixel(int x, int y) {
        return buffer[y * width + x];
    }

    public void clear(Color c) {
        for(int i = 0; i < buffer.length; i++) {
            buffer[i] = c;
        }
    }

    public void clear() {
        clear(new Color(0, 0, 0));
    }

    public void drawRect(int x, int y, int w, int h, Color c) {
        for(int i = x; i < x + w; i++) {
            for(int j = y; j < y + h; j++) {
                setPixel(i, j, c);
            }
        }
    }

    public void drawRectAbs(int x1, int y1, int x2, int y2, Color c) {
        // Expects a rectangle
        int x = Math.min(x1, x2);
        int y = Math.min(y1, y2);
        int w = Math.abs(x1 - x2);
        int h = Math.abs(y1 - y2);
        drawRect(x, y, w, h, c);
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
        frame.getGraphics().drawImage(img, 0, 0, frame.getWidth(), frame.getHeight(), null);

        // Dispose of the graphics context
        g.dispose();
    }

    public void drawLine(int x, int y, int x2, int y2, Color color) {
        int dx = x2 - x;
        int dy = y2 - y;
        int steps = Math.max(Math.abs(dx), Math.abs(dy));
        float xInc = dx / (float)steps;
        float yInc = dy / (float)steps;
        float xCur = x;
        float yCur = y;
        for(int i = 0; i < steps; i++) {
            setPixel((int)xCur, (int)yCur, color);
            xCur += xInc;
            yCur += yInc;
        }
    }

    public void drawFastVLine(int x, int y, int h, Color color) {
        for(int i = y; i < y + h; i++) {
            setPixel(x, i, color);
        }
    }

    public void DrawString(String s, Vector2 pos, Color color) {
        int x = (int)pos.x;
        int y = (int)pos.y;
        for(int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            int charIndex = (int)c - 32;
            int charX = charIndex % 16;
            int charY = charIndex / 16;
            drawRect(x, y, 8, 8, color);
            x += 8;
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
