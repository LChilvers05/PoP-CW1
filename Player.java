/**
 * Abstract player.
 *
 */
public abstract class Player {

    protected GameLogic logic;
    /** Player's x,y on map */
    protected int[] position;
    /** All legal inputs other than MOVE */
    protected String[] commands = {"HELLO", "GOLD", "PICKUP", "LOOK", "QUIT"};

    /**
	 * Default constructor
	 */
    public Player(GameLogic logic, int[] position) {
        this.logic = logic;
        this.position = position;
    }

    public int[] getPosition() {
        return position;
    }

    public void setPosition(int[] position) {
        this.position = position;
    }

    /**
     * @return A string containing the input the player entered.
     */
    protected abstract String getInput(String cmd);

    /**
     * Processes the command. Returns a reply in form of a String.
     * Otherwise it should return the string "Invalid".
     *
     * @return : Processed output or Invalid if the input is wrong.
     */
    protected String getNextAction(String cmd) {
        String command = getInput(cmd);
        //use GameLogic to execute commands
        //return response back to GameLogic
        switch(command) {
            case "HELLO":
                return logic.hello();
            case "GOLD":
                return logic.gold();
            case "PICKUP":
                return logic.pickup();
            case "LOOK":
                return logic.look();
            case "QUIT":
                return logic.quitGame();
            default:
                if (command.contains("MOVE")) {
                    char direction = command.split("\\s+")[1].charAt(0);
                    return logic.move(direction);
                }
                //invalid directions for MOVE already caught
                return "Invalid";
        }
    }
}
