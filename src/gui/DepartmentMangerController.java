package gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import DBController.ParkDB;
import DBController.RequestDB;
import EntityClasses.Message;
import EntityClasses.MessageType;
import EntityClasses.Request;
import client.ClientUI;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;

public class DepartmentMangerController {
	@FXML
	private Button showRequestsBtn;
	@FXML
	private TextField reqNumTxt;
	@FXML
	private Button acceptBtn;
	@FXML
	private Button rejectBtn;
	@FXML
	private Button checkSpotsBtn;
	@FXML
	private Button reportsBtn;
	@FXML
	private Button refresh_btn;
	@FXML
	private Button backBtn;
	@FXML
	private Label avaliable_spots_label;
	@FXML
	private Label request_Label;
	@FXML
	private TableView<Request> requestTable;
	@FXML
	private TableColumn<Request, Integer> request_number;
	@FXML
	private TableColumn<Request, String> park_number;
	@FXML
	private TableColumn<Request, String> request_type;
	@FXML
	private TableColumn<Request, Integer> new_value;
	@FXML
	private TableColumn<Request, Boolean> status;
	@FXML
	private TableColumn<Request, Boolean> is_aproved;
	@FXML
	private ComboBox monthCheckAvailableSpots;
	FXMLLoader loader = new FXMLLoader();
	
	public void initialize() {
		monthCheckAvailableSpots.getItems().addAll("1", "2", "3");
	}

	/**
	 * Retrieves and displays all requests for all parks when the showRequestsBtn is
	 * clicked. Populates the requestTable with the fetched requests.
	 *
	 * @param event The ActionEvent representing the button click.
	 */
	// Event Listener on Button[#showRequestsBtn].onAction
	@FXML
	public void showRequets(ActionEvent event) {

		Message messageToServer = new Message(MessageType.ShowRequestsAllParks, null);
		ClientUI.chat.accept(messageToServer);

		if (ClientUI.chat.client.showRequestsAllParksStatus) {
			if (!ClientUI.chat.client.All_Requests_List.isEmpty()) {
				List<Request> l = ClientUI.chat.client.All_Requests_List;
				ObservableList<Request> list = FXCollections.observableArrayList();

				list.addAll(l);

				request_number.setCellValueFactory(new PropertyValueFactory<Request, Integer>("requestNumber"));
				park_number.setCellValueFactory(new PropertyValueFactory<Request, String>("requestedInPark"));
				request_type.setCellValueFactory(new PropertyValueFactory<Request, String>("requestType"));
				new_value.setCellValueFactory(new PropertyValueFactory<Request, Integer>("newValue"));
				status.setCellValueFactory(new PropertyValueFactory<Request, Boolean>("requestStatus"));
				is_aproved.setCellValueFactory(new PropertyValueFactory<Request, Boolean>("Approved"));
				requestTable.setItems(list);

				request_Label.setText("Succes!");
			} else {
				request_Label.setText("there are not any requests!");
			}
		} else {
			request_Label.setText("no requests for now ! ");
		    requestTable.setItems(FXCollections.emptyObservableList()); // Empty the table

		}

	}

	/**
	 * Accepts a request specified by the request number entered in the reqNumTxt
	 * field when the acceptBtn is clicked. Updates the request status accordingly
	 * and displays the result.
	 *
	 * @param event The ActionEvent representing the button click.
	 * @throws SQLException If a database access error occurs.
	 */
	// Event Listener on Button[#acceptBtn].onAction
	@FXML
	public void accept(ActionEvent event) throws SQLException {
		int requestnum = Integer.parseInt(reqNumTxt.getText().trim());

		// Message a=RequestDB.acceptRequest(22);
		Message messageToServer = new Message(MessageType.AcceptRequest, requestnum);
		ClientUI.chat.accept(messageToServer);

		if (!ClientUI.chat.client.AcceptOrRejectRequestStatusIdNotFound) {
			if (ClientUI.chat.client.AcceptOrRejectRequestStatus)
				request_Label.setText("the request " + ClientUI.chat.client.request_num + " has been accepted!");
			else {
				request_Label.setText("Error, accepting the request " + ClientUI.chat.client.request_num + "!");
			}
		} else {
			request_Label.setText("Error, Id not found " + ClientUI.chat.client.request_num + "!");
		}

	}

