
public class Color {

    // It's literally just a struct, but Java doesn't have those so I'm using a class
    // This is why I don't like Java (among many other reasons)

    byte r;
    byte g;
    byte b;
    byte a;

    public Color(int r, int g, int b) {
        this.r = (byte) r;
        this.g = (byte) g;
        this.b = (byte) b;
        this.a = (byte) 255;
    }

}
