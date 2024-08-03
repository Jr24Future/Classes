package MoneyFlow.Users; //Updated Package Name.

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.HashMap;

/**
 * Controller used to showcase Create and Read from a LIST
 * Developed by group TA4_1.
 * @author Kemal Yavuz
 */

@RestController
public class UserController {

    HashMap<Integer, User> userList = new  HashMap<>();
    int userCount = 0;


    @GetMapping("/people")
    public  HashMap<Integer, User> getAllPersons() {
        return userList;
    }

    @PostMapping("/people")
    public  String createPerson(@RequestBody User user) {

        for (User existingPerson : userList.values()) {
            if (existingPerson.getId() == user.getId()) {
                return "Person with the same ID already exists";
            }
        }

        userCount++;
        user.setId(userCount);
        System.out.println(user);
        userList.put(user.getId(), user);
        return "New user "+ user.getFirstName() + " Saved";
    }

    @GetMapping("/people/{id}")
    public User getPerson(@PathVariable int id) {
        return userList.get(id);
    }

    @PutMapping("/people/{id}")
    public User updatePerson(@PathVariable int id, @RequestBody User p) {
        if (!userList.containsKey(id)) {
            // Handle not found case
            return null;
        }

        User updatedPerson = new User(p.getFirstName(), p.getLastName(), p.getAddress(), p.getTelephone(), p.getEmail(), p.getAge(), id);
        updatedPerson.setFirstName(p.getFirstName());
        updatedPerson.setLastName(p.getLastName());
        updatedPerson.setAddress(p.getAddress());
        updatedPerson.setTelephone(p.getTelephone());
        updatedPerson.setEmail(p.getEmail());
        updatedPerson.setAge(p.getAge());
        updatedPerson.setId(id);
        return updatedPerson;
    }
    
    @DeleteMapping("/people/{id}")
    public HashMap<Integer, User> deletePerson(@PathVariable int id) {
        if (!userList.containsKey(id)) {
            // Handle not found case
            return null;
        }

        userList.remove(id);
        return userList;
    }
}