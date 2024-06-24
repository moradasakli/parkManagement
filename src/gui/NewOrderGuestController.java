package gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Optional;

import javafx.event.ActionEvent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import java.awt.JobAttributes.DialogType;
import javax.swing.JOptionPane;

import Server.EchoServer;
import DBController.TouristsDBcontroller;
import EntityClasses.Order;
import EntityClasses.Message;
import EntityClasses.MessageType;
import client.ChatClient;
import client.ClientUI;

public class NewOrderGuestController {
	@FXML
	private TextField nameTxt;
	@FXML
	private TextField lastNameTxt;
	@FXML
	private TextField idTxt;
	@FXML
	private ComboBox parkCB;
	@FXML
	private DatePicker dateDP;
	@FXML
	private ComboBox timeCB;
	@FXML
	private TextField visitorsTxt;
	@FXML
	private CheckBox orgnaizedCB;
	@FXML
	private TextField emailTxt;
	@FXML
	private TextField phoneTxt;
	@FXML
	private Button bookBtn;
	@FXML
	private Button cancelBtn;
	@FXML
	private Label errorMsgLbl;

	FXMLLoader loader = new FXMLLoader();
	// Event Listener on Button[#bookBtn].onAction

	public static final int MAX_NUMBER_OF_VISITORS = 15;

	@FXML
	public void initialize() {
		parkCB.getItems().addAll("Park A", "Park B", "Park C");
		timeCB.getItems().addAll("08:00", "09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00");

		Order order = ClientUI.chat.client.order;
		//Order order = AlternativeOptionController.order;
		if (AlternativeOptionController.backBtnPressed && (order != null)) {
			System.out.println("FILLING ORDER INFO FOR GUEST - AFTER PRESSING BACK");
			nameTxt.setText(order.getOrdererFirstName());
			lastNameTxt.setText(order.getOrdererLastName());
			idTxt.setText(order.getOrdererId());
			parkCB.setValue(order.getParkNameInOrder());
			dateDP.setValue(order.getDateOfVisit());
			timeCB.setValue(order.getTimeOfVisit());
			visitorsTxt.setText(order.getNumberOfVisitorsInOrder());
			orgnaizedCB.setSelected(order.isOrganized());
			emailTxt.setText(order.getMailInOrder());
			phoneTxt.setText(order.getPhoneNumberInOrder());
			AlternativeOptionController.backBtnPressed = false;
		}
		

		Order orderTest = AlternativeOptionController.copyTest;
		if (AlternativeOptionController.backBtnPressed && (orderTest != null)) {
			System.out.println("FILLING ORDER INFO FOR GUEST - AFTER PRESSING BACK");
			nameTxt.setText(orderTest.getOrdererFirstName());
			lastNameTxt.setText(orderTest.getOrdererLastName());
			idTxt.setText(orderTest.getOrdererId());
			parkCB.setValue(orderTest.getParkNameInOrder());
			dateDP.setValue(orderTest.getDateOfVisit());
			timeCB.setValue(orderTest.getTimeOfVisit());
			visitorsTxt.setText(orderTest.getNumberOfVisitorsInOrder());
			orgnaizedCB.setSelected(orderTest.isOrganized());
			emailTxt.setText(orderTest.getMailInOrder());
			phoneTxt.setText(orderTest.getPhoneNumberInOrder());
			AlternativeOptionController.backBtnPressed = false;
		}

	}

	@FXML
	public void bookNewOrder(ActionEvent event) throws IOException {
		Order guestOrder = validateOrderInfo();
		if (guestOrder != null) {
			Message messageToServer = new Message(MessageType.GuestTouristNewOrder, guestOrder);
			ClientUI.chat.accept(messageToServer);

			if (ChatClient.resultFromServer == MessageType.GuestAlreadyRegistered) {
				JOptionPane.showMessageDialog(null, "You are already registered , please sign in to make a new order",
						"Error", JOptionPane.ERROR_MESSAGE);
			}
			if (ChatClient.resultFromServer == MessageType.GuestNotRegisteredAsTravelGuide) {
				JOptionPane.showMessageDialog(null,
						"You can't make order for organized groups , contact our service representative to be registered as a travel guide first",
						"Error", JOptionPane.ERROR_MESSAGE);
			}

			if (ChatClient.resultFromServer == MessageType.GuestOrderSuccess) {
				JOptionPane.showMessageDialog(null,
						"We booked your new order , press OK to proceed to payment options.\n", "Info",
						JOptionPane.INFORMATION_MESSAGE);
				PaymentController.isNewTourist = true;
				Stage primaryStage;
				((Node) event.getSource()).getScene().getWindow().hide();
				primaryStage = new Stage();
				Pane root = (Pane) loader.load(this.getClass().getResource("/gui/Payment.fxml").openStream());
				PaymentController PaymentController = (PaymentController) loader.getController();
				Scene scene = new Scene(root);
				primaryStage.setTitle("PaymentPage");
				primaryStage.setScene(scene);
				primaryStage.show();

			}

			if (ChatClient.resultFromServer == MessageType.GuestGoingForAlternativeOptions) {
				JOptionPane.showMessageDialog(null,
						"There are NO available spaces at the park at this time , we will offer you alternative options.",
						"Info", JOptionPane.INFORMATION_MESSAGE);
				// load alternative options page
				Stage primaryStage;
				((Node) event.getSource()).getScene().getWindow().hide();
				primaryStage = new Stage();
				Pane root = (Pane) loader.load(this.getClass().getResource("/gui/AlternativeOption.fxml").openStream());
				AlternativeOptionController AlternativeOptionController = (AlternativeOptionController) loader
						.getController();
				Scene scene = new Scene(root);
				primaryStage.setTitle("AlternativeOptionPage");
				primaryStage.setScene(scene);
				primaryStage.show();
			}

		} else {
			System.out.println("NULL ORDER");
		}

	}// end bookNewOrder

