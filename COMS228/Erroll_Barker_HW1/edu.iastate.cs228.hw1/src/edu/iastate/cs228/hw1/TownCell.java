package edu.iastate.cs228.hw1;

/**
 * 
 * @author <<Erroll barker>>
 *	Also provide appropriate comments for this class
 *
 */
public abstract class TownCell {

	protected Town plain;
	protected int row;
	protected int col;
	
	
	// constants to be used as indices.
	protected static final int RESELLER = 0;
	protected static final int EMPTY = 1;
	protected static final int CASUAL = 2;
	protected static final int OUTAGE = 3;
	protected static final int STREAMER = 4;
	
	public static final int NUM_CELL_TYPE = 5;
	
	//Use this static array to take census.
	public static final int[] nCensus = new int[NUM_CELL_TYPE];

	public TownCell(Town p, int r, int c) {
		plain = p;
		row = r;
		col = c;
	}
	
	/**
	 * Checks all neigborhood cell types in the neighborhood.
	 * Refer to homework pdf for neighbor definitions (all adjacent
	 * neighbors excluding the center cell).
	 * Use who() method to get who is present in the neighborhood
	 *  
	 * @param counts of all customers
	 */
	public void census(int nCensus[]) {
		// zero the counts of all customers
		nCensus[RESELLER] = 0; 
		nCensus[EMPTY] = 0; 
		nCensus[CASUAL] = 0; 
		nCensus[OUTAGE] = 0; 
		nCensus[STREAMER] = 0; 

		int fRow = 0;
		int fCol = 0;
		int lRow,lCol;
		
		if(row != 0 && col != 0) {
			fRow = row - 1;			//subtract one for the start index
			fCol = col - 1;
			lRow = fRow + 2;
			lCol = fCol + 2;
		}
		else if(col == 0 && row != 0) {
			fRow = row - 1;
			lRow = fRow + 2;
			lCol = 1;
		}
		else if(row == 0 && col != 0) {
			fCol = col - 1;
			lCol = fCol + 2;
			lRow = 1;
		}
		else {
			lRow = 1;
			lCol = 1;
		}
		
		if(lRow >= plain.getLength()) {
			lRow = row;
		}
		if(lCol >= plain.getWidth()) {
			lCol = col;
		}
		
		for(int i = fRow; i <= lRow; i++) {
			for(int j = fCol; j <= lCol; j++) {
				if(i != row || j != col) {
					State find = plain.grid[i][j].who();	//find state of cell
					
					if (find.equals(State.RESELLER)) {
						nCensus[RESELLER]++;
					}
					else if (find.equals(State.EMPTY)) {
						nCensus[EMPTY]++;
					}
					else if (find.equals(State.CASUAL)) {
						nCensus[CASUAL]++;
					}
					else if (find.equals(State.STREAMER)) {
						nCensus[STREAMER]++;
					}
					else if (find.equals(State.OUTAGE)) {
						nCensus[OUTAGE]++;
					}
				}
			}
		}
	}

	/**
	 * Gets the identity of the cell.
	 * 
	 * @return State
	 */
	public abstract State who();

	/**
	 * Determines the cell type in the next cycle.
	 * 
	 * @param tNew: town of the next cycle
	 * @return TownCell
	 */
	public abstract TownCell next(Town tNew);
}
