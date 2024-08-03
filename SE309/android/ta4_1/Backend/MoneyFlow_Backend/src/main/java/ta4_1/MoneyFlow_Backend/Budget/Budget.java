package ta4_1.MoneyFlow_Backend.Budget;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import ta4_1.MoneyFlow_Backend.Users.User;

import java.util.UUID;

@Entity
@Table(name = "budgets")
public class Budget {
    @Id
    @GeneratedValue(generator = "UUID")
    private UUID id;

    @Column(name = "personal_limit")
    private double personalLimit;

    @Column(name = "work_limit")
    private double workLimit;

    @Column(name = "home_limit")
    private double homeLimit;

    @Column(name = "other_limit")
    private double otherLimit;

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Budget() {
    }

    public Budget(double personalLimit, double workLimit, double homeLimit, double otherLimit) {
        this.personalLimit = personalLimit;
        this.workLimit = workLimit;
        this.homeLimit = homeLimit;
        this.otherLimit = otherLimit;
    }

    // Getters and setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public double getPersonalLimit() {
        return personalLimit;
    }

    public void setPersonalLimit(double personalLimit) {
        this.personalLimit = personalLimit;
    }

    public double getWorkLimit() {
        return workLimit;
    }

    public void setWorkLimit(double workLimit) {
        this.workLimit = workLimit;
    }

    public double getHomeLimit() {
        return homeLimit;
    }

    public void setHomeLimit(double homeLimit) {
        this.homeLimit = homeLimit;
    }

    public double getOtherLimit() {
        return otherLimit;
    }

    public void setOtherLimit(double otherLimit) {
        this.otherLimit = otherLimit;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
