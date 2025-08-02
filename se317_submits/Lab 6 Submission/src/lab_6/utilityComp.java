package lab_6;

import java.util.HashMap;
import java.io.Serializable;
import java.util.Random;

/**
 * Represents the Utility Company subsystem.
 * Handles account creation, login, bill payments, and payment history.
 */
public class utilityComp implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * Stores all utility users using their username as the key.
     */
    private HashMap<String, utilityUser> users = new HashMap<>();

    /**
     * Creates a new utility account with a unique 6-digit account number.
     *
     * @param username The username provided by the user.
     * @param password The password provided by the user.
     * @return The generated 6-digit account number.
     */
    public String createAccount(String username, String password) {
        String accountNum = String.format("%06d", new Random().nextInt(999999));
        utilityUser user = new utilityUser(username, password, accountNum);
        users.put(username, user);
        return accountNum;
    }

    /**
     * Logs in to a utility account using a username or account number and a password.
     *
     * @param usernameOrAccNum The username or account number.
     * @param password         The account password.
     * @return true if login is successful, false otherwise.
     */
    public boolean login(String usernameOrAccNum, String password) {
        for (utilityUser user : users.values()) {
            if ((user.username.equals(usernameOrAccNum) || user.accountNumber.equals(usernameOrAccNum))
                    && user.password.equals(password)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Processes a bill payment for a utility user.
     * Adds the current bill to payment history and generates a new one.
     *
     * @param username The username of the utility account.
     * @param password The password of the utility account.
     * @param amount   The amount paid.
     * @return true if payment is successful, false otherwise.
     */
    public boolean receivePayment(String username, String password, double amount) {
        utilityUser user = users.get(username);
        if (user != null && user.password.equals(password)) {
            Bill paid = user.nextBill;
            user.addPayment(paid); // Add current bill to history
            user.nextBill = new Bill(65.25, "2025-06-01"); // Generate new upcoming bill
            return true;
        }
        return false;
    }

    /**
     * Displays the last three paid bills for the user.
     *
     * @param username The username of the utility account.
     * @param password The password of the utility account.
     */
    public void showPaymentHistory(String username, String password) {
        utilityUser user = users.get(username);
        if (user != null && user.password.equals(password)) {
            System.out.println("Last 3 paid bills:");
            for (Bill b : user.paymentHistory) {
                System.out.println(" - " + b);
            }
        }
    }

    /**
     * Displays the upcoming bill (amount and due date) for the user.
     *
     * @param username The username of the utility account.
     * @param password The password of the utility account.
     */
    public void showNextBill(String username, String password) {
        utilityUser user = users.get(username);
        if (user != null && user.password.equals(password)) {
            System.out.println("Next bill: " + user.nextBill);
        }
    }

    /**
     * Returns a utilityUser object by username (used in testing).
     *
     * @param username The username to look up.
     * @return The utilityUser object, or null if not found.
     */
    public utilityUser getUser(String username) {
        return users.get(username);
    }
}
