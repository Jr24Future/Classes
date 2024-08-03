package ta4_1.MoneyFlow_Backend.Recommendations;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ta4_1.MoneyFlow_Backend.Users.User;
import ta4_1.MoneyFlow_Backend.Users.UserRepository;

import java.util.*;

/**
 * Controller for Recommendations
 *
 * @author Onur Onal
 * @author Kemal Yavuz
 */

@RestController
public class RecommendationController {

    @Autowired
    private RecommendationRepository recommendationRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * Retrieves all recommendations of all users.
     *
     * @return A list of maps containing recommendations for each user.
     */
    @GetMapping("/recommendations")
    public List<Collection<Recommendation>> getAllRecommendations() {
        List<User> users = userRepository.findAll();
        List<Collection<Recommendation>> allRecommendations = new ArrayList<>();

        for (User u : users) {
            allRecommendations.add(u.getRecommendations().values());
        }

        return allRecommendations;
    }

    /**
     * Retrieves all recommendations for a specific user.
     *
     * @param id The UUID of the user.
     * @return A map of recommendations for the specified user.
     */
    @GetMapping("/recommendations/userId/{id}")
    public ResponseEntity<Collection<Recommendation>> getAllRecommendationsOfUser(@PathVariable UUID id) {
        return userRepository.findById(id)
                .map(user -> ResponseEntity.ok(user.getRecommendations().values()))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Retrieves the dates of all recommendations for a specific user.
     *
     * @param id The UUID of the user.
     * @return A list of date strings for the specified user.
     */
    @GetMapping("/recommendations/userId/{id}/dates")
    public ResponseEntity<List<String>> getUserRecommendationDates(@PathVariable UUID id) {
        return userRepository.findById(id)
                .map(user -> {
                    Set<String> dates = user.getRecommendations().keySet();
                    List<String> datesList = new ArrayList<>(dates);
                    return ResponseEntity.ok(datesList);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Retrieves a user recommendation by its date.
     *
     * @param id The UUID of the user.
     * @return The recommendation object.
     */
    @GetMapping("/recommendations/userId/{id}/byDate")
    public ResponseEntity<String> getRecommendationByDate(@PathVariable UUID id, @RequestParam String date) {
        return userRepository.findById(id)
                .map(user -> {
                    String s = user.getRecommendations().get(date).getRecommendation();
                    return ResponseEntity.ok(s);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Creates a new recommendation for a user.
     *
     * @param id                 The UUID of the user.
     * @param recommendationText The text with which the recommendation is created .
     * @return The UUID of the newly created recommendation.
     */
    @PostMapping("/recommendations/{id}")
    public ResponseEntity<?> createRecommendation(@PathVariable UUID id, @RequestBody String recommendationText) {
        return userRepository.findById(id)
                .map(user -> {
                    Recommendation r = user.addRecommendation(recommendationText);
                    userRepository.save(user);
                    r.setUser(user);
                    recommendationRepository.save(r);
                    return ResponseEntity.ok(Map.of("id", r.getId()));

                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Updates a recommendation of a user.
     *
     * @param id The UUID of the user.
     * @return The updated recommendation.
     */
    @PutMapping("/recommendations/userId/{id}/byDate")
    @Transactional
    public ResponseEntity<?> updateRecommendation(@PathVariable UUID id, @RequestBody Recommendation recommendation) {
        return userRepository.findById(id)
                .map(user -> {
                    Recommendation r = user.getRecommendations().get(recommendation.getDate());

                    if (r == null) {
                        return ResponseEntity.notFound().build();
                    }

                    r.setRecommendation(recommendation.getRecommendation());
                    recommendationRepository.save(r);
                    return ResponseEntity.ok(r);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Deletes a recommendation by its unique ID.
     *
     * @param userId           The UUID of the user.
     * @param recommendationId The UUID of the recommendation.
     * @return success message
     */
    @DeleteMapping("/recommendations/id/{userId}/{recommendationId}")
    @Transactional
    public ResponseEntity<?> deleteRecommendation(@PathVariable UUID userId, @PathVariable UUID recommendationId) {
        return userRepository.findById(userId)
                .map(user -> {
                    Recommendation deletedRecommendation = null;
                    for (Recommendation r : user.getRecommendations().values()) {
                        if (r.getId().equals(recommendationId)) {
                            deletedRecommendation = r;
                            break;
                        }
                    }
                    if (deletedRecommendation != null) {
                        user.getRecommendations().remove(deletedRecommendation.getDate());
                        recommendationRepository.delete(deletedRecommendation);
                        userRepository.save(user);
                        return ResponseEntity.ok("Recommendation deleted successfully.");
                    }
                    else {
                        return ResponseEntity.notFound().build();
                    }
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
