package modified_code;

import static org.junit.Assert.*;
import org.junit.Test;
import modified_code.FindLast;

public class FindLastTest {
    @Test
    public void Part_B() {
        int[] arr = {1, 2, 3, 4, 2};
        System.out.println(FindLast.findLast(arr, 2));
        int result = FindLast.findLast(arr, 2);
        assertEquals("The last occurrence of 2 should be at index 4", 4, result);
    }

    @Test
    public void Part_C() {
        int[] arr = {5, 6, 7, 0};  
        System.out.println(FindLast.findLast(arr, 5));
        int result = FindLast.findLast(arr, 5);
        assertEquals("The last occurrence of 5 should be at index 0", 0, result);
    }
    
    @Test
    public void Part_D() {
    	int[] arr = null;
        System.out.println(FindLast.findLast(arr, 1));
        int result = FindLast.findLast(arr, 1);
        assertEquals("when pass null, the method should return -1", -1, result);
    }
   
}
