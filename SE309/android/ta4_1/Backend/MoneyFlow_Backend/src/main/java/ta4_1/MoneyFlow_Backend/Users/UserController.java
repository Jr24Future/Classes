package ta4_1.MoneyFlow_Backend.Users;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * Controller for managing user-related operations in the MoneyFlow application.
 *
 * @author Onur Onal
 * @author Kemal Yavuz
 */
@RestController
public class UserController {
    @Autowired
    UserRepository userRepository; // Injects the UserRepository for database operations.

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;  // Injects the password encoder for hashing passwords.

    /**
     * Retrieves a list of users filtered by their type.
     *
     * @param userType the type of users to retrieve
     * @return a list of users with the specified type
     */
    @GetMapping("/users/type/{userType}")
    public List<User> getUsersByType(@PathVariable String userType) {
        return userRepository.findByType(userType);
    }

    /**
     * Retrieves a user by their unique ID.
     *
     * @param id the ID of the user to retrieve
     * @return an Optional containing the user if found, or an empty Optional otherwise
     */
    @GetMapping("/users/id/{id}")
    public Optional<User> getUser(@PathVariable UUID id) {
        return userRepository.findById(id);
    }

    /**
     * Retrieves a list of all users.
     *
     * @return a list of all users
     */
    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userRepository.findAllWithFamily();
    }

    /**
     * Handles user signup by creating a new user with the provided information.
     *
     * @param u the user to be created
     * @return ID of the User
     */
    @PostMapping("/signup")
    public UUID signup(@RequestBody User u) {
        u.setType("regular");
        u.setCurrencyExchangeSetting("EUR,CAD");
        if (u.getFamilyStatus() == null) {
            u.setFamilyStatus("independent"); // Set default family status if not provided
        }
        u.setPassword(passwordEncoder.encode(u.getPassword())); // Use the autowired encoder for password encoding
        userRepository.save(u);
        return u.getId();
    }

    /**
     * Handles guest login by creating a new user with the type "guest".
     *
     * @return a success message
     */
    @PostMapping("/login/type/guest")
    public UUID loginGuest() {
        User u = new User();
        u.setType("guest");
        u.setFirstName("Guest");
        u.setLastName("User");
        u.setPassword("N/A");
        String uniqueEmail = "guest_" + UUID.randomUUID().toString() + "@example.com";
        u.setEmail(uniqueEmail);
        u.setMonthlyIncome(0.0);
        u.setAnnualIncome(0.0);
        userRepository.save(u);
        return u.getId();
    }

    /**
     * Handles user login by verifying the provided email and password.
     *
     * @param email    the email of the user attempting to log in
     * @param password the password of the user attempting to log in
     * @return ID of the User
     * @throws ResponseStatusException if the user is not found or the password is incorrect
     */
    @PostMapping("/login")
    public UUID login(@RequestParam String email, @RequestParam String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No such user"));

        if (passwordEncoder.matches(password, user.getPassword())) {
            return user.getId();
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Incorrect password, Access Denied");
        }
    }

    /**
     * Updates the information of an existing user.
     *
     * @param id the ID of the user to be updated
     * @param u  the updated user information
     * @return Updated user if the update is successful
     */
    @PutMapping("/users/{id}")
    @Transactional
    public ResponseEntity<User> updateUser(@PathVariable UUID id, @RequestBody User u) {
        return userRepository.findById(id)
                .map(user -> {
                    user.setFirstName(u.getFirstName());
                    user.setLastName(u.getLastName());
                    user.setEmail(u.getEmail());
                    user.setMonthlyIncome(u.getMonthlyIncome());
                    user.setAnnualIncome(u.getAnnualIncome());
                    user.setPassword(passwordEncoder.encode(u.getPassword()));
                    return ResponseEntity.ok(userRepository.save(user));
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Updates the income of a user.
     *
     * @param id the ID of the user for whom the income is updated.
     * @return a ResponseEntity of the user.
     */
    @PatchMapping("/users/{id}/income")
    public ResponseEntity<User> updateIncome(@PathVariable UUID id, @RequestBody Map<String, Double> incomeMap) {
        Double boxedIncome = incomeMap.get("income");
        if (boxedIncome == null) {  // Check if the value is null
            return ResponseEntity.badRequest().build();
        }
        double income = boxedIncome;
        return userRepository.findById(id)
                .map(user -> {
                    user.setMonthlyIncome(income);
                    return ResponseEntity.ok(userRepository.save(user));
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Retrieve the monthly income of the user.
     *
     * @param id the ID of the user from whom the monthly income is retrieved.
     * @return a ResponseEntity of the user's monthly income.
     */
    @GetMapping("/{id}/monthlyIncome")
    public ResponseEntity<Double> getMonthlyIncome(@PathVariable UUID id) {
        return userRepository.findById(id)
                .map(user -> {
                    double monthlyIncome = user.getMonthlyIncome();
                    return ResponseEntity.ok(monthlyIncome);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Retrieve the annual income of the user.
     *
     * @param id the ID of the user from whom the annual income is retrieved.
     * @return a ResponseEntity of the user's annual income.
     */
    @GetMapping("/{id}/annualIncome")
    public ResponseEntity<Double> getAnnualIncome(@PathVariable UUID id) {
        return userRepository.findById(id)
                .map(user -> {
                    double annualIncome = user.getAnnualIncome();
                    return ResponseEntity.ok(annualIncome);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/getCurrency")
    public ResponseEntity<String> getCurrencyExchangeSetting(@PathVariable UUID id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            String currencyExchangeSetting = user.getCurrencyExchangeSetting();
            return ResponseEntity.ok(currencyExchangeSetting);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{userId}/setCurrency/{Settings}")
    public ResponseEntity<String> setCurrency(@PathVariable UUID userId, @PathVariable String Settings) {
        return userRepository.findById(userId)
                .map(user -> {
                    user.setCurrencyExchangeSetting(Settings);
                    userRepository.save(user);
                    return ResponseEntity.ok("User's currency exchange settings has been updated");
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Upgrades the user to premium.
     *
     * @param userId the ID of the user to be upgraded to premium.
     * @return a ResponseEntity of a confirmation string.
     */
    @PostMapping("/upgradeType/{userId}")
    public ResponseEntity<String> upgradeType(@PathVariable UUID userId) {
        return userRepository.findById(userId)
                .map(user -> {
                    user.setType("premium");
                    userRepository.save(user);
                    return ResponseEntity.ok("User upgraded to premium");
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Retrieve the type of the user.
     *
     * @param userId the ID of the user whose type is retrieved.
     * @return a ResponseEntity of the user's type.
     */
    @GetMapping("/userType/{userId}")
    public ResponseEntity<String> getUserType(@PathVariable UUID userId) {
        return userRepository.findById(userId)
                .map(user -> ResponseEntity.ok(user.getType()))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


    /**
     * Generates a financial report for a user.
     *
     * @param id the ID of the user for whom the report is to be generated
     * @return a ResponseEntity containing the financial report or a not found status
     */
    @GetMapping("/{id}/financial-report")
    public ResponseEntity<Double> generateFinancialReport(@PathVariable UUID id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            double budget = user.generateBudget();
            return ResponseEntity.ok(budget);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Deletes a user by their unique ID.
     *
     * @param id the ID of the user to be deleted
     * @return a success message
     */
    @DeleteMapping("/users/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable UUID id) {
        return userRepository.findById(id)
                .map(user -> {
                    userRepository.deleteById(id);
                    return ResponseEntity.ok("User deleted successfully");
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/delete-guest-users")
    public ResponseEntity<String> deleteAllGuestUsers() {
        List<User> users = userRepository.findAll();
        for (User user : users) {
            if ("guest".equals(user.getType())) {
                userRepository.deleteById(user.getId());
            }
        }
        return ResponseEntity.ok("All guest users have been deleted.");
    }

    @DeleteMapping("/delete-regular-users")
    public ResponseEntity<String> deleteAllRegularUsers() {
        List<User> users = userRepository.findAll();
        for (User user : users) {
            if ("regular".equals(user.getType())) {
                userRepository.deleteById(user.getId());
            }
        }
        return ResponseEntity.ok("All regular users have been deleted.");
    }

    @DeleteMapping("/delete-familyMember-users")
    public ResponseEntity<String> deleteAllFamilyMemberUsers() {
        List<User> users = userRepository.findAll();
        for (User user : users) {
            if ("familyMember".equals(user.getFamilyStatus())) {
                userRepository.deleteById(user.getId());
            }
        }
        return ResponseEntity.ok("All family member status users have been deleted.");
    }

}