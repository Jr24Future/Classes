package test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import part2.QuickSort;

import java.util.Arrays;

class QuickSortTest {

    private void testSorting(int[] input, int[] expected) {
        int[] testArray = input.clone();  // Clone to avoid modifying the original array
        QuickSort.correctedQuickSort(testArray, 0, testArray.length - 1);  // ✅ Using the corrected method now
        assertArrayEquals(expected, testArray);
    }
    
    @Test
    void testArrayWithDuplicates() {  // ✅ Part C: Does NOT reveal the fault
        int[] input = {5, 5, 5, 5, 5};  
        int[] expected = {5, 5, 5, 5, 5}; 
        testSorting(input, expected);
    }

    @Test
    void testSingleElementArray() {  // ✅ Part C: Does NOT reveal the fault
        int[] input = {42};  
        int[] expected = {42}; 
        testSorting(input, expected);
    }
    
    @Test
    void testSortedArray() {  // ❌ Part D: REVEALS the fault (Fails because it gets reversed)
        int[] input = {10, 20, 30, 40, 50, 60, 70};  
        int[] expected = {10, 20, 30, 40, 50, 60, 70}; 
        testSorting(input, expected);
    }

    @Test
    void testUnsortedArray_RevealingFault() {  // ❌ Part D: REVEALS the fault
        int[] input = {64, 25, 12, 22, 11};  
        int[] expected = {11, 12, 22, 25, 64}; 
        testSorting(input, expected);
    }
}
