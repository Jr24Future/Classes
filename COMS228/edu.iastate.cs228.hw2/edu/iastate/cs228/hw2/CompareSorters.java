package edu.iastate.cs228.hw2;

/**
 *  
 * @author Erroll Barker
 *
 */

/**
 * 
 * This class executes four sorting algorithms: selection sort, insertion sort, mergesort, and
 * quicksort, over randomly generated integers as well integers from a file input. It compares the 
 * execution times of these algorithms on the same input. 
 *
 */

import java.io.FileNotFoundException;
import java.util.Scanner; 
import java.util.Random;
import java.io.File;

public class CompareSorters 
{
	/**
	 * Repeatedly take integer sequences either randomly generated or read from files. 
	 * Use them as coordinates to construct points.  Scan these points with respect to their 
	 * median coordinate point four times, each time using a different sorting algorithm.  
	 * 
	 * @param args
	 **/
	public static void main(String[] args) throws FileNotFoundException
	{		
		// TODO 
		// 
		// Conducts multiple rounds of comparison of four sorting algorithms.  Within each round, 
		// set up scanning as follows: 
		// 
		//    a) If asked to scan random points, calls generateRandomPoints() to initialize an array 
		//       of random points. 
		// 
		//    b) Reassigns to the array scanners[] (declared below) the references to four new 
		//       PointScanner objects, which are created using four different values  
		//       of the Algorithm type:  SelectionSort, InsertionSort, MergeSort and QuickSort. 
		// 
		// 	
		PointScanner[] scanners = new PointScanner[4];
		Scanner scnr = new Scanner(System.in);
		int numTrials = 1;
		Boolean exit = false;
		System.out.println("keys:  1 (random integers)   2 (file input)   3 (exit)");
		while(exit == false) {
		System.out.print("Trial " + numTrials + ": ");
		numTrials++;
		int numInput = scnr.nextInt();
		PointScanner scanner = null;
			if (numInput == 1) {
				Random rand = new Random();
				System.out.print("Enter number of random points: ");
				int numRandPoints = scnr.nextInt();
		        Point[] randomPoints = generateRandomPoints(numRandPoints, rand);
		        System.out.println("algorithm size time (ns)");
				System.out.println("----------------------------------");
		        for (Algorithm algo : Algorithm.values()) {
		            scanner = new PointScanner(randomPoints, algo);
		            scanner.scan();
		            scanners[algo.ordinal()] = scanner;
		            System.out.println(scanner.stats());
		        }
		        System.out.println("----------------------------------");
		        //scanner.writeMCPToFile();
			} else if (numInput == 2) {
				System.out.println("Points from a file");
				System.out.print("File name: ");
				String fileName = scnr.next();
		        System.out.println("algorithm size time (ns)");
				System.out.println("----------------------------------");
		        for (Algorithm algo : Algorithm.values()) {
		            scanner = new PointScanner(fileName, algo);
		            scanner.scan();
		            scanners[algo.ordinal()] = scanner;
		            System.out.println(scanner.stats());
		        }
		        System.out.println("----------------------------------");
		        //scanner.writeMCPToFile();
			} else if (numInput == 3) {
				exit = true;
				break;
			}
		}
		scnr.close();
		
		// For each input of points, do the following. 
		// 
		//     a) Initialize the array scanners[].  
		//
		//     b) Iterate through the array scanners[], and have every scanner call the scan() 
		//        method in the PointScanner class.  
		//
		//     c) After all four scans are done for the input, print out the statistics table from
		//		  section 2.
		//
		// A sample scenario is given in Section 2 of the project description. 
    }
	
	/**
	 * This method generates a given number of random points.
	 * The coordinates of these points are pseudo-random numbers within the range 
	 * [-50,50] x [-50,50]. Please refer to Section 3 on how such points can be generated.
	 * 
	 * Ought to be private. Made public for testing. 
	 * 
	 * @param numPts  	number of points
	 * @param rand      Random object to allow seeding of the random number generator
	 * @throws IllegalArgumentException if numPts < 1
	 */
	public static Point[] generateRandomPoints(int numPts, Random rand) throws IllegalArgumentException
	{ 
		if (numPts < 1) {
            throw new IllegalArgumentException("Number of points must be at least 1.");
        }

        Point[] randomPoints = new Point[numPts];
        for (int i = 0; i < numPts; i++) {
            int x = rand.nextInt(101) - 50; // Generates random integer between -50 and 50
            int y = rand.nextInt(101) - 50; // Generates random integer between -50 and 50
            randomPoints[i] = new Point(x, y);
        }

        return randomPoints;
	}
}