package lab2;

import java.util.Random;

public class RabbitModel
{
	/**private  int P;
  public RabbitModel()
  {
    reset();
  }  
 
  
  public int getPopulation()
  {
    
    return P;
  }
  
  
  public void simulateYear()
  {
    P += 1;
  }
  
  
  public void reset()
  {
   P = 2;
  }*/
//	private  int P;
//	  public RabbitModel()
//	  {
//	    reset();
//	  }  
//	 
//	  
//	  public int getPopulation()
//	  {
//	    
//	    return P;
//	  }
//	  
//	  
//	  public void simulateYear()
//	  {
//	    P += 1;
//	    if(P==5) {
//	    	P=0;
//	    }
//	  }
//	  
//	  
//	  public void reset()
//	  {
//	   P = 0;
//	  }
//	private  int P;
//	  public RabbitModel()
//	  {
//	    reset();
//	  }  
//	 
//	  
//	  public int getPopulation()
//	  {
//	    
//	    return P;
//	  }
//	  
//	  
//	  public void simulateYear()
//	  {
//	    P /=2;
//	    
//	  }
//	  
//	  
//	  public void reset()
//	  {
//	   P = 500;
//	  }
//	private  int P;
//	  public RabbitModel()
//	  {
//	    reset();
//	  }  
//	 
//	  
//	  public int getPopulation()
//	  {
//	    
//	    return P;
//	  }
//	  
//	  
//	  public void simulateYear()
//	  {
//		  Random rand = new Random();
//		  P += rand.nextInt(10);
//	    
//	  }
//	  
//	  
//	  public void reset()
//	  {
//	   P = 0;
//	  }
	private  int P;
	private int yearBefore;
	private int lastYear;
	  public RabbitModel()
	  {
	    reset();
	  }  
	 
	  
	  public int getPopulation()
	  {
	    
	    return P;
	  }
	  
	  
	  public void simulateYear()
	  {
		  yearBefore = lastYear;
		  lastYear = P;
		  P = lastYear + yearBefore;
	    
	  }
	  
	  
	  public void reset()
	  {
		  yearBefore = 0;
		  lastYear = 1;
	   P = 1;
	  }
}