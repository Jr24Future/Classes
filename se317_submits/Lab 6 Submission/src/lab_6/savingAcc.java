package lab_6;

import java.io.Serializable;

/**
 * Represents a saving account that supports deposits and transfers to checking.
 */
public class savingAcc implements Serializable {
    private static final long serialVersionUID = 1L;

    private double balance = 0;
    private double dailyDeposit = 0;
    private double dailyTransferToChecking = 0;

    /**
     * Deposits money into the saving account, enforcing daily limits.
     */
    public void deposit(double amount) throws Exception {
        if (dailyDeposit + amount > 5000) {
            throw new Exception("Exceeded daily deposit limit ($5000).");
        }
        balance += amount;
        dailyDeposit += amount;
    }

    /**
     * Transfers money to a checking account, enforcing $100/day cap.
     */
    public void transferToChecking(checkingAcc checking, double amount) throws Exception {
        if (dailyTransferToChecking + amount > 100) {
            throw new Exception("Exceeded daily transfer limit to checking ($100).");
        }
        if (balance < amount) {
            throw new Exception("Insufficient funds in saving account.");
        }
        checking.deposit(amount);
        balance -= amount;
        dailyTransferToChecking += amount;
    }

    /**
     * Accepts a transfer from checking (no limit).
     */
    public void receiveTransfer(double amount) {
        balance += amount;
    }

    /**
     * Returns the current account balance.
     */
    public double getBalance() {
        return balance;
    }
}
