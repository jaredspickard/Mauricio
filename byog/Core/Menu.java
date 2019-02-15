package byog.Core;

import byog.TileEngine.Tileset;
import edu.princeton.cs.introcs.StdDraw;
import java.awt.Color;
import java.awt.Font;

public class Menu {

    private static final String TITLE = "MAURICIO";
    private static final String SUBTITLE = "The Sicilian Electrician";
    private static final String NEW = "New Game (N)";
    private static final String LOAD = "Load Game (L)";
    private static final String QUIT = "Quit (Q)";
    private static final String SELECT = "Select Player (S)";
    private static final Color ORANGE = new Color(255, 127, 50);
    private static final Color RED = new Color(255, 0, 0);
    private static final Color GREEN = new Color(0, 190, 12);
    private static final Color YELLOW = new Color(255, 181, 0);
    private static final Color PISTACHIO = new Color(237, 245, 146);
    private static final Color LIGHTBLUE = new Color(0, 191, 255);
    private static Font font;
    private static String seed = "0";
    private static int colorIndex = 0;
    private static final Color[] COLORS = {Color.WHITE, GREEN, YELLOW, LIGHTBLUE, PISTACHIO};

    /**
     * Displays the main menu of the game.
     * @return the seed used to generate the world
     */
    public static String displayMainMenu() {
        StdDraw.enableDoubleBuffering();
        StdDraw.setCanvasSize(Game.WIDTH * 16, Game.HEIGHT * 16);
        initializeMenu();
        showMenu();
        String userInput = Character.toString(askForNLQS());
        if (!userInput.equals("n")) {
            return userInput;
        }
        initializeSeedMenu();
        showMenu();
        askForSeed();
        userInput += seed;
        return userInput;
    }

    /**
     * Sets up the basic menu structure, does not call StdDraw.show().
     */
    private static void initializeMenu() {
        font = new Font("Monaco", Font.BOLD, 70);
        StdDraw.setFont(font);
        StdDraw.setXscale(0, Game.WIDTH);
        StdDraw.setYscale(0, Game.HEIGHT);
        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(ORANGE);
        StdDraw.text(Game.WIDTH / 2, 42, TITLE);
        font = new Font("Monaco", Font.BOLD, 35);
        StdDraw.setFont(font);
        StdDraw.text(Game.WIDTH / 2, 38, SUBTITLE);
        font = new Font("Monaco", Font.PLAIN, 20);
        StdDraw.setFont(font);
        StdDraw.setPenColor(GREEN);
        StdDraw.text(Game.WIDTH / 2, 29, NEW);
        StdDraw.setPenColor(YELLOW);
        StdDraw.text(Game.WIDTH / 2, 26, LOAD);
        StdDraw.setPenColor(RED);
        StdDraw.text(Game.WIDTH / 2, 23, QUIT);
        StdDraw.setPenColor(LIGHTBLUE);
        StdDraw.text(Game.WIDTH / 2, 20, SELECT);
    }

    private static void initializeSeedMenu() {
        initializeMenu();
        font = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.setPenColor(PISTACHIO);
        StdDraw.text(Game.WIDTH / 2, 15, "Seed");
    }

    /**
     * Asks the user if they want a new game, loaded game, or to quit.
     * @return a char representing their answer
     */
    private static char askForNLQS() {
        char menuChoice = '0';
        for (int i = 0; i < 1;) {
            if (StdDraw.hasNextKeyTyped()) {
                menuChoice = Character.toLowerCase(StdDraw.nextKeyTyped());
                if (menuChoice == 'n' || menuChoice == 'l' || menuChoice == 'q') {
                    i++;
                } else if (menuChoice == 's') {
                    selectCharacterScreen();
                }
                initializeNLQErrorMessage();
                showMenu();
            }
        }
        return menuChoice;
    }

