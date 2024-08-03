package ta4_1.MoneyFlow_Backend.Users;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

// Annotate the class to use WebMvcTest for only UserController
@WebMvcTest(UserController.class)
@ExtendWith(SpringExtension.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private BCryptPasswordEncoder passwordEncoder;

    private User user;
    private UUID userId;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        userId = UUID.randomUUID();
        user = new User();
        user.setId(userId);
        user.setFirstName("Test");
        user.setLastName("User");
        user.setEmail("test@example.com");
        user.setPassword("password");
        user.setType("regular");

        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(userRepository.findAll()).thenReturn(List.of(user));
        when(userRepository.findByType("regular")).thenReturn(List.of(user));
    }

    @Test
    void testGetUsersByType() throws Exception {
        mockMvc.perform(get("/users/type/regular"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].email").value("test@example.com"));
    }

    @Test
    void testGetUser() throws Exception {
        mockMvc.perform(get("/users/id/{id}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test@example.com"));
    }

    @Test
    void testGetAllUsers() throws Exception {
        // Mock user data
        User user1 = new User();
        user1.setId(UUID.randomUUID());
        user1.setFirstName("John");
        user1.setLastName("Doe");
        user1.setEmail("test@example.com");
        user1.setPassword("password123");
        user1.setMonthlyIncome(5000);
        user1.setAnnualIncome(60000);

        User user2 = new User();
        user2.setId(UUID.randomUUID());
        user2.setFirstName("Jane");
        user2.setLastName("Smith");
        user2.setEmail("jane@example.com");
        user2.setPassword("password123");
        user2.setMonthlyIncome(7000);
        user2.setAnnualIncome(80000);

        // Mock repository behavior
        when(userRepository.findAllWithFamily()).thenReturn(List.of(user1, user2));

        // Perform the GET request and verify
        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].email").value("test@example.com"))
                .andExpect(jsonPath("$[1].email").value("jane@example.com"));

        // Verify findAllWithFamily method was called on the repository
        verify(userRepository).findAllWithFamily();
    }


    @Test
    void testSignup() throws Exception {
        mockMvc.perform(post("/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstName\":\"Test\",\"lastName\":\"User\",\"email\":\"new@example.com\",\"password\":\"newpass\"}"))
                .andExpect(status().isOk());

        verify(userRepository).save(any(User.class));
    }

    @Test
    void testLoginGuest() throws Exception {
        mockMvc.perform(post("/login/type/guest"))
                .andExpect(status().isOk());

        verify(userRepository).save(any(User.class));
    }

    @Test
    void testLogin() throws Exception {
        mockMvc.perform(post("/login")
                        .param("email", "test@example.com")
                        .param("password", "password"))
                .andExpect(status().isOk());

        verify(userRepository).findByEmail("test@example.com");
    }

    @Test
    void testUpdateUser() throws Exception {
        UUID userId = UUID.randomUUID();

        // Mock the user before and after update
        User existingUser = new User();
        existingUser.setId(userId);
        existingUser.setFirstName("Existing");
        existingUser.setLastName("User");
        existingUser.setEmail("existing@example.com");
        existingUser.setPassword("oldpass");
        existingUser.setMonthlyIncome(4000);
        existingUser.setAnnualIncome(50000);

        User updatedUser = new User();
        updatedUser.setId(userId);
        updatedUser.setFirstName("Updated");
        updatedUser.setLastName("User");
        updatedUser.setEmail("updated@example.com");
        updatedUser.setPassword("newpass");
        updatedUser.setMonthlyIncome(5000);
        updatedUser.setAnnualIncome(60000);

        // Mock repository behavior
        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);

        // Perform the PUT request and verify
        mockMvc.perform(put("/users/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstName\":\"Updated\",\"lastName\":\"User\",\"email\":\"updated@example.com\",\"password\":\"newpass\",\"monthlyIncome\":5000,\"annualIncome\":60000}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Updated"))
                .andExpect(jsonPath("$.email").value("updated@example.com"));

        // Verify save method was called on the repository
        verify(userRepository).save(any(User.class));
    }


    @Test
    void testUpdateIncome() throws Exception {
        mockMvc.perform(patch("/users/{id}/income", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"income\":5000}"))
                .andExpect(status().isOk());

        verify(userRepository).save(any(User.class));
    }

    @Test
    void testGetMonthlyIncome() throws Exception {
        mockMvc.perform(get("/{id}/monthlyIncome", userId))
                .andExpect(status().isOk());
    }

    @Test
    void testGetAnnualIncome() throws Exception {
        mockMvc.perform(get("/{id}/annualIncome", userId))
                .andExpect(status().isOk());
    }

    @Test
    void testGetCurrencyExchangeSetting() throws Exception {
        mockMvc.perform(get("/{id}/getCurrency", userId))
                .andExpect(status().isOk());
    }

    @Test
    void testSetCurrency() throws Exception {
        mockMvc.perform(post("/{userId}/setCurrency/{Settings}", userId, "USD,EUR"))
                .andExpect(status().isOk());

        verify(userRepository).save(any(User.class));
    }

    @Test
    void testUpgradeType() throws Exception {
        mockMvc.perform(post("/upgradeType/{userId}", userId))
                .andExpect(status().isOk())
                .andExpect(content().string("User upgraded to premium"));

        verify(userRepository).save(any(User.class));
    }

    @Test
    void testGetUserType() throws Exception {
        mockMvc.perform(get("/userType/{userId}", userId))
                .andExpect(status().isOk())
                .andExpect(content().string("regular"));
    }

    @Test
    void testGenerateFinancialReport() throws Exception {
        mockMvc.perform(get("/{id}/financial-report", userId))
                .andExpect(status().isOk());
    }

    @Test
    void testDeleteUser() throws Exception {
        mockMvc.perform(delete("/users/{id}", userId))
                .andExpect(status().isOk())
                .andExpect(content().string("User deleted successfully"));

        verify(userRepository).deleteById(userId);
    }

    @Test
    void testDeleteAllGuestUsers() throws Exception {
        // Mock multiple guest users
        User guestUser1 = mock(User.class);
        UUID guestUserId1 = UUID.randomUUID();
        when(guestUser1.getId()).thenReturn(guestUserId1);
        when(guestUser1.getType()).thenReturn("guest");

        User guestUser2 = mock(User.class);
        UUID guestUserId2 = UUID.randomUUID();
        when(guestUser2.getId()).thenReturn(guestUserId2);
        when(guestUser2.getType()).thenReturn("guest");

        List<User> users = List.of(guestUser1, guestUser2);

        // Mock the repository's findAll method
        when(userRepository.findAll()).thenReturn(users);

        // Perform the DELETE request and verify the output
        mockMvc.perform(delete("/delete-guest-users"))
                .andExpect(status().isOk())
                .andExpect(content().string("All guest users have been deleted."));

        // Verify that the deleteById method was called for each guest user
        verify(userRepository).deleteById(guestUserId1);
        verify(userRepository).deleteById(guestUserId2);
    }


    @Test
    void testDeleteAllRegularUsers() throws Exception {
        // Mock users with "regular" type
        List<User> regularUsers = List.of(new User());
        regularUsers.get(0).setId(UUID.randomUUID());
        regularUsers.get(0).setType("regular");
        when(userRepository.findAll()).thenReturn(regularUsers);

        mockMvc.perform(delete("/delete-regular-users"))
                .andExpect(status().isOk())
                .andExpect(content().string("All regular users have been deleted."));

        verify(userRepository).deleteById(regularUsers.get(0).getId());
    }


    @Test
    void testDeleteAllFamilyMemberUsers() throws Exception {
        // Mock multiple familyMember users
        User familyMemberUser1 = mock(User.class);
        UUID familyMemberUserId1 = UUID.randomUUID();
        when(familyMemberUser1.getId()).thenReturn(familyMemberUserId1);
        when(familyMemberUser1.getFamilyStatus()).thenReturn("familyMember");

        User familyMemberUser2 = mock(User.class);
        UUID familyMemberUserId2 = UUID.randomUUID();
        when(familyMemberUser2.getId()).thenReturn(familyMemberUserId2);
        when(familyMemberUser2.getFamilyStatus()).thenReturn("familyMember");

        List<User> users = List.of(familyMemberUser1, familyMemberUser2);

        // Mock the repository's findAll method
        when(userRepository.findAll()).thenReturn(users);

        // Perform the DELETE request and verify the output
        mockMvc.perform(delete("/delete-familyMember-users"))
                .andExpect(status().isOk())
                .andExpect(content().string("All family member status users have been deleted."));

        // Verify that the deleteById method was called for each family member user
        verify(userRepository).deleteById(familyMemberUserId1);
        verify(userRepository).deleteById(familyMemberUserId2);
    }
}
