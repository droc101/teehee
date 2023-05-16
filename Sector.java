import java.util.ArrayList;

public class Sector {
    public double floorHeight = 0;
    public double ceilingHeight = 1;
    public double lightLevel = 1.0;

    public String floorTexture = "null";
    public String ceilingTexture = "null";

    public ArrayList<Wall> walls = new ArrayList<Wall>();

    public ArrayList<Integer> wallIndices = new ArrayList<Integer>();

    public Sector() {

    }

    public Sector(double floorHeight, double ceilingHeight, double lightLevel, String floorTexture, String ceilingTexture) {
        this.floorHeight = floorHeight;
        this.ceilingHeight = ceilingHeight;
        this.lightLevel = lightLevel;
        this.floorTexture = floorTexture;
        this.ceilingTexture = ceilingTexture;
    }

    public void loadFromLevelWalls(ArrayList<Wall> walls) {
        // Loop over each wall index
        for (int i : wallIndices) {
            // Add the wall to the sector
            this.walls.add(walls.get(i));
        }
    }

    public boolean containsWall(Wall wall) {
        return walls.contains(wall);
    }
}
