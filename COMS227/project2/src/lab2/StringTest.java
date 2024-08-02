package lab2;

public class StringTest {
	

	public static void main(String[] args) {
		String message;
		// TODO Auto-generated method stub
		message = "Hello, world!";
		System.out.println(message);
		
		int theLength = message.length();
		System.out.println(theLength);
		
		char theChar = message.charAt(2);
		System.out.println(theChar);

		theChar = message.charAt(4);
		System.out.println(theChar);
		
		System.out.println(message.toUpperCase());
		
		System.out.println(message.substring(0,5));
		
		
	}

}
