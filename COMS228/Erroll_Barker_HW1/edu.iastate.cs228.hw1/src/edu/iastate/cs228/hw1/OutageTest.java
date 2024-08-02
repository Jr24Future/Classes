package edu.iastate.cs228.hw1;

import static org.junit.jupiter.api.Assertions.*;

import java.io.FileNotFoundException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OutageTest {

	@BeforeEach
	void setUp() throws Exception {
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void testWho() throws FileNotFoundException{
		Town tNew = new Town("ISP4x4.txt");
		State s = tNew.grid[0][0].who();
		assertEquals(State.OUTAGE,s);
	}

	@Test
	void testNext() throws FileNotFoundException{
		Town t = new Town("ISP4x4.txt");
		Town tNew = new Town(4,4);
		t.grid[0][0].census(TownCell.nCensus);
		State s = t.grid[0][0].next(tNew).who();
		//System.out.println(TownCell.nCensus[0]);
		assertEquals(State.EMPTY,s);
	}

	@Test
	void testOutage() {
		//constructor
		Town tNew = null;
		Outage o = new Outage(tNew,0,0);
		assertEquals(tNew,o.plain);
		assertEquals(0,o.row);
		assertEquals(0,o.col);
	}

}
