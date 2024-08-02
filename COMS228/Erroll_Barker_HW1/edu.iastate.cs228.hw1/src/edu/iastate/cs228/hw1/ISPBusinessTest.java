package edu.iastate.cs228.hw1;

import static org.junit.jupiter.api.Assertions.*;

import java.io.FileNotFoundException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


class ISPBusinessTest {

	@BeforeEach
	void setUp() throws Exception {
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void testUpdatePlain() throws FileNotFoundException {
		Town tOld = new Town("ISP4x4.txt");
		//make new town using the method, then check
		Town tNew = ISPBusiness.updatePlain(tOld);
		//check 4 random values in array
		assertEquals(State.EMPTY,tNew.grid[0][0].who());
		assertEquals(State.EMPTY,tNew.grid[3][3].who());
		assertEquals(State.OUTAGE,tNew.grid[2][1].who());
		assertEquals(State.OUTAGE,tNew.grid[1][2].who());
	}

	@Test
	void testGetProfit() throws FileNotFoundException {
		Town test = new Town("ISP4x4.txt");
		test.grid[1][2].census(TownCell.nCensus);
		assertEquals(0,TownCell.nCensus[2]);
	}

}
