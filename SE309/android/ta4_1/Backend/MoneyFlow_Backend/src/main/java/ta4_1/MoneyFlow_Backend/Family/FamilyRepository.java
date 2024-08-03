package ta4_1.MoneyFlow_Backend.Family;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

/**
 * Interface for the Family repository, extending JpaRepository for CRUD operations.
 * Provides custom methods for querying the Family entity.
 */
public interface FamilyRepository extends JpaRepository<Family, UUID> {

    /**
     * Finds a family by its UUID.
     *
     * @param id The UUID of the family.
     * @return An Optional containing the family if found, otherwise empty.
     */
    Optional<Family> findById(UUID id);

    /**
     * Deletes a family by its UUID.
     * Transactional annotation ensures the operation is executed within a transaction.
     *
     * @param id The UUID of the family to delete.
     */
    @Transactional
    void deleteById(UUID id);
}