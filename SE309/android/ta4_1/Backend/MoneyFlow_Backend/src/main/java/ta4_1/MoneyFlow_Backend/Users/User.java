package ta4_1.MoneyFlow_Backend.Users;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import ta4_1.MoneyFlow_Backend.Budget.Budget;
import ta4_1.MoneyFlow_Backend.Cards.Card;
import ta4_1.MoneyFlow_Backend.Expenses.Expenses;
import ta4_1.MoneyFlow_Backend.Family.Family;
import ta4_1.MoneyFlow_Backend.Goals.Goal;
import ta4_1.MoneyFlow_Backend.Portfolio.Portfolio;
import ta4_1.MoneyFlow_Backend.Recommendations.Recommendation;
import ta4_1.MoneyFlow_Backend.Statements.Statement;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Provides the Definition/Structure for the user table
 * Represents a User in the MoneyFlow application.
 * Each user has a unique ID, personal information, an income, associated expenses, and a list of cards.
 *
 * @author Onur Onal
 * @author Kemal Yavuz
 */
@Entity // Indicates that this class is an entity in the database.
@Table(name = "users")  // Specifies the table name in the database.
public class User {

    @Id // Indicates that this field is the primary key.
    @GeneratedValue(generator = "UUID") // Specifies that the ID should be generated automatically as a UUID.
    private UUID id; // Unique identifier for the user.

    @Column(name = "first_name", nullable = false)  // Maps this field to the "first_name" column in the database.
    private String firstName;   // The user's first name.

    @Column(name = "last_name", nullable = false)   // Maps this field to the "last_name" column in the database.
    private String lastName;    // The user's last name.

    @Column(name = "password", nullable = false)    // Maps this field to the "password" column in the database.
    private String password;    // The user's password.

    @Column(name = "email", unique = true, nullable = false)
    // Maps this field to the "email" column in the database.
    private String email;   // The user's email.

    @Column(name = "type")  // Maps this field to the "type" column in the database.
    private String type;    // The user's type (e.g., regular, premium).

    @Column(name = "monthly_income")    // Maps this field to the "monthly_income" column in the database.
    private double monthlyIncome;  // The user's monthly income.

    @Column(name = "annual_income")    // Maps this field to the "annual_income" column in the database.
    private double annualIncome;  // The user's annual income.

    @Column(name = "currency_exchange_setting")
    private String currencyExchangeSetting;

    @Column(name = "family_status")
    private String familyStatus; // "headOfHousehold", "familyMember", or "independent"

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    // Establishes a one-to-one relationship with the Expenses entity.
    private Expenses expenses;  // The user's associated expenses.

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    // Establishes a one-to-one relationship with the Expenses entity.
    private Budget budget;  // The user's associated expenses.

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    // Establishes a one-to-one relationship with the Portfolio entity.
    private Portfolio portfolio;  // The user's associated portfolio.

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    // Establishes a one-to-many relationship with the Card entity.
    private List<Card> cards = new ArrayList<>();   // The user's associated cards.

    @ManyToOne
    @JoinColumn(name = "family_id")
    @JsonBackReference
    private Family family;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Goal> goals = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    // Establishes a one-to-many relationship with the Recommendation entity.
    @MapKey(name = "date") // Designating the map key
    private Map<String, Recommendation> recommendations = new HashMap<>();    // The user's associated recommendations.

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    // Establishes a one-to-many relationship with the Statement entity.
    @MapKey(name = "date")
    private Map<String, Statement> statements = new HashMap<>();    // The user's associated statements.

    /**
     * Default constructor for JPA.
     */
    public User() {

    }

    /**
     * Constructs a new User with the specified personal information and income.
     *
     * @param firstName     - the first name of the user
     * @param lastName      - the last name of the user
     * @param password      - the password of the user
     * @param email         - the email of the user
     * @param monthlyIncome - the monthly income of the user
     * @param annualIncome  - the annual income of the user
     */
    public User(String firstName, String lastName, String password, String email, double monthlyIncome, double annualIncome) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.email = email;
        this.monthlyIncome = monthlyIncome;
        this.annualIncome = annualIncome;
    }

    // Getters and setters for each field

    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UUID getId() {
        return this.id;
    }

    public void setId(UUID uuid) {
        this.id = UUID.randomUUID();
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getMonthlyIncome() {
        return monthlyIncome;
    }

    public void setMonthlyIncome(double monthlyIncome) {
        this.monthlyIncome = monthlyIncome;
    }

    public double getAnnualIncome() {
        return annualIncome;
    }

    public void setAnnualIncome(double annualIncome) {
        this.annualIncome = annualIncome;
    }

    public List<Card> getCards() {
        return this.cards;
    }

    public void addCard(Card card) {
        if (cards != null) {
            this.cards.add(card);
            card.setUser(this);
        }
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
    }

    public Expenses getExpenses() {
        return expenses;
    }

    public void setExpenses(Expenses expenses) {
        this.expenses = expenses;
        if (expenses != null) {
            expenses.setUser(this); // Set the user in the Expenses entity
        }
    }

    public Portfolio getPortfolio() {
        return portfolio;
    }

    public void setPortfolio(Portfolio portfolio) {
        this.portfolio = portfolio;
        if (portfolio != null) {
            portfolio.setUser(this); // Set the user in the Portfolio entity
        }
    }

    public String getCurrencyExchangeSetting() {
        return currencyExchangeSetting;
    }

    public void setCurrencyExchangeSetting(String currencyExchangeSetting) {
        this.currencyExchangeSetting = currencyExchangeSetting;
    }

    public Map<String, Recommendation> getRecommendations() {
        return this.recommendations;
    }

    public Recommendation addRecommendation(String recommendationText) {
        LocalDate date = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        String formattedDate = date.format(formatter);

        Recommendation recommendation = new Recommendation();
        recommendation.setRecommendation(recommendationText);
        recommendation.setDate(formattedDate);
        recommendation.setUser(this);

        this.recommendations.put(formattedDate, recommendation);
        return recommendation;
    }

    public Map<String, Statement> getStatements() {
        return this.statements;
    }

    public Family getFamily() {
        return family;
    }

    public void setFamily(Family family) {
        this.family = family;
    }

    public String getFamilyStatus() {
        return familyStatus;
    }

    public void setFamilyStatus(String familyStatus) {
        this.familyStatus = familyStatus;
    }

    /**
     * Generates a financial report for the user, summarizing their income, expenses, and remaining budget.
     *
     * @return Budget = Income - Expenses
     */
    public double generateBudget() {
        double totalExpenses = expenses != null ? expenses.getTotalExpenses() : 0;
        return monthlyIncome - totalExpenses;
    }

    // Getter and setter for goals
    public List<Goal> getGoals() {
        return goals;
    }

    public void setGoals(List<Goal> goals) {
        this.goals = goals;
    }

    // Method to add a goal to the user's list of goals
    public void addGoal(Goal goal) {
        this.goals.add(goal);
        goal.setUser(this);
    }

    // Method to remove a goal from the user's list of goals
    public void removeGoal(Goal goal) {
        this.goals.remove(goal);
        goal.setUser(null);
    }

    public void setBudget(Budget savedBudget) {
        budget = savedBudget;
    }

    public Budget getBudget() {
        return budget;
    }

    /**
     * Represents the user as a string containing their ID, personal information, and income.
     *
     * @return a string representation of the user
     */
    @Override
    public String toString() {
        return id + " "
                + firstName + " "
                + lastName + " "
                + password + " "
                + email + " "
                + monthlyIncome + " "
                + annualIncome + " "
                + type + " "
                + currencyExchangeSetting + " "
                + familyStatus;
    }
}