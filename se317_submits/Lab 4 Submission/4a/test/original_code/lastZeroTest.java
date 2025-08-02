package original_code;

import static org.junit.Assert.*;
import org.junit.Test;
import original_code.lastZero;

public class lastZeroTest {

    @Test
    public void Part_B() {
        int[] arr = {1, 0, 2, 3};
        System.out.println(lastZero.lastZero(arr));
        int result = lastZero.lastZero(arr);
        assertEquals("The index of 0 should be 1", 1, result);
    }
    
    @Test
    public void Part_C() {
        int[] arr = {1, 0, 2, 0, 3};
        System.out.println(lastZero.lastZero(arr));
        int result = lastZero.lastZero(arr);
        assertEquals("The method returns the first occurrence.", 3, result);
    }
    
    @Test
    public void Part_D() {
        int[] arr = null;
        System.out.println(lastZero.lastZero(arr));
        int result = lastZero.lastZero(arr);
        assertEquals("when pass null, the method should return -1", -1, result);
    }
}