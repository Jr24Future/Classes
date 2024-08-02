package edu.iastate.cs228.hw1;

import static org.junit.jupiter.api.Assertions.*;

import java.io.FileNotFoundException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ResellerTest {

	@BeforeEach
	void setUp() throws Exception {
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void testWho() throws FileNotFoundException{
		Town tNew = new Town("ISP4x4.txt");
		State s = tNew.grid[0][1].who();
		assertEquals(State.RESELLER,s);
	}

	@Test
	void testNext() throws FileNotFoundException{
		Town t = new Town("ISP4x4.txt");
		Town tNew = new Town(4,4);
		t.grid[0][1].census(TownCell.nCensus);
		State s = t.grid[0][1].next(tNew).who();
		assertEquals(State.EMPTY,s);
	}
	
	@Test
	void testReseller() {
		//constructor
		Town tNew = null;
		Reseller r = new Reseller(tNew,0,0);
		assertEquals(tNew,r.plain);
		assertEquals(0,r.row);
		assertEquals(0,r.col);
	}

}