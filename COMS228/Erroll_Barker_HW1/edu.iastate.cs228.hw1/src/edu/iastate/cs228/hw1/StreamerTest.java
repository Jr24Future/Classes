package edu.iastate.cs228.hw1;

import static org.junit.jupiter.api.Assertions.*;

import java.io.FileNotFoundException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class StreamerTest {

	@BeforeEach
	void setUp() throws Exception {
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void testWho() throws FileNotFoundException {
		Town tNew = new Town("ISP4x4.txt");
		State s = tNew.grid[2][1].who();
		assertEquals(State.STREAMER, s);
	}

	@Test
	void testNext() throws FileNotFoundException {
		Town t = new Town("ISP4x4.txt");
		Town tNew = new Town(4, 4);
		t.grid[2][1].census(TownCell.nCensus);
		State s = t.grid[2][1].next(tNew).who();
		assertEquals(State.OUTAGE, s);
	}

	@Test
	void testStreamer() {
		// constructor
		Town tNew = null;
		Streamer s = new Streamer(tNew, 0, 0);
		assertEquals(tNew, s.plain);
		assertEquals(0, s.row);
		assertEquals(0, s.col);
	}

}

