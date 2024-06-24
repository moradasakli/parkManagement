package gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

import javax.swing.JOptionPane;

import EntityClasses.Message;
import EntityClasses.MessageType;
import EntityClasses.Order;
import EntityClasses.OrderInTable;
import EntityClasses.PairOfFullOrders;
import EntityClasses.PairOfOrders;
import EntityClasses.Tourist;
import client.ClientUI;
import javafx.animation.PauseTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;

import javafx.scene.control.ComboBox;

import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;

public class EditReservationController {
	@FXML
	private DatePicker dateP;
	@FXML
	private TextField numOfVisirorsLbl;
	@FXML
	private ComboBox parkCB;
	@FXML
	private ComboBox timeCB;
	@FXML
	private CheckBox orgnaizedCB;
	@FXML
	private Button submitBtn;
	@FXML
	private Button backBtn;
	@FXML
	private Label OrderNumberLbl;
	@FXML
	private Label errorMsgLbl;

	Order fullOrder;
	public static final int MAX_NUMBER_OF_VISITORS = 15;
	Tourist tourist = new Tourist(ClientUI.chat.client.tourist);
	public static boolean cameFromEdit = false;

	FXMLLoader loader = new FXMLLoader();

	/**
	 * @param number as string
	 * @return checks if the parameter is a number
	 */
	public static boolean isValidNumber(String number) {
		String temp = "\\d+";
		return number.matches(temp);
	}

