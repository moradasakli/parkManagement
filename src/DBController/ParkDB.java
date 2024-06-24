package DBController;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import EntityClasses.*;
import HelperKit.mysqlConnection;
import Server.EchoServer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


public class ParkDB {

	
	 /**
     * Retrieves the number of available parking spots in the specified park.
     * 
     * @param park_num the park number
     * @return a Message object containing the result (success or failure) and the number of available spots
     */
//	public static Message getAvailableSpots(String park_num) {
//		Park park = getPark(park_num);
//
//		if (park != null)
//			return new Message(MessageType.AvailableSpotsSucces, park.checkAvailableSpots());
//
//		return new Message(MessageType.AvailableSpotsFailed, -1);
//	}
	
	
	
	  /**
     * Retrieves the Park object associated with the specified park number.
     * 
     * @param park_num the park number
     * @return an instance of the Park class if the park exists; otherwise, null
     */
	public static Park getPark(String park_num) // return the object of the park with (park_num) if exists
	{
		if (!IsParkExist(park_num)) {
			return null;
		} else {
			try {
				PreparedStatement ps = EchoServer.mySql.connection
						.prepareStatement("SELECT * FROM parks WHERE park_number ='" + park_num + "'");
				ResultSet rs = ps.executeQuery();

				if (rs.next()) {
					String parkNumber = rs.getString("park_number");
					String parkName = rs.getString("park_name");
					int maxCapacity = rs.getInt("max_capacity");
					int numberOfReservedSpaces = rs.getInt("number_of_reserved_spaces");
					int defaultDurationForVisit = rs.getInt("default_duration_for_visit");
					int currentCapacity = rs.getInt("current_capacity");
					return (new Park(parkNumber, parkName, maxCapacity, numberOfReservedSpaces, defaultDurationForVisit,
							currentCapacity));
				}
			} catch (Exception e) {

			}

			return null;
		}
	}

	 /**
     * Checks if the park with the specified park number exists.
     * 
     * @param park_num the park number
     * @return true if the park exists; otherwise, false
     */
	public static boolean IsParkExist(String park_num) // check if the park number (park_num) exists
	{
		try {
			PreparedStatement ps = EchoServer.mySql.connection
					.prepareStatement("SELECT * FROM parks WHERE park_number ='" + park_num + "'");
			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				return true;
			}

		} catch (Exception e) {

		}
		return false;
	}

}
