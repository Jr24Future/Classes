package ta4_1.MoneyFlow_Backend.Expenses;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import ta4_1.MoneyFlow_Backend.Users.User;

import java.util.UUID;

/**
 * Represents the expenses associated with a user.
 *
 * @author Kemal Yavuz
 * @author Onur Onal
 */
@Entity
@Table(name = "expenses")
public class Expenses {
    @Id
    @GeneratedValue(generator = "UUID")
    private UUID id;    // Unique identifier for the expenses.

    @Column(name = "personal")
    private double personal;    // Personal expenses.

    @Column(name = "work")
    private double work;    // Work-related expenses.

    @Column(name = "home")
    private double home;    // Home expenses.

    @Column(name = "other")
    private double other;   // Other expenses.

    @JsonIgnore
    @OneToOne
    //@MapsId
    @JoinColumn(name = "user_id")
    private User user;  // The user associated with these expenses.

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Expenses() {
    }

    public Expenses(double personal, double work, double home, double other) {
        this.personal = personal;
        this.work = work;
        this.home = home;
        this.other = other;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public double getPersonal() {
        return personal;
    }

    public void setPersonal(double personal) {
        if (personal >= 0) {
            this.personal = personal;
        }
    }

    public double getWork() {
        return work;
    }

    public void setWork(double work) {
        if (work >= 0) {
            this.work = work;
        }
    }

    public double getHome() {
        return home;
    }

    public void setHome(double home) {
        if (home >= 0) {
            this.home = home;
        }
    }

    public double getOther() {
        return other;
    }

    public void setOther(double other) {
        if (other >= 0) {
            this.other = other;
        }
    }

    public double getTotalExpenses() {
        return personal + work + home + other;
    }

    @Override
    public String toString() {
        return "Expenses{" + "id=" + id + ", personal=" + personal + ", work=" + work + ", home=" + home + ", other=" + other + '}';
    }
}