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
        // Check if this is an NT or linux system
        String os = System.getProperty("os.name");
        if (os.startsWith("Windows")) {
            main.MessageBox("yeah so you cant run this on windows, go get linux", "Error");
            System.exit(1);
        } else if (os.startsWith("Linux")) {
            // Check if the system is 32 or 64 bit
            String arch = System.getProperty("os.arch");
            if (arch.equals("amd64")) {
                // 64 bit
                System.out.println("64 bit Linux detected");
            } else {
                // 32 bit
                main.MessageBox("this only runs on 64 bit systems, go get something modern", "Error");
                System.exit(1);
            }
        } else {
            // Unknown OS
            main.MessageBox("yeah so you cant run this on *unknown os*, go get linux", "Error");
            System.exit(1);
        }
        // Library is located in /native
        // Update the path to the library here
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
