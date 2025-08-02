package part2;

public class QuickSort {
    // Correct Quick Sort Implementation
    public static void goodQuickSort(int[] arr, int low, int high) {
        if (low < high) {
            int pi = partition(arr, low, high);

            goodQuickSort(arr, low, pi - 1);  // Recursively sort left half
            goodQuickSort(arr, pi + 1, high); // Recursively sort right half
        }
    }

    // Partition function
    private static int partition(int[] arr, int low, int high) {
        int pivot = arr[high];  // Choosing last element as pivot
        int i = low - 1;

        for (int j = low; j < high; j++) {
            if (arr[j] < pivot) {
                i++;
                int temp = arr[i];
                arr[i] = arr[j];
                arr[j] = temp;
            }
        }

        int temp = arr[i + 1];
        arr[i + 1] = arr[high];
        arr[high] = temp;

        return i + 1;
    }
    
 // Faulty Quick Sort Implementation (Error: Skipping elements during partition)
    public static void faultyQuickSort(int[] arr, int low, int high) {
        if (low < high) {
            int pi = faultyPartition(arr, low, high);  // Using faulty partition

            faultyQuickSort(arr, low, pi - 1);  // Left partition
            faultyQuickSort(arr, pi + 1, high); // Right partition
        }
    }

    // Faulty Partition function (Incorrect pivot selection logic)
    private static int faultyPartition(int[] arr, int low, int high) {
        int pivot = arr[high];  
        int i = low - 1;

        for (int j = low; j < high; j++) {
            if (arr[j] > pivot) {  // ERROR: Uses '>' instead of '<' (Incorrect comparison)
                i++;
                int temp = arr[i];
                arr[i] = arr[j];
                arr[j] = temp;
            }
        }

        int temp = arr[i + 1];
        arr[i + 1] = arr[high];
        arr[high] = temp;

        return i + 1;
    }

 // Corrected Quick Sort Implementation
    public static void correctedQuickSort(int[] arr, int low, int high) {
        if (low < high) {
            int pi = correctedPartition(arr, low, high);  // Use corrected partition function

            correctedQuickSort(arr, low, pi - 1);  // Recursively sort left half
            correctedQuickSort(arr, pi + 1, high); // Recursively sort right half
        }
    }

    // Corrected Partition function
    private static int correctedPartition(int[] arr, int low, int high) {
        int pivot = arr[high];  // Choosing last element as pivot
        int i = low - 1;

        for (int j = low; j < high; j++) {
            if (arr[j] < pivot) {  //  FIXED: Correct condition
                i++;
                int temp = arr[i];
                arr[i] = arr[j];
                arr[j] = temp;
            }
        }

        int temp = arr[i + 1];
        arr[i + 1] = arr[high];
        arr[high] = temp;

        return i + 1;
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

        System.out.println("Original Array:");
        printArray(testArray);

        // Run Good Quick Sort
        int[] goodArray = testArray.clone();
        System.out.println("\nRunning Good Quick Sort:");
        goodQuickSort(goodArray, 0, goodArray.length - 1);
        printArray(goodArray);

        // Run Faulty Quick Sort
        int[] faultyArray = testArray.clone();
        System.out.println("\nRunning Faulty Quick Sort:");
        faultyQuickSort(faultyArray, 0, faultyArray.length - 1);
        printArray(faultyArray);

        // Run Corrected Quick Sort
        int[] correctedArray = testArray.clone();
        System.out.println("\nRunning Corrected Quick Sort:");
        correctedQuickSort(correctedArray, 0, correctedArray.length - 1);
        printArray(correctedArray);
    }
}
