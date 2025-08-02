package lab_6;

import java.io.Serializable;

/**
 * Represents a utility bill with amount and due date.
 */
public class Bill implements Serializable {
    private static final long serialVersionUID = 1L;

    public double amount;
    public String dueDate;

    public Bill(double amount, String dueDate) {
        this.amount = amount;
        this.dueDate = dueDate;
    }

    @Override
    public String toString() {
        return "$" + amount + " due by " + dueDate;
    }
}
