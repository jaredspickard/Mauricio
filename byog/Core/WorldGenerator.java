package byog.Core;

import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.util.Random;
import java.util.ArrayList;

/**
 * @source The algorithm that we developed to create hallways was inspired by 'phidinh6' on Reddit.
 *         His explanation of dungeon generation included hallway creation using Delauney
 *         Triangulation. We then attempted to implement our hallways using a similar method, yet
 *         we could not figure it out so we devised an alternate method and implemented that
 *         instead.
 */
public class WorldGenerator {

    private TETile[][] world;
    private long seed;
    private ArrayList<Room> rooms;
    private ArrayList<Line> lines;
    private int playerXPos;
    private int playerYPos;
    private int teleport1XPos;
    private int teleport1YPos;
    private int teleport2XPos;
    private int teleport2YPos;
    private int guard1XPos;
    private int guard2XPos;
    private int guard3XPos;
    private int guard1YPos;
    private int guard2YPos;
    private int guard3YPos;

    public WorldGenerator(long seed) {
        this.seed = seed;
        world = new TETile[Game.WIDTH][Game.HEIGHT];
        rooms = new ArrayList<>();
        lines = new ArrayList<>();
    }

    /**
     * Generates a pseudorandom world based on the seed.
     * @return a randomly generated 'world' of TETile[][]
     */
    public TETile[][] generateWorld() {
        initializeWorld();
        Random random = new Random(seed);
        generateRooms(random);
        updateLines();
        generateHalls();
        placeItems(random);
        return world;
    }

    /**
     * Creates an empty world of WIDTH and HEIGHT
     */
    private void initializeWorld() {
        for (int r = 0; r < Game.WIDTH; r++) {
            for (int c = 0; c < Game.HEIGHT; c++) {
                world[r][c] = Tileset.LAVA;
            }
        }
    }

    //---------------------------------------------------------------------------------------------
    //                                          Rooms
    //---------------------------------------------------------------------------------------------

    /**
     * Generates pseudorandom rooms around the map.
     * @param random a Random object based on the seed variable
     */
    private void generateRooms(Random random) {
        int numOfRooms = 20 + random.nextInt(11);
        Room holder; //acts as a placeholder for the room that was just added (allows line drawing)
        for (int i = 0; i < 3; i++) {
            addRoom(random);
        }
        createLines(rooms.get(0), 0);
        createLines(rooms.get(1), 1);
        //createLines(rooms.get(2), 2);
        for (int i = 3; i < numOfRooms; i++) {
            holder = addRoom(random);
            createLines(holder, i);
        }
    }

    /**
     * Creates and adds a single room to the world.
     * @param random a Random object based on the seed variable
     */
    private Room addRoom(Random random) {
        int roomWidth = 6 + random.nextInt(9);
        int roomHeight = 6 + random.nextInt(9);
        int xCoord = random.nextInt(Game.WIDTH - roomWidth);
        int yCoord = random.nextInt(Game.HEIGHT - roomHeight);
        while (!checkRoomPlacement(roomWidth, roomHeight, xCoord, yCoord)) {
            return addRoom(random);
        }
        for (int x = xCoord; x < roomWidth + xCoord; x++) {
            for (int y = yCoord; y < roomHeight + yCoord; y++) {
                if (x == xCoord || x == roomWidth + xCoord - 1
                        || y == yCoord || y == roomHeight + yCoord - 1) {
                    world[x][y] = Tileset.WALL;
                } else {
                    world[x][y] = Tileset.FLOOR;
                }
            }
        }
        Room justCreated = new Room(xCoord, yCoord, roomHeight, roomWidth);
        rooms.add(justCreated);
        return justCreated;
    }

    /**
     * Checks whether or not a room can be placed at this specific location.
     * @param rWidth width of the room
     * @param rHeight height of the room
     * @param x x coordinate
     * @param y y coordinate
     * @return true if the room can be placed there, false if not
     */
    private boolean checkRoomPlacement(int rWidth, int rHeight, int x, int y) {
        int[] center = {(2 * x + rWidth) / 2, (2 * y + rHeight) / 2};
        if (world[center[0]][center[1]] != Tileset.LAVA) {
            return false;
        }
        for (int xP = x; xP < rWidth + x; xP++) {
            if (world[xP][y] != Tileset.LAVA || world[xP][y + rHeight - 1] != Tileset.LAVA) {
                return false;
            }
        }
        for (int yP = y; yP < rHeight + y; yP++) {
            if (world[x][yP] != Tileset.LAVA || world[x + rWidth - 1][yP] != Tileset.LAVA) {
                return false;
            }
        }
        return true;
    }

    //---------------------------------------------------------------------------------------------
    //                                          Lines
    //---------------------------------------------------------------------------------------------

