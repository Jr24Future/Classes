package ta4_1.MoneyFlow_Backend.Statements;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import ta4_1.MoneyFlow_Backend.Users.User;
import java.util.UUID;

/**
 * Entity representing a bank statement.
 *
 * @author Onur Onal
 * @author Kemal Yavuz
 */
@Entity
@Table(name = "statements")
public class Statement {
    @Id
    @GeneratedValue(generator = "UUID")
    private UUID id;

    @Lob
    @Column(name = "statement", nullable = false, columnDefinition = "MEDIUMBLOB")
    private byte[] bankStatement; // byte array to store xlsx file data

    @Column(name = "date", nullable = false)
    private String date;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;  // The user associated with this statement

    /**
     * Default constructor
     */
    public Statement() {

    }

    /**
     * Constructor with parameters.
     *
     * @param bankStatement The bank statement.
     * @param date          The statement's upload date.
     */
    public Statement(byte[] bankStatement, String date) {
        this.bankStatement = bankStatement;
        this.date = date;
    }

    // Getters and setters

    public byte[] getBankStatement() { return this.bankStatement; }

    public void setBankStatement(byte[] statement) {
        this.bankStatement = statement;
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
        String fileInfo = (bankStatement != null) ? "File size: " + bankStatement.length + " bytes" : "No file attached";
        return "Statement{id=" + id + ", " + fileInfo + "}";
    }


}
