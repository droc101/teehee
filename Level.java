import java.io.File; // Import the File class
import java.io.FileNotFoundException; // Import this class to handle errors
import java.util.*;

public class Level {

    // This class is responsible for loading levels from files and storing them in memory.

    public ArrayList<Entity> entities = new ArrayList<Entity>();

    public ArrayList<Sector> sectors = new ArrayList<Sector>();
    public String name;
    public Vector2 spawn;
    public double spawnRot;

    sound bgm;

    enum LoadMode {
        HEADER,
        VERTS,
        WALLS,
        SECTORS
    }

    public Level(String path, Player player) {
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
        List<Vector2> verts = new ArrayList<Vector2>();
        List<Wall> walls = new ArrayList<Wall>();
        // Loop over each line of the level
        for (String line : level) {
            if (line.charAt(0) == '#') {
                continue;
            } else if (line.charAt(0) == '[') {
                // Get the mode from the line
                String modeStr = line.substring(1, line.length() - 1);
                switch (modeStr) {
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
            } else {
                // Split the line by commas
                String[] parts = line.split(",");
                switch (mode) {
                    case HEADER:
                        // Get the name of the level
                        name = parts[0];
                        // Get the spawn position
                        spawn = new Vector2(Double.parseDouble(parts[1]), Double.parseDouble(parts[2]));
                        // Get the spawn rotation
                        spawnRot = Double.parseDouble(parts[3]);

                        player.position = spawn.clone();
                        player.rotation = spawnRot;
                        player.health = 10;
                        player.ammo = 0;

                        String bgmPath = parts[4];
                        bgm = new sound();
                        bgm.play("audio/" + bgmPath + ".wav", true);
                        break;
                    case VERTS:
                        // Add a new vertex to the level
                        verts.add(new Vector2(Double.parseDouble(parts[0]), Double.parseDouble(parts[1])));
                        break;
                    case WALLS:
                        // Add a new wall to the level
                        Wall wall = new Wall();
                        wall.vertA = verts.get(Integer.parseInt(parts[0]));
                        wall.vertB = verts.get(Integer.parseInt(parts[1]));
                        wall.texture = parts[2];

                        walls.add(wall);
                        break;
                    case SECTORS:
                        // Add a new sector to the level
                        Sector sector = new Sector();
                        // split the first part by semicolons
                        String[] walll = parts[0].split(";");
                        for (String wallStr : walll) {
                            sector.walls.add(walls.get(Integer.parseInt(wallStr)));
                        }
                        sectors.add(sector);
                        break;
                }
            }
        }

    }

    public Sector findSector(Vector2 pos) {
        for (Sector sector : sectors) {
            if (sector.containsPoint(pos)) {
                return sector;
            }
        }
        return null;
    }

    public ArrayList<Wall> GetAllWalls() {
        ArrayList<Wall> walls = new ArrayList<Wall>();
        for (Sector sector : sectors) {
            for (Wall wall : sector.walls) {
                walls.add(wall);
            }
        }
        for (Entity entity : entities) {
            if (entity.doCollide) {
                walls.add(entity.makeWall());
            }
        }
        return walls;
    }

}
