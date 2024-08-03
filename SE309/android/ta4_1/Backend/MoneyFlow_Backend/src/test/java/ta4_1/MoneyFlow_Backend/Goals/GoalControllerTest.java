package ta4_1.MoneyFlow_Backend.Goals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import ta4_1.MoneyFlow_Backend.Users.User;
import ta4_1.MoneyFlow_Backend.Users.UserRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(GoalController.class)
public class GoalControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GoalRepository goalRepository;

    @MockBean
    private UserRepository userRepository;

    private UUID userId;
    private User user;
    private Goal goal;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        user = new User();
        user.setId(userId);
        goal = new Goal();
        goal.setId(UUID.randomUUID());
        goal.setAmount(1000);
        goal.setTimeFrame(12);
        goal.setTotalAmount(1000);
        goal.setUser(user);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(goalRepository.save(any(Goal.class))).thenReturn(goal);
    }

    @Test
    void testCreateGoal() throws Exception {
        mockMvc.perform(post("/goals/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"goalString\":\"Save $1000 in 12 months\",\"amount\":1000,\"timeFrame\":12}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.amount").value(1000));

        verify(goalRepository).save(any(Goal.class));
    }

    @Test
    void testGetGoalsByUser() throws Exception {
        List<Goal> goals = Arrays.asList(goal);
        when(goalRepository.findByUserId(userId)).thenReturn(goals);

        mockMvc.perform(get("/goals/{userId}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].amount").value(1000));

        verify(goalRepository).findByUserId(userId);
    }

    @Test
    void testUpdateGoalProgress() throws Exception {
        when(goalRepository.findById(goal.getId())).thenReturn(Optional.of(goal));

        mockMvc.perform(put("/goals/user/{userId}/goal/{goalId}/progress/{amount}/{timeFrame}", userId, goal.getId(), 500, 6))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.amount").value(500));

        verify(goalRepository).save(goal);
    }

    @Test
    void testDeleteGoal() throws Exception {
        when(goalRepository.findById(goal.getId())).thenReturn(Optional.of(goal));

        mockMvc.perform(delete("/goals/{goalId}", goal.getId()))
                .andExpect(status().isOk());

        verify(goalRepository).delete(goal);
    }
}
