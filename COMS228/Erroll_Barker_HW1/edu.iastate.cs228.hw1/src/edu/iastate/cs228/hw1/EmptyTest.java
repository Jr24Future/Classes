package edu.iastate.cs228.hw1;

import static org.junit.jupiter.api.Assertions.*;

import java.io.FileNotFoundException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EmptyTest {

	@BeforeEach
	void setUp() throws Exception {
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void testWho() throws FileNotFoundException{
		Town tNew = new Town("ISP4x4.txt");
		State s = tNew.grid[1][0].who();
		assertEquals(State.EMPTY,s);
	}

	@Test
	void testNext() throws FileNotFoundException{
		Town t = new Town("ISP4x4.txt");
		Town tNew = new Town(4,4);
		t.grid[1][0].census(TownCell.nCensus);
		State s = t.grid[1][0].next(tNew).who();
		assertEquals(State.CASUAL,s);
	}

	@Test
	void testEmpty() {
		//constructor
		Town tNew = null;
		Empty e = new Empty(tNew,0,0);
		assertEquals(tNew,e.plain);
		assertEquals(0,e.row);
		assertEquals(0,e.col);
	}

}
