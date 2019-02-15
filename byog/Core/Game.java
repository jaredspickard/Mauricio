package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import edu.princeton.cs.introcs.StdDraw;
import edu.princeton.cs.introcs.StdAudio;
import java.util.Random;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.awt.Color;
import java.awt.Font;

public class Game {
    TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 100;
    public static final int HEIGHT = 50;
    private World world;
    private long seed;
    private String userInput;
    private Random random = new Random(seed);
    //private int playerXPos;
    //private int playerYPos;
    //private int numKeys = 0;

    /**
     * Method used for playing a fresh game. The game should start from the main menu.
     */
    public void playWithKeyboard() {
        StdAudio.loop("/byog/Media/The_Evil_King_Bowser.wav");
        userInput = Menu.displayMainMenu();
        String gameType = userInput.substring(0, 1);
        if (gameType.equals("q")) {
            System.exit(0);
        } else if (gameType.equals("l")) {
            world = loadGame();
        } else {
            WorldGenerator generator = new
                    WorldGenerator(Long.parseLong(userInput.substring(1, userInput.length() - 1)));
            world = new World(generator.generateWorld(), generator.getPlayerXPos(), generator.getPlayerYPos(), generator.getTeleport1XPos(), generator.getTeleport1YPos(), generator.getTeleport2XPos(), generator.getTeleport2YPos(), generator.getGuard1XPos(), generator.getGuard2XPos(), generator.getGuard3XPos(), generator.getGuard1YPos(), generator.getGuard2YPos(), generator.getGuard3YPos());
            //playerXPos = generator.getPlayerXPos();
            //playerYPos = generator.getPlayerYPos();
        }
        ter.initialize(WIDTH, HEIGHT + 2);
        ter.pendingKeys(world.getNumKeys());
        ter.pendingHearts(world.getNumHearts());
        ter.pendingLineOfSight(world.getPlayerXPos(), world.getPlayerYPos());
        ter.renderFrame(world.getWorld());
        //main loop for game movement
        char moveChoice;
        boolean readyToQuit = false;
        boolean endGame = false;
        for (int i = 0; i < 1; ) {
            if (StdDraw.hasNextKeyTyped()) {
                moveChoice = Character.toLowerCase(StdDraw.nextKeyTyped());
                if (readyToQuit && moveChoice == 'q') {
                    saveGame();
                    System.exit(0);
                }
                if (makeMove(moveChoice)) {
                    readyToQuit = true;
                } else {
                    readyToQuit = false;
                }
                ter.pendingLineOfSight(world.getPlayerXPos(), world.getPlayerYPos());
                ter.pendingHearts(world.getNumHearts());
                ter.pendingKeys(world.getNumKeys());
                ter.renderFrame(world.getWorld());
                world.makeGuardMove(random.nextInt(8), random.nextInt(8), random.nextInt(8));
                if (world.getPlayerXPos() == world.getGuard1XPos() && world.getPlayerYPos() == world.getGuard1YPos()) {
                    world.updatePlayer();
                    if (world.getNumHearts() > 0) {
                        StdAudio.play("/byog/Media/lose.wav");
                    }
                    ter.pendingLineOfSight(world.getPlayerXPos(), world.getPlayerYPos());
                    ter.pendingHearts(world.getNumHearts());
                    ter.pendingKeys(world.getNumKeys());
                    ter.renderFrame(world.getWorld());
                } else if (world.getPlayerXPos() == world.getGuard2XPos() && world.getPlayerYPos() == world.getGuard2YPos()) {
                    world.updatePlayer();
                    if (world.getNumHearts() > 0) {
                        StdAudio.play("/byog/Media/lose.wav");
                    }
                    ter.pendingLineOfSight(world.getPlayerXPos(), world.getPlayerYPos());
                    ter.pendingHearts(world.getNumHearts());
                    ter.pendingKeys(world.getNumKeys());
                    ter.renderFrame(world.getWorld());
                } else if (world.getPlayerXPos() == world.getGuard3XPos() && world.getPlayerYPos() == world.getGuard3YPos()) {
                    world.updatePlayer();
                    if (world.getNumHearts() > 0) {
                        StdAudio.play("/byog/Media/lose.wav");
                    }
                    ter.pendingLineOfSight(world.getPlayerXPos(), world.getPlayerYPos());
                    ter.pendingKeys(world.getNumKeys());
                    ter.renderFrame(world.getWorld());
                }
                if (world.getNumHearts() == 0) {
                    endGame = true;
                    ter.pendingTileType("");
                }
            }
            if (!endGame) {
                double mouseX = StdDraw.mouseX();
                double mouseY = StdDraw.mouseY();
                if (mouseX < WIDTH && mouseY < HEIGHT) {
                    String tileType = world.getWorld()[(int) mouseX][(int) mouseY].description();
                    ter.pendingTileType(tileType);
                    ter.renderFrame(world.getWorld());
                }
            }
            if (world.getNumKeys() == 3) {
                bossBattle();
                i++;
            } else if (world.getNumHearts() <= 0) {
                youLose();
                i++;
            }
        }
    }

