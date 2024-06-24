package DBController;

import java.net.InetAddress;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import HelperKit.mysqlConnection;

public class onlineClientDBcontroller {
	static Statement stmt;

	public static void insertIntoOnlineClients(String ip, InetAddress host, mysqlConnection mySql) {

		String query = "INSERT INTO onlineclients (ip, host, status) VALUES ('" + ip + "','" + host + "','online');";

		try {
			stmt = mySql.connection.createStatement();
			stmt.executeUpdate(query);
		} catch (SQLException e) {
			System.out.println("catched in insert function");
			e.printStackTrace();
		}

	}

	public static void DeleteIntoOnlineClients(String ip, mysqlConnection mySql) {
		System.out.println("trying to delete : " + ip);
		String query = "DELETE FROM onlineclients WHERE ip = ?";
		System.out.println("this query " + query);

		try {
			PreparedStatement pstmt = mySql.connection.prepareStatement(query);
			pstmt.setString(1, ip);

			pstmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println("Exception caught in delete function");
			e.printStackTrace();
		}
	}

}
