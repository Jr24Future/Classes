package lab_6;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class savingAccTest {

    private savingAcc saving;
    private checkingAcc checking;

    @BeforeEach
    public void setUp() {
        saving = new savingAcc();
        checking = new checkingAcc();
    }

    @Test
    public void testValidDeposit() throws Exception {
        saving.deposit(2500);
        assertEquals(2500, saving.getBalance(), 0.001);
    }

    @Test
    public void testDepositLimitExceeded() {
        Exception ex = assertThrows(Exception.class, () -> {
            saving.deposit(6000);
        });
        assertEquals("Exceeded daily deposit limit ($5000).", ex.getMessage());
    }

    @Test
    public void testTransferToCheckingValid() throws Exception {
        saving.deposit(200);
        saving.transferToChecking(checking, 100);
        assertEquals(100, saving.getBalance(), 0.001);
        assertEquals(100, checking.getBalance(), 0.001);
    }

    @Test
    public void testTransferLimitExceeded() throws Exception {
        saving.deposit(300);
        Exception ex = assertThrows(Exception.class, () -> {
            saving.transferToChecking(checking, 200);
        });
        assertEquals("Exceeded daily transfer limit to checking ($100).", ex.getMessage());
    }

    @Test
    public void testOverdraftOnTransfer() {
        Exception ex = assertThrows(Exception.class, () -> {
            saving.transferToChecking(checking, 100);
        });
        assertEquals("Insufficient funds in saving account.", ex.getMessage());
    }
}
