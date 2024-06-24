package gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import javax.swing.JOptionPane;

import EntityClasses.Message;
import EntityClasses.MessageType;
import EntityClasses.Order;
import client.ClientUI;
import javafx.event.ActionEvent;

public class CasualVisitController {
	@FXML
	private TextField orderIdLbl;
	@FXML
	private TextField num_of_visitors;
	@FXML
	private TextField mailLbl;
	@FXML
	private TextField phoneLbl;
	@FXML
	private Button visitLbl;
	@FXML
	private Button backLbl;
	@FXML 
	private Label errorLbl;
	
	FXMLLoader loader = new FXMLLoader();

	@FXML
	/**
	 * Handles the functionality of the visit button when clicked.
	 *
	 * @param event An ActionEvent representing the event of clicking the visit button
	 * @throws IOException If an input or output exception occurs while handling the event
	 */
	public void visitBtnFunc(ActionEvent event) throws IOException {
	    // Check if all required fields are filled
	    if (orderIdLbl.getText().trim().equals("") || num_of_visitors.getText().trim().equals("") ||
	            mailLbl.getText().trim().equals("") || phoneLbl.getText().trim().equals("")) {
	        errorLbl.setText("You should fill all of the fields");
	    } else {
	        // Get current date and time
	        LocalDate currentDate = LocalDate.now();
	        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	        String dateNow = currentDate.format(dateFormatter);

	        // Get current time in the format HH:mm
	        LocalTime currentTime = LocalTime.now();
	        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
	        String timeNow = currentTime.format(timeFormatter);

	        // Create an Order object and set its properties
	        Order order = new Order();
	        order.setOrdererId(orderIdLbl.getText());
	        order.setDateOfVisit(currentDate);
	        order.setTimeOfVisit(timeNow);
	        order.setNumberOfVisitorsInOrder(num_of_visitors.getText());
	        order.setOrganized(false);
	        order.setMailInOrder(mailLbl.getText());
	        order.setPhoneNumberInOrder(phoneLbl.getText());
	        order.setOrderStatus(true);
	        order.setArrivalTime(timeNow);
	        order.setConfirmed(true);
	        order.setPreOrdered(false);
	        order.setPayed(true);
	        order.setPrice(order.calculateBill(false));
	        order.setParkNameInOrder(ClientUI.chat.client.employee.getParkWorkingAt());

	        // Create a message to send to the server
	        Message messageToServer = new Message(MessageType.CasualVisit, order);
	        ClientUI.chat.accept(messageToServer);

	        // Handle the response from the server
	        if (ClientUI.chat.client.resultFromServer == MessageType.orderAddedToEntryListSuccusefully) {
	            // Show a welcome message and the price to the user
	            JOptionPane.showMessageDialog(null, "Welcome, your price is: " + order.getPrice(), "Info", JOptionPane.INFORMATION_MESSAGE);
	            
	            // Close the current window and open the EntranceWorkerPage
	            Stage primaryStage;
	            ((Node) event.getSource()).getScene().getWindow().hide();
	            primaryStage = new Stage();
	            Pane root;
	            Scene scene;
	            FXMLLoader loader = new FXMLLoader();
	            root = (Pane) loader.load(this.getClass().getResource("/gui/EntranceWorker.fxml").openStream());
	            EntranceWorkerController entranceWorkerController = (EntranceWorkerController) loader.getController();
	            scene = new Scene(root);
	            primaryStage.setTitle("EntranceWorkerPage");
	            primaryStage.setScene(scene);
	            primaryStage.show();
	        }
	    }
	}

	@FXML
	/**
	 * Handles the functionality of the back button when clicked.
	 *
	 * @param event An ActionEvent representing the event of clicking the back button
	 * @throws IOException If an input or output exception occurs while handling the event
	 */
	public void backBtnFunc(ActionEvent event) throws IOException {
	    // Close the current window and open the EntranceWorkerPage
	    Stage primaryStage;
	    ((Node) event.getSource()).getScene().getWindow().hide();
	    primaryStage = new Stage();
	    Pane root;
	    Scene scene;
	    FXMLLoader loader = new FXMLLoader();
	    root = (Pane) loader.load(this.getClass().getResource("/gui/EntranceWorker.fxml").openStream());
	    EntranceWorkerController entranceWorkerController = (EntranceWorkerController) loader.getController();
	    scene = new Scene(root);
	    primaryStage.setTitle("EntranceWorkerPage");
	    primaryStage.setScene(scene);
		scene.getStylesheets().add(getClass().getResource("/gui/graphs.css").toExternalForm());

	    primaryStage.show();
	}

}
