package hw3;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import api.ScoreUpdateListener;
import api.ShowDialogListener;
import api.Tile;

/**
 * Class that models a game.
 * @author Erroll Barker
 */
public class ConnectGame {
	private ShowDialogListener dialogListener;
	private ScoreUpdateListener scoreListener;
	private int width,height,min,max;
	private long pScore;   
	private Random rand;
	private Grid grid;
	private Tile currentSelection;   //holds the value of the current tile
	private Tile ranTile;			//the random tile
	private ArrayList<Tile> selectedTiles=new ArrayList<Tile>();
	
	//private int [][] table = new int[width][height];
	/**
	 * Constructs a new ConnectGame object with given grid dimensions and minimum
	 * and maximum tile levels.
	 * 
	 * @param width  grid width
	 * @param height grid height
	 * @param min    minimum tile level
	 * @param max    maximum tile level
	 * @param rand   random number generator
	 */
	public ConnectGame(int width, int height, int min, int max, Random rand) {
		this.width = width;
		this.height = height;
		this.min = min;
		this.max = max;
		this.rand = rand;
		
		grid = new Grid(width, height);
		
		
	}

	/**
	 * Gets a random tile with level between minimum tile level inclusive and
	 * maximum tile level exclusive. For example, if minimum is 1 and maximum is 4,
	 * the random tile can be either 1, 2, or 3.
	 * <p>
	 * DO NOT RETURN TILES WITH MAXIMUM LEVEL
	 * 
	 * @return a tile with random level between minimum inclusive and maximum
	 *         exclusive
	 */
	public Tile getRandomTile() {
		rand=new Random();
		int randtile =rand.nextInt(max-min)+min;
		ranTile = new Tile(randtile);
		return ranTile;
	}

	/**
	 * Regenerates the grid with all random tiles produced by getRandomTile().
	 */
	public void radomizeTiles() {
		for(int i=0; i<height; i++) {
			for(int j=0; j<width; j++) {
				grid.setTile(getRandomTile(), j, i);
			}
		}
	}

	/**
	 * Determines if two tiles are adjacent to each other. The may be next to each
	 * other horizontally, vertically, or diagonally.
	 * 
	 * @param t1 one of the two tiles
	 * @param t2 one of the two tiles
	 * @return true if they are next to each other horizontally, vertically, or
	 *         diagonally on the grid, false otherwise
	 */
	public boolean isAdjacent(Tile t1, Tile t2) {
				
				  int dx = Math.abs(t1.getX() - t2.getX());
				  int dy = Math.abs(t1.getY() - t2.getY());
				  return (dx == 1 && dy == 0) || (dx == 0 && dy == 1) || (dx == 1 && dy == 1);
				  
	}

	/**
	 * Indicates the user is trying to select (clicked on) a tile to start a new
	 * selection of tiles.
	 * <p>
	 * If a selection of tiles is already in progress, the method should do nothing
	 * and return false.
	 * <p>
	 * If a selection is not already in progress (this is the first tile selected),
	 * then start a new selection of tiles and return true.
	 * 
	 * @param x the column of the tile selected
	 * @param y the row of the tile selected
	 * @return true if this is the first tile selected, otherwise false
	 */
	public boolean tryFirstSelect(int x, int y) {
		 Tile selectedTile = grid.getTile(x, y);
		    
		    if (currentSelection == null) {
		        
		        currentSelection = selectedTile;
		        selectedTile.setSelect(true);
		        selectedTiles.add(currentSelection);
		        return true;
		    } else if (selectedTile == currentSelection) {
		        //checks to see if its in the same spot
		        return false;
		    } else {
		        
		        currentSelection.setSelect(false);
		        currentSelection = null;
		        return false;
		    }
	}

