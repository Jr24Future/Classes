package edu.iastate.cs228.hw1;

/**
/		@author <<Erroll Barker>>
*/

public class Empty extends TownCell{

	public Empty(Town p, int r, int c) {
		super(p, r, c);
		// TODO Auto-generated constructor stub
	}

	@Override
	public State who() {
		return State.EMPTY;
	}

	@Override
	public TownCell next(Town tNew) {
		plain.grid[row][col].census(nCensus);
		if (nCensus[OUTAGE] + nCensus[EMPTY] <= 1) {		//6a
			return new Reseller(tNew, this.row, this.col);
		}
		else {
			return new Casual(tNew, this.row, this.col);
		}
	}

}
