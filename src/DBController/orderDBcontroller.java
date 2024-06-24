package DBController;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import javax.swing.JOptionPane;

import EntityClasses.ClientsTable;
import EntityClasses.Message;
import EntityClasses.MessageType;
import EntityClasses.Order;
import EntityClasses.OrderInTable;
import EntityClasses.PairOfFullOrders;
import EntityClasses.PairOfOrders;
import EntityClasses.Park;
import EntityClasses.Tourist;
import Server.EchoServer;
import client.ClientUI;
import javafx.animation.PauseTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.util.Duration;

public class orderDBcontroller {

	/**
	 * @param o
	 * @return message , that indicates if we added the order to the db or not , by
	 *         checking available spaces
	 */
	public static Message touristMakeNewOrder(Order o) {
		System.out.println("touristMakeNewOrder info : " + o.toString());
		Message result = addOrder(o, MessageType.ExistingTouristMakeNewOrder);
		return result;
	}
	
	public static void makeOrderValidFromWaitingList(Order o)
	{
		
		Message m = addOrder(o,MessageType.MoveOrderFromWaitingListToValid);
		deleteOrderFromDB(o, "waitinglist");
	}
	
	public static void deleteFromWaitingList(Order o)
	{
		
		deleteOrderFromDB(o, "waitinglist");
	}

	/**
	 * @param o update the order after a payment operation
	 * 
	 * @return appropriate message about the status of the operation
	 */
	public static Message updateOrderInfoAfterPayment(Order o) {
		try {
			int parkNumber = getParkNumber(o.getParkNameInOrder());
//			PreparedStatement ps1 = EchoServer.mySql.connection.prepareStatement(
//					"UPDATE `ordersforpark" + parkNumber + "` SET `is_payed` = ?, `price` = ?, `is_confirmed` = ? WHERE `order_num` = ?");

			PreparedStatement ps1 = EchoServer.mySql.connection.prepareStatement("UPDATE `ordersforpark" + parkNumber
					+ "` SET `is_payed` = ?, `price` = ?, `is_confirmed` = ? WHERE `order_num` = ?");
			ps1.setBoolean(1, o.isPayed());
			ps1.setDouble(2, o.getPrice());
			ps1.setBoolean(3, o.isConfirmed());
			ps1.setInt(4, Integer.parseInt(o.getOrderNumber()));
			int rs1 = ps1.executeUpdate();
		} catch (SQLException e) {
			System.err.println("Error updateOrderInfoAfterPayment : " + e.getMessage());
		}

		Message result = new Message(MessageType.UpdateOrderAfterPaymentDone, o);
		return result;

	}

	/**
	 * @param pair of orders , before and after edit handles editing an order
	 * @return a message indicating whether
	 */
	public static Message editingExistingOrder(PairOfOrders pair) {

		System.out.println("old order is : " + pair.getFrom().toString());
		System.out.println("new order is : " + pair.getTo().toString());

		Order fullOldOrder = getOrderInfoFromDB(pair.getFrom());
		Order fullNewOrder = new Order(fullOldOrder);
		fullNewOrder.setParkNameInOrder(pair.getTo().getParkNameInOrder());
		fullNewOrder.setDateOfVisit(pair.getTo().getDateOfVisit());
		fullNewOrder.setTimeOfVisit(pair.getTo().getTimeOfVisit());
		fullNewOrder.setNumberOfVisitorsInOrder(pair.getTo().getNumberOfVisitorsInOrder());

		// set status ,confirmed ,paid and price, for new order
		fullNewOrder.setOrderStatus(false);
		fullNewOrder.setConfirmed(false);
		fullNewOrder.setPayed(false);
		fullNewOrder.setPrice(fullNewOrder.calculateBill(false));

		PairOfFullOrders fullPair = new PairOfFullOrders(fullOldOrder, fullNewOrder);

		// delete old order from db
		if (pair.getFrom().getOrderStatus().equals("Valid"))

			deleteOrderFromDB(fullOldOrder, "orders");
		else
			deleteOrderFromDB(fullOldOrder, "waitinglist");

		Message backToClient = null;

		Message result = addOrder(fullNewOrder, MessageType.ExistingTouristEditingOrder);

		if (result.getMessageType() == MessageType.ExistingTouristEditingOrderSuccess) {
			backToClient = new Message(MessageType.ExistingTouristEditingOrderSuccess, fullPair);
			return backToClient;
		}

		else {

			// resolve old status
			if (pair.getFrom().getOrderStatus().equals("Valid")) {

				// EDIT LATER
				result = addOrder(fullOldOrder, MessageType.TEMP_VALUE_FOR_TESTING_EDIT);
				// result = addOrder(fullOldOrder, MessageType.ExistingTouristEditingOrder);

			} else {
				enterWaitingList(fullOldOrder);
			}

			backToClient = new Message(MessageType.ExistingTouristEditOrder3Options, fullPair);
			return backToClient;
		}

//		Message result = addOrder(fullNewOrder, MessageType.ExistingTouristEditingOrder);
//		if (result.getMessageType() == MessageType.ExistingTouristEditingOrderSuccess)
//			return result;
//		else {
//			
//			//resolve old status
//			if (pair.getFrom().getOrderStatus().equals("Valid")) {
//				result = addOrder(fullOldOrder, MessageType.ExistingTouristEditingOrder);
//
//			} else {
//				enterWaitingList(fullOldOrder);
//			}
//			Message result2 = new Message(MessageType.ExistingTouristEditOrder3Options, pair);
//			return result2;
//		}
	}// end editingExistingOrder

