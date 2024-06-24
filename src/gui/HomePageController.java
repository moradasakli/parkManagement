package gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.Optional;

import javax.swing.JOptionPane;

import EntityClasses.Message;
import EntityClasses.MessageType;
import EntityClasses.Order;
import client.ClientUI;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;

public class HomePageController {
	@FXML
	private Button employeeBtn;
	@FXML
	private Button touristBtn;
	@FXML
	private Button notSignedBtn;
	@FXML
	private Button backBtn;

	FXMLLoader loader = new FXMLLoader();

	// Event Listener on Button[#employeeBtn].onAction
	@FXML
	public void employeeFunc(ActionEvent event) throws IOException{
		Stage primaryStage;
		((Node) event.getSource()).getScene().getWindow().hide();
		primaryStage = new Stage();
		Pane root = (Pane) loader.load(this.getClass().getResource("/gui/EmployeeLogin.fxml").openStream());
		EmployeeLoginController EmployeeLoginController = (EmployeeLoginController) loader.getController();
		Scene scene = new Scene(root);
		primaryStage.setTitle("EmployeeLogin");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	// Event Listener on Button[#touristBtn].onAction
	@FXML
	public void touristFunc(ActionEvent event) throws IOException {
		Stage primaryStage;
		((Node) event.getSource()).getScene().getWindow().hide();
		primaryStage = new Stage();
		Pane root = (Pane) loader.load(this.getClass().getResource("/gui/TouristLogin.fxml").openStream());
		TouristLoginController TouristLoginController = (TouristLoginController) loader.getController();
		Scene scene = new Scene(root);
		primaryStage.setTitle("TouristLogin");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	// Event Listener on Button[#notSignedBtn].onAction
	@FXML
	public void notSignedFunc(ActionEvent event) throws IOException {
		
		Stage primaryStage;
		((Node) event.getSource()).getScene().getWindow().hide();
		primaryStage = new Stage();
		Pane root = (Pane) loader.load(this.getClass().getResource("/gui/NewOrderGuest.fxml").openStream());
		NewOrderGuestController GuestController = (NewOrderGuestController) loader.getController();
		//GuestController.loadDataForOrder();
		
		Scene scene = new Scene(root);
		primaryStage.setTitle("GuestPage");
		primaryStage.setScene(scene);
		primaryStage.show();
		
	}

	@FXML
	public void backBtn(ActionEvent event) throws IOException {
		Stage primaryStage;
		((Node) event.getSource()).getScene().getWindow().hide();
		primaryStage = new Stage();
		Pane root = (Pane) loader.load(this.getClass().getResource("/gui/clientConnect.fxml").openStream());
		clientConnectController clientConnectController = (clientConnectController) loader.getController();
		Scene scene = new Scene(root);
		primaryStage.setTitle("TouristLogin");
		primaryStage.setScene(scene);
		primaryStage.show();
		
        Message messageToServer = new Message(MessageType.DisConnectToServer);
			ClientUI.chat.accept(messageToServer);


	}
	
	@FXML
	public void initialize() {
		if (PaymentController.cameFromPayment) {
			PaymentController.cameFromPayment = false;

			Order order = PaymentController.afterPayment;

			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Simulation");
			alert.setHeaderText("hello " + order.getOrdererFirstName() + ",\n"
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
				}
				else
				{
					JOptionPane.showMessageDialog(null,
							"We are sorry to inform you that your order for tomorrow at " + order.getParkNameInOrder()
									+ " has been cancelled, Hope to see you again.\n",
							"Info", JOptionPane.INFORMATION_MESSAGE);
				}
			}

		} // end if confirmation
	}//end initialize
	
}
//last update : 25/3 , 23:11 ..
