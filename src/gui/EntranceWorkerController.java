package gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import EntityClasses.*;
import client.ClientUI;
import javafx.event.ActionEvent;

import javafx.scene.control.Label;

/**
 * @author morad
 *
 */
public class EntranceWorkerController {
	@FXML
	private Button CheckAvailableSpotsBtn;
	@FXML
	private Button ShowBtn;
	@FXML
	private Button Backbtn;
	@FXML
	private TextField RecipeTxtField;
	@FXML
	private Label avaliable_spots_label;
	@FXML
	private Label price_label;
	@FXML
	private DatePicker dateDP;
	@FXML
	private ComboBox timeCB;
	@FXML
	private Label welcomeLbl;

	@FXML
	private ComboBox timeexitCB;
	FXMLLoader loader = new FXMLLoader();
	@FXML
	private Button entryBtn;
	@FXML
	private Button exitBtn;

	private boolean idIsChecked = false;

	/**
	 * this function to initialize the combo box's that we have
	 */
	@FXML
	public void initialize() {
		/*
		 * timeCB.getItems().addAll("08:00", "08:10", "08:20", "08:30", "08:40",
		 * "08:50", "09:00", "09:10", "09:20", "09:30", "09:40", "09:50", "10:00",
		 * "10:10", "10:20", "10:30", "10:40", "10:50", "11:00", "11:10", "11:20",
		 * "11:30", "11:40", "11:50", "12:00", "12:10", "12:20", "12:30", "12:40",
		 * "12:50", "13:00", "13:10", "13:20", "13:30", "13:40", "13:50", "14:00",
		 * "14:10", "14:20", "14:30", "14:40", "14:50", "15:00", "15:10", "15:20",
		 * "15:30", "15:40", "15:50", "16:00");
		 */
		timeexitCB.getItems().addAll("08:00", "08:10", "08:20", "08:30", "08:40", "08:50", "09:00", "09:10", "09:20",
				"09:30", "09:40", "09:50", "10:00", "10:10", "10:20", "10:30", "10:40", "10:50", "11:00", "11:10",
				"11:20", "11:30", "11:40", "11:50", "12:00", "12:10", "12:20", "12:30", "12:40", "12:50", "13:00",
				"13:10", "13:20", "13:30", "13:40", "13:50", "14:00", "14:10", "14:20", "14:30", "14:40", "14:50",
				"15:00", "15:10", "15:20", "15:30", "15:40", "15:50", "16:00");

		welcomeLbl.setText(ClientUI.chat.client.employee.getEmployeeFirstName() + "park is  : ");
	}

	/**
	 * @param event casusal visit is a button when the entrance worker want to make
	 *              an unplanned visit
	 * @throws IOException
	 */
	public void casualVisit(ActionEvent event) throws IOException {
		Stage primaryStage;
		((Node) event.getSource()).getScene().getWindow().hide();
		primaryStage = new Stage();
		Pane root = (Pane) loader.load(this.getClass().getResource("/gui/CasualVisit.fxml").openStream());
		CasualVisitController CasualVisitoginController = (CasualVisitController) loader.getController();
		Scene scene = new Scene(root);
		primaryStage.setTitle("CasualVisit");
		primaryStage.setScene(scene);
		scene.getStylesheets().add(getClass().getResource("/gui/graphs.css").toExternalForm());

		primaryStage.show();
	}

