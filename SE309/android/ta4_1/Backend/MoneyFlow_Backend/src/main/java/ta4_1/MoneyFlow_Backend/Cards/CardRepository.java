package ta4_1.MoneyFlow_Backend.Cards;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for Card entities.
 *
 * @author Onur Onal
 */
public interface CardRepository extends JpaRepository<Card, UUID> {
    /**
     * Finds a card by its ID.
     *
     * @param id The UUID of the card.
     * @return An Optional containing the found card, if any.
     */
    Optional<Card> findById(UUID id);

    /**
     * Deletes a card by its ID.
     *
     * @param id The UUID of the card to be deleted.
     */
    @Transactional
    void deleteById(UUID id);

}