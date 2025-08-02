package ChatGPT_part2Test;

import static org.junit.Assert.*;
import java.util.List;
import org.junit.Test;
import ChatGPT_part2.part_C;

public class part_D {

	 @Test
	    public void testThreeLetterCombinations() {
	        List<String> threeLetter = part_C.generateThreeLetterCombinations();
	        assertEquals("Expected 17576 three-letter combinations", 17576, threeLetter.size());
	        assertEquals("First combination should be 'aaa'", "aaa", threeLetter.get(0));
	        assertEquals("Last combination should be 'zzz'", "zzz", threeLetter.get(threeLetter.size() - 1));
	    }
}