	/**
	 * @param event
	 * 
	 * 
	 * @throws IOException
	 */
	@FXML
	public void submitFunc(ActionEvent event) throws IOException {

		if (dateP.getValue().isBefore(LocalDate.now())) {
			errorMsgLbl.setText("Please choose a valid date");
		}

		else if (numOfVisirorsLbl.getText().trim().isEmpty()) {
			errorMsgLbl.setText("Please fill number of visitors");
		}

		else if (!isValidNumber(numOfVisirorsLbl.getText())) {
			errorMsgLbl.setText("number of visitors must contain digits only");
		}

		else if (Integer.parseInt(numOfVisirorsLbl.getText()) > MAX_NUMBER_OF_VISITORS && orgnaizedCB.isSelected()) {
			errorMsgLbl.setText("number of visitors can't be more than 15 , when booking for an organized group");
		} else {
			System.out.println("here");
			OrderInTable from = new OrderInTable(ExistingTouristHomePageController.OrderToEdit);
			OrderInTable to = new OrderInTable(ExistingTouristHomePageController.OrderToEdit);

			to.setParkNameInOrder(parkCB.getValue().toString());
			to.setDateOfVisit(dateP.getValue());
			to.setTimeOfVisit(timeCB.getValue().toString());
			to.setNumberOfVisitorsInOrder(numOfVisirorsLbl.getText());

			PairOfOrders pair = new PairOfOrders(from, to);

			Message messageToServer = new Message(MessageType.ExistingTouristEditingOrder, pair);
			ClientUI.chat.accept(messageToServer);

			// change to copy constructor if needed
			PairOfFullOrders fullPair = new PairOfFullOrders(ClientUI.chat.client.fullPair.getFrom(),
					ClientUI.chat.client.fullPair.getTo());

			fullPair.getFrom().setOrdererFirstName(tourist.getTouristFirstName());
			fullPair.getFrom().setOrdererLastName(tourist.getTouristLastName());

			fullPair.getTo().setOrdererFirstName(tourist.getTouristFirstName());
			fullPair.getTo().setOrdererLastName(tourist.getTouristLastName());

			System.out.println("CHECKING FULL PAIR :");
			System.out.println("FULL PAIR BEFORE :" + fullPair.getFrom());
			System.out.println("FULL PAIR AFTER :" + fullPair.getTo());

			if (ClientUI.chat.client.resultFromServer == MessageType.ExistingTouristEditingOrderSuccess) {

				Order beforeEdit = fullPair.getFrom();
				Order afterEdit = fullPair.getTo();

//				if(fullPair.getFrom().isConfirmed())
//					fullPair.getTo().setConfirmed(true);

				if (fullPair.getFrom().isPayed()) {

					if (fullPair.getFrom().getPrice() > fullPair.getTo().getPrice()) {

						fullPair.getTo().setPayed(true);
						fullPair.getTo().setPrice(fullPair.getTo().calculateBill(true));
						if (fullPair.getFrom().isConfirmed())
							fullPair.getTo().setConfirmed(true);

						System.out.println(fullPair.getTo().toString());

						double diff = fullPair.getFrom().getPrice() - fullPair.getTo().getPrice();
						JOptionPane.showMessageDialog(null,
								"Awesome ! Your order has been editted and saved.\n"
										+ "We have also refunded your credit card with " + diff + " NIS .",
								"Info", JOptionPane.INFORMATION_MESSAGE);

						// update order info in db

						messageToServer = new Message(MessageType.TouristUpdateOrderAfterPayment, fullPair.getTo());
						ClientUI.chat.accept(messageToServer);

						// handle waiting list according to old order , if order was valid
						if (fullPair.getFrom().isOrderStatus()) {
							messageToServer = new Message(MessageType.HandleWaitingListAfterEditingOrder,
									fullPair.getFrom());
							ClientUI.chat.accept(messageToServer);
							notifyOrdersAtWaitingList(ClientUI.chat.client.listOfOrdersToNotify);
						}

						Stage primaryStage;
						((Node) event.getSource()).getScene().getWindow().hide();
						primaryStage = new Stage();
						Pane root = (Pane) loader
								.load(this.getClass().getResource("/gui/ExistingTouristHomePage.fxml").openStream());
						ExistingTouristHomePageController ExistingTouristHomePageConnectController = (ExistingTouristHomePageController) loader
								.getController();
						Scene scene = new Scene(root);
						primaryStage.setTitle("ExistingTouristHomePage");
						primaryStage.setScene(scene);
						primaryStage.show();

					}// refund situation

					if (fullPair.getFrom().getPrice() < fullPair.getTo().getPrice()) {
						double diff = fullPair.getTo().getPrice() - fullPair.getFrom().getPrice();

						JOptionPane.showMessageDialog(null,
								"Awesome ! Your order has been editted and saved.\n"
										+ "We will present you with payment options to pay the difference , which is "
										+ diff + " NIS/n.",
								"Info", JOptionPane.INFORMATION_MESSAGE);

						fullPair.getTo().setPrice(diff);
						cameFromEdit = true;

						// ClientUI.chat.client.order = fullPair.getTo();


						// handle waiting list according to old order , if order was valid
						if (fullPair.getFrom().isOrderStatus()) {
							messageToServer = new Message(MessageType.HandleWaitingListAfterEditingOrder,
									fullPair.getFrom());
							ClientUI.chat.accept(messageToServer);
							notifyOrdersAtWaitingList(ClientUI.chat.client.listOfOrdersToNotify);
						}
						
						messageToServer = new Message(MessageType.EdittedPayment, fullPair.getTo());
						ClientUI.chat.accept(messageToServer);

						Stage primaryStage;
						((Node) event.getSource()).getScene().getWindow().hide();
						primaryStage = new Stage();
						Pane root = (Pane) loader.load(this.getClass().getResource("/gui/Payment.fxml").openStream());
						PaymentController PaymentController = (PaymentController) loader.getController();
						Scene scene = new Scene(root);
						primaryStage.setTitle("PaymentPage");
						primaryStage.setScene(scene);
						primaryStage.show();

					} // pay difference situation

				} // paid in advance

				else {

					System.out.println("came from waiting list or not paid in advance");

					JOptionPane.showMessageDialog(null,
							"Awesome ! Your order has been editted and saved.\n"
									+ "Press OK to proceed to payment options.",
							"Info", JOptionPane.INFORMATION_MESSAGE);

					System.out.println("Reached here before payment after edit******");


					// handle waiting list according to old order , if order was valid
					if (fullPair.getFrom().isOrderStatus()) {
						messageToServer = new Message(MessageType.HandleWaitingListAfterEditingOrder,
								fullPair.getFrom());
						ClientUI.chat.accept(messageToServer);
						notifyOrdersAtWaitingList(ClientUI.chat.client.listOfOrdersToNotify);
					}

					messageToServer = new Message(MessageType.EdittedPayment, fullPair.getTo());
					ClientUI.chat.accept(messageToServer);
					
					Stage primaryStage;
					((Node) event.getSource()).getScene().getWindow().hide();
					primaryStage = new Stage();
					Pane root = (Pane) loader.load(this.getClass().getResource("/gui/Payment.fxml").openStream());
					PaymentController PaymentController = (PaymentController) loader.getController();
					Scene scene = new Scene(root);
					primaryStage.setTitle("PaymentPage");
					primaryStage.setScene(scene);
					primaryStage.show();

				} // not paid in advance

			} // end success

			else if (ClientUI.chat.client.resultFromServer == MessageType.ExistingTouristEditOrder3Options) {

				Alert alert = new Alert(AlertType.CONFIRMATION);
				alert.setTitle("Please choose another option:");
				alert.setHeaderText("Sorry,there are no available spots at this time for your editted order");
				alert.setContentText("Choose your option:");

				ButtonType cancelBtn = new ButtonType("Cancel Order");
				ButtonType SaveOldOrder = new ButtonType("Stay with the old order");
				ButtonType WaitingListButton = new ButtonType("Enter Waiting list with new Order");

				alert.getButtonTypes().setAll(cancelBtn, SaveOldOrder, WaitingListButton);

				Optional<ButtonType> result = alert.showAndWait();
				if (result.isPresent()) {
					if (result.get() == cancelBtn) {

						System.out.println("CHECKING from before cancel:" + from.toString());

						messageToServer = new Message(MessageType.TouristWantsToDeleteAnOrder, from);

						// messageToServer = new Message(MessageType.TouristWantsToDeleteAnOrder,
						// pair.getFrom());
						ClientUI.chat.accept(messageToServer);

						System.out.println("CHECKING FULL PAIR :");
						System.out.println("FULL PAIR BEFORE :" + fullPair.getFrom());
						System.out.println("FULL PAIR AFTER :" + fullPair.getTo());

						if (fullPair.getFrom().isPayed()) {
							JOptionPane.showMessageDialog(null,
									"We are sorry to hear, your order has been cancelled, Hope to see you again.\n"
											+ "We have refunded your credit card with " + fullPair.getFrom().getPrice()
											+ " NIS",
									"Info", JOptionPane.INFORMATION_MESSAGE);
						} else {
							JOptionPane.showMessageDialog(null,
									"We are sorry to hear, your order has been cancelled, Hope to see you again. ",
									"Info", JOptionPane.INFORMATION_MESSAGE);
						}

						// handle waiting list according to old order , if order was valid
						if (fullPair.getFrom().isOrderStatus()) {
							messageToServer = new Message(MessageType.HandleWaitingListAfterEditingOrder,
									fullPair.getFrom());
							ClientUI.chat.accept(messageToServer);
							notifyOrdersAtWaitingList(ClientUI.chat.client.listOfOrdersToNotify);
						}

					} // end cancel button

					else if (result.get() == SaveOldOrder) {
						JOptionPane.showMessageDialog(null, "No changes was made to your order, Have a nice day! ",
								"Info", JOptionPane.INFORMATION_MESSAGE);

					} // end SaveOldOrder button

					else if (result.get() == WaitingListButton) {
						messageToServer = new Message(MessageType.TouristAddEditingOrderToWaitingList, pair);
						ClientUI.chat.accept(messageToServer);

						if (fullPair.getFrom().isPayed()) {
							JOptionPane.showMessageDialog(null,
									"Great! your new order was added to the waiting list.\n"
											+ "we have refunded your credit card with " + fullPair.getFrom().getPrice()
											+ " NIS " + ", that you paid in advance for your old order.",
									"Info", JOptionPane.INFORMATION_MESSAGE);
						} else {
							JOptionPane.showMessageDialog(null, "Great! your new order was added to the waiting list. ",
									"Info", JOptionPane.INFORMATION_MESSAGE);
						}

						// handle waiting list according to old order , if order was valid
						if (fullPair.getFrom().isOrderStatus()) {
							messageToServer = new Message(MessageType.HandleWaitingListAfterEditingOrder,
									fullPair.getFrom());
							ClientUI.chat.accept(messageToServer);
							notifyOrdersAtWaitingList(ClientUI.chat.client.listOfOrdersToNotify);
						}

					} // end WaitingList Button

				}

				Stage primaryStage;
				((Node) event.getSource()).getScene().getWindow().hide();
				primaryStage = new Stage();
				Pane root = (Pane) loader
						.load(this.getClass().getResource("/gui/ExistingTouristHomePage.fxml").openStream());
				ExistingTouristHomePageController ExistingTouristHomePageConnectController = (ExistingTouristHomePageController) loader
						.getController();
				Scene scene = new Scene(root);
				primaryStage.setTitle("ExistingTouristHomePage");
				primaryStage.setScene(scene);
				primaryStage.show();

			}

		}

	}// end submitFunc

