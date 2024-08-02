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
 * This class implements the version of the quicksort algorithm presented in the lecture.   
 *
 */

public class QuickSorter extends AbstractSorter
{
	
	// Other private instance variables if you need ... 
	private int[] tempList = new int[points.length];
	private Point pivotPoint = null;
	private int pivot;
	/** 
	 * Constructor takes an array of points.  It invokes the superclass constructor, and also 
	 * set the instance variables algorithm in the superclass.
	 *   
	 * @param pts   input array of integers
	 */
	public QuickSorter(Point[] pts)
	{
		super(pts);
		this.algorithm = "quicksort"; 
	}
		

	/**
	 * Carry out quicksort on the array points[] of the AbstractSorter class.  
	 * 
	 */
	@Override 
	public void sort()
	{
		quickSortRec(0,this.points.length-1);
	}
	
	
	/**
	 * Operates on the subarray of points[] with indices between first and last. 
	 * 
	 * @param first  starting index of the subarray
	 * @param last   ending index of the subarray
	 */
	private void quickSortRec(int first, int last)
	{
		//base case
				if(first >= last) {
					return;
				}
				int pivot = partition(first,last);
				quickSortRec(first,pivot-1);
				quickSortRec(pivot+1,last);
	}
	
	
	/**
	 * Operates on the subarray of points[] with indices between first and last.
	 * 
	 * @param first
	 * @param last
	 * @return
	 */
	private int partition(int first, int last)
	{
		Point p = points[last];
		int i= first-1;
		//nick lomuto method derived from lecture slides
		if(Point.xORy) {
			for(int j = first; j < last; j++) {
				if(this.points[j].getX() <= p.getX()) {
					i++;
					swap(i,j);
				}
			}
			swap(i+1,last);
			return i+1;
		}
		else {
			for(int j = first; j < last; j++) {
				if(this.points[j].getY() <= p.getY()) {
					i++;
					swap(i,j);
				}
			}
			swap(i+1,last);
			return i+1;
		}
	}	
		


	
	// Other private methods if needed ...
}
