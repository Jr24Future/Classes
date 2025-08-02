package lab_6;

import java.io.Serializable;

/**
 * Represents a checking account with deposit, withdrawal, transfer, and bill payment functionality.
 * Enforces daily transaction limits and prevents overdrafts.
 */
public class checkingAcc implements Serializable {
    private static final long serialVersionUID = 1L;

    private double balance = 0;
    private double dailyDeposit = 0;
    private double dailyWithdraw = 0;

    /**
     * Deposits money into the checking account.
     * Enforces a daily deposit limit of $5000.
     *
     * @param amount The amount to deposit
     * @throws Exception if the daily deposit limit is exceeded
     */
    public void deposit(double amount) throws Exception {
        if (dailyDeposit + amount > 5000) {
            throw new Exception("Exceeded daily deposit limit ($5000).");
        }
        balance += amount;
        dailyDeposit += amount;
    }

    /**
     * Withdraws money from the checking account.
     * Enforces a daily withdrawal limit of $500 and prevents overdraft.
     *
     * @param amount The amount to withdraw
     * @throws Exception if limit is exceeded or balance is insufficient
     */
    public void withdraw(double amount) throws Exception {
        if (dailyWithdraw + amount > 500) {
            throw new Exception("Exceeded daily withdrawal limit ($500).");
        }
        if (amount > balance) {
            throw new Exception("Insufficient funds in checking account.");
        }
        balance -= amount;
        dailyWithdraw += amount;
    }

    /**
     * Transfers funds from checking to a saving account.
     * No daily transfer limit applied, but prevents overdraft.
     *
     * @param saving The target saving account
     * @param amount The amount to transfer
     * @throws Exception if balance is insufficient
     */
    public void transferToSaving(savingAcc saving, double amount) throws Exception {
        if (amount > balance) {
            throw new Exception("Insufficient funds for transfer.");
        }
        saving.receiveTransfer(amount);
        balance -= amount;
    }

    /**
     * Pays a utility bill through the utility company system.
     * Only allowed if sufficient balance exists and utility login is successful.
     *
     * @param utility  The utility company system
     * @param username The utility account username
     * @param password The utility account password
     * @param amount   The amount to pay
     * @throws Exception if payment fails or funds are insufficient
     */
    public void payBill(utilityComp utility, String username, String password, double amount) throws Exception {
        if (balance < amount) {
            throw new Exception("Insufficient funds to pay bill.");
        }
        if (!utility.receivePayment(username, password, amount)) {
            throw new Exception("Failed to process bill payment.");
        }
        balance -= amount;
    }

    /**
     * Returns the current balance of the checking account.
     *
     * @return The current balance
     */
    public double getBalance() {
        return balance;
    }
}
