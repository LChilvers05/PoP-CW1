import java.io.File;
import java.util.Random;
import java.util.Scanner;
/**
 * Contains the main game logic.
 *
 */
public class GameLogic {
    
    Map map;
    //The game's players
    HumanPlayer p1;
    BotPlayer p2;
    Player currentPlayer = p1;
    Player nextPlayer = p2;
    
    Boolean gameRunning = false;

    public static Scanner userInput = new Scanner(System.in);
    private Random random = new Random();
	
	/**
	 * Default constructor
	 */
	public GameLogic() {
		map = new Map();
    }

    /**
	 * Checks if the game is running.
	 *
     * @return if the game is running.
     */
    protected boolean gameRunning() {
        return gameRunning;
    }

    /**
	 * Returns the gold required to win.
	 *
     * @return : Gold required to win.
     */
    protected String hello() {
        return "Gold to win: " + Integer.toString(map.getGoldRequired());
    }
	
	/**
	 * Returns the gold currently owned by the player.
	 *
     * @return : Gold currently owned.
     */
    protected String gold() {
        // only human player collects gold
        return "Gold owned: " + Integer.toString(p1.getGoldOwned());
    }

    /**
     * Checks if movement is legal and updates player's location on the map.
     *
     * @param direction : The direction of the movement.
     * @return : Protocol if success or not.
     */
    protected String move(char direction) {
        //(0,0) is top left corner
        char[][] mapState = map.getMap();
        int[] pos = currentPlayer.getPosition();
        //update the current players position (if move is legal)
        if (direction == 'N') {
            if (mapState[pos[1] - 1][pos[0]] != '#') {
                pos[1] = pos[1] - 1;
                currentPlayer.setPosition(pos);
                return "Success";
            }
        } else if (direction == 'S') {
            if (mapState[pos[1] + 1][pos[0]] != '#') {
                pos[1] = pos[1] + 1;
                currentPlayer.setPosition(pos);
                return "Success";
            }
        } else if (direction == 'E') {
            if (mapState[pos[1]][pos[0] + 1] != '#') {
                pos[0] = pos[0] + 1;
                currentPlayer.setPosition(pos);
                return "Success";
            }
        } else if (direction == 'W') {
            if (mapState[pos[1]][pos[0] - 1] != '#') {
                pos[0] = pos[0] - 1;
                currentPlayer.setPosition(pos);
                return "Success";
            }
        }
        return "Fail";
    }

    /**
     * Converts the map from a 2D char array to a single string.
     * A 5x5 grid of players surroundings.
     * 
     * @return : A String representation of the game map.
     */
    protected String look() {
        String strMap = "";
        char[][] mapState = map.getMap();
        int[] playerPos = currentPlayer.getPosition();
        int[] opponentPos = nextPlayer.getPosition();
        //peek the map in 5x5 grid
        for(int y = playerPos[1]-2; y < playerPos[1]+3; y++) {
            for(int x = playerPos[0]-2; x < playerPos[0]+3; x++) {
                //print 'P' or 'B' in centre of 5x5
                if (playerPos[0] == x && playerPos[1] == y) {
                    if (currentPlayer == p1) {
                        strMap = strMap + 'P';
                    } else {
                        strMap = strMap + 'B';
                    }
                //print 'P' or 'B' elsewhere if in 5x5
                } else if (opponentPos[0] == x && opponentPos[1] == y) {
                    if (nextPlayer == p1) {
                        strMap = strMap + 'P';
                    } else {
                        strMap = strMap + 'B';
                    }
                //fill rest of 5x5 and if out of map fill with wall
                } else {
                    try {
                        strMap = strMap + mapState[y][x];
                    } catch (IndexOutOfBoundsException e) {
                        strMap = strMap + '#';
                    }
                }
            }
            strMap = strMap + "\n";
        }
        return strMap;
    }

    /**
     * Returns the current state of the entire map.
     * Used to debug, not for players to use.
     *
     * @return : A String representation of the entire game map.
     */
    protected String checkMap() {
        String strMap= "";
        char[][] mapState = map.getMap();
        for(int y = 0; y < mapState.length; y++) {
            for(int x = 0; x < mapState[y].length; x++) {
                if (x == p1.getPosition()[0] && y == p1.getPosition()[1]) {
                    strMap = strMap + 'P';
                } else if (x == p2.getPosition()[0] && y == p2.getPosition()[1]){
                    strMap = strMap + 'B';
                } else {
                    strMap = strMap + mapState[y][x];
                }
            }
            strMap = strMap + "\n";
        }
        return strMap;
    }