    /**
     * Creates lines between the given room and its closest two rooms
     * @param room the room we want to give lines to
     * @param selfIndex the index of the room inside of the rooms ArrayList
     */
    private void createLines(Room room, int selfIndex) {
        //find indices 2 closest distances
        int minIndex = findClosest(room, selfIndex, selfIndex);
        int minIndex2 = findClosest(room, selfIndex, minIndex);
        //find two closest rooms (based on distances)
        Room room1 = rooms.get(minIndex);
        Room room2 = rooms.get(minIndex2);
        //delete the line between the two rooms
        room1.deleteLine(room2);
        room2.deleteLine(room1);
        //add a line from this room to the two rooms
        room.addLine(room1);
        room.addLine(room2);
        //add a line from the the two rooms to this room
        room1.addLine(room);
        room2.addLine(room);
    }

    /**
     * Returns the index of the closest room in rooms that is not roomIndex or minIndex.
     * @param room the room we are comparing to other rooms
     * @param roomIndex the index of the room itself
     * @param minIndex either the index of the room itself or the index of the closest room
     * @return the index of the closest room in rooms
     */
    private int findClosest(Room room, int roomIndex, int minIndex) {
        double closestDistance = Double.MAX_VALUE;
        int closestIndex = 0;
        for (int i = 0; i < rooms.size(); i++) {
            if (!(i == roomIndex || i == minIndex)) {
                double currentDistance = room.calculateDistance(rooms.get(i));
                if (currentDistance < closestDistance) {
                    closestIndex = i;
                    closestDistance = currentDistance;
                }
            }
        }
        return closestIndex;
    }

    /**
     * Updates the lines ArrayList by iterating through the rooms ArrayList.
     */
    private void updateLines() {
        for (int i = 0; i < rooms.size(); i++) {
            Room room = rooms.get(i);
            ArrayList<Room> roomLines = room.getLines();
            for (int j = 0; j < roomLines.size(); j++) {
                Line line = new Line(room, roomLines.get(j));
                if (!checkLineExistence(line)) {
                    lines.add(line); // adds a line if that line does not already exist in lines
                }
            }
        }
    }

    /**
     * Checks whether or not a given line exists in lines.
     * @param line the line we are checking
     * @return true if the line already exists, false if not
     */
    private boolean checkLineExistence(Line line) {
        for (int i = 0; i < lines.size(); i++) {
            if (lines.get(i).equals(line)) {
                return true;
            }
        }
        return false;
    }

    //---------------------------------------------------------------------------------------------
    //                                          Hallways
    //---------------------------------------------------------------------------------------------

    /**
     * Generates halls on the world 2d array.
     */
    private void generateHalls() {
        for (int i = 0; i < lines.size(); i++) {
            addHall(lines.get(i));
        }
    }

    /**
     * Draws the hall on the world.
     * @param line the line that the hall is based on
     */
    private void addHall(Line line) {
        int x1 = line.getXStart();
        int x2 = line.getXStart() + line.getXComponent();
        int y1 = line.getYStart();
        int y2 = line.getYStart() + line.getYComponent();
        boolean xPositive = (line.getXComponent() > 0);
        boolean yPositive = (line.getYComponent() > 0);
        while (x1 != x2) {
            world[x1][y1] = Tileset.FLOOR;
            if (world[x1][y1 + 1] != Tileset.FLOOR) {
                world[x1][y1 + 1] = Tileset.WALL;
            }
            if (world[x1][y1 - 1] != Tileset.FLOOR) {
                world[x1][y1 - 1] = Tileset.WALL;
            }
            if (xPositive) {
                x1++;
            } else {
                x1--;
            }
        }
        if (world[x1][y1 + 1] != Tileset.FLOOR) {
            world[x1][y1 + 1] = Tileset.WALL;
        }
        if (world[x1][y1 - 1] != Tileset.FLOOR) {
            world[x1][y1 - 1] = Tileset.WALL;
        }
        int storeY1 = y1;
        if (yPositive) {
            storeY1--;
        } else {
            storeY1++;
        }
        while (y1 != y2) {
            world[x1][y1] = Tileset.FLOOR;
            if (world[x1 + 1][y1] != Tileset.FLOOR) {
                world[x1 + 1][y1] = Tileset.WALL;
            }
            if (world[x1 - 1][y1] != Tileset.FLOOR) {
                world[x1 - 1][y1] = Tileset.WALL;
            }
            if (yPositive) {
                y1++;
            } else {
                y1--;
            }
        }
        if (world[x1 + 1][storeY1] != Tileset.FLOOR) {
            world[x1 + 1][storeY1] = Tileset.WALL;
        }
        if (world[x1 - 1][storeY1] != Tileset.FLOOR) {
            world[x1 - 1][storeY1] = Tileset.WALL;
        }
    }