	/**
	 * Indicates the user is trying to select (mouse over) a tile to add to the
	 * selected sequence of tiles. The rules of a sequence of tiles are:
	 * 
	 * <pre>
	 * 1. The first two tiles must have the same level.
	 * 2. After the first two, each tile must have the same level or one greater than the level of the previous tile.
	 * </pre>
	 * 
	 * For example, given the sequence: 1, 1, 2, 2, 2, 3. The next selected tile
	 * could be a 3 or a 4. If the use tries to select an invalid tile, the method
	 * should do nothing. If the user selects a valid tile, the tile should be added
	 * to the list of selected tiles.
	 * 
	 * @param x the column of the tile selected
	 * @param y the row of the tile selected
	 */
	public void tryContinueSelect(int x, int y) {
		 Tile thisTile = grid.getTile(x, y);
		    
			    if (currentSelection !=null && !isAdjacent(currentSelection,thisTile)) {
			        return;
			    }
			    
			    if (selectedTiles.size() == 1) {								//if the arraylist is 1 and if current tile is same with given values it will do
			    	if (currentSelection.getLevel() == thisTile.getLevel()) {	//adds current tile to the list and makes the add tile its new location 
			    		selectedTiles.add(thisTile);
			    		thisTile.setSelect(true);
			    		currentSelection = thisTile;
			    	}
			    	else {
			    		selectedTiles.remove(thisTile);
			    		currentSelection.setSelect(false);
			    	}
			    }
			    else if (selectedTiles.size() > 1) {
		    		if (currentSelection.getLevel() == thisTile.getLevel() || currentSelection.getLevel() + 1 == thisTile.getLevel()) {
			    		selectedTiles.add(thisTile);
			    		thisTile.setSelect(true);
			    		currentSelection = thisTile;
			    	}
			    	else {
			    		selectedTiles.remove(thisTile);
			    		currentSelection.setSelect(false);
			    	}
	    	}
	    
	}

	/**
	 * Indicates the user is trying to finish selecting (click on) a sequence of
	 * tiles. If the method is not called for the last selected tile, it should do
	 * nothing and return false. Otherwise it should do the following:
	 * 
	 * <pre>
	 * 1. When the selection contains only 1 tile reset the selection and make sure all tiles selected is set to false.
	 * 2. When the selection contains more than one block:
	 *     a. Upgrade the last selected tiles with upgradeLastSelectedTile().
	 *     b. Drop all other selected tiles with dropSelected().
	 *     c. Reset the selection and make sure all tiles selected is set to false.
	 * </pre>
	 * 
	 * @param x the column of the tile selected
	 * @param y the row of the tile selected
	 * @return return false if the tile was not selected, otherwise return true
	 */
	public boolean tryFinishSelection(int x, int y) {
		Tile tilelast =grid.getTile(x, y);
		
		
		if(tilelast == selectedTiles.get(selectedTiles.size()-1)) {
			if(selectedTiles.size() == 1) {
				for(int i = 0; i < selectedTiles.size();i++) {
					pScore += selectedTiles.get(i).getValue();
					selectedTiles.get(i).setSelect(false);
				}
			}
			else if(selectedTiles.size() > 1){
				upgradeLastSelectedTile();
				dropSelected();
				for(int i = 0; i < selectedTiles.size();i++) {
				pScore += selectedTiles.get(i).getValue();
				selectedTiles.get(i).setSelect(false);
				}
			}
			if(tilelast.getX() == selectedTiles.get(selectedTiles.size()-1).getX() && tilelast.getY() == selectedTiles.get(selectedTiles.size()-1).getY()) {
				pScore = 0;					//if the given x and y are the same value then score = 0 
			}
			pScore*=2;
			return true;
		}
		return false;
	}

	/**
	 * Increases the level of the last selected tile by 1 and removes that tile from
	 * the list of selected tiles. The tile itself should be set to unselected.
	 * <p>
	 * If the upgrade results in a tile that is greater than the current maximum
	 * tile level, both the minimum and maximum tile level are increased by 1. A
	 * message dialog should also be displayed with the message "New block 32,
	 * removing blocks 2". Not that the message shows tile values and not levels.
	 * Display a message is performed with dialogListener.showDialog("Hello,
	 * World!");
	 */
	public void upgradeLastSelectedTile() {
		currentSelection.setLevel(currentSelection.getLevel()+1);
		selectedTiles.remove(currentSelection);
		currentSelection.setSelect(false);
		
		if (currentSelection.getLevel() > max) {		//if the current level greater then max
			max++;
			min++;
			dialogListener.showDialog("New block " + Math.pow(2, max) + ", " + "removing blocks " + Math.pow(2, min));
			scoreListener.updateScore(pScore);			//it will change tile to the power of the value
		}
	}

