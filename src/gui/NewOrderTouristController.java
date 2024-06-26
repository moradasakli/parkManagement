package gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.time.LocalDate;

import javax.swing.JOptionPane;

import EntityClasses.Message;
import EntityClasses.MessageType;
import EntityClasses.Order;
import EntityClasses.Tourist;
import client.ChatClient;
import client.ClientUI;
import javafx.event.ActionEvent;

import javafx.scene.control.Label;

import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;

public class NewOrderTouristController {
	@FXML
	private ChoiceBox parkCB;
	@FXML
	private DatePicker dateDP;
	@FXML
	private ComboBox timeCB;
	@FXML
	private TextField NumberOfVisitorstxtField;
	@FXML
	private CheckBox orgnaizedCB;
	@FXML
	private Button BackBtn;
	@FXML
	private Button BookBtn;
	@FXML
	private Label ChooseParkLbl;
	@FXML
	private Label DateLbl;
	@FXML
	private Label timeLbl;
	@FXML
	private Label NumberOfVisitorsLbl;
	@FXML
	private Label MsgLbl;

	FXMLLoader loader = new FXMLLoader();
	public static final int MAX_NUMBER_OF_VISITORS = 15;
	Tourist touristInfo = ClientUI.chat.client.tourist;
	// Event Listener on Button[#BackBtn].onAction
	@FXML
	public void ReturnToRelaventPageFunc(ActionEvent event) throws IOException {
		Stage primaryStage;
		((Node) event.getSource()).getScene().getWindow().hide();
		primaryStage = new Stage();
		Pane root = (Pane) loader.load(this.getClass().getResource("/gui/ExistingTouristHomePage.fxml").openStream());
		ExistingTouristHomePageController ExistingTouristHomePageConnectController = (ExistingTouristHomePageController) loader
				.getController();
		Scene scene = new Scene(root);
		primaryStage.setTitle("ExistingTouristHomePage");
		primaryStage.setScene(scene);
		primaryStage.show();
		// TODO Autogenerated
	}

	// Event Listener on Button[#BookBtn].onAction
	@FXML
	public void BookNewOrderFunc(ActionEvent event) throws IOException {

		System.out.println("BOOKING NEW ORDER FOR TOURIST ::");
		Order o = validateOrderInfo();
		if (o != null) {
			Message messageToServer = new Message(MessageType.ExistingTouristMakeNewOrder, o);
			ClientUI.chat.accept(messageToServer);

			if (ChatClient.resultFromServer == MessageType.ExistingTouristNewOrderSuccess) {
				JOptionPane.showMessageDialog(null, "We booked your new order , press OK to proceed to payment options", "Info",
						JOptionPane.INFORMATION_MESSAGE);
				Stage primaryStage;
				((Node) event.getSource()).getScene().getWindow().hide();
				primaryStage = new Stage();
				Pane root = (Pane) loader.load(this.getClass().getResource("/gui/Payment.fxml").openStream());
				PaymentController PaymentController = (PaymentController) loader.getController();
				Scene scene = new Scene(root);
				primaryStage.setTitle("PaymentPage");
				primaryStage.setScene(scene);
				primaryStage.show();
				//send existing tourist to a different payment page
				
				


			}

			if (ChatClient.resultFromServer == MessageType.ExistingTouristGoingForAlternativeOptions) {
				JOptionPane.showMessageDialog(null,
						"There are NO available spaces at the park at this time , we will offer you alternative options",
						"Info", JOptionPane.INFORMATION_MESSAGE);
				// load alternative options page
				Stage primaryStage;
				((Node) event.getSource()).getScene().getWindow().hide();
				primaryStage = new Stage();
				Pane root = (Pane) loader
						.load(this.getClass().getResource("/gui/TouristAlternativeOption.fxml").openStream());
				TouristAlternativeOptionController TouristAlternativeOptionController = (TouristAlternativeOptionController) loader
						.getController();
				Scene scene = new Scene(root);
				primaryStage.setTitle("AlternativeOptionPage");
				primaryStage.setScene(scene);
				primaryStage.show();
			}

		}

	}// end BookNewOrderFunc

