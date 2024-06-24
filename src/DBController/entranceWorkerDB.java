package DBController;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

import EntityClasses.Message;
import EntityClasses.MessageType;
import EntityClasses.Order;
import EntityClasses.Park;
import Server.EchoServer;

public class entranceWorkerDB {
	/**
	 * Adds the given order to the exit list for the specific park.
	 * 
	 * @param o Order object representing the order to be added to the exit list
	 * @return Message object indicating the status of the operation
	 */
	public static Message getOrderExitList(Order o) {
		Order order = new Order();

		// Check if the order exists in the entry list for the specified park
		if (doesOrderExist(o.getOrderNumber(), o.getPark_num(), "entrylistforpark")) {
			// Retrieve order information from the database
			order = getOrderInfoFromDB(o.getOrderNumber(), o.getPark_num(), "entrylistforpark");
			int num_of_visitors = Integer.parseInt(order.getNumberOfVisitorsInOrder());

			String park_num = String.valueOf(o.getPark_num());
			o.setParkNameInOrder(park_num);
			deleteOrderFromDB(o, "entrylistforpark");
			currentCapacityUpdate(park_num, num_of_visitors, "exit");

			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

			// Print current time and exit time
			System.out.println("time is now !");
			System.out.println("exit date is : " + o.getExitTime());
			LocalTime exitTime = LocalTime.parse(o.getExitTime(), formatter);
			System.out.println("arrival time is : " + order.getArrivalTime());
			LocalTime arrivalTime = LocalTime.parse(order.getArrivalTime(), formatter);

			// Calculate the absolute difference between exitTime and arrivalTime
			long totalMinutes = Math.abs(exitTime.until(arrivalTime, ChronoUnit.MINUTES));

			// Calculate hours and minutes separately
			long hours = totalMinutes / 60;
			long minutes = totalMinutes % 60;

			// Format the time difference as "HH:mm"
			String timeDifferenceStr = String.format("%02d:%02d", hours, minutes);
			System.out.println("order duration is : " + timeDifferenceStr);

			order.setOrderStatus(false);
			order.setDurationOfVisit2(timeDifferenceStr);

			// Print time difference and updated order information
			System.out.println("printing now and diff is : " + timeDifferenceStr);
			System.out.println("my full order now is : " + order.toString());

			try {
				// Prepare the SQL statement for inserting into the exit list for the park
				String sql = "INSERT INTO `exitlistforpark" + o.getParkNameInOrder()
						+ "` (order_num, orderer_id, date_of_visit, time_of_visit, "
						+ "number_of_visitors, is_organized, mail, phone, order_status, arrival_time, duration, "
						+ "is_confirmed, is_pre_ordered, is_payed, price) "
						+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

				// Create a PreparedStatement with the SQL statement
				try (PreparedStatement pstmt = EchoServer.mySql.connection.prepareStatement(sql)) {
					// Set values for the placeholders in the SQL statement using data from the
					// Order object
					pstmt.setString(1, order.getOrderNumber());
					pstmt.setString(2, order.getOrdererId());
					pstmt.setDate(3, java.sql.Date.valueOf(order.getDateOfVisit()));
					pstmt.setString(4, order.getTimeOfVisit());
					pstmt.setString(5, order.getNumberOfVisitorsInOrder());
					pstmt.setBoolean(6, order.isOrganized());
					pstmt.setString(7, order.getMailInOrder());
					pstmt.setString(8, order.getPhoneNumberInOrder());
					pstmt.setBoolean(9, order.isOrderStatus());
					pstmt.setString(10, order.getArrivalTime());
					pstmt.setString(11, timeDifferenceStr);
					pstmt.setBoolean(12, order.isConfirmed());
					pstmt.setBoolean(13, order.isPreOrdered());
					pstmt.setBoolean(14, true);
					pstmt.setDouble(15, order.getPrice());

					// Execute the INSERT statement
					int rowsAffected = pstmt.executeUpdate();
					if (rowsAffected > 0) {
						System.out.println("it was added  ! ");
						return new Message(MessageType.orderDeletedFromEntryAndMovedToExit, o);
					} else {
						return new Message(MessageType.orderDeletedFromEntryFailed, o);
					}
				}
			} catch (SQLException e) {
				// Handle SQL-related errors
				System.err.println("Error while connecting to the database or executing SQL: " + e.getMessage());
			}
		} else {
			// Order not found in the entry list for the specified park
			return new Message(MessageType.orderDeletedFromEntrynotFound, o);
		}
		return null;
	}

