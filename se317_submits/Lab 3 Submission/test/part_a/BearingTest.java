package part_a;
import org.junit.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

public class BearingTest {
	   	
	//part 1
/*	
	@Test
	public void answersValidBearing() throws Exception {
	    assertThat(new Bearing(Bearing.MAX).value(), equalTo(Bearing.MAX));
	}

	@Test
	public void answersAngleBetweenItAndAnotherBearing() throws Exception {
	    assertThat(new Bearing(15).angleBetween(new Bearing(12)), equalTo(3));
	}

	@Test
	public void angleBetweenIsNegativeWhenThisBearingSmaller() throws Exception {
	    assertThat(new Bearing(12).angleBetween(new Bearing(15)), equalTo(-3));
	}

	
	@Test
	public void answersValidBearing() {
	    try {
	        assertThat(new Bearing(Bearing.MAX).value(), equalTo(Bearing.MAX));
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}

	   @Test
	   public void answersAngleBetweenItAndAnotherBearing() {
	       try {
	           assertThat(new Bearing(15).angleBetween(new Bearing(12)), equalTo(3));
	       } catch (Exception e) {
	           e.printStackTrace();
	       }
	   }
   
	   @Test
	   public void angleBetweenIsNegativeWhenThisBearingSmaller() {
	       try {
	           assertThat(new Bearing(12).angleBetween(new Bearing(15)), equalTo(-3));
	       } catch (Exception e) {
	           e.printStackTrace();
	       }
	   }
 */
	//part 1
	//part 2
	
    @Test
    public void angleBetweenZeroAnd355() {
        try {
            assertThat(new Bearing(0).angleBetween(new Bearing(355)), equalTo(-355));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void angleBetween90And180() {
        try {
            assertThat(new Bearing(90).angleBetween(new Bearing(180)), equalTo(-90));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void angleBetween100And55() {
        try {
            assertThat(new Bearing(100).angleBetween(new Bearing(55)), equalTo(45));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void angleBetween12And123() {
        try {
            assertThat(new Bearing(12).angleBetween(new Bearing(123)), equalTo(-111));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void angleBetween360And0() {
        try {
            assertThat(new Bearing(360).angleBetween(new Bearing(0)), equalTo(360));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void angleBetween200And300() {
        try {
            assertThat(new Bearing(200).angleBetween(new Bearing(300)), equalTo(-100));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void angleBetween50And75() {
        try {
            assertThat(new Bearing(50).angleBetween(new Bearing(75)), equalTo(-25));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void angleBetween5And365() {
        try {
            assertThat(new Bearing(5).angleBetween(new Bearing(365)), equalTo(-360));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
	
    //part 2
}
	
	

