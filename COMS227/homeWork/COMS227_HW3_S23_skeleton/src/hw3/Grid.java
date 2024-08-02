package hw3;

import api.Tile;

/**
 * Represents the game's grid.
 * @author Erroll Barker
 */
public class Grid {
	private int height;
	private int width;
	private Tile [][] tiles;
	/**
	 * Creates a new grid.
	 * 
	 * @param width  number of columns
	 * @param height number of rows
	 */
	public Grid(int width, int height) {
		this.width = width;
		this.height = height;
		tiles = new Tile[height][width];
	}

	/**
	 * Get the grid's width.
	 * 
	 * @return width
	 */
	public int getWidth() {
		// TODO
		return width;
	}

	/**
	 * Get the grid's height.
	 * 
	 * @return height
	 */
	public int getHeight() {
		// TODO
		return height;
	}

	/**
	 * Gets the tile for the given column and row.
	 * 
	 * @param x the column
	 * @param y the row
	 * @return getTile
	 */
	public Tile getTile(int x, int y) {
		
		return tiles[y][x];
	}

	/**
	 * Sets the tile for the given column and row. Calls tile.setLocation().
	 * 
	 * @param tile the tile to set
	 * @param x    the column
	 * @param y    the row
	 */
	public void setTile(Tile tile, int x, int y) {
		tile.setLocation(x, y);
		tiles[y][x] = tile;
		
		
	}
	
	@Override
	public String toString() {
		String str = "";
		for (int y=0; y<getHeight(); y++) {
			if (y > 0) {
				str += "\n";
			}
			str += "[";
			for (int x=0; x<getWidth(); x++) {
				if (x > 0) {
					str += ",";
				}
				str += getTile(x, y);
			}
			str += "]";
		}
		return str;
	}
}