	/**
	 * Calculates the available spots for casual visits at a park on a specified
	 * date and time.
	 *
	 * @param obj An object containing information needed for the calculation (park
	 *            number, date, and time)
	 * @return Message object indicating the number of available spots
	 */
	public static Message availableSpotsForCasualVisit(Object obj) {
		LocalDate requested_date = null;
		ArrayList<String> data = (ArrayList<String>) obj;
		String parkNumber = data.get(0);
		int parkCurrentCapacity = getCurrentCapacity(parkNumber);
		int parkMaxCapacity = getMaxCapacity(parkNumber);
		int parkDefaultDurationForVisit = defaultDurationForVisit(parkNumber);
		int availableSpots = 0;

		DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd");

		try {
			// Parse the string to a LocalDate object using the defined formatter
			requested_date = LocalDate.parse(data.get(1), formatter2);

			// Print the parsed LocalDate object
			System.out.println("Parsed LocalDate: " + requested_date);
		} catch (Exception e) {
			System.out.println("Error parsing the date string: " + e.getMessage());
		}

		// arrange time
		String timeString = data.get(2);// current
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
		LocalTime time = LocalTime.parse(timeString, formatter);

		// Add an hour
		LocalTime newTime1 = time.plusHours(parkDefaultDurationForVisit);

		// Format the new time back into a string
		String timeAfter = newTime1.format(formatter);

		int counterVisitors = 0;
		try {
			// Construct SQL query to retrieve orders within the specified date and time
			// range
			String sql2 = "SELECT * FROM `ordersforpark" + parkNumber + "` WHERE DATE(date_of_visit) = '"
					+ requested_date + "' AND TIME(STR_TO_DATE(time_of_visit, '%H:%i')) >= '" + timeString
					+ "' AND TIME(STR_TO_DATE(time_of_visit, '%H:%i')) < '" + timeAfter + "' AND order_status = 1";

			// Create PreparedStatement with the SQL query
			PreparedStatement statement = EchoServer.mySql.connection.prepareStatement(sql2);

			// Execute the query and get the result set
			ResultSet resultSet = statement.executeQuery();

			// Process the result set to calculate the total number of visitors
			while (resultSet.next()) {
				int numberOfVisitors = resultSet.getInt("number_of_visitors");
				counterVisitors += numberOfVisitors;
			}

		} catch (SQLException e) {
			// Handle SQL-related errors
			e.printStackTrace();
		}

		// Calculate the available spots based on park capacities and current visitors
		System.out.println("number of visitors is : " + counterVisitors);
		availableSpots = parkMaxCapacity - counterVisitors - parkCurrentCapacity;

		// Return a Message object indicating the number of available spots
		return new Message(MessageType.GetAvailableSpots, availableSpots);
	}

	/**
	 * Retrieves the current capacity of a park based on its park number.
	 *
	 * @param parknum The park number for which to retrieve the current capacity
	 * @return The current capacity of the park, or 0 if an error occurs
	 */
	public static int getCurrentCapacity(String parknum) {
		try {
			// Prepare SQL statement to select current capacity from the parks table
			PreparedStatement ps = EchoServer.mySql.connection
					.prepareStatement("SELECT * FROM parks WHERE park_number ='" + parknum + "'");

			// Execute the query and get the result set
			ResultSet rs = ps.executeQuery();

			// Check if a result exists
			if (rs.next()) {
				// Retrieve the current capacity from the result set
				int currentCapacity = rs.getInt("current_capacity");
				return currentCapacity;
			}
		} catch (Exception e) {
			// Handle any exceptions that occur during the query execution
			System.out.println("Error caught while retrieving current capacity: " + e.getMessage());
		}

		// Return 0 if an error occurred or if no result was found
		return 0;
	}

