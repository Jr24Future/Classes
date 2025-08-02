package part3;

public class MergeSort {
    // Correct Merge Sort Implementation
    public static void goodMergeSort(int[] arr, int left, int right) {
        if (left < right) {
            int mid = left + (right - left) / 2;

            // Recursively sort left and right halves
            goodMergeSort(arr, left, mid);
            goodMergeSort(arr, mid + 1, right);

            // Merge the sorted halves
            merge(arr, left, mid, right);
        }
    }

    // Merging two halves of the array
    private static void merge(int[] arr, int left, int mid, int right) {
        int n1 = mid - left + 1;
        int n2 = right - mid;

        int[] leftArr = new int[n1];
        int[] rightArr = new int[n2];

        // Copy data to temp arrays
        for (int i = 0; i < n1; i++) leftArr[i] = arr[left + i];
        for (int i = 0; i < n2; i++) rightArr[i] = arr[mid + 1 + i];

        // Merge temp arrays back into arr[left...right]
        int i = 0, j = 0, k = left;
        while (i < n1 && j < n2) {
            if (leftArr[i] <= rightArr[j]) {
                arr[k] = leftArr[i];
                i++;
            } else {
                arr[k] = rightArr[j];
                j++;
            }
            k++;
        }

        // Copy remaining elements of leftArr
        while (i < n1) {
            arr[k] = leftArr[i];
            i++;
            k++;
        }

        // Copy remaining elements of rightArr
        while (j < n2) {
            arr[k] = rightArr[j];
            j++;
            k++;
        }
    }
    
 // Faulty Merge Sort Implementation (Error: Skipping elements during merge)
    public static void faultyMergeSort(int[] arr, int left, int right) {
        if (left < right) {
            int mid = left + (right - left) / 2;

            faultyMergeSort(arr, left, mid);
            faultyMergeSort(arr, mid + 1, right);

            faultyMerge(arr, left, mid, right);  // Using faulty merge function
        }
    }

    // Faulty Merging function (Introduced error: Skips copying rightArr elements)
 // Faulty Merge function (More aggressive error: Misplacing elements randomly)
    private static void faultyMerge(int[] arr, int left, int mid, int right) {
        int n1 = mid - left + 1;
        int n2 = right - mid;

        int[] leftArr = new int[n1];
        int[] rightArr = new int[n2];

        for (int i = 0; i < n1; i++) leftArr[i] = arr[left + i];
        for (int i = 0; i < n2; i++) rightArr[i] = arr[mid + 1 + i];

        int i = 0, j = 0, k = left;
        while (i < n1 && j < n2) {
            if (leftArr[i] <= rightArr[j]) {
                arr[k] = leftArr[i];
                i++;
            } else {
                arr[k] = rightArr[j];
                j++;
            }
            k++;
        }

        // ERROR: Swapping elements randomly in left half
        while (i < n1) {
            if (i % 2 == 0 && i + 1 < n1) {  // Swaps pairs of elements
                arr[k] = leftArr[i + 1];  
            } else {
                arr[k] = leftArr[i];
            }
            i++;
            k++;
        }

        // ERROR: Copying duplicate elements from right half incorrectly
        while (j < n2) {
            arr[k] = rightArr[j];  
            if (j % 2 == 0 && j + 1 < n2) {  
                arr[k] = rightArr[j + 1];  // Duplicates some elements
            }
            j++;
            k++;
        }
    }

 // Corrected Merge Sort Implementation
    public static void correctedMergeSort(int[] arr, int left, int right) {
        if (left < right) {
            int mid = left + (right - left) / 2;

            correctedMergeSort(arr, left, mid);
            correctedMergeSort(arr, mid + 1, right);

            correctedMerge(arr, left, mid, right);  // Use corrected merge function
        }
    }

    // Corrected Merging function
    private static void correctedMerge(int[] arr, int left, int mid, int right) {
        int n1 = mid - left + 1;
        int n2 = right - mid;

        int[] leftArr = new int[n1];
        int[] rightArr = new int[n2];

        for (int i = 0; i < n1; i++) leftArr[i] = arr[left + i];
        for (int i = 0; i < n2; i++) rightArr[i] = arr[mid + 1 + i];

        int i = 0, j = 0, k = left;
        while (i < n1 && j < n2) {
            if (leftArr[i] <= rightArr[j]) {
                arr[k] = leftArr[i];
                i++;
            } else {
                arr[k] = rightArr[j];
                j++;
            }
            k++;
        }

        //  FIXED: Copy remaining elements from leftArr
        while (i < n1) {
            arr[k] = leftArr[i];
            i++;
            k++;
        }

        //  FIXED: Copy remaining elements from rightArr
        while (j < n2) {
            arr[k] = rightArr[j];
            j++;
            k++;
        }
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

        // Run Good Merge Sort
        int[] goodArray = testArray.clone();
        System.out.println("\nRunning Good Merge Sort:");
        goodMergeSort(goodArray, 0, goodArray.length - 1);
        printArray(goodArray);

        // Run Faulty Merge Sort
        int[] faultyArray = testArray.clone();
        System.out.println("\nRunning Faulty Merge Sort:");
        faultyMergeSort(faultyArray, 0, faultyArray.length - 1);
        printArray(faultyArray);

        // Run Corrected Merge Sort
        int[] correctedArray = testArray.clone();
        System.out.println("\nRunning Corrected Merge Sort:");
        correctedMergeSort(correctedArray, 0, correctedArray.length - 1);
        printArray(correctedArray);
    }
}
