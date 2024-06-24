package HelperKit;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class mysqlConnection {
	private String url;
	private String username;
	private String password;

	public static Connection connection;
	public mysqlConnection(String url, String username, String password) {
		this.url = url;
		this.username = username;
		this.password = password;
		createConnection();
	}
	
	public void createConnection() {

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			System.out.println("Driver definition succeed");
//			EchoServer.ServerLog.add(EchoServer.getTime() + "> Driver definition succeed.");
		} catch (Exception ex) {
			System.out.println("Driver definition failed");
//			EchoServer.ServerLog.add(EchoServer.getTime() + "> Driver definition failed.");
		}
		try {
			connection = (Connection) DriverManager.getConnection(url, username, password);
			System.out.println("SQL connection succeed");
//			EchoServer.ServerLog.add(EchoServer.getTime() + "> SQL connection succeed.");
		} catch (SQLException ex) {
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}
	
	}
}