    /**
     * Gets the user's seed
     * @return
     */
    private static void askForSeed() {
        char nextChar;
        for (int i = 0; i < 1;) {
            if (StdDraw.hasNextKeyTyped()) {
                nextChar = Character.toLowerCase(StdDraw.nextKeyTyped());
                if (nextChar != 's' && Character.isDigit(nextChar)) {
                    seed += Character.toString(nextChar);
                    updateSeedOnMenu();
                } else if (nextChar == 's') {
                    seed += "s";
                    i++;
                } else {
                    initializeSeedErrorMessage();
                }
                initializeSeedInstructions();
                showMenu();
            }
        }
    }

    /**
     * Adds an error message regarding input of N L Q to the StdDraw canvas.
     */
    private static void initializeNLQErrorMessage() {
        initializeMenu();
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.text(86, 2.25, "Please enter \"N\" \"L\" \"Q\" or \"S\"");
    }

    /**
     * Adds an error message regarding input of the seed.
     */
    private static void initializeSeedErrorMessage() {
        initializeSeedMenu();
        updateSeedOnMenu();
        font = new Font("Monaco", Font.PLAIN, 15);
        StdDraw.setFont(font);
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.text(88, 2, "Seeds may only consist of integers");
    }

    private static void initializeCharacterScreenError() {
        StdDraw.setFont(new Font("Monaco", Font.PLAIN, 15));
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.text(88, 2, "Arrow characters are 'A' and 'D'");
    }

    /**
     * Updates the seed on the StdDraw canvas.
     */
    private static void updateSeedOnMenu() {
        initializeSeedMenu();
        //StdDraw.enableDoubleBuffering();
        font = new Font("Monaco", Font.PLAIN, 23);
        StdDraw.setFont(font);
        StdDraw.setPenColor(PISTACHIO);
        StdDraw.text(Game.WIDTH / 2, 13, seed.substring(1));
    }

    /**
     * Informs the user they must end their seed with the letter 's'.
     */
    private static void initializeSeedInstructions() {
        font = new Font("Monaco", Font.PLAIN, 15);
        StdDraw.setFont(font);
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.text(14, 2.5, "When you have finished entering your seed,");
        StdDraw.text(14, 1.4, "type in the character 'S'");
    }

    /**
     * Allows the user to select their Player's color.
     */
    private static void selectCharacterScreen() {
        char menuChoice = '0';
        displayCharacter();
        for (int i = 0; i < 1;) {
            if (StdDraw.hasNextKeyTyped()) {
                menuChoice = Character.toLowerCase(StdDraw.nextKeyTyped());
                if (menuChoice == 's') {
                    Tileset.setPlayerColor(COLORS[colorIndex]);
                    i++;
                } else if (menuChoice == 'a') {
                    if (colorIndex == 0) {
                        colorIndex = COLORS.length - 1;
                    } else {
                        colorIndex--;
                    }
                } else if (menuChoice == 'd') {
                    if (colorIndex == COLORS.length - 1) {
                        colorIndex = 0;
                    } else {
                        colorIndex++;
                    }
                } else {
                    initializeCharacterScreenError();
                }
                displayCharacter();
            }
        }
    }

    /**
     * Displays the character in their current color, as well as arrows on either side
     */
    private static void displayCharacter() {
        StdDraw.setFont(new Font("Monaco", Font.PLAIN, 500));
        StdDraw.setPenColor(COLORS[colorIndex]);
        StdDraw.text(Game.WIDTH / 2, Game.HEIGHT / 2, "웃");
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.text(25, Game.HEIGHT / 2, "⇦");
        StdDraw.text(81, Game.HEIGHT / 2, "⇨");
        StdDraw.setFont(new Font("Monaco", Font.PLAIN, 60));
        StdDraw.text(22, 23.5, "A");
        StdDraw.text(78, 23.7, "D");
        StdDraw.setFont(new Font("Monaco", Font.PLAIN, 15));
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.text(12, 2, "Type 'S' to select your character");
        showMenu();
    }

    /**
     * Shows the menu on the screen and clears it afterwards.
     */
    private static void showMenu() {
        StdDraw.enableDoubleBuffering();
        StdDraw.show();
        StdDraw.clear(Color.BLACK);
    }
}
