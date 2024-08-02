package lab3;
import balloon4.Balloon;
import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.assertEquals;
public class BalloonTests {
	private Balloon bb;
	@Before
	public void setup()  // runs before every test case
    {
      bb = new Balloon(10);
    }
	
	@Test
	public void testInitial() {
		assertEquals(0, bb.getRadius());
	}
	@Test
	public void testBolloon(){
		assertEquals(false,bb.isPopped());
		}
	@Test
	public void blow() {
		bb.blow(5);
		assertEquals(5, bb.getRadius());
	}
	@Test 
	public void deflate() {
		bb.deflate();
		assertEquals(0, bb.getRadius());
	}
	@Test
	public void getPop() {
		bb.pop();
		assertEquals(true,bb.isPopped());
		assertEquals(0, bb.getRadius());
	}
	@Test
	public void overBlow() {
		bb.blow(11);
		assertEquals(true,bb.isPopped());
		assertEquals(0, bb.getRadius());
	}
	@Test
	public void radiusPop() {
		bb.pop();
		assertEquals(0, bb.getRadius());
	}
	@Test
	public void tryAgain() {
		bb.pop();
		
		bb.blow(2);
		assertEquals(0, bb.getRadius());
	}
	@Test
	public void seeDeflate() {
		bb.deflate();
		assertEquals(false, bb.isPopped());
	}
	@Test
	public void addBlow() {
		bb.blow(2);
		assertEquals(2, bb.getRadius());
		bb.blow(3);
		assertEquals(5, bb.getRadius());
	}
	}
	
