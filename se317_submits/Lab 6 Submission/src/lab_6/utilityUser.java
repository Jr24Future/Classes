package lab_6;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Represents a utility customer with login credentials and billing info.
 */
public class utilityUser implements Serializable {
    private static final long serialVersionUID = 1L;

    public String username;
    public String password;
    public String accountNumber;
    public Queue<Bill> paymentHistory;
    public Bill nextBill;

    public utilityUser(String username, String password, String accountNumber) {
        this.username = username;
        this.password = password;
        this.accountNumber = accountNumber;
        this.paymentHistory = new LinkedList<>();
        this.nextBill = new Bill(75.50, "2025-05-01");
    }

    /**
     * Adds a paid bill to the user's history, keeping only the last 3.
     */
    public void addPayment(Bill bill) {
        if (paymentHistory.size() == 3) {
            paymentHistory.poll();
        }
        paymentHistory.offer(bill);
    }
}
