package DBController;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import EntityClasses.Message;

//import com.mysql.cj.protocol.Message;

import EntityClasses.MessageType;

import EntityClasses.Tourist;
import HelperKit.mysqlConnection;
import Server.EchoServer;

public class TouristsDBcontroller {
	/**
	 * @param function get id of a tourist search for the tourist and return him as
	 *                 entity
	 * @return the tourist with the giving id
	 */

	public static Tourist CreateTourist(Object msg) {
		Tourist tourist = null;
		String id = (String) msg;
		String sql = "SELECT * FROM tourists WHERE id = '" + id + "'";
		try {
			PreparedStatement statement = EchoServer.mySql.connection.prepareStatement(sql);
			ResultSet resultSet = statement.executeQuery();
			if (resultSet.next()) {
				// Tourist with given ID found, populate Tourist object
				tourist = new Tourist();
				tourist.setTouristId(resultSet.getString("id"));
				tourist.setTouristFirstName(resultSet.getString("name"));
				tourist.setTouristLastName(resultSet.getString("last_name"));
				tourist.setTouristEmail(resultSet.getString("email"));
				tourist.setTouristPhoneNumber(resultSet.getString("phone"));
				tourist.setIsTravelGuide(resultSet.getBoolean("is_travel_guide"));
				tourist.setIsTouristLoggedIn(resultSet.getBoolean("is_logged_in"));
			}
		} catch (SQLException e) {
			System.out.println("Error while checking if tourist exists: " + e.getMessage());
		}

		return tourist;

	}

	/**
	 * @param tourist update the logged_in field for the giving tourist in DB
	 * @return
	 */
	public static void UpdateLoggedIn(Tourist tourist) {

		try {
			String sqlUpdateQuery = "UPDATE tourists SET is_logged_in = ? where id = ?;";
			PreparedStatement ps = EchoServer.mySql.connection.prepareStatement(sqlUpdateQuery);
			ps.setString(1, String.valueOf("1"));
			ps.setString(2, String.valueOf(tourist.getTouristId()));
			ps.executeUpdate();
			tourist.setIsTouristLoggedIn(true);
		} catch (SQLException e) {
			System.out.println("Error while checking if tourist exists: " + e.getMessage());
		}
	}

	/**
	 * @param id of tourist updated the tourist with the giving id **> fields
	 *           is_logged_in to 0
	 */
	public static void logOutTourist(String id) {
		try {
			String sqlUpdateQuery = "UPDATE tourists SET is_logged_in = ? where id = ?;";
			PreparedStatement ps = EchoServer.mySql.connection.prepareStatement(sqlUpdateQuery);
			ps.setString(1, String.valueOf("0"));
			ps.setString(2, String.valueOf(id));
			ps.executeUpdate();

		} catch (SQLException e) {
			System.out.println("Error while checking if tourist exists: " + e.getMessage());
		}

	}

	/**
	 * @param id search for a tourist with the id
	 * @return true if founded else false
	 */
	public static boolean isTouristExist(String id) {
		try {
			PreparedStatement ps1 = EchoServer.mySql.connection
					.prepareStatement("SELECT `id` FROM tourists WHERE `id` = ? ");
			ps1.setString(1, id);
			ResultSet rs = ps1.executeQuery();
			rs.next();
			System.out.println("the is users Exist searching for id : " + id);
			try {
				rs.getString(1);
				System.out.println("is users found the id ");
				return true;
			} catch (SQLException var5) {
				ps1.close();
				System.out.println("is users does not found the id ");

				return false;
			}
		} catch (SQLException var6) {
			var6.printStackTrace();
			return false;
		}
	}

	/**
	 * @param id check if the tourist exists, if yes : update the DB and send
	 *           relevant message if not send null
	 * @return message for the client
	 */
	public static Message TouristLogin(String id) {
		Tourist tourist;

		if (!isTouristExist(id)) {
			System.out.println("user not found in tourist login ");
			tourist = null;
			return new Message(MessageType.TouristLogInFailed, tourist);
		}
		System.out.println("user  found in tourist login ");

		tourist = CreateTourist(id);

		if (tourist.getIsTouristLoggedIn()) {

			return new Message(MessageType.TouristAlreadyLoggedIn, tourist);

		} else {
			UpdateLoggedIn(tourist);
			tourist.setIsTouristLoggedIn(true);
			return new Message(MessageType.TouristLogInSucceeded, tourist);
		}

	}

	public static void addTourist(Tourist tourist) {
		try {
			String query = "INSERT INTO tourists (id, name, last_name, email, phone, is_travel_guide, is_logged_in) "
					+ "VALUES (?, ?, ?, ?, ?, ?, ?)";

			try (PreparedStatement preparedStatement = EchoServer.mySql.connection.prepareStatement(query)) {
				preparedStatement.setString(1, tourist.getTouristId());
				preparedStatement.setString(2, tourist.getTouristFirstName());
				preparedStatement.setString(3, tourist.getTouristLastName());
				preparedStatement.setString(4, tourist.getTouristEmail());
				preparedStatement.setString(5, tourist.getTouristPhoneNumber());
				preparedStatement.setBoolean(6, tourist.getIsTravelGuide());
				preparedStatement.setBoolean(7, tourist.getIsTouristLoggedIn());

				int rowsInserted = preparedStatement.executeUpdate();
				if (rowsInserted > 0) {
					System.out.println("Tourist added successfully!");
				} else {
					System.out.println("Failed to add tourist.");
				}
			}
		} catch (SQLException e) {
			System.err.println("Error adding tourist to database: " + e.getMessage());
		}
	}
	
	public static boolean checkIdExists(String idToCheck) {
        boolean idExists = false;

        try  {
            String sql = "SELECT COUNT(*) AS count FROM travelguides WHERE id = ?";
            try (PreparedStatement statement = EchoServer.mySql.connection.prepareStatement(sql)) {
                statement.setString(1, idToCheck);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        int count = resultSet.getInt("count");
                        idExists = count > 0;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return idExists;
    }
	

	public static Message addTravelGuied(String messageData) {
		if(checkIdExists(messageData)) {
			
		}
		else { 
			
		}
		
		return null;
	}

}