	/**
	 * Retrieves the maximum capacity of a park based on its park number.
	 *
	 * @param parknum The park number for which to retrieve the maximum capacity
	 * @return The maximum capacity of the park, or 0 if an error occurs
	 */
	public static int getMaxCapacity(String parknum) {
	    try {
	        // Prepare SQL statement to select maximum capacity from the parks table
	        PreparedStatement ps = EchoServer.mySql.connection
	                .prepareStatement("SELECT * FROM parks WHERE park_number ='" + parknum + "'");
	        
	        // Execute the query and get the result set
	        ResultSet rs = ps.executeQuery();

	        // Check if a result exists
	        if (rs.next()) {
	            // Retrieve the maximum capacity from the result set
	            int maxCapacity = rs.getInt("max_capacity");
	            return maxCapacity;
	        }
	    } catch (Exception e) {
	        // Handle any exceptions that occur during the query execution
	        System.out.println("Error caught while retrieving maximum capacity: " + e.getMessage());
	    }
	    
	    // Return 0 if an error occurred or if no result was found
	    return 0;
	}


	/**
	 * Retrieves the default duration for a visit to a park based on its park number.
	 *
	 * @param parknum The park number for which to retrieve the default duration for a visit
	 * @return The default duration for a visit to the park, or 0 if an error occurs
	 */
	public static int defaultDurationForVisit(String parknum) {
	    try {
	        // Prepare SQL statement to select default duration for visit from the parks table
	        PreparedStatement ps = EchoServer.mySql.connection
	                .prepareStatement("SELECT * FROM parks WHERE park_number ='" + parknum + "'");
	        
	        // Execute the query and get the result set
	        ResultSet rs = ps.executeQuery();

	        // Check if a result exists
	        if (rs.next()) {
	            // Retrieve the default duration for visit from the result set
	            int defaultDurationForVisit = rs.getInt("default_duration_for_visit");
	            return defaultDurationForVisit;
	        }
	    } catch (Exception e) {
	        // Handle any exceptions that occur during the query execution
	        System.out.println("Error caught while retrieving default duration for visit: " + e.getMessage());
	    }
	    
	    // Return 0 if an error occurred or if no result was found
	    return 0;
	}

	/**
	 * Retrieves order details for an entrance worker based on the receipt number and park number.
	 *
	 * @param reciept_number The receipt number of the order
	 * @param park_num       The park number associated with the order
	 * @return Message object containing the order details if found, or an error message if not found
	 */
	public static Message getOrderDetalisForEntranceWorker(String reciept_number, int park_num) {
	    Order o = new Order();
	    boolean checkIfExists = doesOrderExist(reciept_number, park_num, "ordersforpark");
	    if (checkIfExists) {
	        // Retrieve order information from the database
	        o = getOrderInfoFromDB(reciept_number, park_num, "ordersforpark");
	        return new Message(MessageType.GettingPriceSucces, o);
	    }

	    // Return an error message if the order does not exist
	    return new Message(MessageType.ErrorGettingPrice);
	}


