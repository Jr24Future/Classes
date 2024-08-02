package lab2;
import java.util.Random;
public class RandomTest {
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		Random rand = new Random(137);
		System.out.println(rand.nextInt(6));
		System.out.println(rand.nextInt(6));
		System.out.println(rand.nextInt(6));
		System.out.println(rand.nextInt(6));
	}

}