    /**
     * Processes the player's pickup command, updating the map and the player's gold amount.
     *
     * @return If the player successfully picked-up gold or not.
     */
    protected String pickup() {
        //only human player can use pickup command
        char[][] mapState = map.getMap();
        int[] position = p1.getPosition();
        //picked up some gold - update p1
        if (mapState[position[1]][position[0]] == 'G') {
            p1.setGoldOwned(p1.getGoldOwned() + 1);
            map.updateMap(position);
            return "Success. Gold owned: " + p1.getGoldOwned();
        }
        return "Fail";
    }

    /**
     * Quits the game, shutting down the application.
     * 
     * @return If the player achieved WIN or LOSE.
     */
    protected String quitGame() {
        //only human player can quit game
        String result = "";
        char[][] mapState = map.getMap();
        int[] position = p1.getPosition();
        if (mapState[position[1]][position[0]] == 'E' && p1.getGoldOwned() >= map.getGoldRequired()) {
            result = "WIN";
        } else {
            result = "LOSE";
        }
        gameRunning = false;
        return result;
    }

    /**
     * Gets random position on the board avoiding gold, players and walls.
     * 
     * @return Random x,y starting position.
     */
    public int[] randomPosition() { 
        char[][] mapState = map.getMap();
        int x, y;
        //find a suitable random position
        do {
            while (true) {
                y = random.nextInt(mapState.length);
                x = random.nextInt(mapState[y].length);
                //bot must not be placed in same place as human
                if (p1 != null) {
                    if (!(p1.getPosition()[0] == x && p1.getPosition()[1] == y)) {
                        break;
                    }
                } else {
                    break;
                }
            }
        }
        while (mapState[y][x] == '#' || mapState[y][x] == 'G');
        
        int[] position = {x, y};

        return position;
    }

    /**
     * Game start, displays maps to choose from in console.
     * Takes user input to choose map.
     * 
     * @return File of map chosen.
     */
    public File gameIntro() {
        File mapFile = new File("small_example_map.txt");
        // println("=======================================");
        // println("==  Welcome to the Dungeon of Doom!  ==");
        // println("=======================================");
        // println("Please select map or enter custom_map_file_name.txt :");
        // println("SMALL");
        // println("MEDIUM");
        // println("LARGE");
        // println("");
        boolean chosen = false;
        while (!chosen) {
            // String mapChoice = userInput.nextLine().toLowerCase();
            String mapChoice = "medium";
            //entered choice shortcut
            if (mapChoice.equals("small") || mapChoice.equals("medium") || mapChoice.equals("large")) {
                mapFile = new File(mapChoice + "_example_map.txt");
                chosen = mapFile.exists();
            } else {
                //check for other map file
                mapFile = new File(mapChoice);
                chosen = mapFile.exists();
            }
            if (chosen == false) {
                println("File not found.");
                println("Please type 'SMALL', 'MEDIUM' or 'LARGE' to begin.");
            }
        }
        return mapFile;
    }
	
	// public static void main(String[] args) {
    //     //create game, map and player
    //     GameLogic logic = new GameLogic();
    //     logic.map = new Map(logic.gameIntro());
    //     logic.p1 = new HumanPlayer(logic, logic.randomPosition());
    //     logic.p2 = new BotPlayer(logic, logic.randomPosition());
        
    //     //game loop
    //     logic.gameRunning = true;
    //     while(logic.gameRunning()) {
    //         // println(logic.checkMap()); //for debug
    //         //bot caught human
    //         if (Arrays.equals(logic.p1.position, logic.p2.position)) {
    //             println("LOSE");
    //             logic.gameRunning = false;
    //             break;
    //         }
    //         //swap turns
    //         logic.currentPlayer = (logic.currentPlayer == logic.p1) ? logic.p2 : logic.p1;
    //         logic.nextPlayer = (logic.nextPlayer == logic.p2) ? logic.p1 : logic.p2;
    //         //prints the response from the players command inputs
    //         println(logic.currentPlayer.getNextAction());
    //     }
    //     // program end - close input scanner
    //     userInput.close();
    // }

    //helpers
    public static void println(String message) {
        System.out.println(message);
    }
    public static void print(String message) {
        System.out.print(message);
    }
}