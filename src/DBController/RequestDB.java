package DBController;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import EntityClasses.Message;
import EntityClasses.MessageType;
import EntityClasses.Request;
import Server.EchoServer;


public class RequestDB {

	/**
	 * Generates and returns a valid ID for a new request from the database.
	 *
	 * @return A valid ID for a new request.
	 * @throws SQLException If an SQL exception occurs.
	 */
	public static String generateRequestId() throws SQLException {
		String request_id = "-1";
		PreparedStatement ps;
		ResultSet rs;
		ps = EchoServer.mySql.connection.prepareStatement("SELECT requests_counter FROM generalinfoandcounters");
		rs = ps.executeQuery();

		if (rs.next())
			request_id = rs.getString("requests_counter");

		return "" + (Integer.parseInt(request_id) + 1);
	}

	/**
	 * Updates the number of generated IDs of requests in the database.
	 *
	 * @throws SQLException If an SQL exception occurs.
	 */
	public static void updateRequestId() throws SQLException {
		PreparedStatement ps;
		ResultSet rs;
		int rowsAffected;

		String request_id = generateRequestId();
		ps = EchoServer.mySql.connection
				.prepareStatement("UPDATE generalinfoandcounters SET requests_counter =" + request_id);
		rowsAffected = ps.executeUpdate();

	}

	/**
	 * Returns a list of all active requests for a specific park.
	 *
	 * @param park_num The park number.
	 * @return A message containing the type of the result (success or failure) and a list of active requests for the park.
	 */
	// RequestDB majd
	public static Message showRequestsOnePark(String park_num) {

		List<Request> list = getRequestsOnePark(park_num);

		if (list == null)
			return new Message(MessageType.ShowRequestsOneParkFailed, null);
		else
			return new Message(MessageType.ShowRequestsOneParkSucces, list);

	}

	
	/**
	 * Retrieves a list of all active requests for a specific park from the database.
	 *
	 * @param park_num The park number.
	 * @return A list of active requests for the specified park.
	 */
//RequestDB  majd
	public static List<Request> getRequestsOnePark(String park_num) {
		List<Request> list = new ArrayList<>();

		try {
			PreparedStatement ps;
			ResultSet rs;
			String query;
			String requestNumber;
			String requestedInPark;
			String requestType;
			int newValue;
			boolean requestStatus;
			boolean isApproved;

			switch (park_num) {
			case "1": {
				ps = EchoServer.mySql.connection
						.prepareStatement("SELECT * FROM listofrequestsforpark1 WHERE request_status=1");
				rs = ps.executeQuery();
				while (rs.next()) {
					requestNumber = rs.getString("request_number");
					requestedInPark = "1";
					requestType = rs.getString("request_type");
					newValue = rs.getInt("new_value");
					requestStatus = rs.getBoolean("request_status");
					isApproved = rs.getBoolean("is_approved");

					list.add(new Request(requestNumber, requestedInPark, requestType, newValue, requestStatus,
							isApproved));
				}
				break;
			}
			case "2": {
				ps = EchoServer.mySql.connection
						.prepareStatement("SELECT * FROM listofrequestsforpark2 WHERE request_status=1");
				rs = ps.executeQuery();
				while (rs.next()) {
					requestNumber = rs.getString("request_number");
					requestedInPark = "2";
					requestType = rs.getString("request_type");
					newValue = rs.getInt("new_value");
					requestStatus = rs.getBoolean("request_status");
					isApproved = rs.getBoolean("is_approved");

					list.add(new Request(requestNumber, requestedInPark, requestType, newValue, requestStatus,
							isApproved));
				}
				break;
			}
			case "3": {
				ps = EchoServer.mySql.connection
						.prepareStatement("SELECT * FROM listofrequestsforpark3 WHERE request_status=1");
				rs = ps.executeQuery();
				while (rs.next()) {
					requestNumber = rs.getString("request_number");
					requestedInPark = "3";
					requestType = rs.getString("request_type");
					newValue = rs.getInt("new_value");
					requestStatus = rs.getBoolean("request_status");
					isApproved = rs.getBoolean("is_approved");

					list.add(new Request(requestNumber, requestedInPark, requestType, newValue, requestStatus,
							isApproved));
				}
				break;
			}

			}
		} catch (SQLException e) {

		}

		return list;
	}

	/**
	 * Returns a list of all active requests for all parks.
	 *
	 * @return A message containing the type of the result (success or failure) and a list of active requests for all parks.
	 */
	// RequestDB majd
	public static Message showRequestsAllParks() {

		// List<Request> list = getRequestsAllParks();

		List<Request> list = new ArrayList<>();
		list.addAll(getRequestsOnePark("1"));
		list.addAll(getRequestsOnePark("2"));
		list.addAll(getRequestsOnePark("3"));

		if (list.isEmpty())
			return new Message(MessageType.ShowRequestsAllParksFailed, null);
		else
			return new Message(MessageType.ShowRequestsAllParksSucces, list);

	}

