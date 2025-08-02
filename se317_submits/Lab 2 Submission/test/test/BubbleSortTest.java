package test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import part1.BubbleSort;

import java.util.Arrays;

class BubbleSortTest {

    // Helper method to check sorting correctness
    private void testSorting(int[] input, int[] expected) {
        int[] testArray = input.clone();  // Clone to avoid modifying the original array
        BubbleSort.correctedBubbleSort(testArray);  // Using the corrected method
        assertArrayEquals(expected, testArray);
    }
    @Test
    void testSortedArray() {  // ✅ Part C: Does NOT reveal the fault
        int[] input = {10, 20, 30, 40, 50, 60, 70};  
        int[] expected = {10, 20, 30, 40, 50, 60, 70}; 
        testSorting(input, expected);
    }

    @Test
    void testArrayWithDuplicates() {  // ✅ Part C: Does NOT reveal the fault
        int[] input = {3, 3, 3, 3, 3};  
        int[] expected = {3, 3, 3, 3, 3}; 
        testSorting(input, expected);
    }
    
    @Test
    void testUnsortedArray_RevealingFault() { // ❌ Part D: REVEALS the fault
        int[] input = {5, 1, 4, 2, 8}; 
        int[] expected = {1, 2, 4, 5, 8}; 
        testSorting(input, expected);
    }

    @Test
    void testAnotherUnsortedArray_RevealingFault() { // ❌ Part D: REVEALS the fault
        int[] input = {64, 25, 12, 22, 11};
        int[] expected = {11, 12, 22, 25, 64}; 
        testSorting(input, expected);
    }
}