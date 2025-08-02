package ChatGPT_part2;

import java.util.ArrayList;
import java.util.List;

public class part_E {

    // Generalized method: generate all letter combinations of a given length.
    public static List<String> generateLetterCombinations(int length) {
        List<String> combinations = new ArrayList<>();
        if (length <= 0) {
            return combinations;
        }
        generateHelper("", length, combinations);
        return combinations;
    }
    
    // Helper method to build combinations recursively.
    private static void generateHelper(String prefix, int remaining, List<String> combinations) {
        if (remaining == 0) {
            combinations.add(prefix);
            return;
        }
        for (char c = 'a'; c <= 'z'; c++) {
            generateHelper(prefix + c, remaining - 1, combinations);
        }
    }
}
