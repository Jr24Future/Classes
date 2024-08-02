package mini;

/**
 * Utility class with static methods for loop practice.
 */
public class LoopsInfinityAndBeyond {

	/**
	 * Private constructor to disable instantiation.
	 */
	private LoopsInfinityAndBeyond() {
	}

	/**
	 * Define a flying saucer as the following string pattern: one ‘(‘, followed by
	 * zero to many ‘=’, followed by one ‘)’. Write a Java method that, given a
	 * string find the first instance of a flying saucer (starting from the left)
	 * and return its length. If no flying saucer exists return 0.
	 * <p>
	 * For example: Given: "(==)" Return: 4
	 * <p>
	 * Given: "***()**(===)" Return: 2
	 * <p>
	 * Given: "****(***)" Return: 0
	 * 
	 * @param source input string
	 * @return the length
	 */
	public static int flyingSaucerLength(String s) {
	    int countEquals = 0;
	    boolean foundOpeningBracket = false;

	    for (char c : s.toCharArray()) {
	        if (c == '(') {
	            foundOpeningBracket = true;
	        } else if (c == ')') {
	            if (foundOpeningBracket) {
	                return countEquals + 2;
	            } else {
	                return 0;
	            }
	        } else if (c == '=') {
	            if (!foundOpeningBracket) {
	                return 0;
	            }
	            countEquals++;
	        } else {
	            if (foundOpeningBracket) {
	                return 0;
	            }
	        }
	    }

	    return 0;
	}

	/**
	 * Write a Java method that, given a string which many contain a flying saucer
	 * broken into two parts with characters in between, return a string where the
	 * flying is fixed by removing the in between characters. Look for the two parts
	 * of the flying saucer from left to right and fix the saucer with the first
	 * available parts.
	 * <p>
	 * For example:
	 * Given: ***(==****===)***
	 * Return: ***(=====)***
	 * <p>
	 * Given: ***(==****)**=)*
	 * Return: ***(==)**=)*
	 * <p>
	 * Given: ***(==)**
	 * Return: ***(==)**
	 * 
	 * @param s
	 * @return
	 */
    public static String fixFlyingSaucer(String s) {
    	boolean find = false;
    	int start = 0;
    	int done = 0;
    	
    	for(int i = 0; i < s.length();i++) {
    		if (s.charAt(i) == '(') {
    			find = true;
    		}
    		else if (s.charAt(i)=='*' && find) {
    			if (start==0) {
    				start = i;
    			}
    			done = i;
    		}
    		if(s.charAt(i) == ')' && find) {
    			find = false;
    			if(start>0&&done>0) {
    				s = s.substring(0,start)+s.substring(done+1);
    			}
    		}
    		
    	}
    	
        return s;
    }

	/**
	 * Write a Java method that, given a string which many contain many flying
	 * saucers, return the number of flying saucers. For this problem a flying
	 * saucer may wrap around from the right side of the string to the left.
	 * <p>
	 * For example:
	 * Given: ***(===)***
	 * Return: 1
	 * <p>
	 * Given: =)**(==)**(
	 * Return: 2
	 * <p>
	 * Given: ***(=*=)**
	 * Return: 0
	 * 
	 * @param s
	 * @return
	 */
	public static int countFlyingSaucers(String s) {
		int count = 0;
		boolean find = false;
		
		for(int i = 0; i<s.length();i++) {
			char current = s.charAt(i);
			if(s.charAt(i) == '(') {
				count++;
				find = true;
			}
				if(current == '=') {
					find = true;
					}
			else if (current == '*' && find) {
					count = 0;
					break;
				}
				if(current == ')' && find == true) {
					find = false;
				}
			
		}
		    return count;
	}

	
	    
	/**
	 * Write a Java method that, given a string which many contain many flying
	 * saucers, shifts all of the saucers one character to the right. For this
	 * problem a flying saucer may wrap around from the right side of the string to
	 * the left. The returned string should have the same number of characters as
	 * the given string. This is achieved by moving the character to the right of a
	 * saucer to its left. It can be assumed that saucers will never be touching
	 * each other (i.e., there will always be at least one character between any two
	 * saucers). Also, a saucer will not touch itself (e.g., "=)(=").
	 * <p>
	 * For example:
	 * Given: ***(===)***
	 * Return: ****(===)**
	 * <p>
	 * Given: =)**(==)**(
	 * Return: (=)***(==)*
	 * <p>
	 * Given: a()bcde(=*=)fg
	 * Return: ab()cde(=*=)fg
	 * 
	 * @param s
	 * @return
	 */
	public static String flyingSaucersFly(String s) {
			
		
	    char lastChar = s.charAt(s.length()-1);
	    String newString = s.substring(0, s.length()-1);
	    newString = lastChar+ newString;
	    
	    return newString;
	}
}