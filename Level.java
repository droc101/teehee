import java.io.File; // Import the File class
import java.io.FileNotFoundException; // Import this class to handle errors
import java.util.*;

public class Level {

    public ArrayList<Vector2> verts = new ArrayList<Vector2>();
    public ArrayList<Wall> walls = new ArrayList<Wall>();

    public ArrayList<Entity> entities = new ArrayList<Entity>();
    public String name;
    public Vector2 spawn;
    public double spawnRot;

    enum LoadMode {
        HEADER,
        VERTS,
        WALLS
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
        }

        LoadMode mode = LoadMode.HEADER;
        ArrayList<int[]> tempWalls = new ArrayList<>();
        ArrayList<String> tempWallTex = new ArrayList<String>();
        // Loop over each line of the level
        for (String line : level) {
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
                    }
                }
            } else {
                switch (mode) {
                    case HEADER:
                        // Split the line into two parts seperated by =
                        String[] parts = line.split("=");
                        switch (parts[0]) {
                            case "Name":
                                name = parts[1];
                                break;
                            case "Spawn":
                                // Split the line into two parts seperated by ,
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
                }
            }
        }

        // Convert tempWalls to walls
        int i = 0;
        for (int[] wall : tempWalls) {
            walls.add(new Wall(verts.get(wall[0]), verts.get(wall[1]), tempWallTex.get(i)));
            i++;
        }
    }

}
