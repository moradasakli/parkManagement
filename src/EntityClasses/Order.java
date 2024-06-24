package EntityClasses;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;

public class Order implements Serializable {
	private static final long serialVersionUID = 1L;

	private String orderNumber;
	private String ordererId;
	private String ordererFirstName;
	private String ordererLastName;

	private String parkNameInOrder;
	private LocalDate dateOfVisit;
	private String timeOfVisit;
	private String numberOfVisitorsInOrder;
	private boolean isOrganized;
	private String mailInOrder;
	private String phoneNumberInOrder;

	private boolean orderStatus;
	private String arrivalTime;
	private double durationOfVisit;

	private boolean isConfirmed;
	private boolean isPreOrdered;
	private boolean isPayed;
	private double price;

	// ----------------------------
	// majd
	private int park_num;
	private String durationOfVisit2;
	private String exitTime;
	private int numberOfVisitors;

	public Order() {
	}

	public Order(String orderNumber, String ordererId, String ordererFirstName, String ordererLastName,
			String parkNameInOrder, LocalDate dateOfVisit, String timeOfVisit, String numberOfVisitorsInOrder,
			boolean isOrganized, String mailInOrder, String phoneNumberInOrder, boolean orderStatus,
			boolean isPreOrdered) {
		super();
		this.orderNumber = orderNumber;
		this.ordererId = ordererId;
		this.ordererFirstName = ordererFirstName;
		this.ordererLastName = ordererLastName;
		this.parkNameInOrder = parkNameInOrder;
		this.dateOfVisit = dateOfVisit;
		this.timeOfVisit = timeOfVisit;
		this.numberOfVisitorsInOrder = numberOfVisitorsInOrder;
		this.isOrganized = isOrganized;
		this.mailInOrder = mailInOrder;
		this.phoneNumberInOrder = phoneNumberInOrder;
		this.orderStatus = orderStatus;
		this.isPreOrdered = isPreOrdered;

		this.durationOfVisit = 0;
		this.isConfirmed = false;
		this.isPayed = false;
		this.price = this.calculateBill(false);

	}

	public Order(String ordererId, String ordererFirstName, String ordererLastName, String parkNameInOrder,
			LocalDate dateOfVisit, String timeOfVisit, String numberOfVisitorsInOrder, boolean isOrganized,
			String mailInOrder, String phoneNumberInOrder, boolean isPreOrdered) {
		this.ordererId = ordererId;
		this.ordererFirstName = ordererFirstName;
		this.ordererLastName = ordererLastName;
		this.parkNameInOrder = parkNameInOrder;
		this.dateOfVisit = dateOfVisit;
		this.timeOfVisit = timeOfVisit;
		this.numberOfVisitorsInOrder = numberOfVisitorsInOrder;
		this.isOrganized = isOrganized;
		this.mailInOrder = mailInOrder;
		this.phoneNumberInOrder = phoneNumberInOrder;

		this.isPreOrdered = isPreOrdered;

		this.durationOfVisit = 0;
		this.isConfirmed = false;
		this.isPayed = false;
		this.price = this.calculateBill(false);

	}

	public Order(Order o1) {
		super();
		this.orderNumber = o1.orderNumber;
		this.ordererId = o1.ordererId;
		this.ordererFirstName = o1.ordererFirstName;
		this.ordererLastName = o1.ordererLastName;

		this.parkNameInOrder = o1.parkNameInOrder;
		this.dateOfVisit = o1.dateOfVisit;
		this.timeOfVisit = o1.timeOfVisit;
		this.numberOfVisitorsInOrder = o1.numberOfVisitorsInOrder;
		this.isOrganized = o1.isOrganized;
		this.mailInOrder = o1.mailInOrder;
		this.phoneNumberInOrder = o1.phoneNumberInOrder;

		this.orderStatus = o1.orderStatus;
		this.arrivalTime = o1.arrivalTime;
		this.durationOfVisit = o1.durationOfVisit;

		this.isPreOrdered = o1.isPreOrdered;
		this.isConfirmed = o1.isConfirmed;
		this.isPayed = o1.isPayed;
		this.price = o1.price;
	}

	public String getDurationOfVisit2() {
		return durationOfVisit2;
	}

	public void setDurationOfVisit2(String durationOfVisit2) {
		this.durationOfVisit2 = durationOfVisit2;
	}

	public String getExitTime() {
		return exitTime;
	}

	public void setExitTime(String exitTime) {
		this.exitTime = exitTime;
	}

	public int getPark_num() {
		return park_num;
	}

	public void setPark_num(int park_num) {
		this.park_num = park_num;
	}

// majd
	public Order(String orderNumber, int numberOfVisitors, boolean isOrganized, String arrivalTime) {
		this.orderNumber = orderNumber;
		this.numberOfVisitors = numberOfVisitors;
		this.isOrganized = isOrganized;
		this.arrivalTime = arrivalTime; // String
	}

// majd
	public Order(String orderNumber, int numberOfVisitors, boolean isOrganized, double price) {
		this.orderNumber = orderNumber;
		this.numberOfVisitors = numberOfVisitors;
		this.isOrganized = isOrganized;
		this.price = (int) price; // double
	}

