package ta4_1.MoneyFlow_Backend.Portfolio;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import ta4_1.MoneyFlow_Backend.Users.User;
import ta4_1.MoneyFlow_Backend.Users.UserRepository;

@WebMvcTest(PortfolioController.class)
@ExtendWith(SpringExtension.class)
public class PortfolioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PortfolioRepository portfolioRepository;

    @MockBean
    private PortfolioService portfolioService;

    @MockBean
    private UserRepository userRepository;

    @Test
    void testGetPortfolioValue() throws Exception {
        // Arrange for existing user
        UUID existingUserId = UUID.randomUUID();
        User user = new User();
        Portfolio portfolio = new Portfolio();
        portfolio.setPortfolioValue(10000.0);
        user.setPortfolio(portfolio);
        when(userRepository.findById(existingUserId)).thenReturn(Optional.of(user));

        // Act & Assert for existing user
        mockMvc.perform(get("/portfolio/{id}/value", existingUserId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").value(10000.0));

        // Arrange for non-existing user
        UUID nonExistingUserId = UUID.randomUUID();
        when(userRepository.findById(nonExistingUserId)).thenReturn(Optional.empty());

        // Act & Assert for non-existing user
        mockMvc.perform(get("/portfolio/{id}/value", nonExistingUserId))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreatePortfolio() throws Exception {
        // Arrange
        UUID userId = UUID.randomUUID();
        User user = new User();
        Portfolio portfolio = new Portfolio();
        portfolio.setId(UUID.randomUUID());
        portfolio.setUser(user); // Simulating a user being set in the portfolio

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(portfolioRepository.save(any(Portfolio.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Create JSON payload for the request
        ObjectMapper mapper = new ObjectMapper();
        String portfolioJson = mapper.writeValueAsString(portfolio);

        // Act & Assert
        mockMvc.perform(post("/portfolio/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(portfolioJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.portfolioId").value(portfolio.getId().toString()));

        // Verify that save methods were called on the repository
        verify(portfolioRepository).save(any(Portfolio.class));
        verify(userRepository).save(any(User.class));
    }

    @Test
    void testBuyShares() throws Exception {
        // Arrange
        UUID userId = UUID.randomUUID();
        User user = new User();
        Portfolio currentPortfolio = new Portfolio();
        currentPortfolio.setAppleShares(10);
        currentPortfolio.setAmazonShares(5);
        currentPortfolio.setBitcoin(3);
        currentPortfolio.setDogecoin(100);
        user.setPortfolio(currentPortfolio);

        Portfolio updatedPortfolio = new Portfolio();
        updatedPortfolio.setAppleShares(2);  // intended to buy 2 more Apple shares
        updatedPortfolio.setAmazonShares(1); // intended to buy 1 more Amazon share
        updatedPortfolio.setBitcoin(0.5);    // intended to buy 0.5 more Bitcoins
        updatedPortfolio.setDogecoin(50);    // intended to buy 50 more Dogecoins

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(portfolioService.updatePortfolioValue(any())).thenReturn(100000.0);

        when(portfolioRepository.save(any(Portfolio.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ObjectMapper mapper = new ObjectMapper();
        String updatedPortfolioJson = mapper.writeValueAsString(updatedPortfolio);

        // Act
        mockMvc.perform(put("/portfolio/{id}/buy", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedPortfolioJson))
                .andExpect(status().isOk());

        // Verify
        verify(portfolioRepository).save(any(Portfolio.class)); // Generalize the saving method to accept any Portfolio
    }


    @Test
    void testUpdateStockPrices() throws Exception {
        // Arrange
        UUID userId = UUID.randomUUID();
        User user = new User();
        Portfolio currentPortfolio = new Portfolio();
        currentPortfolio.setApplePrice(100.0);
        currentPortfolio.setAmazonPrice(200.0);
        currentPortfolio.setBitcoinPrice(300.0);
        currentPortfolio.setDogecoinPrice(400.0);
        user.setPortfolio(currentPortfolio);

        Map<String, Double> updatedPrices = new HashMap<>();
        updatedPrices.put("applePrice", 150.0);
        updatedPrices.put("amazonPrice", 250.0);
        updatedPrices.put("bitcoinPrice", 350.0);
        updatedPrices.put("dogecoinPrice", 450.0);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(portfolioService.updatePortfolioValue(currentPortfolio)).thenReturn(500000.0);
        when(portfolioRepository.save(any(Portfolio.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ObjectMapper mapper = new ObjectMapper();
        String updatedPricesJson = mapper.writeValueAsString(updatedPrices);

        // Act
        mockMvc.perform(put("/portfolio/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedPricesJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.applePrice").value(150.0))
                .andExpect(jsonPath("$.amazonPrice").value(250.0))
                .andExpect(jsonPath("$.bitcoinPrice").value(350.0))
                .andExpect(jsonPath("$.dogecoinPrice").value(450.0))
                .andExpect(jsonPath("$.portfolioValue").value(500000.0));

        // Verify
        verify(portfolioRepository).save(any(Portfolio.class));
    }

    @Test
    void testDeletePortfolio() throws Exception {
        // Arrange
        UUID userId = UUID.randomUUID();
        User user = new User();
        Portfolio portfolio = new Portfolio();
        user.setPortfolio(portfolio);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // Act
        mockMvc.perform(delete("/portfolio/{userId}", userId))
                .andExpect(status().isOk());

        // Verify interactions
        verify(portfolioRepository).delete(portfolio); // Verify that the portfolio is deleted from the repository
        verify(userRepository).save(user); // Verify that the user is updated in the repository
        assertNull(user.getPortfolio(), "Portfolio should be null after deletion"); // Assert that the user's portfolio is set to null
    }


}