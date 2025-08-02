package ChatGPT_part2;

import java.util.ArrayList;
import java.util.List;

public class part_A {

    // Generate all possible two-letter combinations (e.g., aa, ab, …, zz)
    public static List<String> generateTwoLetterCombinations() {
        List<String> combinations = new ArrayList<>();
        for (char first = 'a'; first <= 'z'; first++) {
            for (char second = 'a'; second <= 'z'; second++) {
                combinations.add("" + first + second);
            }
        }
        return combinations;
    }
}