	public static boolean isValidNumber(String number) {
		String temp = "\\d+";
		return number.matches(temp);
	}

	public Order validateOrderInfo() throws IOException {

		if (nameTxt.getText().trim().isEmpty()) {
			errorMsgLbl.setText("Please fill your name");
		} else if (lastNameTxt.getText().trim().isEmpty()) {
			errorMsgLbl.setText("Please fill your last name");
		}

		else if (idTxt.getText().trim().isEmpty()) {
			errorMsgLbl.setText("Please fill your ID");
		} else if (!isValidNumber(idTxt.getText())) {
			errorMsgLbl.setText("ID must contain digits only");
		}

		else if (parkCB.getSelectionModel().getSelectedItem() == null) {
			errorMsgLbl.setText("Please choose a park");
		} else if (dateDP.getValue() == null) {
			errorMsgLbl.setText("Please pick a date");

		}

		else if (dateDP.getValue().isBefore(LocalDate.now())) {
			errorMsgLbl.setText("Please choose a valid date");
		}

		else if (timeCB.getSelectionModel().getSelectedItem() == null) {
			errorMsgLbl.setText("Please choose arrival time");
		}

		else if (visitorsTxt.getText().trim().isEmpty()) {
			errorMsgLbl.setText("Please fill number of visitors");
		}

		else if (!isValidNumber(visitorsTxt.getText())) {
			errorMsgLbl.setText("number of visitors must contain digits only");
		}

		else if (Integer.parseInt(visitorsTxt.getText()) > MAX_NUMBER_OF_VISITORS && orgnaizedCB.isSelected()) {
			errorMsgLbl.setText("number of visitors can't be more than 15 , when booking for an organized group");
		}

		else if (emailTxt.getText().trim().isEmpty()) {
			errorMsgLbl.setText("Please fill your Email");
		} else if (phoneTxt.getText().trim().isEmpty()) {
			errorMsgLbl.setText("Please fill your phone number");
		}

		else if (!isValidNumber(phoneTxt.getText())) {
			errorMsgLbl.setText("phone number must contain digits only");
		} else {
			Order guestOrder = new Order(idTxt.getText(), nameTxt.getText(), lastNameTxt.getText(),
					parkCB.getSelectionModel().getSelectedItem().toString(), dateDP.getValue(),
					timeCB.getSelectionModel().getSelectedItem().toString(), visitorsTxt.getText(),
					orgnaizedCB.isSelected(), emailTxt.getText(), phoneTxt.getText(), true);
			System.out.println(guestOrder.toString());

			if (((ClientUI.chat.client.order) != null) && ((ClientUI.chat.client.order).getOrderNumber() != null))
				guestOrder.setOrderNumber((ClientUI.chat.client.order).getOrderNumber());

			return guestOrder;
		}

		return null;

	}// end validateOrderInfo

	// Event Listener on Button[#cancelBtn].onAction
	@FXML
	public void cancelFunc(ActionEvent event) throws IOException {

		Stage primaryStage;
		((Node) event.getSource()).getScene().getWindow().hide();
		primaryStage = new Stage();
		Pane root = (Pane) loader.load(this.getClass().getResource("/gui/HomePage.fxml").openStream());
		HomePageController clientConnectController = (HomePageController) loader.getController();
		Scene scene = new Scene(root);
		primaryStage.setTitle("HomePage");
		primaryStage.setScene(scene);
		primaryStage.show();
	}
}

//last update : 24/3 , 18:31