	/**
	 * @param event pressing this button check the available spots now in the park
	 */
	@FXML
	public void CheckSpotsFunc(ActionEvent event) {

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
	 * @param event button to show the reciept for an specific order
	 */
	@FXML
	public void ShowRecipeFunc(ActionEvent event) {
		// getting the reciept number
		String reciept_number = RecipeTxtField.getText().trim();
		// getting the number of the park
		int park_num = Integer.valueOf(ClientUI.chat.client.employee.getParkWorkingAt());
		// check if order number was typed
		if (RecipeTxtField.getText().trim().equals("")) {
			price_label.setText("please enter reciept number!");
		} else {
			Order order = new Order(park_num, reciept_number);
			Message messageToServer = new Message(MessageType.GetPriceForRecipe, order);
			ClientUI.chat.accept(messageToServer); // ***cant reach EchoServer
			Order o = ClientUI.chat.client.order;

			if (ClientUI.chat.client.resultFromServer == MessageType.ErrorGettingPrice || !o.isOrderStatus()) {
				idIsChecked = false;

				price_label.setText("order num was not found or have been cancelled ! ");
			} else if (ClientUI.chat.client.resultFromServer == MessageType.GettingPriceSucces) {

				idIsChecked = true;
				price_label.setText("Details for Order number : " + RecipeTxtField.getText() + " \n" + "ordererId : "
						+ o.getOrdererId() + "\n price : " + o.getPrice() + "NIS\n number of visitor : "
						+ o.getNumberOfVisitorsInOrder() + "\n order paid : " + (o.isPayed() ? "Yes" : "No"));
			}
		}
	}

	/**
	 * @param event this button gets back to the relavent page
	 * @throws IOException
	 */
	@FXML
	public void ReturnToRelaventPageFunc(ActionEvent event) throws IOException {
		// send to server to sign the employee out
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
	 * getting the specefic order into the park entry list
	 * 
	 * @param event
	 * @throws IOException
	 */
	@FXML
	public void enterPark(ActionEvent event) throws IOException {
		// first check if the order is typed to show the entrance worker the price and
		// info
		if(timeexitCB.getItems().equals("")) {
			price_label.setText("enter the time of visit/exit please. ");

		}
		else {
		if (idIsChecked) {
			Order o = ClientUI.chat.client.order;
			System.out.println("order : " + o.toString());
			o.setParkNameInOrder(ClientUI.chat.client.employee.getParkWorkingAt());
			o.setArrivalTime((String) timeexitCB.getValue());
			// sending to the server a message that a tourist has arrived
			Message messageToServer = new Message(MessageType.OrderEntringThePark, o);
			ClientUI.chat.accept(messageToServer);
			// this section checks if the tourist arrived on time or there's an error while
			// entering the tourist
			if (ClientUI.chat.client.resultFromServer == MessageType.orderAddedToEntryListSuccusefully) {
				JOptionPane.showMessageDialog(null, "weclome the tourist", "Info", JOptionPane.INFORMATION_MESSAGE);
			} else if (ClientUI.chat.client.resultFromServer == MessageType.orderAddedToEntryListFailed)
				JOptionPane.showMessageDialog(null, "failed to add order, contact manger please.", "Info",
						JOptionPane.INFORMATION_MESSAGE);
			else if (ClientUI.chat.client.resultFromServer == MessageType.orderCameLateToVisit) {
				JOptionPane.showMessageDialog(null,
						"you are late, please ask the worker if there's any available spots for you.", "Info",
						JOptionPane.INFORMATION_MESSAGE);
			}
			idIsChecked=false;
			
		}

		else {
			price_label.setText("check the order if exist first !  ");

		}
		}
	}

	/**
	 * @param event this button to check out a tourist from the park
	 * @throws IOException
	 */
	public void exitpark(ActionEvent event) throws IOException {
		Order exitOrder = new Order();
		exitOrder.setExitTime((String) timeexitCB.getValue());
		exitOrder.setOrderNumber((String) RecipeTxtField.getText());
		int park_num = Integer.parseInt(ClientUI.chat.client.employee.getParkWorkingAt());
		exitOrder.setPark_num(park_num);
		Message messageToServer = new Message(MessageType.OrderLeavingPark, exitOrder);
		ClientUI.chat.accept(messageToServer);
		if (ClientUI.chat.client.resultFromServer == MessageType.orderDeletedFromEntryAndMovedToExit) {
			JOptionPane.showMessageDialog(null, "Hope to see you very soon ! ", "Info",
					JOptionPane.INFORMATION_MESSAGE);

		} else if (ClientUI.chat.client.resultFromServer == MessageType.orderDeletedFromEntryFailed) {
			JOptionPane.showMessageDialog(null, "Error while updating park details , contact manger ! ", "Info",
					JOptionPane.INFORMATION_MESSAGE);

		} else if (ClientUI.chat.client.resultFromServer == MessageType.orderDeletedFromEntrynotFound) {
			JOptionPane.showMessageDialog(null, "check the order number again! ", "Info",
					JOptionPane.INFORMATION_MESSAGE);

		}

	}
}
