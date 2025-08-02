package ChatGPT_part2;

import java.util.ArrayList;
import java.util.List;

public class part_C {

	   // Generate all possible three-letter combinations.
    public static List<String> generateThreeLetterCombinations() {
        List<String> combinations = new ArrayList<>();
        for (char first = 'a'; first <= 'z'; first++) {
            for (char second = 'a'; second <= 'z'; second++) {
                for (char third = 'a'; third <= 'z'; third++) {
                    combinations.add("" + first + second + third);
                }
            }
        }
        return combinations;
    }
}
