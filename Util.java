public class Util {

    // This class contains a bunch of utility functions that are used throughout the game

    public static void DrawInt(FrameBuffer fb, Vector2 pos, int value) {
        // Convert the value to a string
        String str = Integer.toString(value);
        int xpos = (int) pos.x;
        for (int i = 0; i < str.length(); i++) {
            // Get the character at the current index
            char c = str.charAt(i);

            // Get the index of the character in the font
            int charIndex = (int)c - '0';

            // Calculate the x and y position of the character in the font
            fb.BlitSpriteRect("fon/numbers", new Vector2(xpos, pos.y), new Vector2(charIndex * 16, 0), new Vector2(16, 32));

            // Increment the x position
            xpos += 16;
        }
    }

    public static void DrawString(FrameBuffer fb, Vector2 pos, String value) {
        String letterOrder = "abcdefghijklmnopqrstuvwxyz0123456789.";
        value = value.toLowerCase();
        int xpos = (int) pos.x;
        for (int i = 0; i < value.length(); i++) {
            // Get the character at the current index
            char c = value.charAt(i);

            if (c == '-') {
                c = 'm';
            }

            if (c == ' ') {
                xpos += 8;
                continue;
            }

            // Get the index of the character in the font
            int charIndex = letterOrder.indexOf(c);

            // Calculate the x and y position of the character in the font
            fb.BlitSpriteRect("fon/all", new Vector2(xpos, pos.y), new Vector2(charIndex * 16, 0), new Vector2(16, 32));

            // Increment the x position
            xpos += 16;
        }
    }

    public static double wrap(double value, double minValue, double maxValue) {
        if (value < minValue) {
            double range = maxValue - minValue;
            value = ((value - minValue) % range + range) % range + minValue;
        } else if (value >= maxValue) {
            double range = maxValue - minValue;
            value = ((value - minValue) % range + range) % range + minValue;
        }
        return value;
    }

    public static double clamp(double value, double minValue, double maxValue) {
        if (value < minValue) {
            value = minValue;
        } else if (value > maxValue) {
            value = maxValue;
        }
        return value;
    }

    public static void LoadLibNative() {
        String os = System.getProperty("os.name");
        if (os.startsWith("Windows")) {
            String arch = System.getProperty("os.arch");
            if (arch.equals("amd64")) {
                System.out.println("64 bit NT detected");
                String path = System.getProperty("user.dir") + "/native/libnative.dll";
                try {
                    System.out.println("Loading NT native library from: " + path);
                    System.load(path);
                } catch (Exception e) {
                    System.out.println("Failed to load NT native library from: " + path);
                    main.MessageBox("Failed to load NT native library from: " + path, "Error");
                    System.exit(1);
                }
            } else {
                main.MessageBox("this only runs on 64 bit systems, go get something modern", "Error");
                System.exit(1);
            }
        } else if (os.startsWith("Linux")) {
            String arch = System.getProperty("os.arch");
            if (arch.equals("amd64")) {
                System.out.println("64 bit Linux detected");
                String path = System.getProperty("user.dir") + "/native/libnative.so";
                try {
                    System.out.println("Loading native library from: " + path);
                    System.load(path);
                } catch (Exception e) {
                    System.out.println("Failed to load native library from: " + path);
                    main.MessageBox("Failed to load native library from: " + path, "Error");
                    System.exit(1);
                }
            } else {
                main.MessageBox("this only runs on 64 bit systems, go get something modern", "Error");
                System.exit(1);
            }
        } else {
            // Unknown OS
            main.MessageBox("yeah so you cant run this on *unknown os*, go get linux", "Error");
            System.exit(1);
        }

    }
    
}
