import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Stack;

/**
 * Runs the game with bot player and contains code needed to chase human player.
 *
 */
public class BotPlayer extends Player {

    /** What 5x5 grid the bot remembers since it last used LOOK */
    private char[][] mapMemory = new char[5][5];
    /** Queue of commands bot will perform */
    private ArrayList<String> moveSet = new ArrayList<String>();
    /** Last seen position (in 5x5) of human player. */
    private int[] humanPos = new int[2];
    private Boolean moveRandomly = true;
    /** Current graph of 5x5. Updates every LOOK. */
    private Graph graph;
    private char failDir = 'n';

    /**
     * Constructor.
     * 
     * @param logic the GameLogic class the bot belongs to.
     * @param position the bot's starting position.
     */
    public BotPlayer(GameLogic logic, int[] position) {
        super(logic, position);
    }

    /**
     * Gets the next input from moveSet.
     * 
     * @return Bot's next String command.
     */
    @Override
    protected String getInput() {
        //run out of stuff to do - update graph
        if (moveSet.isEmpty()) {
            moveSet.add("LOOK");
        }
        String nextCommand = moveSet.remove(0);
        return nextCommand;
    }

    /**
     * Processes the command. Returns a reply in form of a String.
     */
    @Override
    protected String getNextAction() {
        String command = getInput();
        //bot can only use LOOK and MOVE <N/S/E/W>
        switch(command) {
            case "LOOK":
                storeMapInMemory(logic.look());
                //intelligently choose moves to get to human
                refreshMoveSet();
                return "";
            default:
                //MOVE N/S/E/W
                char dir = command.split("\\s+")[1].charAt(0);
                String result = logic.move(dir);
                //if bot failed to move in a direction,
                if(result.equals("Fail")) {
                    //do not go this way again on random movement
                    failDir = dir;
                    moveSet.clear();
                    //try and find human and update graph
                    moveSet.add("LOOK");
                } else {
                    failDir = 'n';
                }
                return "";
        }
    }

    /**
     * Save the 5x5 grid into 2D array.
     * 
     * @param strMap The string 5x5 grid returned from LOOK.
     */
    private void storeMapInMemory(String strMap) {
        String[] rows = strMap.split("\n");
        for(int y = 0; y < rows.length; y++) {
            String row = rows[y];
            for(int x = 0; x < row.length(); x++) {
                mapMemory[y][x] = row.charAt(x);
            }
        }
        findPlayer();
        createGraphFromMemory(mapMemory);
    }

    /**
     * Identify human player in 5x5 map.
     * Update their known position and bot's moveSet queue.
     */
    private void findPlayer() {
        moveRandomly = true;
        for(int y = 0; y < mapMemory.length; y++) {
            for(int x = 0; x < mapMemory[y].length; x++) {
                if (mapMemory[y][x] == 'P') {
                    moveRandomly = false;
                    humanPos[0] = x; humanPos[1] = y;
                    moveSet.clear();
                    break;
                }
            }
        }
    }

    /**
     * Creates graph data structure out of all non-wall map locations.
     * @param map 5x5 grid provided by LOOK.
     */
    private void createGraphFromMemory(char[][] map) {
        graph = new Graph();
        for (int y = 0; y < map.length; y++) {
            for (int x = 0; x < map[0].length; x++) {
                //ignore walls
                if (map[y][x] != '#') {
                    //create edges
                    graph.setNeighbour(x, y, map);
                }
            }
        }
    }

    /**
     * Fill moveSet queue with moves decided from a BFS or random choice.
     */
    private void refreshMoveSet() {
        if (!moveRandomly) {
            int[] botPos = {2, 2};
            //create a path of coordinates from Bot to last known human position
            HashMap<String, String> solution = graph.breadthFirstSearch(botPos, humanPos);
            //check there was no error
            if (!solution.containsKey("ERROR")) {
                //solution in form:
                //a -> bot
                //b ->  a
                //human -> b
                //creates path out of this
                Stack<String> path = new Stack<>();
                String child = Graph.createId(humanPos);
                while(!child.equals(Graph.createId(botPos))) {
                    path.push(child);
                    child = solution.get(child);
                }
                path.push(Graph.createId(botPos));
                //translate path into series of MOVE <N/S/E/W>
                for(String move : getMovesFromPath(path)) {
                    moveSet.add(move);
                }
            }
        //move in random direction x3
        } else {
            char[] directions = {'N', 'S', 'E', 'W'}; 
            char dir;
            do {
                int rnd = new Random().nextInt(directions.length);
                dir = directions[rnd];
            //do not move in direction that failed last move
            } while (dir == failDir);
            for(int i = 0; i < 3; i++) {
                moveSet.add("MOVE " + dir);
            }
        }
    }

    /**
     * Translate stack (path) of x,y positions into MOVE <N/S/E/W> commands.
     * 
     * @param path Stack of path to human in x,y terms.
     * @return ArrayList of MOVE <N/S/E/W> - path to human.
     */
    private ArrayList<String> getMovesFromPath(Stack<String> path) {
        ArrayList<String> moves = new ArrayList<>();
        String currentPosId = path.pop();
        //start from bot and loop through path until reached human
        while (!currentPosId.equals(Graph.createId(humanPos))){
            int[] currentPos = Graph.getPosFromId(currentPosId);
            String nextPosId = path.pop();
            int[] nextPos = Graph.getPosFromId(nextPosId);

            //find direction
            int dx = nextPos[0] - currentPos[0];
            int dy = nextPos[1] - currentPos[1];

            if (dx > 0) {
                moves.add("MOVE E");
            } else if (dx < 0) {
                moves.add("MOVE W");
            } else if (dy > 0) {
                moves.add("MOVE S");
            } else if (dy < 0) {
                moves.add("MOVE N");
            }
            currentPosId = nextPosId;
        }

        return moves;
    }
}
