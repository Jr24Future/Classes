package ta4_1.MoneyFlow_Backend.Goals;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import ta4_1.MoneyFlow_Backend.Users.User;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Entity
@Table(name = "goals")
public class Goal {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "goal_string")
    private String goalString;

    @Column(name = "amount")
    private double amount;  // The financial target of the goal

    @Column(name = "total_amount")
    private double totalAmount;  // The original financial target of the goal

    @Column(name = "time_frame")
    private int timeFrame;  // The timeframe in months

    @Column(name = "is_completed")
    private boolean isCompleted;  // Status of the goal

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Goal() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getGoalString() {
        return goalString;
    }

    public void setGoalString(String goalString) {
        this.goalString = goalString;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public int getTimeFrame() {
        return timeFrame;
    }

    public void setTimeFrame(int timeFrame) {
        this.timeFrame = timeFrame;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    // Standard getters and setters
    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public void updateGoalString() {
        if (this.goalString != null) {
            this.goalString = replaceNumericalValues(this.goalString, this.amount, this.timeFrame);
        } else {
            // Handle the null case, e.g., log an error, throw an exception, or initialize it
            this.goalString = "Initial goal description not set."; // Example of initialization
        }
    }

    private String replaceNumericalValues(String originalString, double amount, int timeFrame) {
        Pattern pattern = Pattern.compile("(\\d+\\.?\\d*)"); // Matches integers and decimals
        Matcher matcher = pattern.matcher(originalString);

        StringBuffer newString = new StringBuffer();

        // Replace the first occurrence with the amount
        if (matcher.find()) {
            matcher.appendReplacement(newString, String.format("%.2f", amount));
        }

        // Replace the second occurrence with the timeframe
        if (matcher.find()) {
            matcher.appendReplacement(newString, Integer.toString(timeFrame));
        }

        matcher.appendTail(newString); // Append the rest of the original string

        return newString.toString();
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
