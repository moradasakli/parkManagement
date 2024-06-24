package gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;

import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import DBController.ParkDB;
import EntityClasses.ClientsTable;
import EntityClasses.Message;
import EntityClasses.MessageType;
import EntityClasses.Park;
import EntityClasses.Report;
import EntityClasses.Request;
import Server.EchoServer;
import client.ClientUI;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;

import javafx.scene.control.Label;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;

public class ParkMangerController {
	@FXML
	private Label ParkMangerLbl;
	@FXML
	private Label EditLbl;
	@FXML
	private ComboBox EditCBox;
	@FXML
	private TextField AskTxtField;
	@FXML
	private Button AskBtn;
	@FXML
	private ComboBox report_type_combo;
	@FXML
	private ComboBox report_month_combo;
	@FXML
	private Button send_report_btn;
	@FXML
	private Button CheckAvailableSpotBtn;
	@FXML
	private Label avaliable_spots_label;
	@FXML
	private Button BackBtn;
	@FXML
	private Button ShowRequestsBtn;
	@FXML
	private Button refresh_btn;
	@FXML
	private Label show_requests_label;
	@FXML
	private Label ask_request_label;
	@FXML
	private Label send_reports_label;
	@FXML
	private TableView<Request> requestTable;
	@FXML
	private TableColumn<Request, Integer> request_number;
	@FXML
	private TableColumn<Request, String> park_number;
	@FXML
	private TableColumn<Request, String> request_type;
	@FXML
	private TableColumn<Request, String> old_value;
	@FXML
	private TableColumn<Request, Integer> new_value;
	@FXML
	private TableColumn<Request, Boolean> status;
	@FXML
	private TableColumn<Request, Boolean> is_approved;


	
	FXMLLoader loader = new FXMLLoader();

	private transient ObservableList<Request> list = FXCollections.observableArrayList();

	  /**
     * Initializes the controller.
     */
	@FXML
	public void initialize() {
		EditCBox.getItems().addAll("max capacity", "reserved spaces", "default duration");
		report_type_combo.getItems().addAll("Number Of Visitors Report","Usage Report");
		report_month_combo.getItems().addAll("1","2","3","4","5","6","7","8","9","10","11","12");
	}

    /**
     * Handles the action event when the "Ask Permission" button is clicked.
     * Sends a request for permission to the server based on the selected type and value.
     * Displays a success message if the request is added successfully, otherwise displays an error message.
     * 
     * @param event The event generated when the button is clicked.
     */
	// Event Listener on Button[#AskBtn].onAction
	@FXML
	public void AskPremission(ActionEvent event) {
		String park_num = ClientUI.chat.client.employee.getParkWorkingAt();
		String requestType = (String) EditCBox.getValue();
		
		switch (requestType)
		{
		case "max capacity":
		{
			requestType="max_capacity";
			break;
		}
		case "reserved spaces":
		{
			requestType="number_of_reserved_spaces";
			break;
		}
		case "default duration":
		{
			requestType="default_duration_for_visit";
			break;
		}
		}
		
		if (!(EditCBox.getSelectionModel().getSelectedItem() == null)) {
			if (!AskTxtField.getText().equals("")) {
				int new_value = Integer.parseInt(AskTxtField.getText().trim());
			
				Request new_request = new Request(park_num, requestType, new_value);

				Message messageToServer = new Message(MessageType.AskPremission, new_request);
				ClientUI.chat.accept(messageToServer);

				if (ClientUI.chat.client.new_request != null) {
					ask_request_label.setText("the new request has been added!");
				} else {
					ask_request_label.setText("Error ,the new request has not been added!");
				}
			}
			else {
				ask_request_label.setText("please fill a value!");
			}

		} else {
			ask_request_label.setText("please fill the type of request!");
		}

	}

	
	   /**
     * Handles the action event when the "Send Report" button is clicked.
     * Sends a report to the server based on the selected report type and month.
     * Displays a success message if the report is added successfully, otherwise displays an error message.
     * 
     * @param event The event generated when the button is clicked.
     */
	// Event Listener on Button[#SendBtn].onAction
	@FXML
	public void SendReport(ActionEvent event) {
		String report_type=(String) report_type_combo.getValue();
		int month=Integer.parseInt( (String) report_month_combo.getValue());
		String park_num=ClientUI.chat.client.employee.getParkWorkingAt();
		int id=-1;
		if(report_type.equals("Number Of Visitors Report"))
			report_type="Number Of Visitors";
		Report new_report=new Report(id,park_num,month,report_type);
		Message messageToServer;
		
		switch (report_type)
		{
		case "Number Of Visitors":
			messageToServer = new Message(MessageType.CreateNewNumberOfVisitorsReport, new_report);
			ClientUI.chat.accept(messageToServer);
			
			
			break;
		case "Usage Report":	
			messageToServer = new Message(MessageType.CreateNewUsageReport, new_report);
			ClientUI.chat.accept(messageToServer);
			break;
		}
		
		if(ClientUI.chat.client.create_number_of_visitors_report_status)
		{
			send_reports_label.setText(" number of visitors report has been added succesfully");
		}
		else
		{
			if(ClientUI.chat.client.create_Usage_Report_status)
			{
				send_reports_label.setText("the usage report has been created succesfully ");
			}
			else
			{
				send_reports_label.setText("error");
			}
		}
			
		
	}

