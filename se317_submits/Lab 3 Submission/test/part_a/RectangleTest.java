package part_a;

import static org.junit.Assert.*;

import java.awt.Point;

import static org.hamcrest.CoreMatchers.*;
import static part_a.ConstrainsSidesTo.constrainsSidesTo;
import org.junit.*;


public class RectangleTest {
	private Rectangle rectangle;
	 @After
	public void ensureInvariant() {
		 assertThat(rectangle, constrainsSidesTo(100));
	 }
	@Test
	public void answersArea() {
		rectangle = new Rectangle(new Point(5, 5), new Point (19, 10));
		assertThat(rectangle.area(), equalTo(70));
	}
	
	@Test
	public void allowsDynamicallyChangingSize() {
		rectangle = new Rectangle(new Point(5, 5));
		rectangle.setOppositeCorner(new Point(80, 80));
		assertThat(rectangle.area(), equalTo(75 * 75));
	}
}