	/**
	 * Rejects a request specified by the request number entered in the reqNumTxt
	 * field when the rejectBtn is clicked. Updates the request status accordingly
	 * and displays the result.
	 *
	 * @param event The ActionEvent representing the button click.
	 */
	// Event Listener on Button[#rejectBtn].onAction
	@FXML
	public void reject(ActionEvent event) {
		int requestnum = Integer.parseInt(reqNumTxt.getText().trim());
		Message messageToServer = new Message(MessageType.RejectRequest, requestnum);
		ClientUI.chat.accept(messageToServer);

		if (ClientUI.chat.client.AcceptOrRejectRequestStatus)
			request_Label.setText("the request " + ClientUI.chat.client.request_num + " has been rejected!");
		else {
			request_Label.setText("Error, rejecting the request " + ClientUI.chat.client.request_num + "!");
		}
	}

	/**
	 * Retrieves and displays the available spots in the park where the department
	 * manager is assigned when the checkSpotsBtn is clicked. Displays the result in
	 * the avaliable_spots_label.
	 *
	 * @param event The ActionEvent representing the button click.
	 */
	// Event Listener on Button[#checkSpotsBtn].onAction
	@FXML
	public void showSpots(ActionEvent event) {
		if (monthCheckAvailableSpots.getValue() == null) {

			avaliable_spots_label.setText("please, choose park first.");

		} else {

			// Get the current date and time
			LocalDateTime now = LocalDateTime.now();

			// Format date and time using DateTimeFormatter
			DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

			// Format date and time into strings
			String formattedDate = now.format(dateFormatter);
			String formattedTime = now.format(timeFormatter);
			String park_num = ClientUI.chat.client.employee.getParkWorkingAt();
			ArrayList<String> infoList = new ArrayList<>();
			infoList.add((String) monthCheckAvailableSpots.getValue());
			infoList.add(formattedDate);
			infoList.add(formattedTime);

			Message messageToServer = new Message(MessageType.GetAvailableSpots, infoList);
			ClientUI.chat.accept(messageToServer);

			if (ClientUI.chat.client.available_spots == -1)
				avaliable_spots_label.setText("Error getting available spots!");
			else {
				avaliable_spots_label
						.setText("the available spots in park" + (String) monthCheckAvailableSpots.getValue() + " is : "
								+ ClientUI.chat.client.available_spots);
			}
		}
	}

	/**
	 * Redirects to the DepartmentMangerReportsPage when the reportsBtn is clicked.
	 *
	 * @param event The ActionEvent representing the button click.
	 * @throws IOException If an input or output exception occurs.
	 */
	// Event Listener on Button[#reportsBtn].onAction
	@FXML
	public void openReportPage(ActionEvent event) throws IOException {

		Stage primaryStage;
		((Node) event.getSource()).getScene().getWindow().hide();
		primaryStage = new Stage();
		Pane root = (Pane) loader.load(this.getClass().getResource("/gui/DepartmentMangerReports.fxml").openStream());
		DepartmentMangerReportsController departmentMangerReportsController = (DepartmentMangerReportsController) loader
				.getController();
		Scene scene = new Scene(root);
		primaryStage.setTitle("DepartmentMangerReportsPage");
		primaryStage.setScene(scene);
		scene.getStylesheets().add(getClass().getResource("/gui/graphs.css").toExternalForm());

		primaryStage.show();
	}

	/**
	 * Logs out the employee and returns to the EmployeeLoginPage when the backBtn
	 * is clicked.
	 *
	 * @param event The ActionEvent representing the button click.
	 * @throws IOException If an input or output exception occurs.
	 */
	// Event Listener on Button[#backBtn].onAction
	@FXML
	public void back(ActionEvent event) throws IOException {
		Message messageToServer = new Message(MessageType.EmployeeLogout, ClientUI.chat.client.employee);
		ClientUI.chat.accept(messageToServer);

		Stage primaryStage;
		((Node) event.getSource()).getScene().getWindow().hide();
		primaryStage = new Stage();
		Pane root = (Pane) loader.load(this.getClass().getResource("/gui/EmployeeLogin.fxml").openStream());
		EmployeeLoginController EmployeeLoginController = (EmployeeLoginController) loader.getController();
		Scene scene = new Scene(root);

		primaryStage.setTitle("EmployeeLoginPage");
		primaryStage.setScene(scene);
		primaryStage.show();
	}
}