	public static Message updateWitingListDueToEdit(Order oldOrder) {
		int parkNumber = getParkNumber(oldOrder.getParkNameInOrder());

		Park park = ParkDBcontroller.getParkInfo(oldOrder.getParkNameInOrder());

		String timeString = oldOrder.getTimeOfVisit();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
		LocalTime time = LocalTime.parse(timeString, formatter);

		// Add an hour
		LocalTime newTime1 = time.plusHours(park.getDefaultDurationForVisit());
		LocalTime newTime2 = time.minusHours(park.getDefaultDurationForVisit() - 1);

		// Format the new time back into a string
		String timeAfter = newTime1.format(formatter);
		String timeBefore = newTime2.format(formatter);

		System.out.println("Original time: " + timeString);
		System.out.println("time before: " + timeBefore);
		System.out.println("time after: " + timeAfter);

		ArrayList<Order> listOfOrdersToNotify = new ArrayList();

		try {
			String sql2 = "SELECT * FROM `waitinglistforpark" + parkNumber + "` WHERE DATE(date_of_visit) = '"
					+ oldOrder.getDateOfVisit() + "' AND TIME(STR_TO_DATE(time_of_visit, '%H:%i')) >= '" + timeBefore
					+ "' AND TIME(STR_TO_DATE(time_of_visit, '%H:%i')) < '" + timeAfter + "'";

			PreparedStatement statement = EchoServer.mySql.connection.prepareStatement(sql2);

			ResultSet rs = statement.executeQuery();

			while (rs.next()) {
				Order o = new Order();

//				java.sql.Date orderDate = rs1.getDate("date_of_visit");
//				LocalDate localorderDate = orderDate.toLocalDate();
//				order.setDateOfVisit(localorderDate.plusDays(1));

				o.setOrderNumber(rs.getString("order_num"));
				o.setOrdererId(rs.getString("orderer_id"));
				// o.setDateOfVisit(rs.getDate("date_of_visit").toLocalDate());
				java.sql.Date orderDate = rs.getDate("date_of_visit");
				LocalDate localorderDate = orderDate.toLocalDate();
				o.setDateOfVisit(localorderDate.plusDays(1));
				o.setTimeOfVisit(rs.getString("time_of_visit"));
				o.setNumberOfVisitorsInOrder(rs.getString("number_of_visitors"));
				o.setOrganized(rs.getBoolean("is_organized"));
				o.setMailInOrder(rs.getString("mail"));
				o.setPhoneNumberInOrder(rs.getString("phone"));
				o.setOrderStatus(rs.getBoolean("order_status"));
				o.setArrivalTime(rs.getString("arrival_time"));
				o.setDurationOfVisit(rs.getDouble("duration"));
				o.setConfirmed(rs.getBoolean("is_confirmed"));
				o.setPreOrdered(rs.getBoolean("is_pre_ordered"));
				o.setPayed(rs.getBoolean("is_payed"));
				o.setPrice(rs.getDouble("price"));
				
				Tourist t = TouristsDBcontroller.CreateTourist(o.getOrdererId());
				o.setOrdererFirstName(t.getTouristFirstName());
				o.setOrdererLastName(t.getTouristLastName());
				o.setParkNameInOrder(oldOrder.getParkNameInOrder());

				listOfOrdersToNotify.add(o);

				// Add 'o' to a list or process it as needed
			}

			System.out.println("listOfOrdersToNotify size is : " + listOfOrdersToNotify.size());

		} catch (SQLException e) {
			e.printStackTrace();
		}
		Collections.sort(listOfOrdersToNotify, Comparator.comparing(Order::getOrderNumber));


		return new Message(MessageType.HandleWaitingListAfterEditingOrder, listOfOrdersToNotify);

	}// end updateWitingListDueToEdit

	public static Message prepareOrderForPayment(Order o) {
		Message result = new Message(MessageType.EdittedPayment, o);
		return result;
	}

	public static void deleteOrderFromDB(Order o, String table) {
		String fullTableName = table + "forpark" + getParkNumber(o.getParkNameInOrder());
		System.out.println("delete table name test : " + fullTableName);

		// Directly include the table name in the SQL statement
		String sql = "DELETE FROM " + fullTableName + " WHERE order_num = ?";
		System.out.println("full delete sql is : " + sql);

		try {
			// Prepare the SQL statement
			try (PreparedStatement statement = EchoServer.mySql.connection.prepareStatement(sql)) {
				// Set the order number as a parameter in the prepared statement
				statement.setString(1, o.getOrderNumber());
				int rowsAffected = statement.executeUpdate();
				if (rowsAffected > 0) {
					System.out.println("Order with order number " + o.getOrderNumber() + " deleted successfully.");
				} else {
					System.out.println("Order with order number " + o.getOrderNumber() + " not found.");
				}
			}
		} catch (SQLException e) {
			System.err.println("Error deleting order: " + e.getMessage());
		}
	}
	// Execute the DELETE statement

	public static Message checkOrderFields(Order o) {

		if (TouristsDBcontroller.isTouristExist(o.getOrdererId()))
			return new Message(MessageType.GuestAlreadyRegistered);

		boolean isTravelGuide = isRegisteredAsTravelGuide(o.getOrdererId());
		if (o.isOrganized() && (!isTravelGuide))
			return new Message(MessageType.GuestNotRegisteredAsTravelGuide);

		Tourist tourist = new Tourist(o.getOrdererId(), o.getOrdererFirstName(), o.getOrdererLastName(),
				o.getMailInOrder(), o.getPhoneNumberInOrder(), isTravelGuide);

		Message result = addOrder(o, MessageType.GuestTouristNewOrder);

		if (result.getMessageType() == MessageType.GuestOrderSuccess)
			TouristsDBcontroller.addTourist(tourist);

		return result;

	}// end checkOrderFields

