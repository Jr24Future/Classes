package ChatGPT_part2Test;

import static org.junit.Assert.*;
import java.util.List;
import org.junit.Test;
import ChatGPT_part2.part_A;

public class part_B {

	@Test
    public void testTwoLetterCombinations() {
        List<String> twoLetter = part_A.generateTwoLetterCombinations();
        assertEquals("Expected 676 two-letter combinations", 676, twoLetter.size());
        assertEquals("First combination should be 'aa'", "aa", twoLetter.get(0));
        assertEquals("Last combination should be 'zz'", "zz", twoLetter.get(twoLetter.size() - 1));
    }
}
