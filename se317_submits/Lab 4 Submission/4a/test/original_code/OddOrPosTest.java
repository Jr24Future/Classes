package original_code;

import static org.junit.Assert.*;
import org.junit.Test;
import original_code.OddOrPos;

public class OddOrPosTest {

    @Test
    public void testOddOrPos_NoFault() {
        int[] arr = {2, 4, 1, 6};
        System.out.println(OddOrPos.oddOrPos(arr));
        int result = OddOrPos.oddOrPos(arr);
        assertEquals("The count should be 4", 4, result);
    }
    
    @Test
    public void testOddOrPos_ExecutesFault() {
        int[] arr = {-3, -2, 0, 1, 4};
        System.out.println(OddOrPos.oddOrPos(arr));
        int result = OddOrPos.oddOrPos(arr);
        assertEquals("Fault: Negative odd numbers are not counted", 3, result);
    }
    
    @Test
    public void testOddOrPos_Error() {
        int[] arr = null;
        System.out.println(OddOrPos.oddOrPos(arr));
        int result = OddOrPos.oddOrPos(arr);
        assertEquals("when pass null, the method should return -1", -1, result);
    }
}