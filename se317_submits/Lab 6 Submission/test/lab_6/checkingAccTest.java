package lab_6;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class checkingAccTest {

    private checkingAcc account;
    private savingAcc saving;

    @BeforeEach
    public void setUp() {
        account = new checkingAcc();
        saving = new savingAcc();
    }

    @Test
    public void testValidDeposit() throws Exception {
        account.deposit(3000);
        assertEquals(3000, account.getBalance(), 0.001);
    }

    @Test
    public void testDepositOverLimit() {
        Exception ex = assertThrows(Exception.class, () -> {
            account.deposit(6000);
        });
        assertEquals("Exceeded daily deposit limit ($5000).", ex.getMessage());
    }

    @Test
    public void testValidWithdraw() throws Exception {
        account.deposit(1000);
        account.withdraw(200);
        assertEquals(800, account.getBalance(), 0.001);
    }

    @Test
    public void testWithdrawOverLimit() throws Exception {
        account.deposit(1000);
        Exception ex = assertThrows(Exception.class, () -> {
            account.withdraw(600);
        });
        assertEquals("Exceeded daily withdrawal limit ($500).", ex.getMessage());
    }

    @Test
    public void testOverdraftNotAllowed() throws Exception {
        account.deposit(100);
        Exception ex = assertThrows(Exception.class, () -> {
            account.withdraw(200);
        });
        assertEquals("Insufficient funds in checking account.", ex.getMessage());
    }

    @Test
    public void testTransferToSaving() throws Exception {
        account.deposit(500);
        account.transferToSaving(saving, 400);
        assertEquals(100, account.getBalance(), 0.001);
        assertEquals(400, saving.getBalance(), 0.001);
    }
}
