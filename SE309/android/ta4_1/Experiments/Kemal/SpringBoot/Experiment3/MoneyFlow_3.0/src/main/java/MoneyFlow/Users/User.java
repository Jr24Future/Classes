package MoneyFlow.Users;

/**
 * Provides the Definition/Structure for the people row
 *
 * @author Kemal Yavuz
 */

public class User {

    private String firstName;

    private String lastName;

    private String address;

    private String telephone;

    // New attributes
    private String email;
    private int age;

    private int id;

    private double income;

    private double expenses;

    public User(){
        // Default constructor
    }

    public User(String firstName, String lastName, String address, String telephone, String email, int age, int id, double income, double expenses){
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.telephone = telephone;
        this.email = email;
        this.age = age;
        this.id = id;
        this.income = income;
        this.expenses = expenses;
    }

    // Getters and setters

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

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTelephone() {
        return this.telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getAge() {
        return this.age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    // Getters and setters for new attributes

    public double getIncome() {
        return income;
    }

    public void setIncome(double income) {
        this.income = income;
    }

    public double getExpenses() {
        return expenses;
    }

    public  void setExpenses(double expenses) {
        this.expenses = expenses;
    }

    @Override
    public String toString() {
        return firstName + " " 
               + lastName + " "
               + address + " "
               + telephone + " "
               + email + " "
               + age + " "
               + income + " "
               + expenses + " "
               + id;
    }
}
