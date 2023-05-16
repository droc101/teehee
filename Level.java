import java.io.File; // Import the File class
import java.io.FileNotFoundException; // Import this class to handle errors
import java.util.*;

public class Level {

    public ArrayList<Vector2> verts = new ArrayList<Vector2>();
    public ArrayList<Wall> walls = new ArrayList<Wall>();

    public ArrayList<Entity> entities = new ArrayList<Entity>();

    public ArrayList<Sector> sectors = new ArrayList<Sector>();
    public String name;
    public Vector2 spawn;
    public double spawnRot;

    enum LoadMode {
        HEADER,
        VERTS,
        WALLS,
        SECTORS
    }

    public Level(String path) {
        ArrayList<String> level = new ArrayList<>();
        // Read the contents of the file at levels/[path].rlev
        try {
            File myObj = new File("levels/" + path + ".rlev");
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                level.add(myReader.nextLine());
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
            main.MessageBox("Level File Not Found!");
        }

        LoadMode mode = LoadMode.HEADER;
        ArrayList<int[]> tempWalls = new ArrayList<>();
        ArrayList<String> tempWallTex = new ArrayList<String>();
        // Loop over each line of the level
        for (String line : level) {
            if (line.charAt(0) == '#') {
                continue;
            }
            // Check if the ine begins with "["
            if (line.charAt(0) == '[') {
                // Check if the line ends with "]"
                if (line.charAt(line.length() - 1) == ']') {
                    // Remove the brackets
                    line = line.substring(1, line.length() - 1);

                    // Set the load mode
                    switch (line) {
                        case "Level":
                            mode = LoadMode.HEADER;
                            break;
                        case "Verts":
                            mode = LoadMode.VERTS;
                            break;
                        case "Walls":
                            mode = LoadMode.WALLS;
                            break;
                        case "Sectors":
                            mode = LoadMode.SECTORS;
                            break;
                    }
                }
            } else {
                switch (mode) {
                    case HEADER:
                        // Split the line into two parts separated by =
                        String[] parts = line.split("=");
                        switch (parts[0]) {
                            case "Name":
                                name = parts[1];
                                break;
                            case "Spawn":
                                // Split the line into two parts separated by ,
                                String[] spawnParts = parts[1].split(",");
                                // Create a new vector from the two parts
                                spawn = new Vector2(Double.parseDouble(spawnParts[0]),
                                        Double.parseDouble(spawnParts[1]));
                                // Set the spawn rotation
                                spawnRot = Double.parseDouble(spawnParts[2]);
                                break;
                        }
                        break;
                    case VERTS:
                        // Split the line into two parts
                        parts = line.split(",");
                        // Create a new vector from the two parts
                        Vector2 vert = new Vector2(Double.parseDouble(parts[0]), Double.parseDouble(parts[1]));
                        // Add the vector to the verts list
                        verts.add(vert);
                        break;
                    case WALLS:
                        // Split the line into two parts
                        parts = line.split(",");
                        // Add to tempWalls
                        tempWalls.add(new int[]{Integer.parseInt(parts[0]), Integer.parseInt(parts[1])});
                        tempWallTex.add(parts[2]);
                        break;
                    case SECTORS:
                        // Split the line into two parts
                        parts = line.split(",");
                        // Create a new sector
                        Sector sector = new Sector();
                        // Split part 0 by semicolon
                        String[] sectorWalls = parts[0].split(";");

                        // Loop over each wall index and add it to the sector
                        for (String wall : sectorWalls) {
                            sector.wallIndices.add(Integer.parseInt(wall));
                        }

                        // Set the floor texture
                        sector.floorTexture = parts[1];
                        // Set the ceiling texture
                        sector.ceilingTexture = parts[2];
                        // Set the light level
                        sector.lightLevel = Double.parseDouble(parts[3]);
                        // Set the floor height
                        sector.floorHeight = Double.parseDouble(parts[4]);
                        // Set the ceiling height
                        sector.ceilingHeight = Double.parseDouble(parts[5]);

                        // Add the sector to the sectors list
                        sectors.add(sector);
                        break;
                }
            }
        }

        // Convert tempWalls to walls
        int i = 0;
        for (int[] wall : tempWalls) {
            walls.add(new Wall(verts.get(wall[0]), verts.get(wall[1]), tempWallTex.get(i)));
            i++;
        }

        // Loop over each sector
        for (Sector sector : sectors) {
            // Load the sector from the level walls
            sector.loadFromLevelWalls(walls);
        }
    }

}
