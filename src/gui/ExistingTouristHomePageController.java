package gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

import javax.swing.JOptionPane;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javafx.scene.control.ComboBox;
import EntityClasses.ClientsTable;
import EntityClasses.Message;
import EntityClasses.MessageType;
import EntityClasses.Order;
import EntityClasses.OrderInTable;
import EntityClasses.Tourist;
import Server.EchoServer;
import client.ChatClient;
import client.ClientUI;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;

import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Label;

public class ExistingTouristHomePageController {

	@FXML
	private Button MakeNewOrderBtn;
	@FXML
	private TextField OrderTxtField;
	@FXML
	private TableView<OrderInTable> listOfOrders;
	@FXML
	private TableColumn<OrderInTable, String> orderNumberCol;
	@FXML
	private TableColumn<OrderInTable, String> parkNameCol;
	@FXML
	private TableColumn<OrderInTable, LocalDate> dateCol;
	@FXML
	private TableColumn<OrderInTable, String> timeCol;
	@FXML
	private TableColumn<OrderInTable, String> numberOfVisitorsCol;
	@FXML
	private TableColumn<OrderInTable, String> orderStatusCol;
	@FXML
	private TableColumn<OrderInTable, String> confirmedCol;
	@FXML
	private TableColumn<OrderInTable, String> paidCol;
	@FXML
	private TableColumn<OrderInTable, Double> priceCol;
	@FXML
	private Button EditBtn;
	@FXML
	private Button CancelBtn;
	@FXML
	private Label OrderLbl;
	@FXML
	private Label welcomeLbl;
	@FXML
	private Label MsgLbl;

	public static OrderInTable OrderToEdit;
	public static Order fullOrderToEdit;
	FXMLLoader loader = new FXMLLoader();
	@FXML
	private Button CancelOrderBtn;

