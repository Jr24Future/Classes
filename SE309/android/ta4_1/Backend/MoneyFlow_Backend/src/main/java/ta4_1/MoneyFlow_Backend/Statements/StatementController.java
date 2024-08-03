package ta4_1.MoneyFlow_Backend.Statements;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ta4_1.MoneyFlow_Backend.Expenses.ExpensesRepository;
import ta4_1.MoneyFlow_Backend.Users.User;
import ta4_1.MoneyFlow_Backend.Users.UserRepository;

import java.io.IOException;
import java.util.*;

/**
 * Controller for Statements
 *
 * @author Onur Onal
 * @author Kemal Yavuz
 */

@RestController
@RequestMapping("/statement")
public class StatementController {

    @Autowired
    private StatementRepository statementRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StatementService statementService;

    @Autowired
    private ExpensesRepository expensesRepository;

    /**
     * Adds a new statement for a user and updates income and expenses accordingly.
     *
     * @param id   The UUID of the user.
     * @param file The file with which the statement is created.
     * @return The UUID of the newly created statement.
     */
    @PostMapping("/userId/{id}")
    public ResponseEntity<?> addStatement(@PathVariable UUID id, @RequestParam("file") MultipartFile file) {
        return userRepository.findById(id)
                .map(user -> {
                    Statement s;
                    statementService = new StatementService(expensesRepository);
                    try {
                        s = statementService.createBankStatement(user, file);
                    }
                    catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    userRepository.save(user);
                    s.setUser(user);
                    statementRepository.save(s);

                    return ResponseEntity.ok(Map.of("id", s.getId()));
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Retrieves all statement of all users.
     *
     * @return A list of maps containing statements for each user.
     */
    @GetMapping
    public List<Collection<Statement>> getAllStatements() {
        List<User> users = userRepository.findAll();
        List<Collection<Statement>> allStatements = new ArrayList<>();

        for (User u : users) {
            allStatements.add(u.getStatements().values());
        }

        return allStatements;
    }

    /**
     * Retrieves a user statement by its date.
     *
     * @param id The UUID of the user.
     * @return The statement as a BLOB.
     */
    @GetMapping("/userId/{id}/byDate")
    public ResponseEntity<Statement> getStatementByDate(@PathVariable UUID id, @RequestParam String date) {
        return userRepository.findById(id)
                .map(user -> {
                    Statement s = user.getStatements().get(date);
                    return ResponseEntity.ok(s);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Updates a statement of a user.
     *
     * @param id The UUID of the user.
     * @param file The new file that replaced the old file.
     * @param date The date of the file.
     * @return The updated statement.
     */
    @PutMapping("/userId/{id}/byDate")
    @Transactional
    public ResponseEntity<?> updateStatement(@PathVariable UUID id, @RequestParam("file") MultipartFile file, @RequestParam("date") String date) {
        return userRepository.findById(id)
                .map(user -> {
                    Statement s = user.getStatements().get(date);

                    if (s == null) {
                        return ResponseEntity.notFound().build();
                    }

                    try {
                        byte[] contents = file.getBytes();
                        s.setBankStatement(contents);
                        statementRepository.save(s);
                    } catch (IOException e) {
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
                    }
                    return ResponseEntity.ok(s);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Deletes a statement by its unique ID.
     *
     * @param userId           The UUID of the user.
     * @param statementId      The UUID of the statement.
     * @return success message
     */
    @DeleteMapping("/id/{userId}/{statementId}")
    @Transactional
    public ResponseEntity<?> deleteStatement(@PathVariable UUID userId, @PathVariable UUID statementId) {
        return userRepository.findById(userId)
                .map(user -> {
                    Statement deletedStatement = null;
                    for (Statement s : user.getStatements().values()) {
                        if (s.getId().equals(statementId)) {
                            deletedStatement = s;
                            break;
                        }
                    }
                    if (deletedStatement != null) {
                        user.getStatements().remove(deletedStatement.getDate());
                        statementRepository.delete(deletedStatement);
                        userRepository.save(user);
                        return ResponseEntity.ok("Statement deleted successfully.");
                    }
                    else {
                        return ResponseEntity.notFound().build();
                    }
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

}
