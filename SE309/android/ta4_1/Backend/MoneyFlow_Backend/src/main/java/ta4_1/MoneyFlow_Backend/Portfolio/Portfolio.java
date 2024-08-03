package ta4_1.MoneyFlow_Backend.Portfolio;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import ta4_1.MoneyFlow_Backend.Users.User;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;

/**
 * Entity Representing a user investment portfolio.
 *
 * @author Onur Onal
 * @author Kemal Yavuz
 */

@Entity
@Table(name = "portfolio")
public class Portfolio {
    @Id
    @GeneratedValue(generator = "UUID")
    private UUID id;

    @Column(name = "portfolio_value")
    private double portfolioValue;

    @Column(name = "aapl_shares")
    private double AAPLShares;

    @Column(name = "amzn_shares")
    private double AMZNShares;

    @Column(name = "btcusdt")
    private double BTCUSDTShares;

    @Column(name = "dogeusdt")
    private double DOGEUSDTShares;

    @Column(name = "aapl_price")
    private double AAPLPrice;

    @Column(name = "amzn_price")
    private double AMZNPrice;

    @Column(name = "btcusdt_price")
    private double BTCUSDTPrice;

    @Column(name = "dogeusdt_price")
    private double DOGEUSDTPrice;

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;  // The user associated with this portfolio.

    public Portfolio() {
    }

    public Portfolio(double portfolioValue, double AAPLShares, double AMZNShares, double BTCUSDTShares, double DOGEUSDTShares, double AAPLPrice, double AMZNPrice, double BTCUSDTPrice, double DOGEUSDTPrice) {
        this.portfolioValue = portfolioValue;
        this.AAPLShares = AAPLShares;
        this.AMZNShares = AMZNShares;
        this.BTCUSDTShares = BTCUSDTShares;
        this.DOGEUSDTShares = DOGEUSDTShares;
        this.AAPLPrice = AAPLPrice;
        this.AMZNPrice = AMZNPrice;
        this.BTCUSDTPrice = BTCUSDTPrice;
        this.DOGEUSDTPrice = DOGEUSDTPrice;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public double getPortfolioValue() {
        return portfolioValue;
    }

    public void setPortfolioValue(double portfolioValue) {
        if (portfolioValue >= 0) {
            this.portfolioValue = roundToTwoDecimalPlaces(portfolioValue);
        }
    }

    public double getAppleShares() {
        return AAPLShares;
    }

    public double getAmazonShares() {
        return AMZNShares;
    }

    public double getBitcoin() {
        return BTCUSDTShares;
    }

    public double getDogecoin() {
        return DOGEUSDTShares;
    }

    public double getApplePrice() {
        return AAPLPrice;
    }

    public double getAmazonPrice() {
        return AMZNPrice;
    }

    public double getBitcoinPrice() {
        return BTCUSDTPrice;
    }

    public double getDogecoinPrice() {
        return DOGEUSDTPrice;
    }

    public void setAppleShares(double AAPLShares) {
        if (AAPLShares >= 0) {
            this.AAPLShares = AAPLShares;
        }
    }

    public void setAmazonShares(double AMZNShares) {
        if (AMZNShares >= 0) {
            this.AMZNShares = AMZNShares;
        }
    }

    public void setBitcoin(double BTCUSDTShares) {
        if (BTCUSDTShares >= 0) {
            this.BTCUSDTShares = BTCUSDTShares;
        }
    }

    public void setDogecoin(double DOGEUSDTShares) {
        if (DOGEUSDTShares >= 0) {
            this.DOGEUSDTShares = DOGEUSDTShares;
        }
    }

    public void setApplePrice(double AAPLPrice) {
        if (AAPLPrice >= 0) {
            this.AAPLPrice = AAPLPrice;
        }
    }

    public void setAmazonPrice(double AMZNPrice) {
        if (AMZNPrice >= 0) {
            this.AMZNPrice = AMZNPrice;
        }
    }

    public void setBitcoinPrice(double BTCUSDTPrice) {
        if (BTCUSDTPrice >= 0) {
            this.BTCUSDTPrice = BTCUSDTPrice;
        }
    }

    public void setDogecoinPrice(double DOGEUSDTPrice) {
        if (DOGEUSDTPrice >= 0) {
            this.DOGEUSDTPrice = DOGEUSDTPrice;
        }
    }

    private double roundToTwoDecimalPlaces(double value) {
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public double getValueOfAppleStock() {
        return roundToTwoDecimalPlaces(AAPLShares * AAPLPrice);
    }

    public double getValueOfAmazonStock() {
        return roundToTwoDecimalPlaces(AMZNShares * AMZNPrice);
    }

    public double getValueOfBitcoin() {
        return roundToTwoDecimalPlaces(BTCUSDTShares * BTCUSDTPrice);
    }

    public double getValueOfDogecoin() {
        return roundToTwoDecimalPlaces(DOGEUSDTShares * DOGEUSDTPrice);
    }

    @Override
    public String toString() {
        return "Portfolio{" +
                "id=" + id +
                ", Apple Shares=" + AAPLShares +
                ", Amazon Shares=" + AMZNShares +
                ", Bitcoin=" + BTCUSDTShares +
                ", Dogecoin=" + DOGEUSDTShares +
                ", Apple Price=" + AAPLPrice +
                ", Amazon Price=" + AMZNPrice +
                ", Bitcoin Price=" + BTCUSDTPrice +
                ", Dogecoin Price=" + DOGEUSDTPrice +
                '}';
    }
}
