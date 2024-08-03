package ta4_1.MoneyFlow_Backend.Expenses;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for the Expenses entity. This interface is used for
 * data access and manipulation related to expenses.
 *
 * @author Kemal Yavuz
 * @author Onur Onal
 */
public interface ExpensesRepository extends JpaRepository<Expenses, UUID> {
    /**
     * Finds an Expenses entity by its unique identifier.
     *
     * @param id The unique identifier (UUID) of the expenses record.
     * @return An Optional containing the Expenses entity if found, otherwise empty.
     */
    Optional<Expenses> findById(UUID id);

    /**
     * Deletes an Expenses entity by its unique identifier.
     *
     * @param id The unique identifier (UUID) of the expenses record to be deleted.
     */
    @Transactional
    void deleteById(UUID id);
}