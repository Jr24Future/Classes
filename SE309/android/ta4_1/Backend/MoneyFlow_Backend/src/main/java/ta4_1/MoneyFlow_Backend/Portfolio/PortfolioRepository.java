package ta4_1.MoneyFlow_Backend.Portfolio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for the Portfolio entity. This interface is used for
 * data access and manipulation related to portfolios.
 *
 * @author Onur Onal
 * @author Kemal Yavuz
 */
public interface PortfolioRepository extends JpaRepository<Portfolio, UUID> {
    /**
     * Finds a Portfolio entity by its unique identifier.
     *
     * @param id The unique identifier (UUID) of the portfolio record.
     * @return An Optional containing the Portfolio entity if found, otherwise empty.
     */
    Optional<Portfolio> findById(UUID id);

    /**
     * Deletes a Portfolio entity by its unique identifier.
     *
     * @param id The unique identifier (UUID) of the portfolio record to be deleted.
     */
    @Transactional
    void deleteById(UUID id);
}