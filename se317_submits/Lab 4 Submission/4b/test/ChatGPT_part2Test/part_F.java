package ChatGPT_part2Test;


import static org.junit.Assert.*;
import java.util.List;
import org.junit.Test;
import ChatGPT_part2.part_E;

public class part_F {

	@Test
    public void testGeneralizedLetterCombinations() {
        List<String> oneLetter = part_E.generateLetterCombinations(1);
        assertEquals("Expected 26 one-letter combinations", 26, oneLetter.size());
        assertEquals("First should be 'a'", "a", oneLetter.get(0));
        assertEquals("Last should be 'z'", "z", oneLetter.get(oneLetter.size() - 1));
        
        List<String> twoLetter = part_E.generateLetterCombinations(2);
        assertEquals("Expected 676 two-letter combinations", 676, twoLetter.size());
        assertEquals("First should be 'aa'", "aa", twoLetter.get(0));
        assertEquals("Last should be 'zz'", "zz", twoLetter.get(twoLetter.size() - 1));
        
        List<String> threeLetter = part_E.generateLetterCombinations(3);
        assertEquals("Expected 17576 three-letter combinations", 17576, threeLetter.size());
        assertEquals("First should be 'aaa'", "aaa", threeLetter.get(0));
        assertEquals("Last should be 'zzz'", "zzz", threeLetter.get(threeLetter.size() - 1));
    }
}
