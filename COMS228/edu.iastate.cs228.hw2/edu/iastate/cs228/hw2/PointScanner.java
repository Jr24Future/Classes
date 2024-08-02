package edu.iastate.cs228.hw2;

import java.io.File;

/**
 * 
 * @author Erroll Barker
 *
 */

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * 
 * This class sorts all the points in an array of 2D points to determine a reference point whose x and y 
 * coordinates are respectively the medians of the x and y coordinates of the original points. 
 * 
 * It records the employed sorting algorithm as well as the sorting time for comparison. 
 *
 */
public class PointScanner  
{
	private Point[] points; 
	
	private Point medianCoordinatePoint;  // point whose x and y coordinates are respectively the medians of 
	                                      // the x coordinates and y coordinates of those points in the array points[].
	private Algorithm sortingAlgorithm;    
	
		
	protected long scanTime; 	       // execution time in nanoseconds. 
	
	/**
	 * This constructor accepts an array of points and one of the four sorting algorithms as input. Copy 
	 * the points into the array points[].
	 * 
	 * @param  pts  input array of points 
	 * @throws IllegalArgumentException if pts == null or pts.length == 0.
	 */
	public PointScanner(Point[] pts, Algorithm algo) throws IllegalArgumentException
	{
		if (pts == null || pts.length == 0) {
            throw new IllegalArgumentException("Input array of points is null or empty.");
        }
        points = new Point[pts.length];
        for (int i = 0; i < pts.length; i++) {
            points[i] = pts[i];
        }
        sortingAlgorithm = algo;
    }

    /**
     * This constructor reads points from a file.
     *
     * @param inputFileName
     * @throws FileNotFoundException
     * @throws InputMismatchException if the input file contains an odd number of integers
     */
    protected PointScanner(String inputFileName, Algorithm algo) throws FileNotFoundException, InputMismatchException {
    	File f = new File(inputFileName);
		Scanner scnr;

		try {
			scnr = new Scanner(f);
		} catch (FileNotFoundException e) {
			throw new FileNotFoundException();
		}

		int counter = 0;
		while (scnr.hasNextInt()) {
			scnr.nextInt();
			counter++;
		}

		if (counter % 2 != 0) {
			throw new InputMismatchException();
		}

		points = new Point[counter / 2];
		scnr = new Scanner(f);

		int pointsCounter = 0;
		while (scnr.hasNextInt()) {
			points[pointsCounter] = new Point(scnr.nextInt(), scnr.nextInt());
			pointsCounter++;
		}

		sortingAlgorithm = algo;
    }

    /**
     * Carry out two rounds of sorting using the algorithm designated by sortingAlgorithm as follows:
     * a) Sort points[] by the x-coordinate to get the median x-coordinate.
     * b) Sort points[] again by the y-coordinate to get the median y-coordinate.
     * c) Construct medianCoordinatePoint using the obtained median x- and y-coordinates.
     * Based on the value of sortingAlgorithm, create an object of SelectionSorter, InsertionSorter, MergeSorter,
     * or QuickSorter to carry out sorting.
     *
     * @param algo
     * @return
     */
    public void scan() {
        AbstractSorter aSorter;

        // Create an object to be referenced by aSorter according to sortingAlgorithm. For each of the two
        // rounds of sorting, have aSorter do the following:
        // a) Call setComparator() with an argument 0 or 1.
        // b) Call sort().
        // c) Use a new Point object to store the coordinates of the medianCoordinatePoint
        // d) Set the medianCoordinatePoint reference to the object with the correct coordinates.
        // e) Sum up the times spent on the two sorting rounds and set the instance variable scanTime.

        aSorter = null;
		if (sortingAlgorithm == Algorithm.SelectionSort) {
			aSorter = new SelectionSorter(points);
		} else if (sortingAlgorithm == Algorithm.InsertionSort) {
			aSorter = new InsertionSorter(points);
		} else if (sortingAlgorithm == Algorithm.MergeSort) {
			aSorter = new MergeSorter(points);
		} else if (sortingAlgorithm == Algorithm.QuickSort) {
			aSorter = new QuickSorter(points);
		}

		aSorter.setComparator(0);
		long xSortingTime = System.nanoTime();
		aSorter.sort();
		xSortingTime = System.nanoTime() - xSortingTime;
		Point xMedian = aSorter.getMedian();

		aSorter.setComparator(1);
		long ySortingTime = System.nanoTime();
		aSorter.sort();
		ySortingTime = System.nanoTime() - ySortingTime;
		Point yMedian = aSorter.getMedian();

		medianCoordinatePoint = new Point(xMedian.getX(), yMedian.getY());
		scanTime = xSortingTime + ySortingTime;

    }

    /**
     * Outputs performance statistics in the format:
     * <sorting algorithm> <size>  <time>
     * For instance,
     * selection sort   1000	  9200867
     * Use the spacing in the sample run in Section 2 of the project description.
     */
    public String stats() {
        return sortingAlgorithm.toString() + "\t" + points.length + "\t" + scanTime;
    }

    /**
     * Write MCP after a call to scan(), in the format "MCP: (x, y)" The x and y coordinates of the point are
     * displayed on the same line with exactly one blank space in between.
     */
    @Override
    public String toString() {
        return "MCP: (" + medianCoordinatePoint.getX() + ", " + medianCoordinatePoint.getY() + ")";
    }

    /**
     * This method, called after scanning, writes point data into a file by outputFileName. The format
     * of data in the file is the same as printed out from toString(). The file can help you verify
     * the full correctness of a sorting result and debug the underlying algorithm.
     *
     * @throws IOException
     */
    public void writeMCPToFile() throws FileNotFoundException {
        try (PrintWriter writer = new PrintWriter(new File("MCP.txt"))) {
            writer.printf(this.toString());
        }
    }

		
}
