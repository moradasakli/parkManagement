package gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import DBController.orderDBcontroller;
import EntityClasses.AvailableDateInTable;
import EntityClasses.Message;
import EntityClasses.MessageType;
import EntityClasses.Order;
import EntityClasses.OrderInTable;
import EntityClasses.Tourist;
import client.ChatClient;
import client.ClientUI;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;

import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class AlternativeOptionController {
	@FXML
	private Button waitingListBtn;
	@FXML
	private Button showDatesBtn;
	@FXML
	private Button backBtn;
	@FXML
	private Button cancelOrderBtn;

	
	@FXML
	private Label MsgLbl;
	@FXML
	private Label OrderNumberLbl;
	@FXML
	private Label FullNameLbl;
	@FXML
	private Label ParkLbl;
	@FXML
	private Label DateLbl;
	@FXML
	private Label TimeLbl;
	@FXML
	private Label NumOfVisitorsLbl;
	@FXML
	private Label PriceLbl;

	@FXML
	private Button pickDateBtn;
	@FXML
	private Label optionLbl;
	@FXML
	private TextField optionTxtField;

	@FXML
	private TableView<AvailableDateInTable> listOfAvailableDates;
	@FXML
	private TableColumn<AvailableDateInTable, String> optionCol;
	@FXML
	private TableColumn<AvailableDateInTable, LocalDate> dateCol;
	@FXML
	private TableColumn<AvailableDateInTable, String> timeCol;

	FXMLLoader loader = new FXMLLoader();

	public static boolean dontClickTwice = false;
	public static boolean backBtnPressed = false;
	Order order = ClientUI.chat.client.order;
	public static Order copyTest = null;
	public static ArrayList<Order> copyOfAvailableDates = null;
	// Event Listener on Button[#waitingListBtn].onAction
	@FXML
	public void enterWaitingList(ActionEvent event) throws IOException {

		if (!dontClickTwice) {
			// dontClickTwice = true;

			if (order != null) {

				Message messageToServer = new Message(MessageType.GuestWaitingList, order);
				ClientUI.chat.accept(messageToServer);
				// MsgLbl.setText("we added you to the waiting list , we will let you know if a
				// space opens up");
				JOptionPane.showMessageDialog(null,
						"we added you to the waiting list , we will let you know if a space opens up.\n To view your order and see your options you need to login.",
						"Info", JOptionPane.INFORMATION_MESSAGE);
				// load alternative options page
				Stage primaryStage;
				((Node) event.getSource()).getScene().getWindow().hide();
				primaryStage = new Stage();
				Pane root = (Pane) loader.load(this.getClass().getResource("/gui/HomePage.fxml").openStream());
				HomePageController clientConnectController = (HomePageController) loader.getController();
				Scene scene = new Scene(root);
				primaryStage.setTitle("HomePage");
				primaryStage.setScene(scene);
				primaryStage.show();

			} else {
				System.out.println("NULL Order in waiting list");
			}
		} else {
			System.out.println("waiting list more than once");

		}

	}

	// Event Listener on Button[#showDatesBtn].onAction
	@FXML
	public void showDates(ActionEvent event) {
		
		copyTest = new Order(order);
		Message messageToServer = new Message(MessageType.ListOfAvailableDates, new Order(order));
		System.out.println("---Display Available Dates : --------");
		ClientUI.chat.accept(messageToServer);
		
		if (ChatClient.resultFromServer == MessageType.AvailableDatesSuccess)
		{
//			System.out.println();
//			for (int i = 0; i < ClientUI.chat.client.availableDates.size(); i++) {
//			System.out.println(ClientUI.chat.client.availableDates.get(i).toString());
//			}
			
			ArrayList<Order> availableDates = ClientUI.chat.client.availableDates;
			ObservableList<AvailableDateInTable> availableDatesObservable = FXCollections.observableArrayList();
			copyOfAvailableDates = new ArrayList(ClientUI.chat.client.availableDates);

			for(int i =0;i<availableDates.size();i++)
			{
				AvailableDateInTable temp = new AvailableDateInTable(i+1,availableDates.get(i).getDateOfVisit(),
						availableDates.get(i).getTimeOfVisit());
				availableDatesObservable.add(temp);
			}
			

			optionCol.setCellValueFactory(new PropertyValueFactory<AvailableDateInTable, String>("optionNumber"));	
			dateCol.setCellValueFactory(new PropertyValueFactory<AvailableDateInTable, LocalDate>("dateOfVisit"));
			timeCol.setCellValueFactory(new PropertyValueFactory<AvailableDateInTable, String>("timeOfVisit"));
			listOfAvailableDates.setItems(availableDatesObservable);
		}
		else
		{
			System.out.println("BACK TO ALT OPTIONS - WITH FAILURE");
			MsgLbl.setText("we are fully booked for the week , please try editing your order or enter waiting list.");
		}
		//print dates for test
		

		
		
		pickDateBtn.setVisible(true);
		optionLbl.setVisible(true);
		optionTxtField.setVisible(true);

		listOfAvailableDates.setVisible(true);
		optionCol.setVisible(true);
		dateCol.setVisible(true);
		timeCol.setVisible(true);
	}

	// Event Listener on Button[#cancelBtn].onAction
	@FXML
	public void ReturnRelevantPage(ActionEvent event) throws IOException {

		if (!dontClickTwice) {
			backBtnPressed = true;
			Stage primaryStage;
			((Node) event.getSource()).getScene().getWindow().hide();
			primaryStage = new Stage();
			Pane root = (Pane) loader.load(this.getClass().getResource("/gui/NewOrderGuest.fxml").openStream());
			NewOrderGuestController GuestController = (NewOrderGuestController) loader.getController();
			Scene scene = new Scene(root);
			primaryStage.setTitle("GuestPage");
			primaryStage.setScene(scene);
			primaryStage.show();
		} else {
			MsgLbl.setText("you need to login before editing your order");
		}

	}

	@FXML
	public void cancelOrder(ActionEvent event) throws IOException {
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

	// Event Listener on Button[#pickDateBtn].onAction
	@FXML
	public void pickDateFunc(ActionEvent event) throws IOException {
		ArrayList<Order> availableDates = copyOfAvailableDates;
//		System.out.println("ORDERS IN PICK DATE FUNC:");
//		for (int i = 0; i < copyOfAvailableDates.size(); i++) {
//			System.out.println(copyOfAvailableDates.get(i).toString());
//		}
		if (optionTxtField.getText().trim().isEmpty()) {
			MsgLbl.setText("Please choose an option first");
		}

		else if (!isValidNumber(optionTxtField.getText())) {
			MsgLbl.setText("option number must contain digits only");
		}

		else if (Integer.parseInt(optionTxtField.getText()) > availableDates.size()) {
			MsgLbl.setText("there is no option with this number , please choose one from the list");
		}
		else
		{
			int index = Integer.parseInt(optionTxtField.getText());
			Order chosenOrder = new Order(availableDates.get(index-1));
			System.out.println("chosen order going for payment");
			System.out.println("CHOSEN ONE : "+chosenOrder.toString());
			
			
			// EDIT HERE
			//Message messageToServer = new Message(MessageType.TempUntillCheckForAvailable, chosenOrder);
			Message messageToServer = new Message(MessageType.GuestTouristNewOrder, chosenOrder);
			ClientUI.chat.accept(messageToServer);
			
			if (ChatClient.resultFromServer == MessageType.GuestOrderSuccess) {
				
				PaymentController.isNewTourist = true;
				JOptionPane.showMessageDialog(null, "we booked your new order , press OK to proceed to payment options", "Info",
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
			
			
		}

	}
	
	public static boolean isValidNumber(String number) {
		String temp = "\\d+";
		return number.matches(temp);
	}

	@FXML
	public void initialize() {
		if ((order != null)) {
			OrderNumberLbl.setText("Order Number " + order.getOrderNumber());
			FullNameLbl.setText(order.getOrdererFirstName() + " " + order.getOrdererLastName());
			ParkLbl.setText(order.getParkNameInOrder());
			DateLbl.setText(order.getDateOfVisit().toString());
			TimeLbl.setText(order.getTimeOfVisit());
			NumOfVisitorsLbl.setText(order.getNumberOfVisitorsInOrder());
			PriceLbl.setText("" + order.getPrice());

		}

		pickDateBtn.setVisible(false);
		optionLbl.setVisible(false);
		optionTxtField.setVisible(false);

		listOfAvailableDates.setVisible(false);
		optionCol.setVisible(false);
		dateCol.setVisible(false);
		timeCol.setVisible(false);

	}
}

//last update : 25/3 , 14:46
