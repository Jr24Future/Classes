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
 * This class implements insertion sort.   
 *
 */

public class InsertionSorter extends AbstractSorter 
{
	// Other private instance variables if you need ... 
	
	/**
	 * Constructor takes an array of points.  It invokes the superclass constructor, and also 
	 * set the instance variables algorithm in the superclass.
	 * 
	 * @param pts  
	 */
	public InsertionSorter(Point[] pts) 
	{
		super(pts);
		this.algorithm ="insertion sort";
	}	

	
	/** 
	 * Perform insertion sort on the array points[] of the parent class AbstractSorter.  
	 */
	@Override 
	public void sort()
	{
		if(Point.xORy) {
			//compare by x
			int n = this.points.length;  
	        for (int j = 1; j < n; j++) {  
	            Point check = this.points[j];  
	            int i = j-1;  
	            while (i >= 0 && this.points[i].getX() > check.getX()){  
	                this.points [i+1] = this.points [i];  
	                i--;  
	            }  
	            this.points[i+1] = check;  
	        }
		}
		else {
		//compare by y
			int n = this.points.length;  
	        for (int j = 1; j < n; j++) {  
	            Point check = this.points[j];  
	            int i = j-1;  
	            while (i >= 0 && this.points[i].getY() > check.getY()){  
	                this.points [i+1] = this.points [i];  
	                i--;  
	            }  
	            this.points[i+1] = check;  
	        }
		}
	}		
}
