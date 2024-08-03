package ta4_1.MoneyFlow_Backend.Expenses;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ta4_1.MoneyFlow_Backend.Users.User;
import ta4_1.MoneyFlow_Backend.Users.UserRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * Controller for managing expenses.
 *
 * @author Kemal Yavuz
 * @author Onur Onal
 */
@RestController
@RequestMapping("/expenses")
public class ExpensesController {
    @Autowired
    private ExpensesRepository expensesRepository;  // Autowire the ExpensesRepository to interact with the database.

    @Autowired
    private UserRepository userRepository;  // Autowire the UserRepository to interact with the User table.

    /**
     * Create or update expenses for a user.
     *
     * @param userId   The ID of the user.
     * @param expenses The expenses object to be created or updated.
     * @return ResponseEntity with the created or updated expenses.
     */
    @PostMapping("/{userId}")
    public ResponseEntity<?> createExpenses(@PathVariable UUID userId, @RequestBody Expenses expenses) {
        Optional<User> userOptional = userRepository.findById(userId);  // Find the user by ID.
        if (!userOptional.isPresent()) {
            return ResponseEntity.notFound().build();   // If the user is not found, return a 404 response.
        }
        User user = userOptional.get();
        expenses.setUser(user); // Set the user for the expenses.
        Expenses savedExpenses = expensesRepository.save(expenses); // Save the expenses to the database.
        user.setExpenses(savedExpenses); // Update the expenses in the User entity
        userRepository.save(user); // Save the updated User entity
        return ResponseEntity.ok(Map.of("expenseId", expenses.getId()));
    }

    /**
     * Get an expense of a user.
     *
     * @param id The ID of the user.
     * @return ResponseEntity with the found expenses.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Expenses> getExpensesOfUser(@PathVariable UUID id) {
        return userRepository.findById(id)
                .map(user -> ResponseEntity.ok(user.getExpenses()))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Get all expenses.
     *
     * @return List of all expenses.
     */
    @GetMapping
    public List<Expenses> getAllExpenses() {
        return expensesRepository.findAll();
    }

    /**
     * Update expenses by user ID.
     *
     * @param id              The ID of the user whose expenses to update.
     * @param updatedExpenses The updated expenses object.
     * @return ResponseEntity with the updated expenses.
     */
    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<Expenses> addExpensesToUser(@PathVariable UUID id, @RequestBody Expenses updatedExpenses) {
        return userRepository.findById(id)
                .map(user -> {
                    Expenses currentExpenses = user.getExpenses();
                    currentExpenses.setPersonal(currentExpenses.getPersonal() + updatedExpenses.getPersonal());
                    currentExpenses.setWork(currentExpenses.getWork() + updatedExpenses.getWork());
                    currentExpenses.setHome(currentExpenses.getHome() + updatedExpenses.getHome());
                    currentExpenses.setOther(currentExpenses.getOther() + updatedExpenses.getOther());

                    return ResponseEntity.ok(currentExpenses); // Return the updated expenses
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Delete expenses by user ID.
     *
     * @param userId The ID of the user whose expenses are to be deleted.
     * @return ResponseEntity indicating the result of the deletion.
     */
    @DeleteMapping("/{userId}")
    public ResponseEntity<?> deleteExpensesByUserId(@PathVariable UUID userId) {
        return userRepository.findById(userId)
                .map(user -> {
                    Expenses expenses = user.getExpenses();
                    if (expenses != null) {
                        expensesRepository.delete(expenses);
                        user.setExpenses(null);
                        userRepository.save(user);
                        return ResponseEntity.ok().<Void>build();
                    }
                    else {
                        return ResponseEntity.notFound().build();
                    }
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


}