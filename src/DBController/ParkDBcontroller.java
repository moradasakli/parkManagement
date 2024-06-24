package DBController;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import EntityClasses.Park;
import Server.EchoServer;

public class ParkDBcontroller {

	public static Park getParkInfo(String parkName) {
		Park park = null;
		try {
			PreparedStatement ps = EchoServer.mySql.connection
					.prepareStatement("select * from `parks` WHERE `park_name` = ? ");
			ps.setString(1, parkName);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				park = new Park();

				park.setParkNumber(rs.getInt("park_number") + "");
				park.setParkName(parkName);
				park.setMaxCapacity(rs.getInt("max_capacity"));
				park.setNumberOfReservedSpaces(rs.getInt("number_of_reserved_spaces"));
				park.setDefaultDurationForVisit(rs.getInt("default_duration_for_visit"));
				park.setCurrentCapacity(rs.getInt("current_capacity"));
			}
		} catch (Exception e) {

		}

		System.out.println("Park info :" + park.toString());

		return park;

	}
}// end ParkDBcontroller
