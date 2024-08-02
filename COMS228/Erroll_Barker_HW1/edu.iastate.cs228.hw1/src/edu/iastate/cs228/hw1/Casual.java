package edu.iastate.cs228.hw1;

/**
/		@author <<Erroll Barker>>
*/

public class Casual extends TownCell{

	public Casual(Town p, int r, int c) {
		super(p, r, c);
		// TODO Auto-generated constructor stub
	}

	@Override
	public State who() {
		return State.CASUAL;
	}

	@Override
	public TownCell next(Town tNew) {
		plain.grid[row][col].census(nCensus);	
		if (nCensus[OUTAGE] + nCensus[EMPTY] <= 1) {       	// 6a 
			return new Reseller(tNew, this.row, this.col);
		}
		else {												//normal
			if (nCensus[RESELLER] > 0) {
				return new Outage(tNew, this.row, this.col);
			}
			else if(nCensus[STREAMER] > 0 || nCensus[CASUAL] >= 5) {
				return new Streamer(tNew, this.row, this.col);
			}
			return new Casual(tNew, this.row, this.col);	//7
			
		}
	}

}
