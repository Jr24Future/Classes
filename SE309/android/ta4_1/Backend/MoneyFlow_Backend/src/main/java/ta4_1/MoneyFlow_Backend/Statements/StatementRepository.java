package ta4_1.MoneyFlow_Backend.Statements;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for Statement entities.
 *
 * @author Onur Onal
 */

public interface StatementRepository extends JpaRepository<Statement, UUID> {
    /**
     * Finds a statement by its ID.
     *
     * @param id The UUID of the statement.
     * @return An Optional containing the found statement, if any.
     */
    Optional<Statement> findById(UUID id);

    /**
     * Deletes a statement by its ID.
     *
     * @param id The UUID of the statement to be deleted.
     */
    @Transactional
    void deleteById(UUID id);

}
