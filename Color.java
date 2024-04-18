
public class Color {

    // It's literally just a struct, but Java doesn't have those so I'm using a class
    // This is why I don't like Java (among many other reasons)

    java.awt.Color col;

    public Color(int r, int g, int b) {

        r = (int)Util.clamp(r, 0, 255);
        g = (int)Util.clamp(g, 0, 255);
        b = (int)Util.clamp(b, 0, 255);

        col = new java.awt.Color(r, g, b);
    }

    public java.awt.Color toAWTColor() {
        return col;
    }

}
