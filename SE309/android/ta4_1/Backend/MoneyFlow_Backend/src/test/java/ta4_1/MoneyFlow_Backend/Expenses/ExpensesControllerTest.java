package ta4_1.MoneyFlow_Backend.Expenses;

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

@WebMvcTest(controllers = ExpensesController.class)
public class ExpensesControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ExpensesRepository expensesRepository;

    @MockBean
    private UserRepository userRepository;

    private UUID userId;
    private Expenses mockExpenses;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        mockExpenses = new Expenses();
        mockExpenses.setId(UUID.randomUUID()); // Ensure that Expenses has an ID set for the test
        mockExpenses.setPersonal(300);
        mockExpenses.setWork(150);
        mockExpenses.setHome(200);
        mockExpenses.setOther(50);

        User user = new User();
        user.setId(userId);
        user.setExpenses(mockExpenses);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        // This ensures that when expenses are saved, it has an ID which can be used in the response
        when(expensesRepository.save(any(Expenses.class))).thenAnswer(invocation -> {
            Expenses expenses = invocation.getArgument(0);
            if (expenses.getId() == null) {
                expenses.setId(UUID.randomUUID());
            }
            return expenses;
        });
    }

    @Test
    void testCreateExpensesWhenUserExists() throws Exception {
        String expenseJson = "{\"personal\":300,\"work\":150,\"home\":200,\"other\":50}";

        mockMvc.perform(post("/expenses/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(expenseJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.expenseId").exists());

        verify(expensesRepository).save(any(Expenses.class));
    }

    @Test
    void testCreateExpensesWhenUserDoesNotExist() throws Exception {
        UUID randomId = UUID.randomUUID();
        String expenseJson = "{\"personal\":300,\"work\":150,\"home\":200,\"other\":50}";

        when(userRepository.findById(randomId)).thenReturn(Optional.empty());

        mockMvc.perform(post("/expenses/{userId}", randomId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(expenseJson))
                .andExpect(status().isNotFound());

        verify(expensesRepository, never()).save(any(Expenses.class));
    }

    @Test
    void testGetExpensesOfUser() throws Exception {
        mockMvc.perform(get("/expenses/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.personal").value(300))
                .andExpect(jsonPath("$.work").value(150))
                .andExpect(jsonPath("$.home").value(200))
                .andExpect(jsonPath("$.other").value(50));
    }

    @Test
    void testUpdateExpenses() throws Exception {
        String updatedExpensesJson = "{\"personal\":500,\"work\":100,\"home\":300,\"other\":150}";

        mockMvc.perform(put("/expenses/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedExpensesJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.personal").value(800)) // Assuming cumulative updates
                .andExpect(jsonPath("$.work").value(250))
                .andExpect(jsonPath("$.home").value(500))
                .andExpect(jsonPath("$.other").value(200));

    }

    @Test
    void testDeleteExpenses() throws Exception {
        mockMvc.perform(delete("/expenses/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(expensesRepository).delete(any(Expenses.class));
        verify(userRepository).save(any(User.class));
    }
}