	/**
	 * Gets the selected tiles in the form of an array. This does not mean selected
	 * tiles must be stored in this class as a array.
	 * 
	 * @return the selected tiles in the form of an array
	 */
	public Tile[] getSelectedAsArray() {
		 List<Tile> selectedTiles = new ArrayList<Tile>();
		    for (int y = 0; y < grid.getHeight(); y++) {
		        for (int x = 0; x < grid.getWidth(); x++) {
		            Tile tile = grid.getTile(x, y);
		            if (tile.isSelected()) {
		                selectedTiles.add(tile);
		            }
		        }
		    }
		    return selectedTiles.toArray(new Tile[selectedTiles.size()]);
	}

	/**
	 * Removes all tiles of a particular level from the grid. When a tile is
	 * removed, the tiles above it drop down one spot and a new random tile is
	 * placed at the top of the grid.
	 * 
	 * @param level the level of tile to remove
	 */
	public void dropLevel(int level) {
		for(int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				if(grid.getTile(j, i).getLevel()==level) {   	//checks the level
					for(int a = i; a > 0; a--) {
						grid.setTile(grid.getTile(j, a-1), j, a);		//a-1 will get the tile but not the top one
					}														//top tile will be add with the random tile
					grid.setTile(getRandomTile(), 0, j);
				}
			}
		}
	}

	/**
	 * Removes all selected tiles from the grid. When a tile is removed, the tiles
	 * above it drop down one spot and a new random tile is placed at the top of the
	 * grid.
	 */
	public void dropSelected() {
		for(int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				if(grid.getTile(j, i).isSelected()) {				//checks if selected
					for(int a = i; a > 0; a--) {
						grid.setTile(grid.getTile(j, a-1), j, a);    //a-1 will get the tile but not the top one
					}
					grid.setTile(getRandomTile(), 0, j);			//top tile will be add with the random tile
				}
			}
		}
	}
	

	/**
	 * Remove the tile from the selected tiles.
	 * 
	 * @param x column of the tile
	 * @param y row of the tile
	 */
	public void unselect(int x, int y) {
		Tile tileToUnselect = grid.getTile(x, y);
	    tileToUnselect.setSelect(false);
	    selectedTiles.remove(tileToUnselect);
	}

	/**
	 * Gets the player's score.
	 * 
	 * @return the score
	 */
	public long getScore() {
		
		return pScore;
	}

	/**
	 * Gets the game grid.
	 * 
	 * @return the grid
	 */
	public Grid getGrid() {
		
		return grid;
	}

	/**
	 * Gets the minimum tile level.
	 * 
	 * @return the minimum tile level
	 */
	public int getMinTileLevel() {
		
		return min;
	}

	/**
	 * Gets the maximum tile level.
	 * 
	 * @return the maximum tile level
	 */
	public int getMaxTileLevel() {
		
		return max;
	}

	/**
	 * Sets the player's score.
	 * 
	 * @param score number of points
	 */
	public void setScore(long score) {
		pScore = score;
	}

	/**
	 * Sets the game's grid.
	 * 
	 * @param grid game's grid
	 */
	public void setGrid(Grid grid) {
		this.grid = grid;
	}

	/**
	 * Sets the minimum tile level.
	 * 
	 * @param minTileLevel the lowest level tile
	 */
	public void setMinTileLevel(int minTileLevel) {
		min = minTileLevel;
	}

	/**
	 * Sets the maximum tile level.
	 * 
	 * @param maxTileLevel the highest level tile
	 */
	public void setMaxTileLevel(int maxTileLevel) {
		max = maxTileLevel;
	}

	/**
	 * Sets callback listeners for game events.
	 * 
	 * @param dialogListener listener for creating a user dialog
	 * @param scoreListener  listener for updating the player's score
	 */
	public void setListeners(ShowDialogListener dialogListener, ScoreUpdateListener scoreListener) {
		this.dialogListener = dialogListener;
		this.scoreListener = scoreListener;
	}

	/**
	 * Save the game to the given file path.
	 * 
	 * @param filePath location of file to save
	 */
	public void save(String filePath) {
		GameFileUtil.save(filePath, this);
	}

	/**
	 * Load the game from the given file path
	 * 
	 * @param filePath location of file to load
	 */
	public void load(String filePath) {
		GameFileUtil.load(filePath, this);
	}
}
