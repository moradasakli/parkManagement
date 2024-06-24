package EntityClasses;

import java.io.Serializable;
import java.time.LocalDate;

public class OrderInTable implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private String orderNumber;
	private String ordererId;
	private String parkNameInOrder;
	private LocalDate dateOfVisit;
	private String timeOfVisit;
	private String numberOfVisitorsInOrder;
	
	private String orderStatus;
	private String Confirmation;
	private String Paid;
	private double price;

	public OrderInTable(String orderNumber, String ordererId, String parkNameInOrder, LocalDate dateOfVisit,
			String timeOfVisit, String numberOfVisitorsInOrder, String orderStatus, String Confirmation,
			String Paid, double price) {
		super();
		this.orderNumber = orderNumber;
		this.ordererId = ordererId;
		this.parkNameInOrder = parkNameInOrder;
		this.dateOfVisit = dateOfVisit;
		this.timeOfVisit = timeOfVisit;
		this.numberOfVisitorsInOrder = numberOfVisitorsInOrder;
		this.orderStatus = orderStatus;
		this.Confirmation = Confirmation;
		this.Paid = Paid;
		this.price = price;
	}
	public OrderInTable(OrderInTable o) {
		
		this.orderNumber = o.getOrderNumber();
		this.ordererId = o.getOrdererId();
		this.parkNameInOrder = o.getParkNameInOrder();
		this.dateOfVisit = o.getDateOfVisit();
		this.timeOfVisit = o.getTimeOfVisit();
		this.numberOfVisitorsInOrder = o.getNumberOfVisitorsInOrder();
		this.orderStatus = o.getOrderStatus();
		this.Confirmation = o.isConfirmed();
		this.Paid = o.isPaid();
		this.price = o.getPrice();
		// TODO Auto-generated constructor stub
	}

	public OrderInTable() {
		// TODO Auto-generated constructor stub
	}

	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	public String getOrdererId() {
		return ordererId;
	}

	public void setOrdererId(String ordererId) {
		this.ordererId = ordererId;
	}

	public String getParkNameInOrder() {
		return parkNameInOrder;
	}

	public void setParkNameInOrder(String parkNameInOrder) {
		this.parkNameInOrder = parkNameInOrder;
	}

	public LocalDate getDateOfVisit() {
		return dateOfVisit;
	}

	public void setDateOfVisit(LocalDate dateOfVisit) {
		this.dateOfVisit = dateOfVisit;
	}

	public String getTimeOfVisit() {
		return timeOfVisit;
	}

	public void setTimeOfVisit(String timeOfVisit) {
		this.timeOfVisit = timeOfVisit;
	}

	public String getNumberOfVisitorsInOrder() {
		return numberOfVisitorsInOrder;
	}

	public void setNumberOfVisitorsInOrder(String numberOfVisitorsInOrder) {
		this.numberOfVisitorsInOrder = numberOfVisitorsInOrder;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	public String isConfirmed() {
		return Confirmation;
	}

	public void setConfirmed(String isConfirmed) {
		this.Confirmation = isConfirmed;
	}

	public String isPaid() {
		return Paid;
	}

	public void setPaid(String isPayed) {
		this.Paid = isPayed;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	@Override
	public String toString() {
		return "OrderInTable [orderNumber=" + orderNumber + ", ordererId=" + ordererId + ", parkNameInOrder="
				+ parkNameInOrder + ", dateOfVisit=" + dateOfVisit + ", timeOfVisit=" + timeOfVisit
				+ ", numberOfVisitorsInOrder=" + numberOfVisitorsInOrder + ", orderStatus=" + orderStatus
				+ ", isConfirmed=" + Confirmation + ", isPayed=" + Paid + ", price=" + price + "]";
	}
	
	
	

}//end class OrderInTable