	/**
	 * Adds an order to the entry list for a park, performing necessary checks and updates.
	 *
	 * @param o The Order object to be added to the entry list
	 * @return Message object indicating the status of adding the order to the entry list
	 */
	public static Message getOrdertoEntryList(Order o) {
	    String park_num = o.getParkNameInOrder();
	    int num_of_visitors = Integer.parseInt(o.getNumberOfVisitorsInOrder());
	    System.out.println("Entering the order to the park, from getOrdertoEntryList");

	    // Set the order status to true
	    o.setOrderStatus(true);

	    // Generate order number if it is not provided
	    if (o.getOrderNumber() == null) {
	        String order_num = getOrderNumber();
	        o.setOrderNumber(order_num);
	    }

	    // Define a DateTimeFormatter for parsing time strings
	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

	    // Convert time strings to LocalTime objects
	    LocalTime visitTime = LocalTime.parse(o.getTimeOfVisit(), formatter);
	    LocalTime arrival = LocalTime.parse(o.getArrivalTime(), formatter);

	    // Calculate the difference in minutes between arrival and visit time
	    long minutesDifference = arrival.until(visitTime, java.time.temporal.ChronoUnit.MINUTES);
	    System.out.println("The difference in time is : " + minutesDifference);

	    if (Math.abs(minutesDifference) > 20) {
	    	System.out.println("cant entert ! ");
	        // Return a message indicating that the order came late to the visit
	        return new Message(MessageType.orderCameLateToVisit, o);
	    } else {
	        System.out.println("The order we want to enter is : " + o.toString());

	        try {
	            // Prepare the SQL statement with placeholders for values
	            String sql = "INSERT INTO `entrylistforpark" + o.getParkNameInOrder()
	                    + "` (order_num, orderer_id, date_of_visit, time_of_visit, "
	                    + "number_of_visitors, is_organized, mail, phone, order_status, arrival_time, duration, "
	                    + "is_confirmed, is_pre_ordered, is_payed, price) "
	                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

	            // Create a PreparedStatement with the SQL statement
	            try (PreparedStatement pstmt = EchoServer.mySql.connection.prepareStatement(sql)) {
	                // Set values for the placeholders in the SQL statement using data from the Order object
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
	                pstmt.setBoolean(14, true);
	                pstmt.setDouble(15, o.getPrice());

	                // Execute the INSERT statement
	                int rowsAffected = pstmt.executeUpdate();
	                if (rowsAffected > 0) {
	                    System.out.println("The order was added successfully!");
	                } else {
	                    return new Message(MessageType.orderAddedToEntryListFailed, o);
	                }
	            }
	        } catch (SQLException e) {
	            // Handle SQL-related errors
	            System.err.println("Error while connecting to the database or executing SQL: " + e.getMessage());
	        }

	        // Delete the order from the database and update current capacity
	        deleteOrderFromDB(o, "ordersforpark");
	        currentCapacityUpdate(park_num, num_of_visitors, "entry");

	        // Check for full capacity and return appropriate message
	        checkForFullCapacity(o, park_num);
	        return new Message(MessageType.orderAddedToEntryListSuccusefully, o);
	    }
	}

	/**
	 * Checks if the park has reached full capacity and updates a report if necessary.
	 *
	 * @param o       The Order object related to the park
	 * @param parknum The park number for which to check the capacity
	 */
	public static void checkForFullCapacity(Order o, String parknum) {
	    int parkCurrentCapacity = getCurrentCapacity(parknum);
	    int parkMaxCapacity = getMaxCapacity(parknum);

	    // Check if the park has reached full capacity
	    if (parkCurrentCapacity == parkMaxCapacity) {
	        System.out.println("Should update to report!");

	        try {
	            // Prepare the SQL statement with placeholders for values
	            String sql = "INSERT INTO `timesoffullcapacityforpark" + o.getParkNameInOrder()
	                    + "` (order_num, orderer_id, date_of_visit, time_of_visit, "
	                    + "number_of_visitors, is_organized, mail, phone, order_status, arrival_time, duration, "
	                    + "is_confirmed, is_pre_ordered, is_payed, price) "
	                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

	            // Create a PreparedStatement with the SQL statement
	            try (PreparedStatement pstmt = EchoServer.mySql.connection.prepareStatement(sql)) {
	                // Set values for the placeholders in the SQL statement using data from the Order object
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
	                pstmt.setBoolean(14, true);
	                pstmt.setDouble(15, o.getPrice());

	                // Execute the INSERT statement
	                int rowsAffected = pstmt.executeUpdate();
	                if (rowsAffected > 0) {
	                    System.out.println("Report updated successfully!");
	                } else {
	                    System.out.println("Report not updated");
	                }
	            }
	        } catch (SQLException e) {
	            // Handle SQL-related errors
	            System.err.println("Error while connecting to the database or executing SQL: " + e.getMessage());
	        }
	    }
	}