    /**
     * Method used for autograding and testing the game code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The game should
     * behave exactly as if the user typed these characters into the game after playing
     * playWithKeyboard. If the string ends in ":q", the same world should be returned as if the
     * string did not end with q. For example "n123sss" and "n123sss:q" should return the same
     * world. However, the behavior is slightly different. After playing with "n123sss:q", the game
     * should save, and thus if we then called playWithInputString with the string "l", we'd expect
     * to get the exact same world back again, since this corresponds to loading the saved game.
     *
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] playWithInputString(String input) {
        input = input.toLowerCase();
        char firstCommand = input.charAt(0);
        input = input.substring(1);
        while (firstCommand != 'n' && firstCommand != 'l' && firstCommand != 'q') {
            firstCommand = input.charAt(0);
            input = input.substring(1);
        }
        if (firstCommand == 'n') {
            String seedString = "";
            while (input.charAt(0) != 's') {
                seedString += input.charAt(0);
                input = input.substring(1);
            }
            seed = Long.parseLong(seedString);
            input = input.substring(1);
            WorldGenerator generator = new WorldGenerator(seed);
            world = new World(generator.generateWorld(), generator.getPlayerXPos(), generator.getPlayerYPos(), generator.getTeleport1XPos(), generator.getTeleport1YPos(), generator.getTeleport2XPos(), generator.getTeleport2YPos(), generator.getGuard1XPos(), generator.getGuard2XPos(), generator.getGuard3XPos(), generator.getGuard1YPos(), generator.getGuard2YPos(), generator.getGuard3YPos());
            //playerXPos = generator.getPlayerXPos();
            //playerYPos = generator.getPlayerYPos();
            executeCommands(input);
        } else if (firstCommand == 'l') {
            world = loadGame();
            executeCommands(input);
        } else {
            System.exit(0);
        }

        return world.getWorld();
    }

    /**
     * Loads and returns the most recent save file from the designated folder
     *
     * @return the correct state of the world from the most recent save
     */
    private World loadGame() {
        File f = new File("world.txt");
        if (f.exists()) {
            try {
                FileInputStream fs = new FileInputStream(f);
                ObjectInputStream os = new ObjectInputStream(fs);
                World loadWorld = (World) os.readObject();
                os.close();
                return loadWorld;
            } catch (FileNotFoundException e) {
                System.out.println("file not found");
                System.exit(0);
            } catch (IOException e) {
                System.out.println(e);
                System.exit(0);
            } catch (ClassNotFoundException e) {
                System.out.println("class not found");
                System.exit(0);
            }
        }

        /* In the case no World has been saved yet, we exit the game. */
        System.exit(0);
        //should never reach this statement
        return world;
    }

    /**
     * Saves the game.
     */
    private void saveGame() {
        File f = new File("world.txt");
        try {
            if (!f.exists()) {
                f.createNewFile();
            }
            FileOutputStream fs = new FileOutputStream(f);
            ObjectOutputStream os = new ObjectOutputStream(fs);
            os.writeObject(world);
            os.close();
            //System.exit(0);
        } catch (FileNotFoundException e) {
            System.out.println("file not found");
            System.exit(0);
        } catch (IOException e) {
            System.out.println(e);
            System.exit(0);
        }
    }

    /**
     * Executes the commands of the given string and updates world based on the commands
     *
     * @param commands the rest of the input string, contains player commands
     */
    private void executeCommands(String commands) {
        char move;
        while (commands.length() > 0) {
            move = commands.charAt(0);
            commands = commands.substring(1);
            if (makeMove(move) && commands.charAt(0) == 'q') {
                saveGame();
                //System.exit(0);
            }
        }
    }

    /**
     * Makes the player move based on the given command.
     *
     * @param move the char representing the desired move
     * @return true if the command was a ':' and false if not
     */
    private boolean makeMove(char move) {
        String moveString = world.makeMove(move);
        userInput += moveString;
        //updatePlayerPosition(moveString);
        return (moveString.equals(":"));
    }

    /**
     * Has you battle the boss.
     */
    private void bossBattle() {
        Battle.main(null);
    }

    /**
     * Displays the losing screen.
     */
    static void youLose() {
        StdAudio.play("/byog/Media/game_over.wav");
        StdDraw.picture(Game.WIDTH / 2, Game.HEIGHT / 2, "/byog/Media/hole1.png", 300, 200);
        StdDraw.picture(Game.WIDTH / 2, Game.HEIGHT / 2, "/byog/Media/gameOver.png", 75, 53.5);
        StdDraw.show();
        StdDraw.pause(3605);
        System.exit(0);
    }

    /**
     * Displays the winning screen.
     */
    static void youWin() {
        StdAudio.play("/byog/Media/smb_world_clear.wav");
        StdDraw.picture(Game.WIDTH / 2, Game.HEIGHT / 2, "/byog/Media/black.png", 320, 220);
        StdDraw.setFont(new Font("Monaco", Font.BOLD, 40));
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.text(Game.WIDTH / 2, Game.HEIGHT / 2, "YOU WIN!");
        StdDraw.show();
        StdDraw.pause(6400);
        System.exit(0);
    }
}
