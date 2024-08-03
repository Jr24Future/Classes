package ta4_1.MoneyFlow_Backend.Users;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Interface for the User repository, extending JpaRepository for CRUD operations.
 * Provides custom methods for querying the User entity.
 *
 * @author Onur Onal
 * @author Kemal Yavuz
 */
public interface UserRepository extends JpaRepository<User, UUID> {
    /**
     * Finds a user by their UUID.
     *
     * @param id The UUID of the user.
     * @return An Optional containing the user if found, otherwise empty.
     */
    Optional<User> findById(UUID id);

    /**
     * Finds a user by their email.
     *
     * @param email The email of the user.
     * @return An Optional containing the user if found, otherwise empty.
     */
    Optional<User> findByEmail(String email);

    /**
     * Finds all users of a specific type.
     *
     * @param type The type of users to find.
     * @return A list of users of the specified type.
     */
    List<User> findByType(String type);

    /**
     * Deletes a user by their UUID.
     * Transactional annotation ensures the operation is executed within a transaction.
     *
     * @param id The UUID of the user to delete.
     */
    @Transactional
    void deleteById(UUID id);

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.family")
    List<User> findAllWithFamily();
}