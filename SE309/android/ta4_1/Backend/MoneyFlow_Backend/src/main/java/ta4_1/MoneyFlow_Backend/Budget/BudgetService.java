package ta4_1.MoneyFlow_Backend.Budget;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ta4_1.MoneyFlow_Backend.Expenses.Expenses;
import ta4_1.MoneyFlow_Backend.Users.User;
import ta4_1.MoneyFlow_Backend.Users.UserRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class BudgetService {

    private final UserRepository userRepository;

    @Autowired
    public BudgetService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Map<String, Double> checkBudgetLimits(UUID userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            Budget budget = user.getBudget();
            Expenses expenses = user.getExpenses();

            Map<String, Double> overages = new HashMap<>();
            overages.put("Personal", budget.getPersonalLimit() - expenses.getPersonal());
            overages.put("Work", budget.getWorkLimit() - expenses.getWork());
            overages.put("Home", budget.getHomeLimit() - expenses.getHome());
            overages.put("Other", budget.getOtherLimit() - expenses.getOther());

            return overages;
        }
        return new HashMap<>();  // Return empty map if user or budget is not found
    }
}
