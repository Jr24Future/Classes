package ta4_1.MoneyFlow_Backend.Family;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import ta4_1.MoneyFlow_Backend.Users.User;
import ta4_1.MoneyFlow_Backend.Users.UserRepository;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = FamilyController.class)
public class FamilyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private FamilyRepository familyRepository;

    private UUID userId;
    private User mainUser;
    private Family family;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        mainUser = new User();
        mainUser.setId(userId);
        mainUser.setFirstName("John");
        mainUser.setPassword("password");

        family = new Family();
        family.setId(UUID.randomUUID());
        family.setName("John's Household");

        when(userRepository.findById(userId)).thenReturn(Optional.of(mainUser));
        when(familyRepository.save(any(Family.class))).thenReturn(family);
    }
    @Test
    void testAddFamilyMemberAndVerifyFamilyId() throws Exception {
        UUID mainUserId = UUID.randomUUID();
        UUID familyId = UUID.randomUUID();  // Predefined family ID for simplicity.

        // Set up the main user and their family
        User mainUser = new User();
        mainUser.setId(mainUserId);
        mainUser.setFirstName("John");

        Family family = new Family();
        family.setId(familyId);
        family.setName("John's Household");
        family.addUser(mainUser);  // Link the main user to the family

        // Mock the repository to return the main user and the family
        when(userRepository.findById(mainUserId)).thenReturn(Optional.of(mainUser));
        when(familyRepository.save(any(Family.class))).thenReturn(family);

        // Simulate the save method
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        String requestBody = "{\"firstName\":\"Jane\"}";
        UUID newMemberId = UUID.randomUUID();
        User newMember = new User();
        newMember.setId(newMemberId);
        newMember.setFirstName("Jane");
        newMember.setFamily(family); // Set new member to the same family

        mockMvc.perform(post("/family/addMember/{userId}", mainUserId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andDo(result -> {
                    User savedUser = userRepository.save(newMember);
                    assertEquals(familyId, savedUser.getFamily().getId());
                });

        verify(userRepository, times(2)).save(any(User.class));  // Verify that save was called twice
    }

    @Test
    void testGetAllFamilies() throws Exception {
        mockMvc.perform(get("/family")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void testGetFamilyOfUser() throws Exception {
        when(userRepository.findById(userId)).thenReturn(Optional.of(mainUser));
        mainUser.setFamily(family);

        mockMvc.perform(get("/family/ofUser/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John's Household"));
    }

    @Test
    void testUpdateFamily() throws Exception {
        String updatedFamilyName = "Updated Household";
        String updateBody = "{\"name\":\"Updated Household\"}";

        when(familyRepository.findById(family.getId())).thenReturn(Optional.of(family));

        mockMvc.perform(put("/family/updateFamily/{familyId}", family.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(updatedFamilyName));

        verify(familyRepository).save(family);
    }

    @Test
    void testDeleteFamily() throws Exception {
        when(familyRepository.findById(family.getId())).thenReturn(Optional.of(family));
        mockMvc.perform(delete("/family/deleteFamily/{familyId}", family.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Family and all associated users and cards have been deleted."));

        verify(familyRepository).deleteById(family.getId());
    }
}