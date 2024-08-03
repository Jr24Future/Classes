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

    @GetMapping("/users")
    public  HashMap<Integer, User> getAllPersons() {
        return userList;
    }

    @PostMapping("/users")
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

    @GetMapping("/users/{id}")
    public User getPerson(@PathVariable int id) {
        return userList.get(id);
    }

    @PutMapping("/users/{id}")
    public User updatePerson(@PathVariable int id, @RequestBody User user) {
        if (!userList.containsKey(id)) {
            // Handle not found case
            return null;
        }

        User updatedPerson = new User(user.getFirstName(), user.getLastName(), user.getAddress(), user.getTelephone(), user.getEmail(), user.getAge(), id, user.getIncome(), user.getExpenses());
        updatedPerson.setFirstName(user.getFirstName());
        updatedPerson.setLastName(user.getLastName());
        updatedPerson.setAddress(user.getAddress());
        updatedPerson.setTelephone(user.getTelephone());
        updatedPerson.setEmail(user.getEmail());
        updatedPerson.setAge(user.getAge());
        updatedPerson.setId(id);
        updatedPerson.setIncome(user.getIncome());
        userList.replace(id, updatedPerson);
        return updatedPerson;
    }
    
    @DeleteMapping("/users/{id}")
    public HashMap<Integer, User> deletePerson(@PathVariable int id) {
        if (!userList.containsKey(id)) {
            // Handle not found case
            return null;
        }

        userList.remove(id);
        return userList;
    }

    @PostMapping("/users/{id}/budget")
    public String reportFinancialStatus(@PathVariable int id, @RequestBody Expenses e) {

        Expenses financialReport = new Expenses(id, e.getRent(), e.getFood(), e.getEducation());
        financialReport.setEducation(e.getEducation());
        financialReport.setFood(e.getFood());
        financialReport.setRent(e.getRent());

        userList.get(id).setExpenses(financialReport.getRent()+ financialReport.getFood()+ financialReport.getEducation());
        double budget = userList.get(id).getIncome() - (userList.get(id).getExpenses());

        return "Income: " + userList.get(id).getIncome() + "\nExpenses: " + userList.get(id).getExpenses() + "\n" + userList.get(id).getFirstName() + ", You Have " + budget + "$ to spend to entertainment, investing or other.";
    }
}

