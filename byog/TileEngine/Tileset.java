package byog.TileEngine;

import java.awt.Color;

/**
 * Contains constant tile objects, to avoid having to remake the same tiles in different parts of
 * the code.
 *
 * You are free to (and encouraged to) create and add your own tiles to this file. This file will
 * be turned in with the rest of your code.
 *
 * Ex:
 *      world[x][y] = Tileset.FLOOR;
 *
 * The style checker may crash when you try to style check this file due to use of unicode
 * characters. This is OK.
 */

public class Tileset {
    private static TETile PLAYER = new TETile('웃', Color.white, Color.black, "Mauricio");
    public static final TETile WALL = new TETile('█', new Color(45, 45, 45), Color.darkGray,
            "wall");
    public static final TETile FLOOR = new TETile('·', Color.lightGray, Color.black,
            "floor");
    public static final TETile NOTHING = new TETile(' ', Color.black, Color.black, "nothing");
    public static final TETile GRASS = new TETile('"', Color.green, Color.black, "grass");
    public static final TETile WATER = new TETile('≈', Color.blue, new Color(0, 0, 205), "water");
    public static final TETile FLOWER = new TETile('❀', Color.magenta, Color.pink, "flower");
    public static final TETile LOCKED_DOOR = new TETile('█', Color.orange, Color.black,
            "locked door");
    public static final TETile UNLOCKED_DOOR = new TETile('▢', Color.orange, Color.black,
            "unlocked door");
    public static final TETile SAND = new TETile('▒', Color.yellow, Color.black, "sand");
    public static final TETile MOUNTAIN = new TETile('▲', Color.gray, Color.black, "mountain");
    public static final TETile TREE = new TETile('♠', Color.green, Color.black, "tree");

    //Our own Tiles are created below
    public static final TETile LAVA = new TETile('~', Color.RED, new Color(255, 127, 50), "lava");
    public static final TETile KEY = new TETile('k', Color.YELLOW, Color.BLACK, "key");
    public static final TETile TELEPORT = new TETile('t', new Color(179, 120, 211), new Color(150, 100, 240), "teleport");
    public static final TETile GUARD = new TETile('ꆜ', Color.RED, Color.BLACK, "guard");

    /**
     * Allows outside code to access the player tile.
     * @return the PLAYER TETile
     */
    public static TETile getPlayer() {
        return PLAYER;
    }

    /**
     * Sets the PLAYER to be a new color.
     * @param color the new color of the player
     */
    public static void setPlayerColor(Color color) {
        PLAYER = new TETile('웃', color, Color.BLACK, "player");
    }
}
