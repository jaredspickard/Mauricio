package byog.Core;

import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;
import java.io.Serializable;
import edu.princeton.cs.introcs.StdAudio;


public class World implements Serializable {
    private TETile[][] world;
    private int startingXPos;
    private int startingYPos;
    private int playerXPos;
    private int playerYPos;
    private int numKeys;
    private int numHearts;
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
    private int key1XPos;
    private int key2XPos;
    private int key3XPos;
    private int key1YPos;
    private int key2YPos;
    private int key3YPos;
    private static final long serialVersionUID = 456456456456L;

    public World(TETile[][] world, int playerXPos, int playerYPos, int t1X, int t1Y, int t2X, int t2Y, int guard1XPos, int guard2XPos, int guard3XPos, int guard1YPos, int guard2YPos, int guard3YPos) {
        this.world = world;
        this.playerXPos = playerXPos;
        this.playerYPos = playerYPos;
        this.startingXPos = playerXPos;
        this.startingYPos = playerYPos;
        numKeys = 0;
        numHearts = 3;
        teleport1XPos = t1X;
        teleport1YPos = t1Y;
        teleport2XPos = t2X;
        teleport2YPos = t2Y;
        this.guard1XPos = guard1XPos;
        this.guard2XPos = guard2XPos;
        this.guard3XPos = guard3XPos;
        this.guard1YPos = guard1YPos;
        this.guard2YPos = guard2YPos;
        this.guard3YPos = guard3YPos;
        this.key1XPos = guard1XPos - 1;
        this.key1YPos = guard1YPos;
        this.key2XPos = guard2XPos - 1;
        this.key2YPos = guard2YPos;
        this.key3XPos = guard3XPos - 1;
        this.key3YPos = guard3YPos;
    }

