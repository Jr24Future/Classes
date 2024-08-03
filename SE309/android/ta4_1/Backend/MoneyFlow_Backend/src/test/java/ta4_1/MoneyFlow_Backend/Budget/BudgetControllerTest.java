package ta4_1.MoneyFlow_Backend.Budget;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import ta4_1.MoneyFlow_Backend.Users.User;
import ta4_1.MoneyFlow_Backend.Users.UserRepository;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BudgetController.class)
public class BudgetControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BudgetRepository budgetRepository;

    @MockBean
    private BudgetService budgetService;

    @MockBean
    private UserRepository userRepository;

    private UUID userId;
    private User user;
    private Budget budget;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        user = new User();
        user.setId(userId);
        budget = new Budget(500, 300, 200, 100);
        budget.setUser(user);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(budgetRepository.save(any(Budget.class))).thenReturn(budget);
    }

    @Test
    void testCreateOrUpdateBudget() throws Exception {
        mockMvc.perform(post("/budget/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"personalLimit\":500, \"workLimit\":300, \"homeLimit\":200, \"otherLimit\":100}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.personalLimit").value(500));

        verify(budgetRepository).save(any(Budget.class));
    }

    @Test
    void testGetBudgetForUser() throws Exception {
        user.setBudget(budget);

        mockMvc.perform(get("/budget/{userId}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.personalLimit").value(500));

        verify(userRepository).findById(userId);
    }

    @Test
    void testUpdateBudget() throws Exception {
        user.setBudget(budget);

        mockMvc.perform(put("/budget/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"personalLimit\":600, \"workLimit\":400, \"homeLimit\":300, \"otherLimit\":200}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.personalLimit").value(600));

        verify(budgetRepository).save(any(Budget.class));
    }

    @Test
    void testDeleteBudget() throws Exception {
        user.setBudget(budget);

        mockMvc.perform(delete("/budget/{userId}", userId))
                .andExpect(status().isOk());

        verify(budgetRepository).delete(budget);
    }
}