    //---------------------------------------------------------------------------------------------
    //                                    Item Placement
    //---------------------------------------------------------------------------------------------

    /**
     * Places the player in a pseudorandomly selected room.
     * @param random Random object used to select placement.
     */
    private void placeItems(Random random) {
        int roomNum = random.nextInt(rooms.size());
        placePlayer(roomNum);
        for (int i = 0; i < 3; i++) {
            roomNum = updateRoomNum(roomNum);
            placeKey(roomNum);
            if (i == 0) {
                guard1XPos = 1 + rooms.get(roomNum).getXCenter();
                guard1YPos = rooms.get(roomNum).getYCenter();
            } else if (i == 1) {
                guard2XPos = 1 + rooms.get(roomNum).getXCenter();
                guard2YPos = rooms.get(roomNum).getYCenter();
            } else if (i == 2) {
                guard3XPos = 1 + rooms.get(roomNum).getXCenter();
                guard3YPos = rooms.get(roomNum).getYCenter();
            }
        }
        for (int i = 0; i < 2; i++) {
            roomNum = updateRoomNum(roomNum);
            if (i == 0) {
                teleport1XPos = rooms.get(roomNum).getXCenter();
                teleport1YPos = rooms.get(roomNum).getYCenter();
            } else {
                teleport2XPos = rooms.get(roomNum).getXCenter();
                teleport2YPos = rooms.get(roomNum).getYCenter();
            }
            placeTeleport(roomNum);
        }
    }

    /**
     * Places the player in a pseudorandomly selected room.
     * @param roomNum the index of the room in rooms
     */
    private void placePlayer(int roomNum) {
        int xPos = rooms.get(roomNum).getXCenter();
        int yPos = rooms.get(roomNum).getYCenter();
        playerXPos = xPos;
        playerYPos = yPos;
        world[xPos][yPos] = Tileset.getPlayer();
    }

    /**
     * Places a key in a pseudorandomly selected room.
     * @param roomNum the index of the room in rooms
     */
    private void placeKey(int roomNum) {
        int xPos = rooms.get(roomNum).getXCenter();
        int yPos = rooms.get(roomNum).getYCenter();
        world[xPos][yPos] = Tileset.KEY;
        placeGuard(xPos, yPos);
    }

    /**
     * Places a teleportation pad in a pseudorandomly selected room.
     * @param roomNum the index of the room in rooms
     */
    private void placeTeleport(int roomNum) {
        int xPos = rooms.get(roomNum).getXCenter();
        int yPos = rooms.get(roomNum).getYCenter();
        world[xPos][yPos] = Tileset.TELEPORT;
    }

    /**
     * Updates the index 'roomNum' to avoid any out of bounds errors.
     */
    private int updateRoomNum(int roomNum) {
        if (roomNum == rooms.size() - 1) {
            return 0;
        }
        return roomNum + 1;
    }

    /**
     * Places a guard in the specified x and y positions
     */
    private void placeGuard(int xPos, int yPos) {
        world[xPos + 1][yPos] = Tileset.GUARD;
    }

    /**
     * Returns the x position of the player.
     */
    public int getPlayerXPos() {
        return playerXPos;
    }

    /**
     * Returns the x position of the player.
     */
    public int getPlayerYPos() {
        return playerYPos;
    }

    /**
     * Returns the x position of the first teleport tab.
     */
    public int getTeleport1XPos() {
        return teleport1XPos;
    }

    /**
     * Returns the y position of the first teleport tab.
     */
    public int getTeleport1YPos() {
        return teleport1YPos;
    }

    /**
     * Returns the x position of the second teleport tab.
     */
    public int getTeleport2XPos() {
        return teleport2XPos;
    }

    /**
     * Returns the y position of the first teleport tab.
     */
    public int getTeleport2YPos() {
        return teleport2YPos;
    }

    /**
     * Returns the x position of guard 1.
     */
    public int getGuard1XPos() {
        return guard1XPos;
    }

    /**
     * Returns the x position of guard 2.
     */
    public int getGuard2XPos() {
        return guard2XPos;
    }

    /**
     * Returns the x position of guard 3.
     */
    public int getGuard3XPos() {
        return guard3XPos;
    }

    /**
     * Returns the y position of guard 1.
     */
    public int getGuard1YPos() {
        return guard1YPos;
    }

    /**
     * Returns the y position of guard 2.
     */
    public int getGuard2YPos() {
        return guard2YPos;
    }

    /**
     * Returns the y position of guard 3.
     */
    public int getGuard3YPos() {
        return guard3YPos;
    }
}
