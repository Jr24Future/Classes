package edu.iastate.cs228.hw1;

import static org.junit.jupiter.api.Assertions.*;

import java.io.FileNotFoundException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TownCellTest {

	@BeforeEach
	void setUp() throws Exception {
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void testCensus() throws FileNotFoundException {
		Town test = new Town("ISP4x4.txt");
		test.grid[0][0].census(TownCell.nCensus);
		assertEquals(1,TownCell.nCensus[0]); //reseller
		assertEquals(2,TownCell.nCensus[1]); //empty
		assertEquals(0,TownCell.nCensus[2]); //casual
		assertEquals(0,TownCell.nCensus[3]); //outage
		assertEquals(0,TownCell.nCensus[4]); //streamer
	}

}
