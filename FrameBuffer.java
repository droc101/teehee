import javax.imageio.ImageIO;
import javax.swing.JFrame;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Dictionary;

public class FrameBuffer {

    // It's an image with extra steps!

    public final int width;
    public final int height;

    java.awt.image.BufferedImage img;
    java.awt.Graphics2D gfx;

    Dictionary<String, BufferedImage> TextureCache;

    public FrameBuffer(int width, int height) {
        this.width = width;
        this.height = height;
        img = new java.awt.image.BufferedImage(width, height, java.awt.image.BufferedImage.TYPE_INT_ARGB);
        gfx = img.createGraphics();
        TextureCache = new java.util.Hashtable<String, BufferedImage>();
    }

    // clear the texture cache
    public void ClearTextureCache() {
        TextureCache = new java.util.Hashtable<String, BufferedImage>();
    }

    // Draw a single pixel
    public void setPixel(int x, int y, Color c) {
        // Check if the pixel is out of bounds
        if (x < 0 || x >= width || y < 0 || y >= height) {
            return;
        }
        gfx.setColor(c.toAWTColor());
        gfx.fillRect(x, y, 1, 1);
    }

    public void drawRect(int x, int y, int w, int h, Color c) {
        for(int i = x; i < x + w; i++) {
            for(int j = y; j < y + h; j++) {
                setPixel(i, j, c);
            }
        }
    }

    public void draw(JFrame frame) {
        frame.getContentPane().getGraphics().drawImage(img, 0, 0, frame.getContentPane().getWidth(), frame.getContentPane().getHeight(), null);
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

        // Check if the texture is in the cache
        if(TextureCache.get(wallTex) != null) {
            tex = TextureCache.get(wallTex);
        } else {
            try {
                tex = ImageIO.read(new File(wallTex));
                TextureCache.put(wallTex, tex);
            } catch (IOException e) {
                // Load texture/missing.png instead
                try {
                    tex = ImageIO.read(new File("texture/missing.png"));
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }

        gfx.drawImage(tex.getSubimage((int)texturePos.x, (int)texturePos.y, (int)textureSize.x, (int)textureSize.y), (int)screenPos.x, (int)screenPos.y, null);
    }

    public void BlitSprite(String name, Vector2 pos) {
        String wallTex = "texture/" + name + ".png";
        // Load the texture using ImageIO
        BufferedImage tex = null;

        // Check if the texture is in the cache
        if(TextureCache.get(wallTex) != null) {
            tex = TextureCache.get(wallTex);
        } else {
            try {
                tex = ImageIO.read(new File(wallTex));
                TextureCache.put(wallTex, tex);
            } catch (IOException e) {
                // Load texture/missing.png instead
                try {
                    tex = ImageIO.read(new File("texture/missing.png"));
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }

        gfx.drawImage(tex, (int)pos.x, (int)pos.y, null);
    }

}
