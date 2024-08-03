package coms309.users;

import java.util.UUID;

/**
 * Provides the Definition/Structure for the user row
 *
 * @author Onur Onal
 */

public class User {

    protected UUID id;

    private String firstName;

    private String lastName;

    private String username;

    private String password;

    private String email;

    public User(){
        
    }

    public User(String firstName, String lastName, String username, String password, String email){
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.email = email;

        this.id = UUID.randomUUID();
    }

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

    public String getUsername() { return this.username; }

    public void setUsername(String username) {
        this.username = username;
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

    public void setId() {
        this.id = UUID.randomUUID();
    }

    @Override
    public String toString() {
        return id + " "
               + firstName + " "
               + lastName + " "
               + username + " "
               + password + " "
               + email;
    }
}
