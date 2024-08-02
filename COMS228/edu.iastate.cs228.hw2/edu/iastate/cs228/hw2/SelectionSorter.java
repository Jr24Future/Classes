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
 * This class implements selection sort.   
 *
 */

public class SelectionSorter extends AbstractSorter
{
	// Other private instance variables if you need ... 
	
	/**
	 * Constructor takes an array of points.  It invokes the superclass constructor, and also 
	 * set the instance variables algorithm in the superclass.
	 *  
	 * @param pts  
	 */
	public SelectionSorter(Point[] pts)  
	{
		super(pts); 
		this.algorithm = "selection sort";
	}	

	
	/** 
	 * Apply selection sort on the array points[] of the parent class AbstractSorter.  
	 * 
	 */
	@Override 
	public void sort()
	{
		if(Point.xORy) {
			//compare x
			for (int i = 0; i < points.length; i++){
	            int test = i;  
	            for (int j = i + 1; j < points.length; j++){  
	                if (points[j].getX() <= points[test].getX()){
	                    test = j;    							 
	                }  
	            }  
	            if(test != i) {
		            Point small = points[test];   
		            points[test] = points[i];  
		            points[i] = small; 
	            }
			}
		}
		else {
			//compare y
			for (int i = 0; i < points.length; i++){
	            int test = i;  
	            for (int j = i + 1; j < points.length; j++){  
	                if (points[j].getY() <= points[test].getY()){  
	                    test = j;    							 
	                }  
	            }  
	            if(test != i) {
		            Point small = points[test];   
		            points[test] = points[i];  
		            points[i] = small; 
	            }
			}
		}
	}	
}
