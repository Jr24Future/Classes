package ta4_1.MoneyFlow_Backend.Goals;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ta4_1.MoneyFlow_Backend.Users.UserRepository;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/goals")
public class GoalController {

    private final GoalRepository goalRepository;
    private final UserRepository userRepository;

    @Autowired
    public GoalController(GoalRepository goalRepository, UserRepository userRepository) {
        this.goalRepository = goalRepository;
        this.userRepository = userRepository;
    }

    @PostMapping("/{userId}")
    public ResponseEntity<Goal> createGoal(@PathVariable UUID userId, @RequestBody Goal goal) {
        return userRepository.findById(userId).map(user -> {
            goal.setUser(user);
            goal.setTotalAmount(goal.getAmount());
            goal.setCompleted(false);  // Initially, the goal is not completed
            return ResponseEntity.ok(goalRepository.save(goal));
        }).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<Goal>> getGoalsByUser(@PathVariable UUID userId) {
        List<Goal> goals = goalRepository.findByUserId(userId);
        return ResponseEntity.ok(goals);
    }

    @PutMapping("/user/{userId}/goal/{goalId}/progress/{amount}/{timeFrame}")
    public ResponseEntity<?> updateGoalProgress(@PathVariable UUID userId, @PathVariable UUID goalId, @PathVariable double amount, @PathVariable int timeFrame) {
        return userRepository.findById(userId).map(user -> {
            return goalRepository.findById(goalId).map(goal -> {
                if (!goal.getUser().getId().equals(user.getId())) {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Goal does not belong to this user.");
                }

                double newRemainingAmount = Math.max(0, goal.getAmount() - amount);
                int newRemainingTimeFrame = Math.max(0, goal.getTimeFrame() - timeFrame);

                goal.setAmount(newRemainingAmount);
                goal.setTimeFrame(newRemainingTimeFrame);

                goal.updateGoalString();

                if (newRemainingAmount == 0 || newRemainingTimeFrame == 0) {
                    goal.setCompleted(true);
                }

                goalRepository.save(goal);
                return ResponseEntity.ok(goal);
            }).orElse(ResponseEntity.notFound().build());
        }).orElse(ResponseEntity.notFound().build());
    }



    @DeleteMapping("/{goalId}")
    public ResponseEntity<Void> deleteGoal(@PathVariable UUID goalId) {
        return goalRepository.findById(goalId).map(goal -> {
            goalRepository.delete(goal);
            return ResponseEntity.ok().<Void>build();
        }).orElse(ResponseEntity.notFound().build());
    }

    @Transactional
    @DeleteMapping("/deleteAll/{userId}")
    public ResponseEntity<?> deleteAllGoalsByUser(@PathVariable UUID userId) {
        return userRepository.findById(userId)
                .map(user -> {
                    List<Goal> goals = goalRepository.findByUserId(userId);
                    if (!goals.isEmpty()) {
                        goalRepository.deleteAll(goals);
                        return ResponseEntity.ok().<Void>build();
                    } else {
                        return ResponseEntity.ok().build(); // No goals found to delete, but operation is successful
                    }
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

}