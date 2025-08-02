package lab_6;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class utilityCompTest {

    private utilityComp utility;

    @BeforeEach
    public void setUp() {
        utility = new utilityComp();
    }

    @Test
    public void testAccountCreation() {
        String acc = utility.createAccount("john", "pass123");
        assertNotNull(acc);
        assertEquals(6, acc.length());
    }

    @Test
    public void testLoginSuccess() {
        utility.createAccount("sara", "xyz");
        assertTrue(utility.login("sara", "xyz"));
    }

    @Test
    public void testLoginFailureWrongPassword() {
        utility.createAccount("sara", "xyz");
        assertFalse(utility.login("sara", "wrong"));
    }

    @Test
    public void testBillPayment() {
        utility.createAccount("alex", "pass");
        assertTrue(utility.receivePayment("alex", "pass", 75.50));
    }

    @Test
    public void testInvalidBillPayment() {
        assertFalse(utility.receivePayment("nonexistent", "nope", 80));
    }
    @Test
    public void testNextBillDisplay() {
        String user = "jane";
        String pass = "123";
        utility.createAccount(user, pass);
        assertDoesNotThrow(() -> {
            utility.showNextBill(user, pass);
        });
    }

    @Test
    public void testPaymentHistoryTracking() {
        String user = "bob";
        String pass = "abc";
        utility.createAccount(user, pass);
        // Simulate 4 payments to overflow history
        for (int i = 0; i < 4; i++) {
            utility.receivePayment(user, pass, 75.50);
        }
        // History should only keep last 3
        utilityUser u = utility.getUser(user);
        assertEquals(3, u.paymentHistory.size());
    }

}
