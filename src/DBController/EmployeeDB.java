package DBController;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import EntityClasses.*;
import HelperKit.*;
import Server.EchoServer;



public class EmployeeDB {



    /**
     * Checks if the specified employee exists in the database and performs login if found.
     * <p>
     * This method takes an Employee object and checks if the employee exists in the database.
     * If the employee is found and not already logged in, the method logs in the employee.
     * </p>
     * <p>
     * If the employee is found and successfully logged in, a success message is returned.
     * If the employee is not found or is already logged in, a failure message is returned.
     * </p>
     *
     * @param e the Employee object representing the employee attempting to log in
     * @return a Message object representing the login status
     * @throws SQLException if an SQL exception occurs while accessing the database
     */
	public static Message EmployeeLogin(Employee e) throws SQLException {
		if (EmployeeExist(e.getUsername(), e.getPassword())) // found
		{
			Employee employee = getEmployee(e.getUsername(), e.getPassword());
			if(employee.isEmployeeLoggedIn())
				return new Message(MessageType.EmployeeIsLogedIn, employee);
			
			EmployeeLogin(e.getUsername(), e.getPassword());// login succes

			return new Message(MessageType.EmployeeLoginSucces, employee);

		} else // not found -> login failed
		{
			return new Message(MessageType.EmployeeLoginFailed, null);
		}
	}
	 /**
     * Retrieves the employee details from the database based on the provided username and password.
     * <p>
     * This method queries the database for an employee with the given username and password.
     * If an employee is found, their details are retrieved and returned as an Employee object.
     * </p>
     *
     * @param username the username of the employee
     * @param password the password of the employee
     * @return the Employee object representing the employee if found; otherwise, null
     * @throws SQLException if an SQL exception occurs while accessing the database
     */
	public static Employee getEmployee(String username, String password) throws SQLException {
		if (!EmployeeExist(username, password)) // the employee doesnt exist
			return null;

		try {
			Employee em;
			PreparedStatement ps = EchoServer.mySql.connection.prepareStatement(
					"SELECT * FROM employees WHERE username ='" + username + "' AND password ='" + password + "'");
			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				String workerId = rs.getString("workerid");
				String name = rs.getString("name");
				String lastName = rs.getString("lastname");
				String email = rs.getString("mail");
				String role = rs.getString("role");
				String park = rs.getString("park");
				String username1 = rs.getString("username");
				String password1 = rs.getString("password");
				boolean islogged = rs.getBoolean("islogged");
				return (new Employee(workerId, name, lastName, email, role, park, username1, password1, islogged));

			}

		} catch (Exception e) {

		}

		return null;

	}

	 /**
     * Checks if an employee with the given username and password exists in the database.
     * <p>
     * This method queries the database to determine if an employee with the specified
     * username and password exists.
     * </p>
     *
     * @param username the username of the employee
     * @param password the password of the employee
     * @return true if the employee exists in the database; otherwise, false
     * @throws SQLException if an SQL exception occurs while accessing the database
     */

	public static boolean EmployeeExist(String username, String password) throws SQLException

	{
		try {
			PreparedStatement ps = EchoServer.mySql.connection.prepareStatement(
					"SELECT * FROM employees WHERE username ='" + username + "' AND password ='" + password + "'");
			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				return true;
			}

		} catch (Exception e) {

		}
		return false;
	}

	
	 /**
     * Logs in an employee with the given username and password.
     * <p>
     * This method updates the database to set the islogged parameter to 1 for the employee
     * with the specified username and password, indicating that the employee is logged in.
     * </p>
     *
     * @param username the username of the employee
     * @param password the password of the employee
     * @throws SQLException if an SQL exception occurs while accessing the database
     */
	public static void EmployeeLogin(String username, String password) throws SQLException {

		try {
			// Assuming EchoServer.mySql.connection is a valid Connection object
			String query = "UPDATE employees SET islogged = ? WHERE username = ? AND password = ?";
			PreparedStatement ps = EchoServer.mySql.connection.prepareStatement(query);
			// Set parameters
			ps.setInt(1, 1); // Assuming 1 represents logged in
			ps.setString(2, username);
			ps.setString(3, password);
			// Execute the update
			int rowsAffected = ps.executeUpdate();

		} catch (Exception e) {

		}

	}

	
	 /**
     * Logs out an employee with the given username and password.
     * <p>
     * This method updates the database to set the islogged parameter to 0 for the employee
     * with the specified username and password, indicating that the employee is logged out.
     * </p>
     *
     * @param username the username of the employee
     * @param password the password of the employee
     * @return a message indicating the success or failure of the logout operation
     * @throws SQLException if an SQL exception occurs while accessing the database
     */
	public static Message EmployeeLogout(String username, String password) throws SQLException {
		Employee em=getEmployee(username,password);
		try {

			// Assuming EchoServer.mySql.connection is a valid Connection object
			String query = "UPDATE employees SET islogged = ? WHERE username = ? AND password = ?";
			PreparedStatement ps = EchoServer.mySql.connection.prepareStatement(query);
			// Set parameters
			ps.setInt(1, 0); // Assuming 1 represents logged in
			ps.setString(2, username);
			ps.setString(3, password);
			// Execute the update
			int rowsAffected = ps.executeUpdate();

			// Check if the update was successful
			if (rowsAffected > 0) {
				// Update successful
				return (new Message(MessageType.EmployeeLogoutSucces,em));
			} 

		} catch (Exception e) {

		}
		return (new Message(MessageType.EmployeeLogoutFailed,em));


	}



}