	/**
	 * Checks if an order exists in a specific table based on the order number and park number.
	 *
	 * @param orderNum   The order number to check for existence
	 * @param parknum    The park number associated with the order
	 * @param tablename  The name of the table to search for the order
	 * @return True if the order exists in the specified table, false otherwise
	 */
	public static boolean doesOrderExist(String orderNum, int parknum, String tablename) {
	    boolean exists = false;
	    String query = "SELECT COUNT(*) AS count FROM " + tablename + parknum + " WHERE order_num = ?";

	    try (PreparedStatement statement = EchoServer.mySql.connection.prepareStatement(query)) {
	        statement.setString(1, orderNum);
	        ResultSet resultSet = statement.executeQuery();
	        if (resultSet.next()) {
	            int count = resultSet.getInt("count");
	            exists = (count > 0);
	        }
	    } catch (SQLException e) {
	        // Handle SQL-related errors
	        System.err.println("Error checking order existence: " + e.getMessage());
	    }

	    return exists;
	}

	/**
	 * Retrieves order information from the database based on the receipt number, park number, and table name.
	 *
	 * @param reciept_number The receipt number of the order
	 * @param park_num       The park number associated with the order
	 * @param tablename      The name of the table to retrieve order information from
	 * @return An Order object containing the retrieved order information, or null if an error occurs
	 */
	public static Order getOrderInfoFromDB(String reciept_number, int park_num, String tablename) {
	    Order order = null;
	    try {
	        // Extract info from orders tables
	        int parkNumber = park_num;
	        PreparedStatement ps;
	        ResultSet rs;

	        // Prepare and execute the SQL query to retrieve order information
	        ps = EchoServer.mySql.connection.prepareStatement(
	                "SELECT * FROM `" + tablename + parkNumber + "` WHERE `order_num` = " + reciept_number);
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
	        }
	    } catch (Exception e) {
	        // Handle any exceptions that occur during the retrieval of order information
	        System.err.println("Error retrieving order information: " + e.getMessage());
	    }
	    return order;
	}

	/**
	 * Updates the current capacity of a park based on the specified park number, amount, and entry or exit operation.
	 *
	 * @param parkNumber   The park number for which to update the current capacity
	 * @param Amount       The amount by which to update the current capacity (positive for entry, negative for exit)
	 * @param entryOrExit  Specifies whether the update is for entry or exit (values: "entry" or "exit")
	 */
	public static void currentCapacityUpdate(String parkNumber, int Amount, String entryOrExit) {
	    String updateQuery;
	    if (entryOrExit.equals("entry")) {
	        updateQuery = "UPDATE parks SET current_capacity = current_capacity + ? WHERE park_number = ?";
	    } else {
	        updateQuery = "UPDATE parks SET current_capacity = current_capacity - ? WHERE park_number = ?";
	    }

	    boolean success = false;

	    try (PreparedStatement statement = EchoServer.mySql.connection.prepareStatement(updateQuery)) {
	        statement.setInt(1, Amount);
	        statement.setString(2, parkNumber);

	        int rowsAffected = statement.executeUpdate();
	        if (rowsAffected > 0) {
	            success = true;
	            System.out.println("Current capacity updated for park " + parkNumber + " by " + Amount);
	        } else {
	            System.out.println("Park not found or capacity could not be updated.");
	        }
	    } catch (SQLException e) {
	        System.err.println("Error updating park capacity: " + e.getMessage());
	    }
	}


	/**
	 * Generates a unique order number by retrieving and updating the orders_counter from the database.
	 *
	 * @return A unique order number as a String
	 */
	public static String getOrderNumber() {
	    String order_num = "";

	    try (
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

	        // SQL query to increment the orders_counter by 1
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
	        // Handle SQL-related errors
	        e.printStackTrace();
	    }

	    return order_num;
	}


	/**
	 * Deletes an order from the specified database table based on the order number and park name.
	 *
	 * @param o         The Order object containing the order information to delete
	 * @param tablename The name of the database table from which to delete the order
	 */
	public static void deleteOrderFromDB(Order o, String tablename) {
	    String sql = "DELETE FROM " + tablename + o.getParkNameInOrder() + " WHERE order_num = ?";
	    System.out.println("Full delete SQL query: " + sql);

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
	        // Handle SQL-related errors
	        System.err.println("Error deleting order: " + e.getMessage());
	    }
	}

}
