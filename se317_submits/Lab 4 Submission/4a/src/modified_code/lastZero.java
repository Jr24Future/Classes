package modified_code;

public class lastZero {
	// Introduction to Software Testing
	// Authors: Paul Ammann & Jeff Offutt
	// Chapter 1; page 13
	// Can be run from command line
	// See LastZeroTest.java for JUnit tests

	  /**
	   * Find LAST index of zero
	   *
	   * @param x array to search
	   * @return index of last 0 in x; -1 if absent
	   * @throws NullPointerException if x is null
	   */
	   public static int lastZero (int[] x)
	   {
	       if (x == null) 
	    	   return -1;
	       
		   for (int i = x.length - 1; i >= 0; i--)	//now iterate from the end of the array
	      {
	         if (x[i] == 0)
	         {
	            return i;
	         }
	      }
	      return -1;
	   }
	   
	   
	}