	 /**
     * Handles the action event when the "Check Available Spots" button is clicked.
     * Retrieves and displays the available spots in the park from the server.
     * 
     * @param event The event generated when the button is clicked.
     */
	// Event Listener on Button[#CheckAvailableSpotBtn].onAction
	@FXML
	public void ShowSpots(ActionEvent event) {
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
		infoList.add(park_num);
		infoList.add(formattedDate);
		infoList.add(formattedTime);
		

		Message messageToServer = new Message(MessageType.GetAvailableSpots, infoList);
		ClientUI.chat.accept(messageToServer);


		if (ClientUI.chat.client.available_spots == -1)
			avaliable_spots_label.setText("Error getting available spots!");
		else {
			avaliable_spots_label.setText(
					"the available spots in park" + park_num + " is : " + ClientUI.chat.client.available_spots);
		}
	}

	  /**
     * Handles the action event when the "Back" button is clicked.
     * Redirects the user to the employee login page after logging out.
     * 
     * @param event The event generated when the button is clicked.
     * @throws IOException If there is an error loading the FXML file or creating the scene.
     */
	// Event Listener on Button[#BackBtn].onAction
	@FXML
	public void RetunToRelaventPage(ActionEvent event) throws IOException {
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
		scene.getStylesheets().add(getClass().getResource("/gui/graphs.css").toExternalForm());

		primaryStage.show();
	}

	 /**
     * Handles the action event when the "Show Requests" button is clicked.
     * Retrieves and displays the requests for the current park from the server.
     * 
     * @param event The event generated when the button is clicked.
     * @throws SQLException If a database access error occurs.
     */
	// Event Listener on Button[#ShowRequestsBtn].onAction
	@FXML
	public void ShowRequests(ActionEvent event) throws SQLException {

		Message messageToServer = new Message(MessageType.ShowRequestsOnePark,(String) ClientUI.chat.client.employee.getParkWorkingAt());
		ClientUI.chat.accept(messageToServer);

		List<Request> l= ClientUI.chat.client.One_Requests_List;
		 ObservableList<Request> list = FXCollections.observableArrayList();

		if (l == null) {

			show_requests_label.setText("Error, try again!");
		}
		else if ( l.size()==0) {
			show_requests_label.setText("No requests for now!");

		}
		else {
		    list.addAll(l);
		    request_number.setCellValueFactory(new PropertyValueFactory<Request, Integer>("requestNumber"));
		    park_number.setCellValueFactory(new PropertyValueFactory<Request, String>("requestedInPark"));
		    request_type.setCellValueFactory(new PropertyValueFactory<Request, String>("requestType"));
		    new_value.setCellValueFactory(new PropertyValueFactory<Request, Integer>("newValue"));
		    status.setCellValueFactory(new PropertyValueFactory<Request, Boolean>("requestStatus"));
		    is_approved.setCellValueFactory(new PropertyValueFactory<Request, Boolean>("Approved"));
		    requestTable.setItems(list);
		      
		     
			
			show_requests_label.setText("Succes!");
		}

	}

}
