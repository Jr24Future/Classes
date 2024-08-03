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

    private int id; // Assuming a long data type for id


    public User(){
        // Default constructor
    }

    public User(String firstName, String lastName, String address, String telephone, String email, int age, int id){
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.telephone = telephone;
        this.email = email;
        this.age = age;
        this.id = id;
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

    // Getters and setters for new attributes

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

    @Override
    public String toString() {
        return firstName + " " 
               + lastName + " "
               + address + " "
               + telephone + " "
               + email + " "
               + age + " "
               + id;
    }
}
