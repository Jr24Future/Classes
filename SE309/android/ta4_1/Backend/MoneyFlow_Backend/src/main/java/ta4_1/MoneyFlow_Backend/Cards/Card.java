package ta4_1.MoneyFlow_Backend.Cards;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import ta4_1.MoneyFlow_Backend.Users.User;

import java.util.UUID;

/**
 * Entity representing a credit/debit card.
 *
 * @author Onur Onal
 * @author Kemal Yavuz
 */
@Entity
@Table(name = "cards")
public class Card {
    @Id
    @GeneratedValue(generator = "UUID")
    private UUID id;    // Unique identifier for the card

    @Column(name = "name", nullable = false)
    private String name;    // Name of the card

    @Column(name = "card_number", unique = true, nullable = false)
    private String cardNumber;  // Card number, must be unique

    @Column(name = "expiration_date", nullable = false)
    private String expirationDate;  // Expiration date of the card

    @Column(name = "cvv", nullable = false)
    private String cvv; // CVV number of the card

    @Column(name = "is_default", nullable = false)
    private boolean isDefault; // If the card is the user's default card.

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;  // The user associated with this card

    /**
     * Default constructor
     */
    public Card() {
    }

    /**
     * Constructor with parameters.
     *
     * @param name           Name associated with the card.
     * @param cardNumber     Card number.
     * @param expirationDate Expiration date of the card.
     * @param cvv            CVV of the card.
     */
    public Card(String name, String cardNumber, String expirationDate, String cvv) {
        this.name = name;
        this.cardNumber = cardNumber;
        this.expirationDate = expirationDate;
        this.cvv = cvv;
    }

    // Getters and setters

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCardNumber() {
        return this.cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getExpirationDate() {
        return this.expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }

    public UUID getId() {
        return this.id;
    }

    public void setId(UUID uuid) {
        this.id = uuid;
    }

    public String getCvv() { return this.cvv; }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean getIsDefault() {
        return this.isDefault;
    }

    public void setIsDefault(boolean isDefault) {
        this.isDefault = isDefault;
    }

    @Override
    public String toString() {
        return id + " "
                + name + " "
                + cardNumber + " "
                + expirationDate + " "
                + cvv + " "
                + isDefault;
    }
}