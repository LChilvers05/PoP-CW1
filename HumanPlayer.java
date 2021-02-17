import java.io.BufferedReader;
import java.io.IOException;
import java.util.Arrays;

/**
 * Runs the game with a human player and contains code needed to read inputs.
 *
 */
public class HumanPlayer extends Player {

    private BufferedReader userInput;

    /** The players score - gold collected with PICKUP */
    private int goldOwned = 0;

    /**
     * Constructor.
     * 
     * @param logic the GameLogic class the player belongs to.
     * @param position the player's starting position.
     */
    public HumanPlayer(GameLogic logic, int[] position) {
        super(logic, position);
    }

    /**
     * Reads player's input from the console.
     * 
     * @return : A string containing the input the player entered.
     */
    @Override
    protected String getInput() {
        String command = "";
        try {
            //INVALID inputs count as a turn
            //TODO: send to client
            println("> Type a command...");
            String input = userInput.readLine().toUpperCase();
            //valid command apart from MOVE
            if (Arrays.asList(commands).contains(input)) {
                command = input;
            //separate MOVE command and check in format MOVE <N/S/E/W>
            } else if (input.contains("MOVE")) {
                try {
                    String[] moveCommand = input.split("\\s+");
                    if (moveCommand.length > 0) {
                        String dir = moveCommand[1];
                        if (dir.equals("N") || dir.equals("S") || dir.equals("E") || dir.equals("W")) {
                            command = input;
                        }
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                    command = "Invalid";
                }
            } else {
                command = "Invalid";
            }
        } catch (IOException e) {
            command = "Invalid";
        }
        return command;
    }

    public int getGoldOwned() {
        return goldOwned;
    }

    public void setGoldOwned(int goldOwned) {
        this.goldOwned = goldOwned;
    }

    public void setUserInput(BufferedReader in) {
        this.userInput = in;
    }
}