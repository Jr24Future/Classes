package ta4_1.MoneyFlow_Backend.Family;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ta4_1.MoneyFlow_Backend.Users.User;
import ta4_1.MoneyFlow_Backend.Users.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Controller for Families
 *
 * @author Onur Onal
 * @author Kemal Yavuz
 */

@RestController
@RequestMapping("/family")
public class FamilyController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FamilyRepository familyRepository;

    /**
     * Add a user to a family.
     *
     * @param userId   The ID of the user from which the family will be retrieved.
     * @param familyMember The user to be added to the family.
     * @return UUID of the newly added family member.
     */
    @PostMapping("/addMember/{userId}")
    public UUID addFamilyMember(@PathVariable UUID userId, @RequestBody User familyMember) {
        User mainUser = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        Family family = mainUser.getFamily();
        if (family == null) {
            family = new Family();
            family.setName(mainUser.getFirstName() + "'s Household");
            familyRepository.save(family);
            mainUser.setFamily(family);
            mainUser.setFamilyStatus("headOfHousehold");
            userRepository.save(mainUser);
        }

        String uniqueEmail = "family_" + UUID.randomUUID().toString() + "@example.com";
        familyMember.setEmail(uniqueEmail);
        familyMember.setPassword(mainUser.getPassword());

        familyMember.setFamilyStatus("familyMember");
        familyMember.setType(mainUser.getType());
        familyMember.setFamily(family);

        userRepository.save(familyMember);
        return familyMember.getId();
    }

    /**
     * Get a list of all families.
     *
     * @return List of families.
     */
    @GetMapping
    public List<Family> getAllFamilies() {
        return familyRepository.findAll();
    }

    @GetMapping("/ofUser/{id}")
    public ResponseEntity<Family> getFamilyOfUser(@PathVariable UUID id) {
        return userRepository.findById(id)
                .map(user -> {
                    Family f = user.getFamily();
                    return ResponseEntity.ok(f);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Update a family by its ID.
     *
     * @param familyId        The ID of the family to be updated.
     * @param updatedFamily   The updated family object.
     * @return ResponseEntity of the updated family.
     */
    @PutMapping("/updateFamily/{familyId}")
    public ResponseEntity<Family> updateFamily(@PathVariable UUID familyId, @RequestBody Family updatedFamily) {
        Family family = familyRepository.findById(familyId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Family not found"));

        // Update family details
        if (updatedFamily.getName() != null) {
            family.setName(updatedFamily.getName());
        }

        familyRepository.save(family);
        return ResponseEntity.ok(family);
    }

    /**
     * Delete a family by its ID.
     *
     * @param familyId The ID of the family to be deleted.
     * @return A success string.
     */
    @DeleteMapping("/deleteFamily/{familyId}")
    public ResponseEntity<String> deleteFamily(@PathVariable UUID familyId) {
        Family family = familyRepository.findById(familyId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Family not found"));

        // Delete all users in the family
        for (User user : family.getUsers()) {
            userRepository.deleteById(user.getId());
        }

        // Delete the family
        familyRepository.deleteById(familyId);

        return ResponseEntity.ok("Family and all associated users and cards have been deleted.");
    }

}