	public Order(int order_number, int orderer_id, int number_of_visitors, boolean is_organized, String mail,
			String phone, boolean order_status, String arrival_time, int duration, boolean is_confirmed,
			boolean is_pre_ordered, boolean is_payed, double price, int park_num) {
		this.orderNumber = "" + order_number;
		this.ordererId = "" + orderer_id;
		this.numberOfVisitors = number_of_visitors;
		this.isOrganized = is_organized;
		this.mailInOrder = mail;
		this.phoneNumberInOrder = phone;
		this.orderStatus = order_status;
		this.arrivalTime = arrival_time;
		this.durationOfVisit = duration;
		this.isConfirmed = is_confirmed;
		this.isPreOrdered = is_pre_ordered;
		this.isPayed = is_payed;
		this.price = (int) price;
		this.park_num = park_num;
	}

// majd
	public Order(String orderNumber, String parkNameInOrder, String numberOfVisitorsInOrder, boolean isOrganized,
			boolean isPayed, int price) {
		this.orderNumber = orderNumber;
		this.parkNameInOrder = parkNameInOrder;
		this.numberOfVisitorsInOrder = numberOfVisitorsInOrder;
		this.isOrganized = isOrganized;
		this.isPayed = isPayed;
		this.price = price;
	}

// majd
	public Order(int park_num, String orderNumber) {
		super();
		this.orderNumber = orderNumber;
		this.park_num = park_num;
	}

	public int getNumberOfVisitors() {
		return numberOfVisitors;
	}

	public void setNumberOfVisitors(int a) {
		this.numberOfVisitors = a;
	}
	
	public String getOrdererFirstName() {
		return ordererFirstName;
	}

	public void setOrdererFirstName(String ordererFirstName) {
		this.ordererFirstName = ordererFirstName;
	}

	public String getOrdererLastName() {
		return ordererLastName;
	}

	public void setOrdererLastName(String ordererLastName) {
		this.ordererLastName = ordererLastName;
	}

	public double calculateBill(boolean isPaidInAdvance) {
		double sum = 0;

		sum += Integer.parseInt(this.numberOfVisitorsInOrder) * (Discount.PRICE_FOR_INDIVIDUAL);

		if ((this.isPreOrdered == true) && (this.isOrganized == false))
			sum = sum * Discount.PRE_ORDERED_PRIVATE_VISIT_DISCOUNT;
		else if ((this.isPreOrdered == false) && (this.isOrganized == false))
			sum = sum * Discount.CASUAL_PRIVATE_VISIT_DISCOUNT;
		else if ((this.isPreOrdered == false) && (this.isOrganized == true))
			sum = sum * Discount.CASUAL_GROUP_VISIT_DISCOUNT;
		else if ((this.isPreOrdered == true) && (this.isOrganized == true)) {
			sum -= Discount.PRICE_FOR_INDIVIDUAL;
			if (isPaidInAdvance)
				sum = sum * Discount.PRE_ORDERED_WITH_ADVANCED_PAYMENT_GROUP_VISIT_DISCOUNT;
			else
				sum = sum * Discount.PRE_ORDERED_GROUP_VISIT_DISCOUNT;
		}

		return sum;
	}// end calculateBill

	public void registerEntry(String arrivalTime) {
		this.setArrivalTime(arrivalTime);
	}// end registerEntry

	public void registerExit(String exitTime) {
		// learn about class time , and how to calculate time difference

		// double duration = exitTime - this.arrivalTime;
		// this.setDurationOfVisit(duration);
	}// end registerExit

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

	public boolean isOrganized() {
		return isOrganized;
	}

	public void setOrganized(boolean isOrganized) {
		this.isOrganized = isOrganized;
	}

	public String getMailInOrder() {
		return mailInOrder;
	}

	public void setMailInOrder(String mailInOrder) {
		this.mailInOrder = mailInOrder;
	}

	public String getPhoneNumberInOrder() {
		return phoneNumberInOrder;
	}

	public void setPhoneNumberInOrder(String phoneNumberInOrder) {
		this.phoneNumberInOrder = phoneNumberInOrder;
	}

	public boolean isOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(boolean orderStatus) {
		this.orderStatus = orderStatus;
	}

	public String getArrivalTime() {
		return arrivalTime;
	}

	public void setArrivalTime(String arrivalTime) {
		this.arrivalTime = arrivalTime;
	}

	public double getDurationOfVisit() {
		return durationOfVisit;
	}

	public void setDurationOfVisit(double durationOfVisit) {
		this.durationOfVisit = durationOfVisit;
	}

	public boolean isConfirmed() {
		return isConfirmed;
	}

	public void setConfirmed(boolean isConfirmed) {
		this.isConfirmed = isConfirmed;
	}

	public boolean isPreOrdered() {
		return isPreOrdered;
	}

	public void setPreOrdered(boolean isPreOrdered) {
		this.isPreOrdered = isPreOrdered;
	}

	public boolean isPayed() {
		return isPayed;
	}

	public void setPayed(boolean isPayed) {
		this.isPayed = isPayed;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	@Override
	public String toString() {
		return "Order [orderNumber=" + orderNumber + ", ordererId=" + ordererId + ", ordererFirstName="
				+ ordererFirstName + ", ordererLastName=" + ordererLastName + ", parkNameInOrder=" + parkNameInOrder
				+ ", dateOfVisit=" + dateOfVisit + ", timeOfVisit=" + timeOfVisit + ", numberOfVisitorsInOrder="
				+ numberOfVisitorsInOrder + ", isOrganized=" + isOrganized + ", mailInOrder=" + mailInOrder
				+ ", phoneNumberInOrder=" + phoneNumberInOrder + ", orderStatus=" + orderStatus + ", arrivalTime="
				+ arrivalTime + ", durationOfVisit=" + durationOfVisit + ", isConfirmed=" + isConfirmed
				+ ", isPreOrdered=" + isPreOrdered + ", isPayed=" + isPayed + ", price=" + price + "]";
	}

}// end class Order

//last update : 31/3/2024 , 21:38
