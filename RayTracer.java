public class RayTracer {
    Level level;

    public RayTracer(Level level) {
        this.level = level;
    }

    public void RenderCol(FrameBuffer buffer, Vector2 FromPos, double FromRot, int col, int screenH) {

        // Get the angle of the column
        // FOV is 90 degrees
        double angle = Math.atan2(col - buffer.width / 2, buffer.width / 2) + FromRot;

        // Create a ray
        Ray ray = new Ray(FromPos, angle);
        // Loop over each wall until a wall is hit
        for (Wall wall : level.walls) {
            // Check if the ray intersects the wall
            if (ray.Intersects(wall)) {
                // Get the intersection point
                Vector2 intersection = ray.Intersection(wall);

                // Get the distance from the ray origin to the intersection point
                double distance = Vector2.Distance(FromPos, intersection) * Math.cos(angle - FromRot);

                // Get the height of the wall on screen
                double height = screenH / distance;

                // Get the y position of the wall
                double y = (buffer.height - height) / 2;

                // Calcuate the shade of the wall based on the camera angle and wall normal
                double shade = Math.abs(Math.cos((FromRot + (1.5*Math.PI)) - wall.getAngle()));
                // Make sure the shade is between 40 and 255
                shade = Math.max(0.4, Math.min(1, shade));

                // Calculate the color of the wall
                int r = (int) (255 * shade);
                int g = (int) (255 * shade);
                int b = (int) (255 * shade);
                Color color = new Color(r, g, b);

                // Draw the wall
                buffer.drawRect(col, (int) y, 1, (int) height, color);

                // Return
                return;
            }
        }
    }

    // Ray trace from a position and rotation until a wall is hit
    public Vector2 RayTrace(Vector2 FromPos, double FromRot) {
        // Create a ray
        Ray ray = new Ray(FromPos, FromRot);

        // Loop over each wall
        for (Wall wall : level.walls) {
            return ray.Intersection(wall);
        }

        // Return null if no wall was hit
        return null;
    }
}
