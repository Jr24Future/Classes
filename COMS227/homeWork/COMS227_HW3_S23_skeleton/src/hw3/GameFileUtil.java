package hw3;

import java.io.BufferedWriter;
import java.io.*;


import api.Tile;

/**
 * Utility class with static methods for saving and loading game files.
 * @author Erroll Barker
 */
public class GameFileUtil {
	
	/**
	 * Saves the current game state to a file at the given file path.
	 * <p>
	 * The format of the file is one line of game data followed by multiple lines of
	 * game grid. The first line contains the: width, height, minimum tile level,
	 * maximum tile level, and score. The grid is represented by tile levels. The
	 * conversion to tile values is 2^level, for example, 1 is 2, 2 is 4, 3 is 8, 4
	 * is 16, etc. The following is an example:
	 * 
	 * <pre>
	 * 5 8 1 4 100
	 * 1 1 2 3 1
	 * 2 3 3 1 3
	 * 3 3 1 2 2
	 * 3 1 1 3 1
	 * 2 1 3 1 2
	 * 2 1 1 3 1
	 * 4 1 3 1 1
	 * 1 3 3 3 3
	 * </pre>
	 * 
	 * @param filePath the path of the file to save
	 * @param game     the game to save
	 */
	public static void save(String filePath, ConnectGame game) {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
			
			writer.write(game.getGrid().getWidth()+ " " + game.getGrid().getHeight() + " " + game.getMinTileLevel() + " " + game.getMaxTileLevel() + " " + game.getScore());
	       
	        writer.write('\n');
	        for (int i = 0; i <  game.getGrid().getHeight(); i++) {					//scans the filepath
	            for (int j = 0; j < game.getGrid().getWidth(); j++) {
	                if(j!=game.getGrid().getWidth()-1)
	                	writer.write(game.getGrid().getTile(j, i).getLevel()+" ");
	                else {
	                	if(i!=game.getGrid().getHeight()-1) 
	                		writer.write(game.getGrid().getTile(j, i).getLevel()+"\n");	//organize the filepath with spaces and newlines
	                	else
	                		writer.write(game.getGrid().getTile(j, i).getLevel()+"");
	                }
	            }
	        }
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Loads the file at the given file path into the given game object. When the
	 * method returns the game object has been modified to represent the loaded
	 * game.
	 * <p>
	 * See the save() method for the specification of the file format.
	 * 
	 * @param filePath the path of the file to load
	 * @param game     the game to modify
	 */
	public static void load(String filePath, ConnectGame game) {
		try {
	        BufferedReader reader = new BufferedReader(new FileReader(filePath));
	        
	        String[] header = reader.readLine().split(" ");
	        int width = Integer.parseInt(header[0]);
	        int height = Integer.parseInt(header[1]);					//reads the file path and organizes the width, heigth
	        int minTileLevel = Integer.parseInt(header[2]);				//min,max,score
	        int maxTileLevel = Integer.parseInt(header[3]);
	        int score = Integer.parseInt(header[4]);
	        
	        Grid grid = new Grid(width, height);
	        for (int i = 0; i < height; i++) {
	            String[] row = reader.readLine().split(" ");
	            for (int j = 0; j < width; j++) {
	                int level = Integer.parseInt(row[j]);
	                grid.setTile(new Tile(level), j, i);
	            }
	        }
	        
	        game.setGrid(grid);
	        game.setMinTileLevel(minTileLevel);
	        game.setMaxTileLevel(maxTileLevel);
	        game.setScore(score);
	        
	        reader.close();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
}
