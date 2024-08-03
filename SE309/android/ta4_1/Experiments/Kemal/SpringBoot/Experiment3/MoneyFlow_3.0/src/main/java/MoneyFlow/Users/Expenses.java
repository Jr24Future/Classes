package MoneyFlow.Users;

/**
 * A class where will the expenses will be stored.
 *
 * @author Kemal Yavuz
 */

public class Expenses {

    private int id;

    private double rent;

    private double food;

    private double education;

    public Expenses(){
        // Default constructor
    }

    public Expenses(int id, double rent, double food, double education){
        this.id = id;
        this.rent = rent;
        this.food = food;
        this.education = education;
    }

    // Getters and setters

    public double getRent() {
        return rent;
    }

    public void setRent(double rent) {
        this.rent = rent;
    }

    public double getFood() {
        return food;
    }

    public void setFood(double food) {
        this.food = food;
    }

    public double getEducation() {
        return education;
    }

    public void setEducation(double education) {
        this.education = education;
    }

    @Override
    public String toString() {
        return rent + " "
                + food + " "
                + education + " "
                + id;
    }
}