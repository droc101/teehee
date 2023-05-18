public class Util {
    // Remap range function
    static double Remap(double input, double inL, double inH, double outL, double outH) {
        // Calculate the input range
        double inRange = inH - inL;
    
        // Calculate the output range
        double outRange = outH - outL;
    
        // Calculate the normalized input value
        double normalizedInput = (input - inL) / inRange;
    
        // Remap the normalized input value to the output range
        double output = outL + (normalizedInput * outRange);
    
        // Return the remapped value
        return output;
    }

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

    public static void LoadLibNative() {
        String os = System.getProperty("os.name");
        if (os.startsWith("Windows")) {
            main.MessageBox("yeah so you cant run this on windows, go get linux", "Error");
            System.exit(1);
        } else if (os.startsWith("Linux")) {
            String arch = System.getProperty("os.arch");
            if (arch.equals("amd64")) {
                System.out.println("64 bit Linux detected");
            } else {
                main.MessageBox("this only runs on 64 bit systems, go get something modern", "Error");
                System.exit(1);
            }
        } else {
            // Unknown OS
            main.MessageBox("yeah so you cant run this on *unknown os*, go get linux", "Error");
            System.exit(1);
        }
        String path = System.getProperty("user.dir") + "/native/libnative.so";
        try {
            System.out.println("Loading native library from: " + path);
            System.load(path);
        } catch (Exception e) {
            System.out.println("Failed to load native library from: " + path);
            main.MessageBox("Failed to load native library from: " + path, "Error");
            System.exit(1);
        }
    }
    
}
