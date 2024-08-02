package plotter;

import java.awt.Point;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PolyLineArrayList {
	public static ArrayList<Polyline> list = new ArrayList<Polyline>();
	
	private static ArrayList<Polyline> readFile(String hello)throws FileNotFoundException{
		File file = new File(hello);
		Scanner scanner = new Scanner(file);
		while(scanner.hasNextLine()) {
			String line = scanner.nextLine();
			line = line.trim();
			if(!line.isEmpty() && !line.startsWith("#")) {
				try (Scanner temp = new Scanner(line)) {
					System.out.println(line);
					String first = temp.next();
					boolean isStringNumber = first.matches("[0-9]+");
					String last = "";
					int n = 0;
					if(isStringNumber){
						n = Integer.parseInt(first);
						last = temp.next();
					}
					else {
						last = first;
					}
					System.out.println(first+" "+last);
					Polyline p1 = new Polyline(last,n);
					while (temp.hasNextInt()) {
						int value = temp.nextInt();
						int value2=0;
						if(temp.hasNextInt())
						value2 = temp.nextInt();
						p1.addPoint(new Point(value, value2));
					}
					list.add(p1);
				} catch (NumberFormatException e) {
					
					e.printStackTrace();
				}
			}
		}
		scanner.close();
		return list;
	}
	
	public static void main(String args[]) throws FileNotFoundException{
		List<Polyline> list = readFile("hello.txt");
		Plotter p = new Plotter();
		for(Polyline p2:list){
			p.plot(p2);
		}
	}

		
}
