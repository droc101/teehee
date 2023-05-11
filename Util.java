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
    
}
