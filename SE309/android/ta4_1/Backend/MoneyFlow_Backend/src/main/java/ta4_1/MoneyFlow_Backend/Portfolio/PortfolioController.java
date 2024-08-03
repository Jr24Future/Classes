package ta4_1.MoneyFlow_Backend.Portfolio;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ta4_1.MoneyFlow_Backend.Users.User;
import ta4_1.MoneyFlow_Backend.Users.UserRepository;

import javax.sound.sampled.Port;
import java.util.*;

/**
 * Controller for managing portfolios.
 *
 * @author Onur Onal
 * @author Kemal Yavuz
 */
@RestController
@RequestMapping("/portfolio")
public class PortfolioController {
    @Autowired
    private PortfolioRepository portfolioRepository;

    @Autowired
    private PortfolioService portfolioService;

    @Autowired
    private UserRepository userRepository;

    /**
     * Get a portfolio value of a user.
     *
     * @param id The ID of the user.
     * @return ResponseEntity with the found portfolio's value.
     */
    @GetMapping("/{id}/value")
    public ResponseEntity<Double> getPortfolioValue(@PathVariable UUID id) {
        return userRepository.findById(id)
                .map(user -> ResponseEntity.ok(user.getPortfolio().getPortfolioValue()))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Get a portfolio of a user.
     *
     * @param id The ID of the user.
     * @return ResponseEntity with the found portfolio.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Portfolio> getPortfolioOfUser(@PathVariable UUID id) {
        return userRepository.findById(id)
                .map(user -> ResponseEntity.ok(user.getPortfolio()))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Get all portfolios.
     *
     * @return List of all portfolios.
     */
    @GetMapping
    public List<Portfolio> getAllPortfolios() {
        return portfolioRepository.findAll();
    }


    /**
     * Create a portfolio for a user.
     *
     * @param userId    The ID of the user.
     * @param portfolio The portfolio object to be created.
     * @return ResponseEntity with the created portfolio.
     */
    @PostMapping("/{userId}")
    public ResponseEntity<?> createPortfolio(@PathVariable UUID userId, @RequestBody Portfolio portfolio) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (!userOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        User user = userOptional.get();
        portfolio.setUser(user);
        Portfolio savedPortfolio = portfolioRepository.save(portfolio);
        user.setPortfolio(savedPortfolio);
        userRepository.save(user);
        return ResponseEntity.ok(Map.of("portfolioId", portfolio.getId()));
    }

    /**
     * Get the detailed portfolio values of a user.
     *
     * @param id The ID of the user.
     * @return ResponseEntity containing the portfolio values.
     */
    @GetMapping("/{id}/detailedValues")
    public ResponseEntity<?> getDetailedPortfolioValues(@PathVariable UUID id) {
        return userRepository.findById(id)
                .map(user -> {
                    Portfolio portfolio = user.getPortfolio();
                    if (portfolio != null) {
                        Map<String, Double> values = portfolioService.mapPortfolioValues(portfolio);
                        return ResponseEntity.ok(values);
                    } else {
                        return ResponseEntity.notFound().build();
                    }
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


    /**
     * Purchases a stock for a user.
     *
     * @param id               The ID of the user who purchases the stock.
     * @param updatedPortfolio The updated portfolio object.
     * @return ResponseEntity with the updated portfolio.
     */
    @PutMapping("/{id}/buy")
    @Transactional
    public ResponseEntity<Portfolio> buyShares(@PathVariable UUID id, @RequestBody Portfolio updatedPortfolio) {
        return userRepository.findById(id)
                .map(user -> {
                    Portfolio currentPortfolio = user.getPortfolio();
                    if (updatedPortfolio.getApplePrice() != 0.0) { currentPortfolio.setApplePrice(updatedPortfolio.getApplePrice()); }
                    if (updatedPortfolio.getAmazonPrice() != 0.0) { currentPortfolio.setAmazonPrice(updatedPortfolio.getAmazonPrice()); }
                    if (updatedPortfolio.getBitcoinPrice() != 0.0) { currentPortfolio.setBitcoinPrice(updatedPortfolio.getBitcoinPrice()); }
                    if (updatedPortfolio.getDogecoinPrice() != 0.0) { currentPortfolio.setDogecoinPrice(updatedPortfolio.getDogecoinPrice()); }
                    currentPortfolio.setAppleShares(currentPortfolio.getAppleShares() + updatedPortfolio.getAppleShares());
                    currentPortfolio.setAmazonShares(currentPortfolio.getAmazonShares() + updatedPortfolio.getAmazonShares());
                    currentPortfolio.setBitcoin(currentPortfolio.getBitcoin() + updatedPortfolio.getBitcoin());
                    currentPortfolio.setDogecoin(currentPortfolio.getDogecoin() + updatedPortfolio.getDogecoin());
                    currentPortfolio.setPortfolioValue(portfolioService.updatePortfolioValue(currentPortfolio));

                    portfolioRepository.save(currentPortfolio);
                    return ResponseEntity.ok(currentPortfolio);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Sells a stock for a user.
     *
     * @param id               The ID of the user who sells the stock.
     * @param updatedPortfolio The updated portfolio object.
     * @return ResponseEntity with the updated portfolio.
     */
    @PutMapping("/{id}/sell")
    @Transactional
    public ResponseEntity<Portfolio> sellShares(@PathVariable UUID id, @RequestBody Portfolio updatedPortfolio) {
        return userRepository.findById(id)
                .map(user -> {
                    Portfolio currentPortfolio = user.getPortfolio();
                    if (updatedPortfolio.getApplePrice() != 0.0) { currentPortfolio.setApplePrice(updatedPortfolio.getApplePrice()); }
                    if (updatedPortfolio.getAmazonPrice() != 0.0) { currentPortfolio.setAmazonPrice(updatedPortfolio.getAmazonPrice()); }
                    if (updatedPortfolio.getBitcoinPrice() != 0.0) { currentPortfolio.setBitcoinPrice(updatedPortfolio.getBitcoinPrice()); }
                    if (updatedPortfolio.getDogecoinPrice() != 0.0) { currentPortfolio.setDogecoinPrice(updatedPortfolio.getDogecoinPrice()); }
                    currentPortfolio.setAppleShares(Math.max(currentPortfolio.getAppleShares() - updatedPortfolio.getAppleShares(), 0));
                    currentPortfolio.setAmazonShares(Math.max(currentPortfolio.getAmazonShares() - updatedPortfolio.getAmazonShares(), 0));
                    currentPortfolio.setBitcoin(Math.max(currentPortfolio.getBitcoin() - updatedPortfolio.getBitcoin(), 0));
                    currentPortfolio.setDogecoin(Math.max(currentPortfolio.getDogecoin() - updatedPortfolio.getDogecoin(), 0));
                    currentPortfolio.setPortfolioValue(portfolioService.updatePortfolioValue(currentPortfolio));

                    portfolioRepository.save(currentPortfolio);
                    return ResponseEntity.ok(currentPortfolio);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Updates stock prices.
     *
     * @param id            The ID of the user.
     * @param updatedPrices A map of the updated prices.
     * @return ResponseEntity with the updated portfolio.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Portfolio> updateStockPrices(@PathVariable UUID id, @RequestBody Map<String, Double> updatedPrices) {
        return userRepository.findById(id)
                .map(user -> {
                    Portfolio currentPortfolio = user.getPortfolio();
                    updatedPrices.forEach((key, value) -> {
                        switch (key) {
                            case "applePrice":
                                currentPortfolio.setApplePrice(value);
                                break;
                            case "amazonPrice":
                                currentPortfolio.setAmazonPrice(value);
                                break;
                            case "bitcoinPrice":
                                currentPortfolio.setBitcoinPrice(value);
                                break;
                            case "dogecoinPrice":
                                currentPortfolio.setDogecoinPrice(value);
                                break;
                            default:
                                break;
                        }
                    });
                    currentPortfolio.setPortfolioValue(portfolioService.updatePortfolioValue(currentPortfolio));
                    portfolioRepository.save(currentPortfolio);
                    return ResponseEntity.ok(currentPortfolio);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Delete portfolio by user ID.
     *
     * @param userId The ID of the user whose portfolio is to be deleted.
     * @return ResponseEntity indicating the result of the deletion.
     */
    @DeleteMapping("/{userId}")
    public ResponseEntity<?> deletePortfolio(@PathVariable UUID userId) {
        return userRepository.findById(userId)
                .map(user -> {
                    Portfolio portfolio = user.getPortfolio();
                    if (portfolio != null) {
                        portfolioRepository.delete(portfolio);
                        user.setPortfolio(null);
                        userRepository.save(user);
                        return ResponseEntity.ok().<Void>build();
                    }
                    else {
                        return ResponseEntity.notFound().build();
                    }
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

}
