package test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import part3.MergeSort;

import java.util.Arrays;

class MergeSortTest {

    private void testSorting(int[] input, int[] expected) {
        int[] testArray = input.clone();  // Clone to avoid modifying the original array
        MergeSort.correctedMergeSort(testArray, 0, testArray.length - 1);
        assertArrayEquals(expected, testArray);
    }

    @Test
    void testSingleElementArray() {  // ✅ Part C: Does NOT reveal the fault
        int[] input = {42};  
        int[] expected = {42}; 
        testSorting(input, expected);
    }
    
    @Test
    void testArrayWithDuplicates() {  // ✅ Part C: Does NOT reveal the fault
        int[] input = {5, 5, 5, 5, 5};  
        int[] expected = {5, 5, 5, 5, 5}; 
        testSorting(input, expected);
    }
    
    @Test
    void testUnsortedArray_RevealingFault() {  // ❌ Part D: REVEALS the fault
        int[] input = {64, 34, 25, 12, 22, 11, 90};  
        int[] expected = {11, 12, 22, 25, 34, 64, 90}; 
        testSorting(input, expected);
    }

    @Test
    void testSortedArray_RevealingFault() {  // ❌ Part D: REVEALS the fault
        int[] input = {10, 20, 30, 40, 50, 60, 70};  
        int[] expected = {10, 20, 30, 40, 50, 60, 70}; 
        testSorting(input, expected);
    }
}
