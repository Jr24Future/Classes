package original_code;

import static org.junit.Assert.*;
import org.junit.Test;

import modified_code.lastZero;
import original_code.CountPositive;

public class CountPositiveTest {

    @Test
    public void Part_B() {
        int[] arr = {-1, 1, 2, -3};
        System.out.println(CountPositive.countPositive(arr));
        int result = CountPositive.countPositive(arr);
        assertEquals("Count of positive numbers should be 2", 2, result);
    }
    
    @Test
    public void Part_C() {
        int[] arr = {-1, 0, 1, 2, -3};
        System.out.println(CountPositive.countPositive(arr));
        int result = CountPositive.countPositive(arr);
        assertEquals("Fault: 0 is counted as positive", 2, result);
    }
    
    @Test
    public void Part_D() {
        int[] arr = null;
        System.out.println(CountPositive.countPositive(arr));
        int result = CountPositive.countPositive(arr);
        assertEquals("when pass null, the method should return -1", -1, result);
    }
}