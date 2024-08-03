package ta4_1.MoneyFlow_Backend.Budget;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ta4_1.MoneyFlow_Backend.Expenses.Expenses;
import ta4_1.MoneyFlow_Backend.Users.User;
import ta4_1.MoneyFlow_Backend.Users.UserRepository;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class BudgetServiceTest {

    @Mock
    private UserRepository userRepository;

    private BudgetService budgetService;

    private UUID userId;
    private User user;
    private Budget budget;
    private Expenses expenses;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        budgetService = new BudgetService(userRepository);

        userId = UUID.randomUUID();
        user = new User();
        user.setId(userId);

        budget = new Budget(500, 300, 200, 100);
        expenses = new Expenses(400, 250, 100, 80);

        user.setBudget(budget);
        user.setExpenses(expenses);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
    }

    @Test
    void testCheckBudgetLimitsWithinLimits() {
        Map<String, Double> overages = budgetService.checkBudgetLimits(userId);

        assertEquals(100, overages.get("Personal"));
        assertEquals(50, overages.get("Work"));
        assertEquals(100, overages.get("Home"));
        assertEquals(20, overages.get("Other"));
    }

    @Test
    void testCheckBudgetLimitsOverLimit() {
        expenses.setPersonal(600); // Over personal limit

        Map<String, Double> overages = budgetService.checkBudgetLimits(userId);

        assertEquals(-100, overages.get("Personal"));
        assertEquals(50, overages.get("Work"));
        assertEquals(100, overages.get("Home"));
        assertEquals(20, overages.get("Other"));
    }

    @Test
    void testCheckBudgetLimitsUserNotFound() {
        when(userRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        Map<String, Double> overages = budgetService.checkBudgetLimits(UUID.randomUUID());

        assertTrue(overages.isEmpty());
    }
}
