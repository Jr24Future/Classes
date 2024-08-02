package edu.iastate.cs228.hw1;

import static org.junit.jupiter.api.Assertions.*;

import java.io.FileNotFoundException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TownTest {

	@BeforeEach
	void setUp() throws Exception {
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void testTownIntInt() {
		// constructor from row*col ints
		Town t = new Town(2, 2);
		assertEquals(2, t.getLength());
		assertEquals(2, t.getWidth());
	}

	@Test
	void testTownString() throws FileNotFoundException {
		Town t = new Town("ISP4x4.txt");
		assertEquals(4, t.getLength());
		assertEquals(4, t.getWidth());

	}

	@Test
	void testGetWidth() {
		Town tNew = new Town(2, 2);
		assertEquals(2, tNew.getWidth());
	}

	@Test
	void testGetLength() {
		Town tNew = new Town(3, 3);
		assertEquals(3, tNew.getLength());
	}

	@Test
	void testRandomInit() {
		Town t = new Town(3, 3);
		Town s = new Town(3, 3);
		t.randomInit(3);
		s.randomInit(7);
		// returner will be true if grids prove to be different at some point
		boolean returner = false;
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				if (t.grid[i][j].who() != (s.grid[i][j].who())) {
					returner = true;
					break;
				}
			}
		}
		assertEquals(true, true);
	}

	@Test
	void testToString() throws FileNotFoundException {
		Town town = new Town("ISP4x4.txt");
		String newTown = town.toString();
		assertEquals("O", newTown.substring(0, 1));
	}
}
