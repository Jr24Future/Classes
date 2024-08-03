package ta4_1.MoneyFlow_Backend.Recommendations;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import ta4_1.MoneyFlow_Backend.Users.User;
import java.util.UUID;

/**
 * Entity representing a recommendation.
 *
 * @author Onur Onal
 * @author Kemal Yavuz
 */
@Entity
@Table(name = "recommendations")
public class Recommendation {
    @Id
    @GeneratedValue(generator = "UUID")
    private UUID id;    // Unique identifier for a recommendation

    @Column(name = "recommendation", nullable = false)
    private String recommendation;    // The recommendation paragraph

    @Column(name = "date", nullable = false)
    private String date;    // The recommendation date

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;  // The user associated with this recommendation

    /**
     * Default constructor
     */
    public Recommendation() {

    }

    /**
     * Constructor with parameters.
     *
     * @param recommendation The recommendation paragraph.
     * @param date           The recommendation date.
     */
    public Recommendation(String recommendation, String date) {
        this.recommendation = recommendation;
        this.date = date;
    }

    // Getters and setters

    public String getRecommendation() {
        return this.recommendation;
    }

    public void setRecommendation(String recommendation) {
        this.recommendation = recommendation;
    }

    public String getDate() {
        return this.date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public UUID getId() {
        return this.id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return id + " "
                + recommendation + " "
                + date;
    }
}
