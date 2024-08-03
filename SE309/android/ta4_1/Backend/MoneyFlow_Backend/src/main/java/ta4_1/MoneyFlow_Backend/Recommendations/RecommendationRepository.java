package ta4_1.MoneyFlow_Backend.Recommendations;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for Recommendation entities.
 *
 * @author Onur Onal
 */

public interface RecommendationRepository extends JpaRepository<Recommendation, UUID> {
    /**
     * Finds a recommendation by its ID.
     *
     * @param id The UUID of the recommendation.
     * @return An Optional containing the found recommendation, if any.
     */
    Optional<Recommendation> findById(UUID id);

    /**
     * Deletes a recommendation by its ID.
     *
     * @param id The UUID of the recommendation to be deleted.
     */
    @Transactional
    void deleteById(UUID id);

}
