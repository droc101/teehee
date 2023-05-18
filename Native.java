public class Native {
    // Native function stubs called upon by other classes for faster maths
    // See the "native" folder for the actual implementation (in C)
    public static native double[] RayCast(double x1, double y1, double x2, double y2, double fx, double fy, double fr); // Raycast function

    public static native int[] render_col(double[] fromPos, double fromRot, int col, int screenH, double minWallDist, int rc, Sector s);

}
