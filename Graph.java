import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Code to parse 5x5 LOOK grid into a graph data structure.
 * Logic adapted from:
 * Cohen 2014, Converting a 2D matrix into a graph, Stack Overflow, viewed 8 December 2020, 
 * <https://stackoverflow.com/questions/25405165/converting-a-2d-matrix-into-a-graph/>
 */
public class Graph {

    HashMap<String, ArrayList<String>> edges = new HashMap<>();

    /**
     * Connect two nodes by edge.
     * 
     * @param n1 Current node.
     * @param n2 Neighbour node.
     */
    public void addEdge(int[] n1, int[] n2) {
        //convert to strings to store in edges.
        String key = createId(n1);
        String val = createId(n2);
        if (edges.containsKey(key)) {
            edges.get(key).add(val);
        } else {
            ArrayList<String> first = new ArrayList<>();
            first.add(val);
            edges.put(key, first);
        }
    }

    /**
     * Get list of all neighbours to node n connected by an edge.
     * @param n Node to find neighbours from.
     * @return ArrayList of neighbour node Ids.
     */
    public ArrayList<String> getNeighbours(String n) {
        return edges.get(n);
    }

    /**
     * Connect all neighbours to the node at x,y.
     * 
     * @param x x Position of current node.
     * @param y y Position of current node.
     * @param map The 5x5 grid returned from LOOK.
     */
    public void setNeighbour(int x, int y, char[][] map) {
        //loop through all positions around the current node
        for (int row = -1; row < 2; row++) {
            for(int col = -1; col < 2; col++) {
                //ignore the center and diagonals
                if ((row ==0 && col == 0) || (row == -1 && col == -1) || (row == 1 && col == 1) || (row == -1 && col == 1) || (row == 1 && col == -1)){
                    continue;
                }
                //ignore outside the map
                if ((0 > x + col) || (x + col >= map[0].length) || (0 > y + row) || (y + row >= map.length)) {
                    continue;
                }
                //ignore walls
                if (map[y][x] == '#'){
                    continue;
                }
                int[] n1 = {x, y};
                int[] n2 = {x + col, y + row};
                addEdge(n1, n2);
            }
        }
    }

    /**
     * Do a breadth first search to find a solution from start to end position.
     * 
     * @param startNode The bot position.
     * @param endNode The last known human position.
     * @return HashMap with embedded path from bot to human.
     */
    public HashMap<String, String> breadthFirstSearch(int[] startNode, int[] endNode) {
        String strStart = createId(startNode);
        String strEnd = createId(endNode);
        HashMap<String, String> solution = new HashMap<>();
        
        String node = strStart;

        //already on goal
        if (node.equals(strEnd)) {
            return solution;
        }

        //Queue so nodes wait to be expanded
        Queue<String> frontier = new LinkedList<>();
        //Do not need to expand nodes already expanded
        ArrayList<String> explored = new ArrayList<>();
        frontier.add(node);

        while(!frontier.isEmpty()) {
            node = frontier.poll();
            if (getNeighbours(node) != null) {
                //
                for (String neighbour : getNeighbours(node)) {
                    if (!explored.contains(neighbour)) {
                        explored.add(neighbour);
                        //add to solution to get path from later
                        solution.put(neighbour, node);
                        //reached goal, do not need to expand further
                        if (neighbour.equals(strEnd)) {
                            return solution;
                        }
                        //add to frontier to expand later
                        frontier.add(neighbour);
                    }
                }
            }
        }
        //failure - never reached because should always have reference to where goal is if doing BFS
        System.out.println("Fatal Error: Performed BFS with no goal");
        solution.clear();
        solution.put("ERROR", "Performed BFS with no goal");
        return solution;
    }
    
    /**
     * Create String key out of position for edges HashMap.
     * 
     * @param pos x,y position.
     * @return String id as "x,y".
     */
    public static String createId(int[] pos) {
        return Integer.toString(pos[0]) + "," + Integer.toString(pos[1]);
    }
    /**
     * Turn String key back into x,y position.
     * 
     * @param id String id as "x,y".
     * @return x,y position.
     */
    public static int[] getPosFromId(String id) {
        String[] strPos = id.split(",");
        int[] pos = new int[2];
        for(int i = 0; i < pos.length; i++) {
            pos[i] = Integer.parseInt(strPos[i]);
        }
        return pos;
    }
}
