package ta4_1.MoneyFlow_Backend.Budget;

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

@RestController
@RequestMapping("/budget")
public class BudgetController {
    @Autowired
    private BudgetRepository budgetRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BudgetService budgetService;  // Autowire the BudgetService

    @PostMapping("/{userId}")
    public ResponseEntity<?> createOrUpdateBudget(@PathVariable UUID userId, @RequestBody Budget budget) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (!userOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        User user = userOptional.get();

        Budget existingBudget = user.getBudget();
        if (existingBudget != null) {
            // If the budget already exists, update its fields
            existingBudget.setPersonalLimit(budget.getPersonalLimit());
            existingBudget.setWorkLimit(budget.getWorkLimit());
            existingBudget.setHomeLimit(budget.getHomeLimit());
            existingBudget.setOtherLimit(budget.getOtherLimit());
            budget = existingBudget; // Point budget to the existingBudget to be updated
        } else {
            // If no budget exists, associate the new budget with the user
            budget.setUser(user);
            user.setBudget(budget); // Set the budget to the user for the first time
        }

        Budget savedBudget = budgetRepository.save(budget); // Save the newly created or updated budget
        return ResponseEntity.ok(savedBudget);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> getBudgetForUser(@PathVariable UUID userId) {
        return userRepository.findById(userId)
                .map(user -> ResponseEntity.ok(user.getBudget()))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{userId}")
    public ResponseEntity<?> updateBudget(@PathVariable UUID userId, @RequestBody Budget budgetDetails) {
        return userRepository.findById(userId)
                .map(user -> {
                    Budget budget = user.getBudget();
                    if (budget != null) {
                        budget.setPersonalLimit(budgetDetails.getPersonalLimit());
                        budget.setWorkLimit(budgetDetails.getWorkLimit());
                        budget.setHomeLimit(budgetDetails.getHomeLimit());
                        budget.setOtherLimit(budgetDetails.getOtherLimit());
                        budgetRepository.save(budget);
                        return ResponseEntity.ok(budget);
                    } else {
                        return ResponseEntity.notFound().build();
                    }
                }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Delete a budget
    @DeleteMapping("/{userId}")
    public ResponseEntity<?> deleteBudget(@PathVariable UUID userId) {
        return userRepository.findById(userId)
                .map(user -> {
                    Budget budget = user.getBudget();
                    if (budget != null) {
                        budgetRepository.delete(budget);
                        user.setBudget(null);
                        userRepository.save(user);
                        return ResponseEntity.ok().build();
                    } else {
                        return ResponseEntity.notFound().build();
                    }
                }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // List all budgets
    @GetMapping
    public List<Budget> getAllBudgets() {
        return budgetRepository.findAll();
    }

    @GetMapping("/check/{userId}")
    public ResponseEntity<?> checkBudgetOverages(@PathVariable UUID userId) {
        Map<String, Double> overages = budgetService.checkBudgetLimits(userId);
        if (overages.isEmpty()) {
            System.out.println("No overages found for user: " + userId);
            return ResponseEntity.ok().body("{}");  // Ensuring a consistent response format
        }
        return ResponseEntity.ok(overages);
    }


}
