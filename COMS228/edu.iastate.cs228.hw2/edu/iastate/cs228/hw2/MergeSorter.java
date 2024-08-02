package edu.iastate.cs228.hw2;

import java.io.FileNotFoundException;
import java.lang.NumberFormatException; 
import java.lang.IllegalArgumentException; 
import java.util.InputMismatchException;

/**
 *  
 * @author Erroll Barker
 *
 */

/**
 * 
 * This class implements the mergesort algorithm.   
 *
 */

public class MergeSorter extends AbstractSorter
{
	// Other private instance variables if needed
	
	/** 
	 * Constructor takes an array of points.  It invokes the superclass constructor, and also 
	 * set the instance variables algorithm in the superclass.
	 *  
	 * @param pts   input array of integers
	 */
	public MergeSorter(Point[] pts) 
	{
		super(pts); 
		this.algorithm = "mergesort";  
	}


	/**
	 * Perform mergesort on the array points[] of the parent class AbstractSorter. 
	 * 
	 */
	@Override 
	public void sort()
	{
		mergeSortRec(this.points);
	}

	
	/**
	 * This is a recursive method that carries out mergesort on an array pts[] of points. One 
	 * way is to make copies of the two halves of pts[], recursively call mergeSort on them, 
	 * and merge the two sorted subarrays into pts[].   
	 * 
	 * @param pts	point array 
	 */
	private void mergeSortRec(Point[] pts)
	{
		if(pts.length < 2) {
			//base case
			return;
		}
		else{
			int mid = pts.length/2;
	        Point[] firstHalf = new Point[mid];
	        Point[] secondHalf = new Point[pts.length - mid];
	        
	        //copy first half
	        for(int i = 0; i < mid; i++) {
	        	firstHalf[i] = pts[i];
	        }
	        //copy second half
	        for(int j = mid; j < pts.length; j++) {
	        	secondHalf[j-mid] = pts[j];
	        }
	        //sort new halves
	        mergeSortRec(firstHalf);
	        mergeSortRec(secondHalf);
	  
	        merge(firstHalf, secondHalf, pts);
		}
	}


	private static void merge(Point[] firstHalf, Point[] secondHalf, Point[] pts) {
		int fLength = firstHalf.length;
		int sLength = secondHalf.length;
		//find smaller length
		int x = 0,y = 0,z = 0;
		//x goes through firstHalf, y goes through secondHalf, z goes through pts
		while(x < fLength && y < sLength) {
			if(firstHalf[x].compareTo(secondHalf[y]) < 1) {
				//if first half has smaller num
				pts[z] = firstHalf[x];
				x++;
			}
			else {
				//if second half has smaller num
				pts[z] = secondHalf[y];
				y++;
			}
			//go to next val
			z++;
		}
		//account for leftover values
		while(x < fLength) {
			pts[z] = firstHalf[x];
			x++;
			z++;
		}
		while(y < sLength) {
			pts[z] = secondHalf[y];
			y++;
			z++;
		}
	}

	
	// Other private methods if needed ...

}