    public TETile[][] getWorld() {
        return world;
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
     * Returns the number of keys the player has collected.
     */
    public int getNumKeys() {
        return numKeys;
    }

    /**
     * Returns the number of lives the player has remaining.
     */
    public int getNumHearts() {
        return numHearts;
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

    /**
     * Makes the move, if possible, for the player
     * @param move the direction the player desires to move in
     * @return what direction the player actually moves
     */
    public String makeMove(char move) {
        if (move == 'w') {
            if (world[playerXPos][playerYPos + 1].equals(Tileset.FLOOR)) {
                world[playerXPos][playerYPos] = Tileset.FLOOR;
                world[playerXPos][playerYPos + 1] = Tileset.getPlayer();
                playerYPos++;
                return "w";
            } else if (world[playerXPos][playerYPos + 1].equals(Tileset.KEY)) {
                world[playerXPos][playerYPos] = Tileset.FLOOR;
                world[playerXPos][playerYPos + 1] = Tileset.getPlayer();
                numKeys++;
                playerYPos++;
                StdAudio.play("/byog/Media/coin.wav");
                return "w";
            } else if (world[playerXPos][playerYPos + 1].equals(Tileset.TELEPORT)) {
                StdAudio.play("/byog/Media/teleport.wav");
                if (playerXPos == teleport1XPos && playerYPos + 1 == teleport1YPos) {
                    world[playerXPos][playerYPos] = Tileset.FLOOR;
                    world[teleport2XPos][teleport2YPos + 1] = Tileset.getPlayer();
                    playerXPos = teleport2XPos;
                    playerYPos = teleport2YPos + 1;
                    return "w";
                } else {
                    world[playerXPos][playerYPos] = Tileset.FLOOR;
                    world[teleport1XPos][teleport1YPos + 1] = Tileset.getPlayer();
                    playerXPos = teleport1XPos;
                    playerYPos = teleport1YPos + 1;
                    return "w";
                }
            } else if (world[playerXPos][playerYPos + 1].equals(Tileset.GUARD)) {
                world[playerXPos][playerYPos] = Tileset.FLOOR;
                world[startingXPos][startingYPos] = Tileset.getPlayer();
                playerXPos = startingXPos;
                playerYPos = startingYPos;
                numHearts--;
                if (numHearts > 0) {
                    StdAudio.play("/byog/Media/lose.wav");
                }
                return "w";
            }
        } else if (move == 'a') {
            if (world[playerXPos - 1][playerYPos].equals(Tileset.FLOOR)) {
                world[playerXPos][playerYPos] = Tileset.FLOOR;
                world[playerXPos - 1][playerYPos] = Tileset.getPlayer();
                playerXPos--;
                return "a";
            } else if (world[playerXPos - 1][playerYPos].equals(Tileset.KEY)) {
                world[playerXPos][playerYPos] = Tileset.FLOOR;
                world[playerXPos - 1][playerYPos] = Tileset.getPlayer();
                numKeys++;
                playerXPos--;
                StdAudio.play("/byog/Media/coin.wav");
                return "a";
            } else if (world[playerXPos - 1][playerYPos].equals(Tileset.TELEPORT)) {
                StdAudio.play("/byog/Media/teleport.wav");
                if (playerXPos - 1 == teleport1XPos && playerYPos == teleport1YPos) {
                    world[playerXPos][playerYPos] = Tileset.FLOOR;
                    world[teleport2XPos - 1][teleport2YPos] = Tileset.getPlayer();
                    playerXPos = teleport2XPos - 1;
                    playerYPos = teleport2YPos;
                    return "a";
                } else {
                    world[playerXPos][playerYPos] = Tileset.FLOOR;
                    world[teleport1XPos - 1][teleport1YPos] = Tileset.getPlayer();
                    playerXPos = teleport1XPos - 1;
                    playerYPos = teleport1YPos;
                    return "a";
                }
            } else if (world[playerXPos - 1][playerYPos].equals(Tileset.GUARD)) {
                world[playerXPos][playerYPos] = Tileset.FLOOR;
                world[startingXPos][startingYPos] = Tileset.getPlayer();
                numHearts--;
                if (numHearts > 0) {
                    StdAudio.play("/byog/Media/lose.wav");
                }
                playerXPos = startingXPos;
                playerYPos = startingYPos;
                return "a";
            }
        } else if (move == 's') {
            if (world[playerXPos][playerYPos - 1].equals(Tileset.FLOOR)) {
                world[playerXPos][playerYPos] = Tileset.FLOOR;
                world[playerXPos][playerYPos - 1] = Tileset.getPlayer();
                playerYPos--;
                return "s";
            } else if (world[playerXPos][playerYPos - 1].equals(Tileset.KEY)) {
                world[playerXPos][playerYPos] = Tileset.FLOOR;
                world[playerXPos][playerYPos - 1] = Tileset.getPlayer();
                numKeys++;
                playerYPos--;
                StdAudio.play("/byog/Media/coin.wav");
                return "s";
            } else if (world[playerXPos][playerYPos - 1].equals(Tileset.TELEPORT)) {
                StdAudio.play("/byog/Media/teleport.wav");
                if (playerXPos == teleport1XPos && playerYPos - 1 == teleport1YPos) {
                    world[playerXPos][playerYPos] = Tileset.FLOOR;
                    world[teleport2XPos][teleport2YPos - 1] = Tileset.getPlayer();
                    playerXPos = teleport2XPos;
                    playerYPos = teleport2YPos - 1;
                    return "s";
                } else {
                    world[playerXPos][playerYPos] = Tileset.FLOOR;
                    world[teleport1XPos][teleport1YPos - 1] = Tileset.getPlayer();
                    playerXPos = teleport1XPos;
                    playerYPos = teleport1YPos - 1;
                    return "s";
                }
            } else if (world[playerXPos][playerYPos - 1].equals(Tileset.GUARD)) {
                world[playerXPos][playerYPos] = Tileset.FLOOR;
                world[startingXPos][startingYPos] = Tileset.getPlayer();
                numHearts--;
                if (numHearts > 0) {
                    StdAudio.play("/byog/Media/lose.wav");
                }
                playerXPos = startingXPos;
                playerYPos = startingYPos;
                return "s";
            }
        } else if (move == 'd') {
            if (world[playerXPos + 1][playerYPos].equals(Tileset.FLOOR)) {
                world[playerXPos][playerYPos] = Tileset.FLOOR;
                world[playerXPos + 1][playerYPos] = Tileset.getPlayer();
                playerXPos++;
                return "d";
            } else if (world[playerXPos + 1][playerYPos].equals(Tileset.KEY)) {
                world[playerXPos][playerYPos] = Tileset.FLOOR;
                world[playerXPos + 1][playerYPos] = Tileset.getPlayer();
                numKeys++;
                playerXPos++;
                StdAudio.play("/byog/Media/coin.wav");
                return "d";
            } else if (world[playerXPos + 1][playerYPos].equals(Tileset.TELEPORT)) {
                StdAudio.play("/byog/Media/teleport.wav");
                if (playerXPos + 1 == teleport1XPos && playerYPos == teleport1YPos) {
                    world[playerXPos][playerYPos] = Tileset.FLOOR;
                    world[teleport2XPos + 1][teleport2YPos] = Tileset.getPlayer();
                    playerXPos = teleport2XPos + 1;
                    playerYPos = teleport2YPos;
                    return "d";
                } else {
                    world[playerXPos][playerYPos] = Tileset.FLOOR;
                    world[teleport1XPos + 1][teleport1YPos] = Tileset.getPlayer();
                    playerXPos = teleport1XPos + 1;
                    playerYPos = teleport1YPos;
                    return "d";
                }
            } else if (world[playerXPos + 1][playerYPos].equals(Tileset.GUARD)) {
                world[playerXPos][playerYPos] = Tileset.FLOOR;
                world[startingXPos][startingYPos] = Tileset.getPlayer();
                numHearts--;
                if (numHearts > 0) {
                    StdAudio.play("/byog/Media/lose.wav");
                }
                playerXPos = startingXPos;
                playerYPos = startingYPos;
                return "d";
            }
        } else if (move == ':') {
            return ":";
        } else {
            return "";
        }
        return "";
    }

    /**
     * Makes guard 1 move
     * @param pos guard 1 will move to the corresponding position around the key
     */
    private void makeGuard1Move(int pos) {
        world[guard1XPos][guard1YPos] = Tileset.FLOOR;
        if (pos == 0) {
            world[key1XPos - 1][key1YPos + 1] = Tileset.GUARD;
            guard1XPos = key1XPos - 1;
            guard1YPos = key1YPos + 1;
        } else if (pos == 1) {
            world[key1XPos][key1YPos + 1] = Tileset.GUARD;
            guard1XPos = key1XPos;
            guard1YPos = key1YPos + 1;
        } else if (pos == 2) {
            world[key1XPos + 1][key1YPos + 1] = Tileset.GUARD;
            guard1XPos = key1XPos + 1;
            guard1YPos = key1YPos + 1;
        } else if (pos == 3) {
            world[key1XPos - 1][key1YPos] = Tileset.GUARD;
            guard1XPos = key1XPos - 1;
            guard1YPos = key1YPos;
        } else if (pos == 4) {
            world[key1XPos + 1][key1YPos] = Tileset.GUARD;
            guard1XPos = key1XPos + 1;
            guard1YPos = key1YPos;
        } else if (pos == 5) {
            world[key1XPos - 1][key1YPos - 1] = Tileset.GUARD;
            guard1XPos = key1XPos - 1;
            guard1YPos = key1YPos - 1;
        } else if (pos == 6) {
            world[key1XPos][key1YPos - 1] = Tileset.GUARD;
            guard1XPos = key1XPos;
            guard1YPos = key1YPos - 1;
        } else if (pos == 7) {
            world[key1XPos + 1][key1YPos - 1] = Tileset.GUARD;
            guard1XPos = key1XPos + 1;
            guard1YPos = key1YPos - 1;
        }
    }

    /**
     * Makes guard 2 move
     * @param pos guard 2 will move to the corresponding position around the key
     */
    private void makeGuard2Move(int pos) {
        world[guard2XPos][guard2YPos] = Tileset.FLOOR;
        if (pos == 0) {
            world[key2XPos - 1][key2YPos + 1] = Tileset.GUARD;
            guard2XPos = key2XPos - 1;
            guard2YPos = key2YPos + 1;
        } else if (pos == 1) {
            world[key2XPos][key2YPos + 1] = Tileset.GUARD;
            guard2XPos = key2XPos;
            guard2YPos = key2YPos + 1;
        } else if (pos == 2) {
            world[key2XPos + 1][key2YPos + 1] = Tileset.GUARD;
            guard2XPos = key2XPos + 1;
            guard2YPos = key2YPos + 1;
        } else if (pos == 3) {
            world[key2XPos - 1][key2YPos] = Tileset.GUARD;
            guard2XPos = key2XPos - 1;
            guard2YPos = key2YPos;
        } else if (pos == 4) {
            world[key2XPos + 1][key2YPos] = Tileset.GUARD;
            guard2XPos = key2XPos + 1;
            guard2YPos = key2YPos;
        } else if (pos == 5) {
            world[key2XPos - 1][key2YPos - 1] = Tileset.GUARD;
            guard2XPos = key2XPos - 1;
            guard2YPos = key2YPos - 1;
        } else if (pos == 6) {
            world[key2XPos][key2YPos - 1] = Tileset.GUARD;
            guard2XPos = key2XPos;
            guard2YPos = key2YPos - 1;
        } else if (pos == 7) {
            world[key2XPos + 1][key2YPos - 1] = Tileset.GUARD;
            guard2XPos = key2XPos + 1;
            guard2YPos = key2YPos - 1;
        }
    }

    /**
     * Makes guard 3 move
     * @param pos guard 3 will move to the corresponding position around the key
     */
    private void makeGuard3Move(int pos) {
        world[guard3XPos][guard3YPos] = Tileset.FLOOR;
        if (pos == 0) {
            world[key3XPos - 1][key3YPos + 1] = Tileset.GUARD;
            guard3XPos = key3XPos - 1;
            guard3YPos = key3YPos + 1;
        } else if (pos == 1) {
            world[key3XPos][key3YPos + 1] = Tileset.GUARD;
            guard3XPos = key3XPos;
            guard3YPos = key3YPos + 1;
        } else if (pos == 2) {
            world[key3XPos + 1][key3YPos + 1] = Tileset.GUARD;
            guard3XPos = key3XPos + 1;
            guard3YPos = key3YPos + 1;
        } else if (pos == 3) {
            world[key3XPos - 1][key3YPos] = Tileset.GUARD;
            guard3XPos = key3XPos - 1;
            guard3YPos = key3YPos;
        } else if (pos == 4) {
            world[key3XPos + 1][key3YPos] = Tileset.GUARD;
            guard3XPos = key3XPos + 1;
            guard3YPos = key3YPos;
        } else if (pos == 5) {
            world[key3XPos - 1][key3YPos - 1] = Tileset.GUARD;
            guard3XPos = key3XPos - 1;
            guard3YPos = key3YPos - 1;
        } else if (pos == 6) {
            world[key3XPos][key3YPos - 1] = Tileset.GUARD;
            guard3XPos = key3XPos;
            guard3YPos = key3YPos - 1;
        } else if (pos == 7) {
            world[key3XPos + 1][key3YPos - 1] = Tileset.GUARD;
            guard3XPos = key3XPos + 1;
            guard3YPos = key3YPos - 1;
        }
    }

    /**
     * makes the calls to make the guards move randomly
     * @param num1 the position guard 1 will move to
     * @param num2 the position guard 2 will move to
     * @param num3 the position guard 3 will move to
     */
    public void makeGuardMove(int num1, int num2, int num3) {
        makeGuard1Move(num1);
        makeGuard2Move(num2);
        makeGuard3Move(num3);
    }

    /**
     * updates the player's attributes after the guards have moved
     */
    public void updatePlayer() {
        this.numHearts--;
        this.playerXPos = this.startingXPos;
        this.playerYPos = this.startingYPos;
        world[playerXPos][playerYPos] = Tileset.getPlayer();
    }
}
