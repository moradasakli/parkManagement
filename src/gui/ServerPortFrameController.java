package gui;

import java.net.InetAddress;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import Server.EchoServer;
import Server.ServerUI;
import HelperKit.mysqlConnection;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import EntityClasses.ClientsTable;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ServerPortFrameController {
	@FXML
	private Button importBtn;
	@FXML
	private Button ConnectBtn;
	@FXML
	private TextField portxt;
	@FXML
	private Label ConnLabel;
	@FXML
	private Label NameLabel;
	@FXML
	private Label IPLabel;
	@FXML
	private Label statuslbl;
	@FXML
	private Label iplbl;
	@FXML
	private Button btn;
	@FXML
	private TableView<ClientsTable> onlineclients;
	@FXML
	private TableColumn<ClientsTable, String> ipCol;
	@FXML
	private TableColumn<ClientsTable, String> hostCol;
	@FXML
	private TableColumn<ClientsTable, String> statusCol;
	@FXML
	private Label portLbl;
	@FXML
	private TextField dbNameTextField;

	@FXML
	private TextField dbUsernameTextField;

	@FXML
	private TextField dbPasswordTextField;

	@FXML

	public void initialize() {
		portxt.setText("5555");
		dbNameTextField.setText("jdbc:mysql://localhost/finalProject?serverTimezone=IST");
		dbUsernameTextField.setText("root");
		dbPasswordTextField.setText("morad12345");// for Test only
	}

	public void ConnectFunc(ActionEvent event) throws Exception {
		String p = portxt.getText();
		if (p.trim().isEmpty()) {
			portLbl.setText("You must enter a port number");
		} else {

			ServerUI.runServer(dbNameTextField.getText(), dbUsernameTextField.getText(), dbPasswordTextField.getText(),
					p);
			ConnLabel.setText("Connected");
			IPLabel.setText(InetAddress.getLocalHost().getHostAddress());
			NameLabel.setText(InetAddress.getLocalHost().getHostName());
		}
	}

	public void ConnecttoImportBtnFunc(ActionEvent event) throws Exception {
		mysqlConnection mySql2 = null;
		try {
			String url = "jdbc:mysql://localhost/employeesystem?serverTimezone=IST";
			mySql2 = new mysqlConnection(url, "root", "morad12345");

			// Create the employees table in the finalproject schema
			createEmployeesTable(EchoServer.mySql.connection);

			
			
			// Copy data from employeesystem.employees to finalproject.employees
			copyDataFromEmployeesSystem(mySql2.connection, EchoServer.mySql.connection);
			createTravelGuidesTable(EchoServer.mySql.connection);
			copyDataFromTravelGuidesSystem(mySql2.connection, EchoServer.mySql.connection);
			System.out
					.println("Table 'employees' created in finalproject schema with data from employeesystem schema.");
		} catch (SQLException e) {
			System.out.println("Error: " + e.getMessage());
			e.printStackTrace();
		}
	}

	public static void createEmployeesTable(Connection connection) throws SQLException {
		String createTableQuery = "CREATE TABLE finalproject.employees (" + "workerid INT PRIMARY KEY AUTO_INCREMENT,"
				+ "name VARCHAR(255)," + "lastname VARCHAR(255)," + "mail VARCHAR(255)," + "role VARCHAR(255),"
				+ "park VARCHAR(255)," + "username VARCHAR(255)," + "password VARCHAR(255)," + "islogged INT DEFAULT 0"
				+ ")";

		PreparedStatement statement = connection.prepareStatement(createTableQuery);
		statement.executeUpdate();
		statement.close();
	}

	public static void copyDataFromEmployeesSystem(Connection sourceConnection, Connection targetConnection)
			throws SQLException {
		String selectQuery = "SELECT * FROM employeesystem.employees";
		PreparedStatement selectStmt = sourceConnection.prepareStatement(selectQuery);
		ResultSet resultSet = selectStmt.executeQuery();

		String insertQuery = "INSERT INTO finalproject.employees "
				+ "(workerid, name, lastname, mail, role, park, username, password, islogged) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
		PreparedStatement insertStmt = targetConnection.prepareStatement(insertQuery);

		while (resultSet.next()) {
			insertStmt.setInt(1, resultSet.getInt("workerid"));
			insertStmt.setString(2, resultSet.getString("name"));
			insertStmt.setString(3, resultSet.getString("lastname"));
			insertStmt.setString(4, resultSet.getString("mail"));
			insertStmt.setString(5, resultSet.getString("role"));
			insertStmt.setString(6, resultSet.getString("park"));
			insertStmt.setString(7, resultSet.getString("username"));
			insertStmt.setString(8, resultSet.getString("password"));
			insertStmt.setInt(9, resultSet.getInt("islogged"));

			insertStmt.executeUpdate();
		}

	}

	public static void createTravelGuidesTable(Connection connection) throws SQLException {
	    String createTableQuery = "CREATE TABLE finalproject.travelguides ("
	            + "id INT PRIMARY KEY AUTO_INCREMENT,"
	            + "name VARCHAR(50),"
	            + "lastname VARCHAR(50),"
	            + "mail VARCHAR(100),"
	            + "phone VARCHAR(20),"
	            + "isActive INT DEFAULT NULL"
	            + ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci";

	    PreparedStatement statement = connection.prepareStatement(createTableQuery);
	    statement.executeUpdate();
	    statement.close();
	}

	public static void copyDataFromTravelGuidesSystem(Connection sourceConnection, Connection targetConnection)
	        throws SQLException {
	    String selectQuery = "SELECT * FROM employeesystem.travelguides";
	    PreparedStatement selectStmt = sourceConnection.prepareStatement(selectQuery);
	    ResultSet resultSet = selectStmt.executeQuery();

	    String insertQuery = "INSERT INTO finalproject.travelguides "
	            + "(name, lastname, mail, phone, isActive) "
	            + "VALUES (?, ?, ?, ?, ?)";
	    PreparedStatement insertStmt = targetConnection.prepareStatement(insertQuery);

	    while (resultSet.next()) {
	        insertStmt.setString(1, resultSet.getString("name"));
	        insertStmt.setString(2, resultSet.getString("lastname"));
	        insertStmt.setString(3, resultSet.getString("mail"));
	        insertStmt.setString(4, resultSet.getString("phone"));
	        insertStmt.setInt(5, resultSet.getInt("isActive"));

	        insertStmt.executeUpdate();
	    }

	    // Close resources
	    resultSet.close();
	    selectStmt.close();
	    insertStmt.close();
	}

	
	public void btnRefresh(ActionEvent event) throws Exception {
		ipCol.setCellValueFactory(new PropertyValueFactory<ClientsTable, String>("ip"));
		hostCol.setCellValueFactory(new PropertyValueFactory<ClientsTable, String>("host"));
		statusCol.setCellValueFactory(new PropertyValueFactory<ClientsTable, String>("status"));
		onlineclients.setItems(getDataUsers());
		System.out.println(getDataUsers().toString());

	}

	public void start(Stage primaryStage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("/gui/ServerPort.fxml"));
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("/gui/server.css").toExternalForm());

		primaryStage.setTitle("Server");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public static ObservableList<ClientsTable> getDataUsers() {

		ObservableList<ClientsTable> list = FXCollections.observableArrayList();
		try {
			PreparedStatement ps = EchoServer.mySql.connection.prepareStatement("select * from onlineclients");
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				list.add(new ClientsTable(rs.getString("ip"), rs.getString("host"), rs.getString("status")));
			}

		} catch (Exception e) {

		}

		return list;
	}// end getDataUsers
 
}
//last update : 23/3 , 00:00
