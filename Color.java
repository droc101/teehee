
public class Color {
    byte r;
    byte g;
    byte b;
    byte a;

    public Color(byte r, byte g, byte b, byte a) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }

    public Color(byte r, byte g, byte b) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = (byte) 255;
    }

    public Color(int r, int g, int b, int a) {
        this.r = (byte) r;
        this.g = (byte) g;
        this.b = (byte) b;
        this.a = (byte) a;
    }

    public Color(int r, int g, int b) {
        this.r = (byte) r;
        this.g = (byte) g;
        this.b = (byte) b;
        this.a = (byte) 255;
    }

    public Color(int argb) {
        this.r = (byte) ((argb >> 16) & 0xFF);
        this.g = (byte) ((argb >> 8) & 0xFF);
        this.b = (byte) ((argb >> 0) & 0xFF);
        this.a = (byte) ((argb >> 24) & 0xFF);
    }

    public int getARGB() {
        return ((a & 0xFF) << 24) |
               ((r & 0xFF) << 16) |
               ((g & 0xFF) << 8) |
               ((b & 0xFF) << 0);
    }
}
