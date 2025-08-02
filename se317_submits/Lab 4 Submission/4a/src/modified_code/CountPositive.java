package modified_code;

public class CountPositive {

	/** 
	   * Counts positive elements in array
	   *
	   * @param x array to search
	   * @return number of positive elements in x
	   * @throws NullPointerException if x is null
	   */
	   public static int countPositive (int[] x)
	   {
		  if (x == null) 
	           return -1; 
		  
	      int count = 0;
	   
	      for (int i=0; i < x.length; i++)
	      {
	         if (x[i] > 0)	//now count only strictly positive numbers
	         {
	            count++;
	         }
	      }
	      return count;
	   }
	     
	   
	   
}
