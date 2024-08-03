package ta4_1.MoneyFlow_Backend.Cards;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ta4_1.MoneyFlow_Backend.Family.Family;
import ta4_1.MoneyFlow_Backend.Users.User;
import ta4_1.MoneyFlow_Backend.Users.UserRepository;

import java.util.*;

/**
 * Controller for Cards
 *
 * @author Onur Onal
 * @author Kemal Yavuz
 */
@RestController
public class CardController {

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * Retrieves all cards for all users.
     *
     * @return A list of lists containing cards for each user.
     */
    @GetMapping("/cards")
    public List<List<Card>> getAllCards() {
        List<User> users = userRepository.findAll();
        List<List<Card>> allCards = new ArrayList<>();

        for (User u : users) {
            allCards.add(u.getCards());
        }

        return allCards;
    }

    /**
     * Retrieves all cards for a specific user.
     *
     * @param id The UUID of the user.
     * @return A list of cards for the specified user.
     */
    @GetMapping("/cards/userId/{id}")
    public ResponseEntity<List<Card>> getAllCardsOfUser(@PathVariable UUID id) {
        return userRepository.findById(id)
                .map(user -> {

                return ResponseEntity.ok(user.getCards());
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Retrieves all cards for a specific user's family.
     *
     * @param id The UUID of the user.
     * @return A list of cards for the specified user's family.
     */
    @GetMapping("/cards/userId/{id}/family")
    public ResponseEntity<List<Card>> getAllCardsOfUserFamily(@PathVariable UUID id) {
        return userRepository.findById(id)
                .map(user -> {
                    List<Card> familyCards = new ArrayList<>();
                    Family family = user.getFamily();

                    for (User u : family.getUsers()) {
                        for (Card c : u.getCards()) {
                            familyCards.add(c);
                        }
                    }
                    return ResponseEntity.ok(familyCards);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Retrieves a specific card of a user.
     *
     * @param userId The UUID of the user.
     * @param cardId The UUID of the card.
     * @return The card with the specified ID.
     */
    @GetMapping("/cards/id/{userId}/{cardId}")
    public ResponseEntity<Card> getCard(@PathVariable UUID userId, @PathVariable UUID cardId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (!userOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        User user = userOptional.get();
        List<Card> userCards = user.getCards();

        for (Card c : userCards) {
            if (c.getId().equals(cardId)) {
                return ResponseEntity.ok(c);
            }
        }

        return ResponseEntity.notFound().build();
    }


    /**
     * Retrieves the default card of a specific user.
     *
     * @param id The UUID of the user.
     * @return The card with the specified ID.
     */
    @GetMapping("/cards/userId/{id}/default")
    public ResponseEntity<Card> getDefaultCard(@PathVariable UUID id) {
        Optional<User> userOptional = userRepository.findById(id);

        if (!userOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        User user = userOptional.get();
        List<Card> userCards = user.getCards();

        for (Card c : userCards) {
            if (c.getIsDefault()) {
                return ResponseEntity.ok(c);
            }
        }

        return ResponseEntity.notFound().build();
    }

    /**
     * Creates a new card for a user.
     *
     * @param id   The UUID of the user.
     * @param card The card to be created.
     * @return The UUID of the newly created card.
     */
    @PostMapping("/cards/{id}")
    public ResponseEntity<?> createCard(@PathVariable UUID id, @RequestBody Card card) {
        return userRepository.findById(id)
                .map(user -> {
                    card.setUser(user);
                    cardRepository.save(card);
                    user.addCard(card);
                    userRepository.save(user);
                    // Wrap the UUID in a JSON object
                    return ResponseEntity.ok(Map.of("id", card.getId()));

                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Sets a new default card for a user.
     *
     * @param userId The UUID of the user.
     * @param cardId The UUID of the card.
     * @return The new default card.
     */
    @PutMapping("/cards/id/{userId}/{cardId}/setDefault")
    public ResponseEntity<Card> setDefaultCard(@PathVariable UUID userId, @PathVariable UUID cardId) {
        return userRepository.findById(userId)
                .map(user -> {
                    List<Card> userCards = user.getCards();
                    Card card = null;

                    for (Card c : userCards) {
                        if (c.getId().equals(cardId)) {
                            c.setIsDefault(true);
                            card = c;
                            cardRepository.save(c);
                        } else if (c.getIsDefault() == true) {
                            c.setIsDefault(false);
                            cardRepository.save(c);
                        }
                    }

                    return ResponseEntity.ok(card);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }



    /**
     * Updates a card of a user.
     *
     * @param userId The UUID of the user.
     * @param cardId The UUID of the card.
     * @return The updated card.
     */
    @PutMapping("/cards/id/{userId}/{cardId}")
    @Transactional
    public ResponseEntity<?> updateCard(@PathVariable UUID userId, @PathVariable UUID cardId, @RequestBody Card card) {
        return userRepository.findById(userId)
                .map(user -> {
                    Card updatedCard = null;
                    for (Card c : user.getCards()) {
                        if (c.getId().equals(cardId)) {
                            updatedCard = c;
                            break;
                        }
                    }
                    if (updatedCard == null) {
                        return ResponseEntity.notFound().build();
                    }
                    if (!card.getCardNumber().equals(null)) {
                        updatedCard.setCardNumber(card.getCardNumber());
                    }
                    if (!card.getExpirationDate().equals(null)) {
                        updatedCard.setExpirationDate(card.getExpirationDate());
                    }
                    if (!card.getName().equals(null)) {
                        updatedCard.setName(card.getName());
                    }
                    if (!card.getCvv().equals(null)) {
                        updatedCard.setCvv(card.getCvv());
                    }

                    cardRepository.save(updatedCard);
                    return ResponseEntity.ok(updatedCard);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Deletes a card by its unique ID.
     *
     * @param userId The UUID of the user.
     * @param cardId The UUID of the card.
     * @return success message
     */
    @DeleteMapping("/cards/id/{userId}/{cardId}")
    @Transactional
    public ResponseEntity<?> deleteCard(@PathVariable UUID userId, @PathVariable UUID cardId) {
        return userRepository.findById(userId)
                .map(user -> {
                    Card deletedCard = null;
                    boolean isCardDeleted = false;
                    for (Card c : user.getCards()) {
                        if (c.getId().equals(cardId)) {
                            deletedCard = c;
                            isCardDeleted = true;
                            break;
                        }
                    }
                    if (isCardDeleted) {
                        user.getCards().remove(deletedCard);
                        if ((deletedCard.getIsDefault()) && (!user.getCards().isEmpty())) {
                            user.getCards().iterator().next().setIsDefault(true);
                        }
                        return ResponseEntity.ok("Card deleted successfully");
                    } else {
                        return ResponseEntity.notFound().build();
                    }
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}