	/**
	 * Retrieves a list of all active requests for all parks from the database.
	 *
	 * @return A list of active requests for all parks.
	 */
	// RequestDB majd
	public static List<Request> getRequestsAllParks() {
		List<Request> list = null;

		try {
			PreparedStatement ps;
			ResultSet rs;
			String requestNumber;
			String requestedInPark;
			String requestType;
			int newValue;
			boolean requestStatus;
			boolean isApproved;
			int index = 1;

			for (; index <= 3; index++) {
				ps = EchoServer.mySql.connection.prepareStatement("SELECT * FROM listofrequestsforpark" + index);
				rs = ps.executeQuery();
				while (rs.next()) {
					requestNumber = rs.getString("request_number");
					requestedInPark = "1";
					requestType = rs.getString("request_type");
					newValue = rs.getInt("new_value");
					requestStatus = rs.getBoolean("request_status");
					isApproved = rs.getBoolean("is_approved");

					list.add(new Request(requestNumber, requestedInPark, requestType, newValue, requestStatus,
							isApproved));
				}

			}

		} catch (Exception e) {

		}

		return list;
	}

	
	/**
	 * Adds a new request to the requests database.
	 *
	 * @param new_request The new request to be added.
	 * @return A message indicating the success or failure of the request addition.
	 */
//RequestDB  majd
	public static Message addRequest(Request new_request) {

		try {
			new_request.setRequestNumber(generateRequestId());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String query = "INSERT INTO listofrequestsforpark" + new_request.getRequestedInPark()
				+ " (request_number,request_type,new_value ,request_status,is_approved)" + "VALUES (?, ?, ?, ?,?)";
		try (PreparedStatement preparedStatement = EchoServer.mySql.connection.prepareStatement(query)) {
			preparedStatement.setString(1, new_request.getRequestNumber());
			preparedStatement.setString(2, new_request.getRequestType());
			preparedStatement.setInt(3, new_request.getNewValue());
			preparedStatement.setBoolean(4, new_request.isRequestStatus());
			preparedStatement.setBoolean(5, new_request.isApproved());

			int rowsInserted = preparedStatement.executeUpdate();
			if (rowsInserted > 0) {
				updateRequestId(); // updating counter request_id
				return new Message(MessageType.AddRequestSucces, new_request);
			} else {
				return new Message(MessageType.AddRequestFailed, new_request);
			}
		} catch (SQLException e) {
			return new Message(MessageType.AddingTravelGuideFailed, null);
		}

	}

	
	/**
	 * Accepts a request with the given request number and updates the data in the database accordingly.
	 *
	 * @param request_num The request number to accept.
	 * @return A message indicating the success or failure of the request acceptance.
	 * @throws SQLException If an SQL exception occurs.
	 */
//RequestDB majd   //**park2 park3 -->not working **//
	public static Message acceptRequest(int request_num) throws SQLException {
		String Query;
		PreparedStatement ps;
		ResultSet rs;
		int park_num = 1;
		 String string_park_num = "1";
		int rowsAffected = 0;
		String request_type = "";
		String new_value = "-1";
		boolean flag=false;
		int parkNum=0;
		for (park_num = 1; park_num <= 3; park_num++) {
			string_park_num=""+park_num;
			// getting the request type for update
			Query = "SELECT request_type,new_value FROM listofrequestsforpark" + string_park_num + "  WHERE request_number ='"
					+ request_num + "'";
			ps = EchoServer.mySql.connection.prepareStatement(Query);
			rs = ps.executeQuery();
			if (rs.next()) {
				parkNum=park_num;
				flag=true;
				request_type = rs.getString("request_type");
				new_value = rs.getString("new_value");
			}
		}
		if(!flag)
		{
			return new Message(MessageType.AcceptRequestFailedIdNotFound, request_num);
		}
		
			//if (request_type.equals("")  && new_value.equalsIgnoreCase("-1")) {
			
				// updating request type in the chosen park
				Query = "UPDATE parks SET " + request_type + " = ?  WHERE park_number = ?";
				ps = EchoServer.mySql.connection.prepareStatement(Query);
				ps.setInt(1, Integer.valueOf( Integer.parseInt(new_value)));
				ps.setInt(2, Integer.valueOf(parkNum));
				rowsAffected = +ps.executeUpdate();

				// updating the request status
				Query = "UPDATE listofrequestsforpark" + parkNum
						+ " SET request_status = ? ,is_approved = ?  WHERE request_number = ?";
				ps = EchoServer.mySql.connection.prepareStatement(Query);
				ps.setBoolean(1, Boolean.valueOf(false));
				ps.setBoolean(2, Boolean.valueOf(true));
				ps.setInt(3, Integer.valueOf(request_num));
				rowsAffected = +ps.executeUpdate();
				
				if (rowsAffected > 0) {

					return new Message(MessageType.AcceptRequestSucces, request_num);
				}
			

			

		
		return new Message(MessageType.AcceptRequestFailed, request_num);
	}

	
	/**
	 * Rejects a request with the given request number and updates the data in the database accordingly.
	 *
	 * @param request_num The request number to reject.
	 * @return A message indicating the success or failure of the request rejection.
	 * @throws SQLException If an SQL exception occurs.
	 */
	// RequestDB majd
	public static Message rejectRequest(int request_num) throws SQLException {

		String Query;
		PreparedStatement ps;
		ResultSet rs;
		int park_num = 1;
		int rowsAffected = 0;

		for (park_num = 1; park_num <= 3; park_num++) {

			// updating the request status
			Query = "UPDATE listofrequestsforpark" + park_num
					+ " SET request_status = ? ,is_approved = ?  WHERE request_number = ?";
			ps = EchoServer.mySql.connection.prepareStatement(Query);
			ps.setString(1, String.valueOf("0"));
			ps.setString(2, String.valueOf("0"));
			ps.setString(3, String.valueOf(request_num));
			rowsAffected = +ps.executeUpdate();
		}

		if (rowsAffected > 0) {

			return new Message(MessageType.RejectRequestSucces, request_num);
		} else {

			return new Message(MessageType.RejectRequestFailed, request_num);
		}

	}

}
