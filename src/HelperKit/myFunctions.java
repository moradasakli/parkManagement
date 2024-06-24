package HelperKit;

import java.net.InetAddress;
import java.sql.SQLException;

import java.sql.Statement;

import Server.EchoServer;

public class myFunctions {
	static Statement stmt;

	public myFunctions() {

	}

	public String[] ConvertMsgToStringArray(Object msg) {

		String st = msg.toString();
		String st1 = st.replace("[", "");
		String st2 = st1.replace("]", "");
		String[] result;
		if (st2 == "empty") {
			result = new String[] { st2 };
			return result;
		} else {
			result = st2.split(",");
			String[] result2 = new String[result.length];

			for (int i = 0; i < result.length; ++i) {
				result2[i] = result[i].replace(" ", "");
			}

			return result2;
		}
	}

	public String serverQueryIdentification(String[] s) {
		String query = new String();
		if (s[0].equals("Connect")) {
			query = "SELECT * FROM order1 WHERE OrderNumber='" + s[1] + "';";
		}

		if (s[0].equals("UpDate")) {
			query = "UPDATE order1 SET ParkName='" + s[2] + "', TelephoneNumber='" + s[3] + "' WHERE OrderNumber ='"
					+ s[1] + "';";
		}

		return query;
	}
	public void insertIntoOnlineClients(String ip, InetAddress host, mysqlConnection mySql) throws SQLException {
		
		String query = "INSERT INTO onlineclients (ip, host, status) VALUES ('" + ip + "','" + host + "','online');";
		
			stmt = mySql.connection.createStatement();
		
		
			stmt.executeUpdate(query);

		

	}
	

}
