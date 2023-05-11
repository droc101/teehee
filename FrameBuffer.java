import javax.swing.JFrame;

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
        frame.getGraphics().drawImage(img, 0, 0, null);

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

}
