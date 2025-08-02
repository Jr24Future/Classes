package part1;

public class BubbleSort {
    // Correct Bubble Sort Implementation
    public static void goodBubbleSort(int[] arr) {
        int n = arr.length;
        boolean swapped;

        System.out.println("Original Array:");
        printArray(arr);

        for (int i = 0; i < n - 1; i++) {
            swapped = false;
            for (int j = 0; j < n - i - 1; j++) {
                if (arr[j] > arr[j + 1]) {
                    // Swap elements
                    int temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                    swapped = true;
                }
            }
            // If no swapping occurs, the array is already sorted
            if (!swapped) break;
        }
        System.out.println("Sorted Array:");
        printArray(arr);
    }
    
 // Faulty Bubble Sort Implementation
    public static void faultyBubbleSort(int[] arr) {
        int n = arr.length;
        boolean swapped;

        System.out.println("Original Array:");
        printArray(arr);

        for (int i = 0; i < n - 1; i++) {
            swapped = false;
            for (int j = 1; j < n - i - 1; j++) {  // ERROR: `j = 1` should be `j = 0`
                if (arr[j] > arr[j + 1]) {
                    int temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                    swapped = true;
                }
            }
            if (!swapped) break;
        }
        System.out.println("Sorted Array:");
        printArray(arr);
    }
    
 // Corrected Bubble Sort Implementation
    public static void correctedBubbleSort(int[] arr) {
        int n = arr.length;
        boolean swapped;

        System.out.println("Original Array:");
        printArray(arr);

        for (int i = 0; i < n - 1; i++) {
            swapped = false;
            for (int j = 0; j < n - i - 1; j++) {  // FIXED: j starts at 0, correct loop condition
                if (arr[j] > arr[j + 1]) {
                    int temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                    swapped = true;
                }
            }
            if (!swapped) break;  // Optimization: Stop early if no swaps were made
        }
        System.out.println("Sorted Array:");
        printArray(arr);
    }

    // Utility function to print an array
    public static void printArray(int[] arr) {
        for (int num : arr) {
            System.out.print(num + " ");
        }
        System.out.println();
    }

    // Main method for testing
    public static void main(String[] args) {
        int[] testArray = {64, 34, 25, 12, 22, 11, 90};
        
        System.out.println("Running Good Bubble Sort:");
        goodBubbleSort(testArray.clone());
        
        System.out.println("\nRunning Faulty Bubble Sort:");
        faultyBubbleSort(testArray.clone());

        System.out.println("\nRunning Corrected Bubble Sort:");
        correctedBubbleSort(testArray.clone());
    }
}
