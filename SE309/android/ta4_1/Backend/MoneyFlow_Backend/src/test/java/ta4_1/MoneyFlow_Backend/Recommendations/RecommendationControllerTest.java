package ta4_1.MoneyFlow_Backend.Recommendations;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


import ta4_1.MoneyFlow_Backend.Users.User;
import ta4_1.MoneyFlow_Backend.Users.UserRepository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;


@WebMvcTest(controllers = RecommendationController.class)
public class RecommendationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RecommendationRepository recommendationRepository;

    @MockBean
    private UserRepository userRepository;

    private User user;
    private UUID userId;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        user = new User();

        Recommendation recommendation = new Recommendation("Test Recommendation", "04/22/2024");
        user.getRecommendations().put(recommendation.getDate(), recommendation);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
    }

    @Test
    void testGetRecommendationByDate() throws Exception {
        String existingDate = "04/22/2024";
        mockMvc.perform(get("/recommendations/userId/{id}/byDate", userId)
                        .param("date", existingDate)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Test Recommendation"));
    }

    @Test
    void testCreateRecommendation() throws Exception {
        UUID userId = UUID.randomUUID();
        String recommendationText = "Check out new budgeting tools!";

        // Assuming you have setters or methods to correctly handle the setting of recommendations in your user entity
        User user = new User();
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        when(recommendationRepository.save(any(Recommendation.class))).then(invocation -> {
            Recommendation r = invocation.getArgument(0);
            if (r.getId() == null) r.setId(UUID.randomUUID());
            return r;
        });

        mockMvc.perform(post("/recommendations/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"text\":\"" + recommendationText + "\"}")) // Proper JSON body
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.id").isNotEmpty());

        verify(userRepository).save(user); // Ensure the user is saved if the recommendation is added
        verify(recommendationRepository).save(any(Recommendation.class)); // Ensure the recommendation is saved
    }

    @Test
    void testUpdateRecommendation() throws Exception {
        UUID userId = UUID.randomUUID();
        String date = "04/22/2024";
        String updatedText = "Updated recommendation text";

        User user = new User();
        Recommendation existingRecommendation = new Recommendation("Original text", date);
        user.getRecommendations().put(date, existingRecommendation);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(recommendationRepository.save(any(Recommendation.class))).thenAnswer(invocation -> invocation.getArgument(0));

        mockMvc.perform(MockMvcRequestBuilders.put("/recommendations/userId/{id}/byDate", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"date\":\"" + date + "\", \"recommendation\":\"" + updatedText + "\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.recommendation").value(updatedText));

        verify(recommendationRepository).save(any(Recommendation.class)); // Ensure the recommendation is updated and saved.
        verify(userRepository, never()).save(any(User.class)); // Verify that no unnecessary user saves are made.
    }

    @Test
    void testDeleteRecommendation() throws Exception {
        UUID userId = UUID.randomUUID();
        UUID recommendationId = UUID.randomUUID();
        String recommendationDate = "04/22/2024";

        // Set up a user with a recommendation
        User user = new User();
        Recommendation recommendation = new Recommendation("Delete this recommendation", recommendationDate);
        recommendation.setId(recommendationId); // Setting the ID to match the deletion ID
        user.getRecommendations().put(recommendationDate, recommendation);

        // Stubbing the repository method calls
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        doAnswer(invocation -> {
            user.getRecommendations().remove(recommendation.getDate());
            return null;
        }).when(recommendationRepository).delete(recommendation);

        // Perform the delete request
        mockMvc.perform(MockMvcRequestBuilders.delete("/recommendations/id/{userId}/{recommendationId}", userId, recommendationId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Recommendation deleted successfully."));

        // Verify that the recommendation is removed
        assertFalse(user.getRecommendations().containsKey(recommendationDate));
        verify(recommendationRepository).delete(recommendation);
        verify(userRepository).save(user); // Verify that the user is saved after the change
    }


}
