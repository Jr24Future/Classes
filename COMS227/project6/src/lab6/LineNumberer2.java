package lab6;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class LineNumberer2
{
	
	public static int lineReader(String scanner) {
		return scanner.length();
	}
  public static void main(String[] args) throws FileNotFoundException
  {
	  File file = new File("../project5/story.txt");
	   Scanner scanner = new Scanner(file);
	   
	   int lineCount = 1;
	   while (scanner.hasNextLine())
	    {
	      String line = scanner.nextLine();
	      System.out.print(lineCount + " ");
	      System.out.println(line.length());
	      lineCount++;
	    }
	    scanner.close();
  }
  
}
//Go up one level to the workspace directory.
//Go up one level to the cs227 directory.
//Go down into the stuff directory.
//Open the file story.txt.
//(Follow the red arrows in the illustration.) As a path, this is written,
//File file = new File("../../stuff/story.txt");