	public static String getOrderNumber() {
		String order_num = "";

		try (
				// Establishing a connection to the database

				// Creating a statement for executing SQL queries
				Statement statement = EchoServer.mySql.connection.createStatement()) {
			// SQL query to fetch orders_counter value for id = 1
			String selectQuery = "SELECT orders_counter FROM generalinfoandcounters WHERE id = 1";

			// Execute the select query and get the result set
			ResultSet resultSet = statement.executeQuery(selectQuery);

			if (resultSet.next()) {
				// Get the orders_counter value from the result set
				int ordersCounterValue = resultSet.getInt("orders_counter");

				// Store the orders_counter value in a String variable
				order_num = String.valueOf(ordersCounterValue);

				// Print the orders_counter value
				System.out.println("orders_counter value: " + order_num);
			} else {
				System.out.println("No record found for id = 1");
			}

			String updateQuery = "UPDATE generalinfoandcounters SET orders_counter = orders_counter + 1 WHERE id = 1";

			// Executing the update query
			int rowsAffected = statement.executeUpdate(updateQuery);

			// Checking the number of rows affected by the update
			if (rowsAffected > 0) {
				System.out.println("Order counter updated successfully.");
			} else {
				System.out.println("No rows were updated.");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return order_num;

	}// end getOrderNumber

	public static int getParkNumber(String parkName) {
		int parkNumber = 0;
		switch (parkName) {
		case "Park A":
			parkNumber = 1;
			break;
		case "Park B":
			parkNumber = 2;
			break;
		default:
			parkNumber = 3;
			break;
		}
		return parkNumber;
	}

	public static Message addOrder(Order o, MessageType msg) {
		int parkNumber = getParkNumber(o.getParkNameInOrder());
		String orderNumber = null;
		// if order num is null that indicates that we are adding a new order,
		// else it's a tourist is editting
		if (o.getOrderNumber() == null) {
			orderNumber = getOrderNumber();
			o.setOrderNumber(orderNumber);
		}

		if (checkForAvailableSpotsNewOrder(o) || msg == MessageType.TEMP_VALUE_FOR_TESTING_EDIT) {

			o.setOrderStatus(true);
			System.out.println("new order info 3: " + o.toString());

			try {
				// Prepare the SQL statement with placeholders for values
				String sql = "INSERT INTO `ordersforpark" + parkNumber
						+ "` (order_num, orderer_id, date_of_visit, time_of_visit, "
						+ "number_of_visitors, is_organized, mail, phone, order_status, arrival_time, duration, "
						+ "is_confirmed, is_pre_ordered, is_payed, price) "
						+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

				// Create a PreparedStatement with the SQL statement
				try (PreparedStatement pstmt = EchoServer.mySql.connection.prepareStatement(sql)) {
					// Set values for the placeholders in the SQL statement using data from the
					// Order object
					pstmt.setString(1, o.getOrderNumber());
					pstmt.setString(2, o.getOrdererId());
					pstmt.setDate(3, java.sql.Date.valueOf(o.getDateOfVisit()));
					pstmt.setString(4, o.getTimeOfVisit());
					pstmt.setString(5, o.getNumberOfVisitorsInOrder());
					pstmt.setBoolean(6, o.isOrganized());
					pstmt.setString(7, o.getMailInOrder());
					pstmt.setString(8, o.getPhoneNumberInOrder());
					pstmt.setBoolean(9, o.isOrderStatus());
					pstmt.setString(10, o.getArrivalTime());
					pstmt.setDouble(11, o.getDurationOfVisit());
					pstmt.setBoolean(12, o.isConfirmed());
					pstmt.setBoolean(13, o.isPreOrdered());
					pstmt.setBoolean(14, o.isPayed());
					pstmt.setDouble(15, o.getPrice());

					// Execute the INSERT statement
					int rowsAffected = pstmt.executeUpdate();
					if (rowsAffected > 0) {
						System.out.println("Order added successfully to the database.");
					} else {
						System.out.println("Failed to add order to the database.");
					}
				}
			} catch (SQLException e) {
				System.err.println("Error while connecting to the database or executing SQL: " + e.getMessage());
			}
			// Order order = new Order(o);
			if (msg == MessageType.GuestTouristNewOrder)
				return new Message(MessageType.GuestOrderSuccess, o);
			if (msg == MessageType.ExistingTouristEditingOrder)
				return new Message(MessageType.ExistingTouristEditingOrderSuccess, o);
			if (msg == MessageType.ExistingTouristMakeNewOrder)
				return new Message(MessageType.ExistingTouristNewOrderSuccess, o);
			if (msg == MessageType.MoveOrderFromWaitingListToValid)
				return new Message(MessageType.MoveOrderFromWaitingListToValid, o);

		}
		// Order order = new Order(o);
		if (msg == MessageType.GuestTouristNewOrder)
			return new Message(MessageType.GuestGoingForAlternativeOptions, o);
		if (msg == MessageType.ExistingTouristEditingOrder)
			return new Message(MessageType.ExistingTouristEditingOrderFailure, o);
		if (msg == MessageType.ExistingTouristMakeNewOrder)
			return new Message(MessageType.ExistingTouristGoingForAlternativeOptions, o);

		return null;
	}

	public static void enterWaitingList(Order o) {
		int parkNumber = getParkNumber(o.getParkNameInOrder());
		try {
			// Prepare the SQL statement with placeholders for values
			String sql = "INSERT INTO `waitinglistforpark" + parkNumber
					+ "` (order_num, orderer_id, date_of_visit, time_of_visit, "
					+ "number_of_visitors, is_organized, mail, phone, order_status, arrival_time, duration, "
					+ "is_confirmed, is_pre_ordered, is_payed, price) "
					+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

			// Create a PreparedStatement with the SQL statement
			try (PreparedStatement pstmt = EchoServer.mySql.connection.prepareStatement(sql)) {
				// Set values for the placeholders in the SQL statement using data from the
				// Order object
				pstmt.setString(1, o.getOrderNumber());
				pstmt.setString(2, o.getOrdererId());
				pstmt.setDate(3, java.sql.Date.valueOf(o.getDateOfVisit()));
				pstmt.setString(4, o.getTimeOfVisit());
				pstmt.setString(5, o.getNumberOfVisitorsInOrder());
				pstmt.setBoolean(6, o.isOrganized());
				pstmt.setString(7, o.getMailInOrder());
				pstmt.setString(8, o.getPhoneNumberInOrder());
				pstmt.setBoolean(9, o.isOrderStatus());
				pstmt.setString(10, o.getArrivalTime());
				pstmt.setDouble(11, o.getDurationOfVisit());
				pstmt.setBoolean(12, o.isConfirmed());
				pstmt.setBoolean(13, o.isPreOrdered());
				pstmt.setBoolean(14, o.isPayed());
				pstmt.setDouble(15, o.getPrice());

				// Execute the INSERT statement
				int rowsAffected = pstmt.executeUpdate();
				if (rowsAffected > 0) {
					System.out.println("Order added successfully to the waiting list database.");
				} else {
					System.out.println("Failed to add order to the waiting list database.");
				}
			}
		} catch (SQLException e) {
			System.err.println("Error while connecting to the database or executing SQL: " + e.getMessage());
		}

		boolean isTravelGuide = isRegisteredAsTravelGuide(o.getOrdererId());
		Tourist tourist = new Tourist(o.getOrdererId(), o.getOrdererFirstName(), o.getOrdererLastName(),
				o.getMailInOrder(), o.getPhoneNumberInOrder(), isTravelGuide);
		TouristsDBcontroller.addTourist(tourist);

	}

	public static Message checkForSpotsForOrder(Order o) {
		boolean res = checkForAvailableSpotsNewOrder(o);
		return new Message(MessageType.CheckSpotsForOrder,res);
	}
	
//	public static boolean checkForAvailableSpotsNewOrder(Order o) {
//		return true;
//	}
	
	public static boolean checkForAvailableSpotsNewOrder(Order o) {
		int parkNumber = getParkNumber(o.getParkNameInOrder());

		Park park = ParkDBcontroller.getParkInfo(o.getParkNameInOrder());

		String timeString = o.getTimeOfVisit();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
		LocalTime time = LocalTime.parse(timeString, formatter);

		// Add an hour
		LocalTime newTime1 = time.plusHours(park.getDefaultDurationForVisit());
		LocalTime newTime2 = time.minusHours(park.getDefaultDurationForVisit() - 1);

		// Format the new time back into a string
		String timeAfter = newTime1.format(formatter);
		String timeBefore = newTime2.format(formatter);

//		System.out.println("Original time: " + timeString);
//		System.out.println("time before: " + timeBefore);
//		System.out.println("time after: " + timeAfter);

		int counterVisitors = 0;

		try {

//			String sql = " SELECT * FROM ordersforpark" + parkNumber + " WHERE DATE(date_of_visit) = ? "
//					+ " AND TIME(STR_TO_DATE(time_of_visit, '%H:%i')) >= ? "
//					+ " AND TIME(STR_TO_DATE(time_of_visit, '%H:%i')) < ? " + "AND order_status = 1 ";

			String sql2 = "SELECT * FROM `ordersforpark" + parkNumber + "` WHERE DATE(date_of_visit) = '"
					+ o.getDateOfVisit() + "' AND TIME(STR_TO_DATE(time_of_visit, '%H:%i')) >= '" + timeBefore
					+ "' AND TIME(STR_TO_DATE(time_of_visit, '%H:%i')) < '" + timeAfter + "' AND order_status = 1";

			//System.out.println(sql2);

			PreparedStatement statement = EchoServer.mySql.connection.prepareStatement(sql2);

			ResultSet resultSet = statement.executeQuery();

//			System.out.println("extracting orders from park " + parkNumber + " at date: " + o.getDateOfVisit()
//					+ " between " + timeBefore + " and " + timeAfter);

			while (resultSet.next()) {
				//System.out.println("number of visitors for order in table is: " + resultSet.getInt("number_of_visitors"));
				int numberOfVisitors = resultSet.getInt("number_of_visitors");
				counterVisitors += numberOfVisitors;
			}

			// Process the result set
		} catch (SQLException e) {
			e.printStackTrace();
		}

		//System.out.println("Total number of visitors: " + counterVisitors);
		int result = park.getMaxCapacity() - park.getNumberOfReservedSpaces() - counterVisitors;
		//System.out.println("max-reserved-sumOfvisitors = " + result);
		int NOV = Integer.parseInt(o.getNumberOfVisitorsInOrder());
		//System.out.println("number of visitors in order  = " + NOV);
		if (result >= NOV)
			return true;
		return false;
	}// end checkForAvailableSpotsNewOrder

//	public static boolean checkForAvailableSpotsCasualVisit(Order o) {
//		return true;
//	}// end checkForAvailableSpotsCasualVisit

	public static Message getListOfAvailableDates(Order originalOrder) {
		System.out.println("in function getListOfAvailableDates :");
		System.out.println(originalOrder.toString());

		ArrayList<Order> listOfAvailableDates = new ArrayList();

		// change back to one H if not solved
		String timeString = originalOrder.getTimeOfVisit();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
		LocalTime time = LocalTime.parse(timeString, formatter);

		// Add an hour
		LocalTime newTime1 = time.plusHours(1);
		LocalTime newTime2 = time.minusHours(1);

		// Format the new time back into a string
		String timeAfter = newTime1.format(formatter);
		String timeBefore = newTime2.format(formatter);

//		System.out.println("Original time: " + timeString);
//		System.out.println("time before: " + timeBefore);
//		System.out.println("time after: " + timeAfter);

		// change 2 to 7
		for (int i = 0; i <= 7; i++) {
			Order orderHourBefore = new Order(originalOrder);
			Order orderHourAfter = new Order(originalOrder);
			Order orderSameHour = new Order(originalOrder);

			LocalDate newDate = originalOrder.getDateOfVisit().plusDays(i);

			if (i == 0) {
				if (!timeBefore.equals("07:00")) {
					orderHourBefore.setTimeOfVisit(timeBefore);
					if (checkForAvailableSpotsNewOrder(orderHourBefore))
						listOfAvailableDates.add(orderHourBefore);
				}
				if (!timeAfter.equals("17:00")) {
					orderHourAfter.setTimeOfVisit(timeAfter);
					if (checkForAvailableSpotsNewOrder(orderHourAfter))
						listOfAvailableDates.add(orderHourAfter);
				}

			} else {
				orderHourBefore.setDateOfVisit(newDate);
				orderHourAfter.setDateOfVisit(newDate);
				orderSameHour.setDateOfVisit(newDate);

				if (!timeBefore.equals("07:00")) {
					orderHourBefore.setTimeOfVisit(timeBefore);
					if (checkForAvailableSpotsNewOrder(orderHourBefore))
						listOfAvailableDates.add(orderHourBefore);
				}

				if (checkForAvailableSpotsNewOrder(orderSameHour))
					listOfAvailableDates.add(orderSameHour);

				if (!timeAfter.equals("17:00")) {
					orderHourAfter.setTimeOfVisit(timeAfter);
					if (checkForAvailableSpotsNewOrder(orderHourAfter))
						listOfAvailableDates.add(orderHourAfter);
				}

			}

		} // end for

		System.out.println("LIST OF DATES SIZE = " + listOfAvailableDates.size());

		Message result;
		if (listOfAvailableDates.size() == 0)
			result = new Message(MessageType.NoAvailableDates);
		else
			result = new Message(MessageType.AvailableDatesSuccess, listOfAvailableDates);

		return result;
	}// end getListOfAvailableDates

	public static boolean isRegisteredAsTravelGuide(String id) {
		try {
			PreparedStatement ps1 = EchoServer.mySql.connection
					.prepareStatement("SELECT `id` FROM travelguides WHERE `id` = ? ");
			ps1.setString(1, id);
			ResultSet rs = ps1.executeQuery();

			if (rs.next()) {
				System.out.println("ID found in travelguide table");
				return true;
			} else {
				System.out.println("ID NOT found in travelguide table");
				return false;
			}
		} catch (SQLException var6) {
			var6.printStackTrace();
			return false;
		}
	}// end isRegisteredAsTravelGuide

	public static Message getListOfOrdersForTourist(String touristID) {

		System.out.println(touristID);

		ArrayList<OrderInTable> listOfOrders = new ArrayList();
		try {

			// System.out.println("EXTRACTING ORDERS FROM DB:");
			// extract info from orders tables
			for (int i = 1; i <= 3; i++) {
				String parkName = "";
				PreparedStatement ps1 = EchoServer.mySql.connection
						.prepareStatement("select * from `ordersforpark" + i + "` WHERE `orderer_id` = ? ");
				ps1.setInt(1, Integer.parseInt(touristID));
				ResultSet rs1 = ps1.executeQuery();
				PreparedStatement ps2 = EchoServer.mySql.connection
						.prepareStatement("select `park_name` from `parks` WHERE `park_number` = ? ");
				ps2.setInt(1, i);
				ResultSet rs2 = ps2.executeQuery();

				if (rs2.next()) {
					parkName = rs2.getString("park_name");
				}
				// System.out.println("park name : " + parkName);
				while (rs1.next()) {

					OrderInTable order = new OrderInTable();

					java.sql.Date orderDate = rs1.getDate("date_of_visit");
					LocalDate localorderDate = orderDate.toLocalDate();

					order.setDateOfVisit(localorderDate.plusDays(1));
					order.setOrderNumber(rs1.getString("order_num"));
					order.setOrdererId(rs1.getString("orderer_id"));
					order.setParkNameInOrder(parkName);
					// check if works correctly

					order.setTimeOfVisit(rs1.getString("time_of_visit"));
					order.setNumberOfVisitorsInOrder(rs1.getString("number_of_visitors"));

					Boolean statusResult = rs1.getBoolean("order_status");
					if (statusResult)
						order.setOrderStatus("Valid");
					else
						order.setOrderStatus("Cancelled");

					Boolean isConfirmedResult = rs1.getBoolean("is_confirmed");
					Boolean isPayed = rs1.getBoolean("is_payed");
					// CHANGE LATER TO IMAGES
					if (isConfirmedResult)
						order.setConfirmed("Yes");
					else
						order.setConfirmed("No");

					if (isPayed)
						order.setPaid("Yes");
					else
						order.setPaid("No");

					order.setPrice(rs1.getDouble("price"));
					listOfOrders.add(order);
				}
			} // end extract info from orders tables

			// end extract info from waiting list tables
			for (int i = 1; i <= 3; i++) {
				String parkName = "";
				PreparedStatement ps1 = EchoServer.mySql.connection
						.prepareStatement("select * from `waitinglistforpark" + i + "` WHERE `orderer_id` = ? ");
				ps1.setString(1, touristID);
				ResultSet rs1 = ps1.executeQuery();
				PreparedStatement ps2 = EchoServer.mySql.connection
						.prepareStatement("select `park_name` from `parks` WHERE `park_number` = ? ");
				ps2.setInt(1, i);
				ResultSet rs2 = ps2.executeQuery();

				if (rs2.next()) {
					parkName = rs2.getString("park_name");
				}

				while (rs1.next()) {
					OrderInTable order = new OrderInTable();
					order.setOrderNumber(rs1.getString("order_num"));
					order.setOrdererId(rs1.getString("orderer_id"));
					order.setParkNameInOrder(parkName);
					// check if works correctly
					java.sql.Date orderDate = rs1.getDate("date_of_visit");
					LocalDate localorderDate = orderDate.toLocalDate();
					order.setDateOfVisit(localorderDate.plusDays(1));
					order.setTimeOfVisit(rs1.getString("time_of_visit"));
					order.setNumberOfVisitorsInOrder(rs1.getString("number_of_visitors"));
					// order.setOrderStatus(rs1.getBoolean("order_status"));
					order.setOrderStatus("Waiting List");
					Boolean isConfirmedResult = rs1.getBoolean("is_confirmed");
					Boolean isPayed = rs1.getBoolean("is_payed");
					// CHANGE LATER TO IMAGES
					if (isConfirmedResult)
						order.setConfirmed("Yes");
					else
						order.setConfirmed("No");

					if (isPayed)
						order.setPaid("Yes");
					else
						order.setPaid("No");
					order.setPrice(rs1.getDouble("price"));
					listOfOrders.add(order);
				}

			} // end extract info from waiting list tables

		} catch (Exception e) {

		}

//		System.out.println("PRINTING ORDERS , array lenght is:" + listOfOrders.size());
//
//		for (int i = 0; i < listOfOrders.size(); i++) {
//			System.out.println(listOfOrders.get(i).toString());
//		}

		Message msg = new Message(MessageType.ExistingTouristListOfOrders, listOfOrders);
		return msg;
	}// end getListOfOrdersForTourist

	public static Message extractFullOrderInfo(OrderInTable o) {
		Order orderFull = getOrderInfoFromDB(o);
		Message result = new Message(MessageType.ExtractFullOrderInfo, orderFull);
		return result;
	}

	public static Order getOrderInfoFromDB(OrderInTable o) {

		Order order = null;
		try {

			System.out.println("EXTRACTING ORDER INFO TO EDIT FROM DB:");
			// extract info from orders tables
			String parkName = o.getParkNameInOrder();
			String orderStatus = o.getOrderStatus();
			int parkNumber = getParkNumber(parkName);
			PreparedStatement ps;
			ResultSet rs;
			if (orderStatus.equals("Valid")) {
				ps = EchoServer.mySql.connection
						.prepareStatement("select * from `ordersforpark" + parkNumber + "` WHERE `order_num` = ? ");
				ps.setInt(1, Integer.parseInt(o.getOrderNumber()));
				rs = ps.executeQuery();
			} else {
				ps = EchoServer.mySql.connection.prepareStatement(
						"select * from `waitinglistforpark" + parkNumber + "` WHERE `order_num` = ? ");
				ps.setInt(1, Integer.parseInt(o.getOrderNumber()));
				rs = ps.executeQuery();
			}

			while (rs.next()) {

				order = new Order();
				order.setParkNameInOrder(parkName);
				order.setOrderNumber(rs.getString("order_num"));
				order.setOrdererId(rs.getString("orderer_id"));
				java.sql.Date orderDate = rs.getDate("date_of_visit");
				LocalDate localorderDate = orderDate.toLocalDate();
				order.setDateOfVisit(localorderDate.plusDays(1));
				order.setTimeOfVisit(rs.getString("time_of_visit"));
				order.setNumberOfVisitorsInOrder(rs.getString("number_of_visitors"));
				order.setOrganized(rs.getBoolean("is_organized"));
				order.setMailInOrder(rs.getString("mail"));
				order.setPhoneNumberInOrder(rs.getString("phone"));
				order.setOrderStatus(rs.getBoolean("order_status"));
				order.setConfirmed(rs.getBoolean("is_confirmed"));
				order.setPreOrdered(rs.getBoolean("is_pre_ordered"));
				order.setPayed(rs.getBoolean("is_payed"));
				order.setPrice((rs.getDouble("price")));
				order.setArrivalTime(rs.getString("arrival_time"));
				order.setDurationOfVisit((rs.getDouble("duration")));
			}

			System.out.println("FULL ORDER INFO IS : " + order.toString());

		} catch (Exception e) {

		}
		return order;

	}// end getOrderInfoFromDB

//	public static void removeOrderAfterEdit(OrderInTable o) {
//
//		Order fullOrder = getOrderInfoFromDB(o);
//		if (o.getOrderStatus().equals("Valid"))
//
//			deleteOrderFromDB(fullOrder, "orders");
//		else
//			deleteOrderFromDB(fullOrder, "waitinglist");
//
//	}

	public static void confirmedArrival(Order o) {
		try {
			int parkNumber = getParkNumber(o.getParkNameInOrder());
			PreparedStatement ps1 = EchoServer.mySql.connection.prepareStatement(
					"UPDATE `ordersforpark" + parkNumber + "` SET `is_confirmed` = ? WHERE `order_num` = ?");
			ps1.setBoolean(1, true);
			ps1.setInt(2, Integer.parseInt(o.getOrderNumber()));
			int rs1 = ps1.executeUpdate();
		} catch (SQLException e) {
			System.err.println("Error confirming order: " + e.getMessage());
		}
	}

	public static void cancelArrival(Order o) {
		try {
			int parkNumber = getParkNumber(o.getParkNameInOrder());
			PreparedStatement ps1 = EchoServer.mySql.connection.prepareStatement(
					"UPDATE `ordersforpark" + parkNumber + "` SET `order_status` = ? WHERE `order_num` = ?");
			ps1.setBoolean(1, false);
			ps1.setInt(2, Integer.parseInt(o.getOrderNumber()));
			int rs1 = ps1.executeUpdate();
		} catch (SQLException e) {
			System.err.println("Error deleting order: " + e.getMessage());
		}
	}

	public static void cancelOrder(OrderInTable o) {

		Order fullOrder = getOrderInfoFromDB(o);
		if (o.getOrderStatus().equals("Waiting List"))
			deleteOrderFromDB(fullOrder, "waitinglist");

		else {
//			try {
//				int parkNumber = getParkNumber(fullOrder.getParkNameInOrder());
//				PreparedStatement ps1 = EchoServer.mySql.connection.prepareStatement(
//						"UPDATE `ordersforpark" + parkNumber + "` SET `order_status` = ? WHERE `order_num` = ?");
//				ps1.setBoolean(1, false);
//				ps1.setInt(2, Integer.parseInt(fullOrder.getOrderNumber()));
//				int rs1 = ps1.executeUpdate();
//			} catch (SQLException e) {
//				System.err.println("Error deleting order: " + e.getMessage());
//			}

			cancelArrival(fullOrder);
		}

	}

	public static void addToWaitingListAfterEdit(PairOfOrders pair) {
		System.out.println("old order is : " + pair.getFrom().toString());
		System.out.println("new order is : " + pair.getTo().toString());

		Order fullOldOrder = getOrderInfoFromDB(pair.getFrom());
		Order fullNewOrder = new Order(fullOldOrder);
		fullNewOrder.setParkNameInOrder(pair.getTo().getParkNameInOrder());
		fullNewOrder.setDateOfVisit(pair.getTo().getDateOfVisit());
		fullNewOrder.setTimeOfVisit(pair.getTo().getTimeOfVisit());
		fullNewOrder.setNumberOfVisitorsInOrder(pair.getTo().getNumberOfVisitorsInOrder());

		fullNewOrder.setOrderStatus(false);
		fullNewOrder.setConfirmed(false);
		fullNewOrder.setPayed(false);
		fullNewOrder.setPrice(fullNewOrder.calculateBill(false));

		// delete old order from db
		if (pair.getFrom().getOrderStatus().equals("Valid"))

			deleteOrderFromDB(fullOldOrder, "orders");
		else
			deleteOrderFromDB(fullOldOrder, "waitinglist");
		enterWaitingList(fullNewOrder);

	}
	
	
	//----------------------------------------------------------------------------------------------
	
	
	public static Order getOrder(String order_number) throws SQLException {
		PreparedStatement ps;
		ResultSet rs;
		String Query;

		for (int park_num = 1; park_num <= 3; park_num++) {
			Order order = null;

			Query = "SELECT * FROM ordersforpark" + park_num + " WHERE order_number='" + order_number + "'";
			ps = EchoServer.mySql.connection.prepareStatement(Query);
			rs = ps.executeQuery();

			while (rs.next()) {
				// Create a new Order object and set its properties based on the retrieved data
				order = new Order();
				order.setOrderNumber(rs.getString("order_num"));
				order.setOrdererId(rs.getString("orderer_id"));
				java.sql.Date orderDate = rs.getDate("date_of_visit");
				LocalDate localorderDate = orderDate.toLocalDate();
				order.setDateOfVisit(localorderDate.plusDays(1));
				order.setTimeOfVisit(rs.getString("time_of_visit"));
				order.setNumberOfVisitorsInOrder(rs.getString("number_of_visitors"));
				order.setOrganized(rs.getBoolean("is_organized"));
				order.setMailInOrder(rs.getString("mail"));
				order.setPhoneNumberInOrder(rs.getString("phone"));
				order.setOrderStatus(rs.getBoolean("order_status"));
				order.setConfirmed(rs.getBoolean("is_confirmed"));
				order.setPreOrdered(rs.getBoolean("is_pre_ordered"));
				order.setPayed(rs.getBoolean("is_payed"));
				order.setPrice((rs.getDouble("price")));
				order.setArrivalTime(rs.getString("arrival_time"));
				order.setDurationOfVisit((rs.getDouble("duration")));
				return order;
			}


		}

		return null; // not found
	}

	public static Message getPrice(String reciept_number, int park_num) {
		double price = -1;
		try {
			PreparedStatement ps;
			ResultSet rs;
			ps = EchoServer.mySql.connection.prepareStatement(
					"SELECT price FROM ordersforpark" + park_num + " WHERE order_number ='" + reciept_number + "'");
			rs = ps.executeQuery();

			if (rs.next()) {
				price = rs.getInt("price");
			}
		} catch (SQLException e) {
			// Handle the exception here
			e.printStackTrace(); // or log the exception
		}
		if (price == -1)
			return new Message(MessageType.ErrorGettingPrice, price);

		return new Message(MessageType.GettingPriceSucces, price);
	}


	// Method to retrieve all orders from the database
	public static List<Order> getAllOrders() {
		List<Order> orders = new ArrayList<Order>();
		String string_park_num;
		PreparedStatement ps;
		ResultSet rs;
		int park_num = 1;
		Order order;

		for (park_num = 1; park_num <= 3; park_num++) {

			try {
				string_park_num = "" + park_num;
				String sql = "SELECT * FROM exitlistforpark" + string_park_num;

				ps = EchoServer.mySql.connection.prepareStatement(sql);
				rs = ps.executeQuery();

				while (rs.next()) {
					order = new Order();
					order.setOrderNumber(rs.getString("order_num"));
					order.setOrdererId(rs.getString("orderer_id"));
					java.sql.Date orderDate = rs.getDate("date_of_visit");
					LocalDate localorderDate = orderDate.toLocalDate();
					order.setDateOfVisit(localorderDate.plusDays(1));
					order.setTimeOfVisit(rs.getString("time_of_visit"));
					order.setNumberOfVisitorsInOrder(rs.getString("number_of_visitors"));
					order.setOrganized(rs.getBoolean("is_organized"));
					order.setMailInOrder(rs.getString("mail"));
					order.setPhoneNumberInOrder(rs.getString("phone"));
					order.setOrderStatus(rs.getBoolean("order_status"));
					order.setConfirmed(rs.getBoolean("is_confirmed"));
					order.setPreOrdered(rs.getBoolean("is_pre_ordered"));
					order.setPayed(rs.getBoolean("is_payed"));
					order.setPrice((rs.getDouble("price")));
					order.setArrivalTime(rs.getString("arrival_time"));
					order.setDurationOfVisit2((rs.getString("duration")));
					orders.add(order);

				}
			} catch (SQLException e) {
				// Handle the exception here
				e.printStackTrace(); // or log the exception
			}
		}
		return orders;
	}
	

	// majd Visitation_Report
	public static List<Order> getOrdersOfAllParksForVisitationReport(int month) throws SQLException {
		List<Order> orders = getAllOrders();
		List<Order> return_orders = new ArrayList<Order>();
		String string_park_num;
		PreparedStatement ps;
		ResultSet rs;

		if (orders.isEmpty())
			System.out.println("orders empty in orderDBCONTROLLER ");
		else
			System.out.println("orders not empty in orderDBCONTROLLER ");

		for (Order o : orders) {

			if (o.getDateOfVisit().getMonthValue() == month) {
				System.out.println("order" + o.toString());
				return_orders.add(o);

			}
				
		}

		return return_orders;

	}
	
	// majd Visitation_Report
		public static List<Order> getOrdersOfAllParksForCancellationReport(int month) throws SQLException {
			List<Order> orders = getAllOrdersFromOrdersOfParks();
			List<Order> return_orders = new ArrayList<Order>();
			String string_park_num;
			PreparedStatement ps;
			ResultSet rs;

			if (orders.isEmpty())
				System.out.println("orders empty in getOrdersOfAllParksForCancellationReport ");
			else
				System.out.println("orders not empty in getOrdersOfAllParksForCancellationReport ");

			for (Order o : orders) {

				if (o.getDateOfVisit().getMonthValue() == month) {
					System.out.println("order getOrdersOfAllParksForCancellationReport" + o.toString());
					return_orders.add(o);

				}
					
			}

			return return_orders;

		}
		

	// Method to retrieve all orders from the database
	public static List<Order> getAllOrdersFromOrdersOfParks() {
		List<Order> orders = new ArrayList<Order>();
		String string_park_num;
		PreparedStatement ps;
		ResultSet rs;
		int park_num = 1;
		Order order;

		for (park_num = 1; park_num <= 3; park_num++) {

			try {
				string_park_num = "" + park_num;
				String sql = "SELECT * FROM ordersforpark" + string_park_num;

				ps = EchoServer.mySql.connection.prepareStatement(sql);
				rs = ps.executeQuery();

				while (rs.next()) {
					order = new Order();
					order.setOrderNumber(rs.getString("order_num"));
					order.setOrdererId(rs.getString("orderer_id"));
					java.sql.Date orderDate = rs.getDate("date_of_visit");
					LocalDate localorderDate = orderDate.toLocalDate();
					order.setDateOfVisit(localorderDate.plusDays(1));
					order.setTimeOfVisit(rs.getString("time_of_visit"));
					order.setNumberOfVisitorsInOrder(rs.getString("number_of_visitors"));
					order.setOrganized(rs.getBoolean("is_organized"));
					order.setMailInOrder(rs.getString("mail"));
					order.setPhoneNumberInOrder(rs.getString("phone"));
					order.setOrderStatus(rs.getBoolean("order_status"));
					order.setConfirmed(rs.getBoolean("is_confirmed"));
					order.setPreOrdered(rs.getBoolean("is_pre_ordered"));
					order.setPayed(rs.getBoolean("is_payed"));
					order.setPrice((rs.getDouble("price")));
					order.setArrivalTime(rs.getString("arrival_time"));
					order.setDurationOfVisit2((rs.getString("duration")));
					order.setParkNameInOrder(string_park_num);
					orders.add(order);

				}
			} catch (SQLException e) {
				// Handle the exception here
				e.printStackTrace(); // or log the exception
			}
		}
		return orders;
	}
	
	
	
}

// last update : 31/3 , 2:03 