	public void notifyOrdersAtWaitingList(ArrayList<Order> listOfOrdersToNotify) {

		for (int i = 0; i < listOfOrdersToNotify.size(); i++) {
			System.out.println("listOfOrdersToNotify :" + listOfOrdersToNotify.get(i).toString());
		}
		
		for (int i = 0; i < listOfOrdersToNotify.size(); i++) {
			Message messageToServer = new Message(MessageType.CheckSpotsForOrder, listOfOrdersToNotify.get(i));
			ClientUI.chat.accept(messageToServer);
			boolean res = ClientUI.chat.client.thereIsEmptySpot;

			if (res) {
				Order order = listOfOrdersToNotify.get(i);

				Alert alert = new Alert(AlertType.CONFIRMATION);
				alert.setTitle("Simulation");
				alert.setHeaderText("hello " + order.getOrdererFirstName() + " ,\n"
						+ "Good news , a space opened up for your visit at " + order.getParkNameInOrder() + " .\n"
						+ "confirm your arrival so we can book your order .\n"
						+ "Please respond to this message within 2 hours .\n" + "An email has been sent to you at : "
						+ order.getMailInOrder() + " ,\n" + "As well as an SMS at your number : "
						+ order.getPhoneNumberInOrder() + " .\n\n"
						+ "** In case you don`t confirm , you order will be cancelled automatically , have a nice day ** \n");
				alert.setContentText("Choose your option:");

				ButtonType cancelBtn = new ButtonType("Cancel Order");
				ButtonType confirmBtn = new ButtonType("Book Order");

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
					System.out.println("book selected");

					messageToServer = new Message(MessageType.MoveOrderFromWaitingListToValid, order);
					ClientUI.chat.accept(messageToServer);
					JOptionPane.showMessageDialog(null,
							"We recieved your confirmation and booked your visit , see you soon! .\n", "Info",
							JOptionPane.INFORMATION_MESSAGE);
				} else {
					System.out.println("cancel selected");
					messageToServer = new Message(MessageType.DeleteFromWaitingList, order);
					ClientUI.chat.accept(messageToServer);

					JOptionPane.showMessageDialog(null,
							"We are sorry to inform you that your order at " + order.getParkNameInOrder()
									+ " has been removed from waiting list , Hope to see you again.\n",
							"Info", JOptionPane.INFORMATION_MESSAGE);

				}
			} // end if
		} // end for
	}// end notifyOrdersAtWaitingList

	// Event Listener on Button[#backBtn].onAction
	@FXML
	public void returnTorelevantPage(ActionEvent event) throws IOException {
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
	}

	@FXML
	public void initialize() {

		OrderInTable order = ExistingTouristHomePageController.OrderToEdit;
		Order fullOrder = ExistingTouristHomePageController.fullOrderToEdit;

		parkCB.getItems().addAll("Park A", "Park B", "Park C");
		timeCB.getItems().addAll("08:00", "09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00");

		if (fullOrder != null) {
			if ((order != null)) {
				OrderNumberLbl.setText("Editing Order :  " + order.getOrderNumber());
				System.out.println("FILLING ORDER INFO FOR EDIT PAGE");

				parkCB.setValue(order.getParkNameInOrder());
				dateP.setValue(order.getDateOfVisit());
				timeCB.setValue(order.getTimeOfVisit());
				numOfVisirorsLbl.setText(order.getNumberOfVisitorsInOrder());
				orgnaizedCB.setSelected(fullOrder.isOrganized());

			}

			orgnaizedCB.setVisible(false);

			if (fullOrder.isOrganized()) {
				orgnaizedCB.setVisible(true);
				orgnaizedCB.setDisable(true);
			}

		}

	}// end initialize

}
//last update : 31/3 , 00:30
