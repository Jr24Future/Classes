package edu.iastate.cs228.hw1;
import java.io.FileNotFoundException;
import java.util.Scanner;
/**
 * @author <<Erroll Barker>>
 *
 * The ISPBusiness class performs simulation over a grid 
 * plain with cells occupied by different TownCell types.
 *
 */
public class ISPBusiness {

	/**
	 * Returns a new Town object with updated grid value for next billing cycle.
	 * @param tOld: old/current Town object.
	 * @return: New town object.
	 */
	public static Town updatePlain(Town tOld) {
		Town tNew = new Town(tOld.getLength(), tOld.getWidth());
		for(int i = 0; i < tOld.getLength(); i++) {
			for(int j = 0; j < tOld.getWidth(); j++) {
				tNew.grid[i][j] = tOld.grid[i][j].next(tNew);
			}
		}
		return tNew;
	}
	
	/**
	 * Returns the profit for the current state in the town grid.
	 * @param town
	 * @return
	 */
	public static int getProfit(Town town) {
		int c = 0;   //c = count
		for(int i = 0; i < town.getLength(); i++) {
			for(int j = 0; j < town.getWidth(); j++) {
				if(town.grid[i][j].who() == State.CASUAL) {
					c++;
				}
			}
		}
		return c;
	}
	

	/**
	 *  Main method. Interact with the user and ask if user wants to specify elements of grid
	 *  via an input file (option: 1) or wants to generate it randomly (option: 2).
	 *  
	 *  Depending on the user choice, create the Town object using respective constructor and
	 *  if user choice is to populate it randomly, then populate the grid here.
	 *  
	 *  Finally: For 12 billing cycle calculate the profit and update town object (for each cycle).
	 *  Print the final profit in terms of %. You should print the profit percentage
	 *  with two digits after the decimal point:  Example if profit is 35.5600004, your output
	 *  should be:
	 *
	 *	35.56%
	 *  
	 * Note that this method does not throw any exception, so you need to handle all the exceptions
	 * in it.
	 * 
	 * @param args
	 * 
	 */
	public static void main(String []args) {
		Scanner myObj = new Scanner(System.in);
		System.out.println("How to populate grid (type 1 or 2): 1: from a file. 2: randomly with seed");
		int Num = myObj.nextInt();
		int profit = 0;
		
		if(Num == 1) {
			System.out.println("Please enter file path:");
			String filePath = myObj.next();
			
			try {
				Town town = new Town(filePath);
				int rows = town.getLength();
				int cols = town.getWidth();
				
				Town[] tList = new Town[12];
				tList[0] = town;
				profit += getProfit(town);
				
				for(int i = 1; i < 12; i++) {
					Town tpNew = updatePlain(tList[i-1]);  
					tList[i] = tpNew;
					profit += getProfit(tList[i]);
					if(i == 11) {
						double num = (100*profit)/(double)(rows*cols*12);
						System.out.printf("%.2f", num);
						System.out.println("%");
					}
				}
			}
			catch(FileNotFoundException e) {
				System.out.println("No file");
			}
		}
		else if(Num == 2) {
			System.out.println("Provide rows, cols and seed integer separated by spaces: ");
			int rows2 = myObj.nextInt();
			int cols2 = myObj.nextInt();
			int sNum2 = myObj.nextInt();
			Town town2 = new Town(rows2,cols2);
			town2.randomInit(sNum2);
			
			Town[] tList2 = new Town[12];
			tList2[0] = town2;
			profit += getProfit(town2);
			
			for(int i = 1; i < 12; i++) {
				Town tpNew2 = updatePlain(tList2[i-1]);
				tList2[i] = tpNew2;
				
				profit += getProfit(tList2[i]);
				if(i == 11) {
					double num2 = (100*profit)/(double)(rows2*cols2*12);
					System.out.printf("%.2f", num2);
					System.out.println("%");
				}
			}
		}
		else {
			System.out.println("try again");
		}
		myObj.close();
	}
}
