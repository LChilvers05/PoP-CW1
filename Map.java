// Reused Code:
// Chilvers, L., 2020. Dungeon of Doom, CM10227. University of Bath. Unpublished assignment.

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
/**
 * Reads and contains in memory the map of the game.
 */
public class Map {

	/* Representation of the map */
	private char[][] map;
	
	/* Map name */
	private String mapName;
	
	/* Gold required for the human player to win */
	private int goldRequired;
	
	/**
	 * Default constructor, creates the default map "Very small Labyrinth of doom".
	 */
	public Map() {
		mapName = "Very small Labyrinth of Doom";
		goldRequired = 2;
		map = new char[][]{
		{'#','#','#','#','#','#','#','#','#','#','#','#','#','#','#','#','#','#','#','#'},
		{'#','.','.','.','.','.','.','.','.','.','.','.','.','.','.','.','.','.','.','#'},
		{'#','.','.','.','.','.','.','G','.','.','.','.','.','.','.','.','.','E','.','#'},
		{'#','.','.','.','.','.','.','.','.','.','.','.','.','.','.','.','.','.','.','#'},
		{'#','.','.','E','.','.','.','.','.','.','.','.','.','.','.','.','.','.','.','#'},
		{'#','.','.','.','.','.','.','.','.','.','.','.','G','.','.','.','.','.','.','#'},
		{'#','.','.','.','.','.','.','.','.','.','.','.','.','.','.','.','.','.','.','#'},
		{'#','.','.','.','.','.','.','.','.','.','.','.','.','.','.','.','.','.','.','#'},
		{'#','#','#','#','#','#','#','#','#','#','#','#','#','#','#','#','#','#','#','#'}
		};
	}
	
	/**
	 * Constructor that accepts a map to read in from.
	 *
	 * @param : The filename of the map file.
	 */
	public Map(File mapFile) {
		readMap(mapFile);
	}

    /**
     * @return : Gold required to exit the current map.
     */
    protected int getGoldRequired() {
        return goldRequired;
    }

    /**
     * @return : The map as stored in memory.
     */
    protected char[][] getMap() {
        return map;
    }


    /**
     * @return : The name of the current map.
     */
    protected String getMapName() {
    		return mapName;
		}

		/**
		 * Update map by replacing gold at specified position.
		 * 
		 * @param : Coordinates to update map with.
		 */
		protected void updateMap(int[] position) {
				map[position[1]][position[0]] = '.';
		}
		


    /**
     * Reads the map from file.
     *
     * @param : The chosen map file.
     */
    protected void readMap(File mapFile) {
				try {
						//read in the map file
						Scanner mapReader = new Scanner(mapFile);

						ArrayList<char[]> rows = new ArrayList<char[]>();
						
						while (mapReader.hasNextLine()) {
								String mapFileRow = mapReader.nextLine();

								//gets the map name
								if (mapFileRow.contains("name")) {
										mapName = mapFileRow.substring(5, mapFileRow.length());
								}

								//gets the required gold to win
								if (mapFileRow.contains("win")) {
										goldRequired = Integer.parseInt(mapFileRow.substring(4, mapFileRow.length()));
										if (goldRequired < 0) {
												goldRequired = 0;
										}
								}

								//record map rows for 2D array
								if (mapFileRow.contains("#")) {
										char[] mapRow = mapFileRow.toCharArray();
										rows.add(mapRow);
								}
						}
						mapReader.close();

						// build the map as 2D array
						if (!rows.isEmpty()) {
								map = new char[rows.size()][rows.get(0).length];
								for(int i = 0; i < rows.size(); i++) {
										map[i] = rows.get(i);
								}
						}
					
				//fatal error: only reached if cannot navigate to map file
				} catch (FileNotFoundException | NumberFormatException e) {
						System.out.println("Fatal Error: Map file not found.");
						e.printStackTrace();
				}
		}
}
