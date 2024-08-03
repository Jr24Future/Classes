package coms309.users;

import org.springframework.web.bind.annotation.*;


import java.util.HashMap;

/**
 * Controller used to showcase Create and Read from a LIST
 *
 * @author Onur Onal
 */

@RestController
public class UserController {

    HashMap<String, User> regularUserList = new HashMap<>();
    HashMap<String, User> premiumUserList = new HashMap<>();
    HashMap<String, User> guestUserList = new HashMap<>();

    // Gets all users of any type.
    @GetMapping("/users/type/{userType}")
    public HashMap<String, User> getUsersByType(@PathVariable String userType) {
        HashMap<String, User> selectedUsers = new HashMap<>();

        if ("regular".equals(userType)) {
            selectedUsers.putAll(regularUserList);
        }
        else if ("premium".equals(userType)) {
            selectedUsers.putAll(premiumUserList);
        }
        else if ("guest".equals(userType)) {
            selectedUsers.putAll(guestUserList);
        }
        else {
            throw new IllegalArgumentException("Invalid user type: " + userType);
        }
        return selectedUsers;
    }

    // Gets a user.
    @GetMapping("/users/type/{userType}/{username}")
    public User getUser(@PathVariable String userType, @PathVariable String username) {
        HashMap<String, User> selectedUsers = new HashMap<>();

        if ("regular".equals(userType)) {
            selectedUsers.putAll(regularUserList);
        }
        else if ("premium".equals(userType)) {
            selectedUsers.putAll(premiumUserList);
        }
        else if ("guest".equals(userType)) {
            selectedUsers.putAll(guestUserList);
        }
        else {
            throw new IllegalArgumentException("Invalid user type: " + userType);
        }

        return selectedUsers.get(username);
    }

    // Gets all users.
    @GetMapping("/users")
    public HashMap<String, User>[] getAllUsers() {
        HashMap<String, User>[] users = new HashMap[3];
        users[0] = regularUserList;
        users[1] = premiumUserList;
        users[2] = guestUserList;

        return users;
    }

    // Sign up operation.
    @PostMapping("/signup/{userType}")
    public String signup(@RequestBody User user, @PathVariable String userType) {
        user.setId();
        System.out.println(user);

        HashMap<String, User> selectedUsers;

        if ("regular".equals(userType)) {
            selectedUsers = regularUserList;
        }
        else if ("premium".equals(userType)) {
            selectedUsers = premiumUserList;
        }
        else if ("guest".equals(userType)) {
            selectedUsers = guestUserList;
        }
        else {
            throw new IllegalArgumentException("Invalid user type: " + userType);
        }

        selectedUsers.put(user.getUsername(), user);
        return "Hello " + user.getFirstName() + ", welcome to MoneyFlow Insight!";
    }

    // Login operation.
    @GetMapping("/login/{userType}")
    public String login(@RequestParam String username, @RequestParam String password, @PathVariable String userType) {
        HashMap<String, User> selectedUsers = new HashMap<>();

        if ("regular".equals(userType)) {
            selectedUsers.putAll(regularUserList);
        }
        else if ("premium".equals(userType)) {
            selectedUsers.putAll(premiumUserList);
        }
        else if ("guest".equals(userType)) {
            selectedUsers.putAll(guestUserList);
        }
        else {
            throw new IllegalArgumentException("Invalid user type: " + userType);
        }

        User user = selectedUsers.get(username);
        if ((user != null) && (user.getPassword().equals(password))) {
            return "Welcome back, " + user.getFirstName() + "!";
        }
        else {
            if (user == null) {
                return "No such user";
            }
            return "Incorrect password, Access Denied";
        }
    }

    // Updates a user.
    @PutMapping("/users/type/{userType}/{username}")
    public String updateUser(@PathVariable String userType, @PathVariable String username, @RequestBody User u) {
        HashMap<String, User> selectedUsers;

        if ("regular".equals(userType)) {
            selectedUsers = regularUserList;
        }
        else if ("premium".equals(userType)) {
            selectedUsers = premiumUserList;
        }
        else if ("guest".equals(userType)) {
            selectedUsers = guestUserList;
        }
        else {
            throw new IllegalArgumentException("Invalid user type: " + userType);
        }

        if (selectedUsers.containsKey(username)) {
            u.id = selectedUsers.get(username).getId();
            selectedUsers.put(username, u);
            return "User credentials successfully updated.";
        }
        else {
            return "User not found.";
        }
    }

    // Deletes a user.
    @DeleteMapping("/users/type/{userType}/{username}")
    public String deleteUser(@PathVariable String userType, @PathVariable String username) {
        HashMap<String, User> selectedUsers;

        if ("regular".equals(userType)) {
            selectedUsers = regularUserList;
        } else if ("premium".equals(userType)) {
            selectedUsers = premiumUserList;
        } else if ("guest".equals(userType)) {
            selectedUsers = guestUserList;
        } else {
            throw new IllegalArgumentException("Invalid user type: " + userType);
        }

        if (selectedUsers.containsKey(username)) {
            String response = selectedUsers.get(username).getFirstName();
            selectedUsers.remove(username);
            return "User " +  response + " has been successfully removed.";
        } else {
            return "User not found.";
        }
    }
}

