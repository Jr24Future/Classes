package edu.iastate.cs228.hw1;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;
import static edu.iastate.cs228.hw1.State.*;


/**
 *  @author <<Erroll Barker>>
 *
 */
public class Town {
	
	private int length, width;  //Row and col (first and second indices)
	public TownCell[][] grid;
	
	
	/**
	 * Constructor to be used when user wants to generate grid randomly, with the given seed.
	 * This constructor does not populate each cell of the grid (but should assign a 2D array to it).
	 * @param length
	 * @param width
	 */
	public Town(int length, int width) {
		this.length = length;
		this.width = width;
		grid = new TownCell[length][width];
		
	}
	
	/**
	 * Constructor to be used when user wants to populate grid based on a file.
	 * Please see that it simple throws FileNotFoundException exception instead of catching it.
	 * Ensure that you close any resources (like file or scanner) which is opened in this function.
	 * @param inputFileName
	 * @throws FileNotFoundException
	 */
	public Town(String inputFileName) throws FileNotFoundException {
		try {
			File file = new File(inputFileName);
			Scanner scan = new Scanner(file); 			//first line
			int row = scan.nextInt();
			int col = scan.nextInt();
			this.length = row;
			this.width = col;
			
			grid = new TownCell[row][col];
			
			for (int i = 0; i < row; i++) {
				for (int j = 0; j < col; j++) {
					String letter = scan.next();	//checks new row

					if (letter.equals("R")) {
						grid[i][j] = new Reseller(this, i, j);
					}
					else if (letter.equals("E")) {
						grid[i][j] = new Empty(this, i, j);
					}
					else if (letter.equals("C")) {
						grid[i][j] = new Casual(this, i, j);
					} 
					else if (letter.equals("O")) {
						grid[i][j] = new Outage(this, i, j);
					} 
					else if (letter.equals("S")) {
						grid[i][j] = new Streamer(this, i, j);
					}
				}
				if (i + 1 == row) {
					scan.close();
				}
			}
			scan.close();
		} catch (IOException e) {
			System.out.println("Error!!!");
		}
	}
	
	/**
	 * Returns width of the grid.
	 * @return
	 */
	public int getWidth() {
		return this.width;
	}
	
	/**
	 * Returns length of the grid.
	 * @return
	 */
	public int getLength() {
		return this.length;
	}

	/**
	 * Initialize the grid by randomly assigning cell with one of the following class object:
	 * Casual, Empty, Outage, Reseller OR Streamer
	 */
	public void randomInit(int seed) {
		Random rand = new Random(seed);
		for (int i = 0; i < this.grid.length; i++) {
			for (int j = 0; j < this.grid[0].length; j++) {
				int sNum = rand.nextInt(5);
				if (sNum == 0) {
					this.grid[i][j] = new Reseller(this, i, j);
				} 
				else if (sNum == 1) {
					this.grid[i][j] = new Empty(this, i, j);
				}
				else if (sNum == 2) {
					this.grid[i][j] = new Casual(this, i, j);
				} 
				else if (sNum == 3) {
					this.grid[i][j] = new Outage(this, i, j);
				} 
				else if (sNum == 4) {
					this.grid[i][j] = new Streamer(this, i, j);
				}
			}
		}
	}
	
	/**
	 * Output the town grid. For each square, output the first letter of the cell type.
	 * Each letter should be separated either by a single space or a tab.
	 * And each row should be in a new line. There should not be any extra line between 
	 * the rows.
	 */
	@Override
	public String toString() {
		String s = "";
		for (int i = 0; i < this.grid.length; i++) {
			for (int j = 0; j < this.grid[0].length; j++) {
				if (this.grid[i][j].who() == RESELLER) {
					s += "R";
				} 
				else if (this.grid[i][j].who() == EMPTY) {
					s += "E";
				} 
				else if (this.grid[i][j].who() == CASUAL) {
					s += "C";
				} 
				else if (this.grid[i][j].who() == OUTAGE) {
					s += "O";
				} 
				else if (this.grid[i][j].who() == STREAMER) {
					s += "S";
				}
				if (j + 1 != this.grid[0].length) {					//add space
					s += " ";
				}
				else {
					s += "\n"; 										//add new line to the end
				}
			}
		}
		return s;
	}
}
