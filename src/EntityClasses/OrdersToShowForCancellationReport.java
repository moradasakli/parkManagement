package EntityClasses;

import java.io.Serializable;

public class OrdersToShowForCancellationReport implements Serializable {

	private int park;
	private int number_of_visitors;
	private String is_payed;
	private double price;
	
	   // Constructor
    public OrdersToShowForCancellationReport(int park, int number_of_visitors, String is_payed, double price) {
        this.park = park;
        this.number_of_visitors = number_of_visitors;
        this.is_payed = is_payed;
        this.price = price;
    }

    // Getters and setters
    public int getPark() {
        return park;
    }

    public void setPark(int park) {
        this.park = park;
    }

    public int getNumber_of_visitors() {
        return number_of_visitors;
    }

    public void setNumber_of_visitors(int number_of_visitors) {
        this.number_of_visitors = number_of_visitors;
    }

    public String getIs_payed() {
        return is_payed;
    }

    public void setIs_payed(String is_payed) {
        this.is_payed = is_payed;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    // toString method
    @Override
    public String toString() {
        return "OrdersToShowForCancellationReport{" +
                "park=" + park +
                ", number_of_visitors=" + number_of_visitors +
                ", is_payed='" + is_payed + '\'' +
                ", price=" + price +
                '}';
    }
}