	@FXML
	public void NewOrderPageFunc(ActionEvent event) throws IOException {
		Stage primaryStage;
		((Node) event.getSource()).getScene().getWindow().hide();
		primaryStage = new Stage();
		Pane root = (Pane) loader.load(this.getClass().getResource("/gui/NewOrderTourist.fxml").openStream());
		NewOrderTouristController NewOrderTouristController = (NewOrderTouristController) loader.getController();
		Scene scene = new Scene(root);
		primaryStage.setTitle("NewOrderTourist");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public static boolean isValidNumber(String number) {
		String temp = "\\d+";
		return number.matches(temp);
	}

	@FXML
	public void OpenEditPageFunc(ActionEvent event) throws IOException {

		ArrayList<OrderInTable> Orders = ClientUI.chat.client.listOfOrders;
		String wantedOrderToEdit = OrderTxtField.getText();
		boolean isOrderExists = false;

		for (int i = 0; i < Orders.size(); i++) {
			if (Orders.get(i).getOrderNumber().equals(wantedOrderToEdit)) {
				isOrderExists = true;
				OrderToEdit = Orders.get(i);
				if (!(OrderToEdit.getOrderStatus().equals("Cancelled"))) {
					Message messageToServer = new Message(MessageType.ExtractFullOrderInfo, OrderToEdit);
					ClientUI.chat.accept(messageToServer);
					fullOrderToEdit = (ClientUI.chat.client.order);
				}

				break;
			}
		}

		if (OrderTxtField.getText().trim().isEmpty()) {
			MsgLbl.setText("Please type the number of the order you wish to edit");
		} else if (!isValidNumber(OrderTxtField.getText())) {
			MsgLbl.setText("Order Number must contain digits only");
		}

		else if (!isOrderExists) {
			MsgLbl.setText(
					"you dont have an order with this number , please select one of the orders presented in the table");
		} else if (OrderToEdit.getOrderStatus().equals("Cancelled")) {
			MsgLbl.setText("you can't edit an order that was cancelled");
		} else {
			Stage primaryStage;
			((Node) event.getSource()).getScene().getWindow().hide();
			primaryStage = new Stage();
			Pane root = (Pane) loader.load(this.getClass().getResource("/gui/EditReservation.fxml").openStream());
			EditReservationController EditReservationController = (EditReservationController) loader.getController();
			Scene scene = new Scene(root);
			primaryStage.setTitle("EditReservationPage");
			primaryStage.setScene(scene);
			primaryStage.show();
		}

	}// end OpenEditPageFunc

	@FXML
	public void ReturnTouristLoginPageFunc(ActionEvent event) throws IOException {
		Message messageToServer = new Message(MessageType.LogOutTourist, ClientUI.chat.client.tourist.getTouristId());
		ClientUI.chat.accept(messageToServer);
		Stage primaryStage;
		((Node) event.getSource()).getScene().getWindow().hide();
		primaryStage = new Stage();
		Pane root = (Pane) loader.load(this.getClass().getResource("/gui/HomePage.fxml").openStream());
		HomePageController homepageController = (HomePageController) loader.getController();
		Scene scene = new Scene(root);
		primaryStage.setTitle("HomePage");
		primaryStage.setScene(scene);
		primaryStage.show();

	}

	@FXML
	public void CancelOrderFunction(ActionEvent event) throws IOException {
		System.out.println("mbla shgal");
		ArrayList<OrderInTable> Orders = ClientUI.chat.client.listOfOrders;
		String wantedOrderToDelete = OrderTxtField.getText();
		boolean isOrderExists = false;

		for (int i = 0; i < Orders.size(); i++) {
			if (Orders.get(i).getOrderNumber().equals(wantedOrderToDelete)) {
				isOrderExists = true;
				OrderToEdit = Orders.get(i);
				OrderToEdit = Orders.get(i);
				if (!(OrderToEdit.getOrderStatus().equals("Cancelled"))) {
					Message messageToServer = new Message(MessageType.ExtractFullOrderInfo, OrderToEdit);
					ClientUI.chat.accept(messageToServer);
					fullOrderToEdit = (ClientUI.chat.client.order);
				}
				break;
			}
		}
		if (OrderTxtField.getText().trim().isEmpty()) {
			MsgLbl.setText("Please type the number of the order you wish to edit");
		} else if (!isValidNumber(OrderTxtField.getText())) {
			MsgLbl.setText("Order Number must contain digits only");
		}

		else if (!isOrderExists) {
			MsgLbl.setText(
					"you dont have an order with this number , please select one of the orders presented in the table");
		} else if (OrderToEdit.getOrderStatus().equals("Cancelled")) {
			MsgLbl.setText("you can't delete an order that was cancelled");
		} else {
			Message messageToServer = new Message(MessageType.TouristWantsToDeleteAnOrder, OrderToEdit);
			ClientUI.chat.accept(messageToServer);
			if (fullOrderToEdit.isPayed()) {
				JOptionPane.showMessageDialog(null,
						"We are sorry to hear, your order has been cancelled, Hope to see you again.\n"
								+ "We have refunded your credit card with " + fullOrderToEdit.getPrice() + " NIS",
						"Info", JOptionPane.INFORMATION_MESSAGE);
			} else {
				JOptionPane.showMessageDialog(null,
						"We are sorry to hear, your order has been cancelled, Hope to see you again. ", "Info",
						JOptionPane.INFORMATION_MESSAGE);
			}

			initialize();
		}
	}

	@FXML
	public void initialize() {

		if (PaymentController.cameFromPayment) {
			PaymentController.cameFromPayment = false;

			Order order = PaymentController.afterPayment;

			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Simulation");
			alert.setHeaderText("hello " + order.getOrdererFirstName() + " ,\n"
					+ "We are reminding you of your visit tomorrow at " + order.getParkNameInOrder() + " .\n"
					+ "Please confirm your arrival within 2 hours from this message .\n"
					+ "An email has been sent to you at : " + order.getMailInOrder() + " ,\n"
					+ "As well as an SMS at your number : " + order.getPhoneNumberInOrder() + " .\n\n"
					+ "** In case you don`t confirm , you order will be cancelled automatically , have a nice day ** \n");
			alert.setContentText("Choose your option:");

			ButtonType cancelBtn = new ButtonType("Cancel Order");
			ButtonType confirmBtn = new ButtonType("Confirm Order");

			alert.getButtonTypes().setAll(cancelBtn, confirmBtn);

			PauseTransition delay = new PauseTransition(Duration.seconds(20));
			delay.setOnFinished(event -> {
				if (alert.isShowing()) {
					alert.setResult(ButtonType.CLOSE);
				}
			});
			delay.play();

			Optional<ButtonType> result = alert.showAndWait();
			alert.resultProperty().addListener((observable, oldValue, newValue) -> {
				if (newValue == ButtonType.CLOSE) {
					System.out.println("Alert closed automatically");
					// Add your logic here
				}
			});

			if (result.isPresent() && result.get() == confirmBtn) {
				System.out.println("confirm selected");
				order.setConfirmed(true);
				Message messageToServer = new Message(MessageType.ConfirmArrival, order);
				ClientUI.chat.accept(messageToServer);
				JOptionPane.showMessageDialog(null, "We recieved your confirmation , see you tomorrow! .\n", "Info",
						JOptionPane.INFORMATION_MESSAGE);
			} else {
				System.out.println("cancel selected");
				order.setOrderStatus(false);
				Message messageToServer = new Message(MessageType.CancelArrival, order);
				ClientUI.chat.accept(messageToServer);

				if (order.isPayed()) {
					JOptionPane.showMessageDialog(null,
							"we are sorry to inform you that your order for tomorrow at " + order.getParkNameInOrder()
									+ " has been cancelled, Hope to see you again.\n"
									+ "we have refunded your credit card with " + order.getPrice() + " NIS",
							"Info", JOptionPane.INFORMATION_MESSAGE);
				} else {
					JOptionPane.showMessageDialog(null,
							"We are sorry to inform you that your order for tomorrow at " + order.getParkNameInOrder()
									+ " has been cancelled, Hope to see you again.\n",
							"Info", JOptionPane.INFORMATION_MESSAGE);
				}
			}

		} // end if confirmation

		// ---------------------------------------------------------------------------------------------------------------------

		welcomeLbl.setText("Welcome  " + ClientUI.chat.client.tourist.getTouristFirstName());
		Tourist tourist = ClientUI.chat.client.tourist;
		Message messageToServer = new Message(MessageType.ExistingTouristListOfOrders, tourist.getTouristId());
		System.out.println("---tourist ID from client : --------" + tourist.getTouristId());
		ClientUI.chat.accept(messageToServer);

		// System.out.println("---printing orders from client : --------");

//		for (int i = 0; i < ClientUI.chat.client.listOfOrders.size(); i++) {
//			System.out.println(ClientUI.chat.client.listOfOrders.get(i).toString());
//		}

		ArrayList<OrderInTable> Orders = ClientUI.chat.client.listOfOrders;
		ObservableList<OrderInTable> ordersObservable = FXCollections.observableArrayList();

		for (OrderInTable obj : Orders) {
			ordersObservable.add(obj);

		}

		orderNumberCol.setCellValueFactory(new PropertyValueFactory<OrderInTable, String>("orderNumber"));
		parkNameCol.setCellValueFactory(new PropertyValueFactory<OrderInTable, String>("parkNameInOrder"));
		dateCol.setCellValueFactory(new PropertyValueFactory<OrderInTable, LocalDate>("dateOfVisit"));
		timeCol.setCellValueFactory(new PropertyValueFactory<OrderInTable, String>("timeOfVisit"));
		numberOfVisitorsCol
				.setCellValueFactory(new PropertyValueFactory<OrderInTable, String>("numberOfVisitorsInOrder"));
		orderStatusCol.setCellValueFactory(new PropertyValueFactory<OrderInTable, String>("orderStatus"));

//		orderStatusCol.setCellValueFactory(cellData -> cellData.getValue().isOrderStatus()?
//		 new SimpleStringProperty("Valid Order"): new SimpleStringProperty("Invalid Order"));

		confirmedCol.setCellValueFactory(new PropertyValueFactory<OrderInTable, String>("confirmed"));
		paidCol.setCellValueFactory(new PropertyValueFactory<OrderInTable, String>("Paid"));

		// isConfirmedCol.setCellValueFactory(new PropertyValueFactory<OrderInTable,
		// String>("isConfirmed"));
		// isPayedCol.setCellValueFactory(new PropertyValueFactory<OrderInTable,
		// String>("isPayed"));
		priceCol.setCellValueFactory(new PropertyValueFactory<OrderInTable, Double>("price"));

		// listOfOrders.getColumns().add(orderStatusCol);
		listOfOrders.setItems(ordersObservable);

	}// end initialize
}
//last update : 26/3 , 8:52 ..
