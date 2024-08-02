package plotter;
	
	import java.io.File;
	import java.io.FileNotFoundException;
	import java.util.ArrayList;
	import java.util.Scanner;
	
public class ReadPolyline {
	public static ArrayList<Polyline> polyline = new ArrayList<Polyline>();
	private static ArrayList<Polyline> readFile(String test) throws FileNotFoundException{
		ArrayList<Polyline>poyline=new ArrayList<>();
		Scanner reader = new Scanner(new File(test));
		String line;
		while(reader.hasNextLine()) {
			line = reader.nextLine();
			if(line.trim().length() != 0) {
				String[] tokens = line.split(" ");
				int width = 0;
				String color = tokens[1].trim();
				try {
					width = Integer.parseInt(tokens[0].trim());
				}
				catch(NumberFormatException ex)
				{}
				Polyline pl;
				int i = 1;
				if (width != 0) {
					pl=new Polyline(color, width);
					i =2;
				}
				else {
					pl = new Polyline(color);
				}
				for(; i <tokens.length; i++) {
					//pl.addPoint(new Point(Integer.parseInt(tokens[i].trim())));
				}
				polyline.add(pl);
			}
		}
		return polyline;
	}
	
	
	
	public static void main(String[] args) throws FileNotFoundException
	  {
	    ArrayList<Polyline> list = readFile("hello.txt");
	    Plotter plotter = new Plotter();

	    for (Polyline p : list)
	    {
	      plotter.plot(p);
	    }
	  }
	

}
