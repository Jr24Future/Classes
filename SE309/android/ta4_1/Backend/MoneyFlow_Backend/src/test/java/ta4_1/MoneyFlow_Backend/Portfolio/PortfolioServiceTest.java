package ta4_1.MoneyFlow_Backend.Portfolio;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PortfolioServiceTest {

    private PortfolioService portfolioService;
    private Portfolio userPortfolio;

    @BeforeEach
    void setUp() {
        portfolioService = new PortfolioService();
        userPortfolio = new Portfolio();

        userPortfolio.setAppleShares(10);
        userPortfolio.setApplePrice(150);

        userPortfolio.setAmazonShares(5);
        userPortfolio.setAmazonPrice(2000);

        userPortfolio.setBitcoin(2);
        userPortfolio.setBitcoinPrice(45000);

        userPortfolio.setDogecoin(10000);
        userPortfolio.setDogecoinPrice(0.3);
    }

    @Test
    void testUpdatePortfolioValue() {
        double expectedPortfolioValue = (10 * 150) + (5 * 2000) + (2 * 45000) + (10000 * 0.3);
        double actualPortfolioValue = portfolioService.updatePortfolioValue(userPortfolio);

        assertEquals(expectedPortfolioValue, actualPortfolioValue, 0.01, "Portfolio value is incorrect");
    }

    @Test
    void testMapPortfolioValues() {
        Map<String, Double> portfolioValues = portfolioService.mapPortfolioValues(userPortfolio);

        assertEquals(1500, portfolioValues.get("Apple"), 0.01, "Apple stock value is incorrect");
        assertEquals(10000, portfolioValues.get("Amazon"), 0.01, "Amazon stock value is incorrect");
        assertEquals(90000, portfolioValues.get("Bitcoin"), 0.01, "Bitcoin value is incorrect");
        assertEquals(3000, portfolioValues.get("Dogecoin"), 0.01, "Dogecoin value is incorrect");
    }
}
