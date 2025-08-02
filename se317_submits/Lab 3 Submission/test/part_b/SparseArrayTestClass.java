package part_b;

import org.junit.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

public class SparseArrayTestClass {
		
    private SparseArray<Object> array;

    @Before
    public void create() {
        array = new SparseArray<>();  
    }
/*
    @Test
    public void handlesInsertionInDescendingOrder() {
        array.put(7, "seven");  
        array.checkInvariants(); 

        array.put(6, "six");  
        array.checkInvariants();  

        assertThat(array.get(6), equalTo("six"));
        assertThat(array.get(7), equalTo("seven"));
    }
*/    
    @Test
    public void insertNullValue() {
        array.put(0, null);  // Insert null
        array.checkInvariants();  // Check consistency
        assertThat(array.size(), equalTo(0));  // Expected size should be 0
    }
    
    @Test
    public void insertReplaceValue() {
        array.put(6, "seis");  // Insert "seis"
        array.put(6, "six");   // Replace with "six"
        assertThat(array.get(6), equalTo("six")); // Should return "six"
    }
	     
}



















