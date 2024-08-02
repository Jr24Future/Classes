package edu.iastate.cs228.hw1;

import static org.junit.jupiter.api.Assertions.*;

import java.io.FileNotFoundException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CasualTest {

	@BeforeEach
	void setUp() throws Exception {
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void testWho() throws FileNotFoundException{
		Town tNew = new Town("ISP4x4.txt");
		State s = tNew.grid[1][2].who();
		assertEquals(State.CASUAL,s);
	}

	@Test 
	void testNext() throws FileNotFoundException{
		Town t = new Town("ISP4x4.txt");
		Town tNew = new Town(4,4);
		t.grid[1][2].census(TownCell.nCensus);
		State s = t.grid[1][2].next(tNew).who();
		assertEquals(State.OUTAGE,s);
	}

	@Test
	void testCasual() {
		//constructor
		Town tNew = null;
		Casual c = new Casual(tNew,0,0);
		assertEquals(tNew,c.plain);
		assertEquals(0,c.row);
		assertEquals(0,c.col);
	}

}
