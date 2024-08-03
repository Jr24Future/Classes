package coms309;

/*
@author Onur Onal
 */
public class User {

    private String firstname;
    private String lastname;

    public User(String firstname, String lastname) {
        this.firstname = firstname;
        this.lastname = lastname;
    }

    public String getName() { return firstname + " " + lastname; }


}