	public static boolean isValidNumber(String number) {
		String temp = "\\d+";
		return number.matches(temp);
	}

	public Order validateOrderInfo() throws IOException {

		if (parkCB.getSelectionModel().getSelectedItem() == null) {
			MsgLbl.setText("Please choose a park");

		} else if (dateDP.getValue() == null) {
			MsgLbl.setText("Please pick a date");

		}

		else if (dateDP.getValue().isBefore(LocalDate.now())) {
			MsgLbl.setText("Please choose a valid date");
		}

		else if (timeCB.getSelectionModel().getSelectedItem() == null) {
			MsgLbl.setText("Please choose arrival time");
		}

		else if (NumberOfVisitorstxtField.getText().trim().isEmpty()) {
			MsgLbl.setText("Please fill number of visitors");
		}

		else if (!isValidNumber(NumberOfVisitorstxtField.getText())) {
			MsgLbl.setText("number of visitors must contain digits only");
		}

		else if (Integer.parseInt(NumberOfVisitorstxtField.getText()) > MAX_NUMBER_OF_VISITORS && orgnaizedCB.isSelected()) {
			MsgLbl.setText("number of visitors can't be more than 15 , when booking for an organized group");
		} else {
			Tourist tourist = ClientUI.chat.client.tourist;

			System.out.println("FINAL ELSE");
			Order Order = new Order(tourist.getTouristId(), tourist.getTouristFirstName(), tourist.getTouristLastName(),
					parkCB.getSelectionModel().getSelectedItem().toString(), dateDP.getValue(),
					timeCB.getSelectionModel().getSelectedItem().toString(), NumberOfVisitorstxtField.getText(),
					orgnaizedCB.isSelected(), tourist.getTouristEmail(), tourist.getTouristPhoneNumber(), true);
			
			if (((ClientUI.chat.client.order) != null) && ((ClientUI.chat.client.order).getOrderNumber() != null))
				Order.setOrderNumber((ClientUI.chat.client.order).getOrderNumber());

			System.out.println("new order info 1: " + Order.toString());
			return Order;

		}
		return null;
	}

	@FXML
	public void initialize() {
		parkCB.getItems().addAll("Park A", "Park B", "Park C");
		timeCB.getItems().addAll("08:00", "09:00", "10:00","11:00","12:00","13:00","14:00","15:00","16:00");
		
		Order order = ClientUI.chat.client.order;
		//Order order = new Order(TouristAlternativeOptionController.order);
		if(TouristAlternativeOptionController.backBtnPressed && (order !=null))
		{
			System.out.println("FILLING ORDER INFO FOR TOURIST - AFTER PRESSING BACK");
			parkCB.setValue(order.getParkNameInOrder());
			dateDP.setValue(order.getDateOfVisit());
			timeCB.setValue(order.getTimeOfVisit());
			NumberOfVisitorstxtField.setText(order.getNumberOfVisitorsInOrder());
			orgnaizedCB.setSelected(order.isOrganized());
			TouristAlternativeOptionController.backBtnPressed = false;

		}
		
		Order orderTest = TouristAlternativeOptionController.copyTest;
		if(TouristAlternativeOptionController.backBtnPressed && (orderTest !=null))
		{
			System.out.println("FILLING ORDER INFO FOR TOURIST - AFTER PRESSING BACK");
			parkCB.setValue(orderTest.getParkNameInOrder());
			dateDP.setValue(orderTest.getDateOfVisit());
			timeCB.setValue(orderTest.getTimeOfVisit());
			NumberOfVisitorstxtField.setText(orderTest.getNumberOfVisitorsInOrder());
			orgnaizedCB.setSelected(orderTest.isOrganized());
			TouristAlternativeOptionController.backBtnPressed = false;

		}
		
		orgnaizedCB.setVisible(false);
		
		if(touristInfo.getIsTravelGuide())
			orgnaizedCB.setVisible(true);

		
	}
}

//last update : 24/3 , 00:33
