package gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;

import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

import EntityClasses.Message;
import EntityClasses.MessageType;
import client.ClientUI;
import javafx.event.ActionEvent;

import javafx.scene.control.Label;

public class ServiceRepresentativeController {
	@FXML
	private TextField AddTravelGuideTxtField;
	@FXML
	private Button BackBtn;
	@FXML
	private Button AddBtn;
	@FXML
	private Label AddTravelGuiedLbl;

	@FXML
	private Label SuccesOrErrorLabel;
	FXMLLoader loader = new FXMLLoader();

	
	 /**
     * Handles the action event when the "Back" button is clicked.
     * Redirects the user to the employee login page after logging out.
     *
     * @param event The event generated when the button is clicked.
     * @throws IOException If there is an error loading the FXML file or creating the scene.
     */
	// Event Listener on Button[#BackBtn].onAction
	@FXML
	public void ReturnToRelaventPageFunc(ActionEvent event) throws IOException {
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

    /**
     * Handles the action event when the "Add" button is clicked.
     * Adds a tourist as a travel guide based on the entered tourist ID.
     * Displays a success message if the tourist is successfully added as a travel guide, otherwise displays an error message.
     *
     * @param event The event generated when the button is clicked.
     */
	// Event Listener on Button[#AddBtn].onAction
	@FXML
	public void AddTravelGuideFunc(ActionEvent event) {
		if (AddTravelGuideTxtField.getText().trim().equals("")) {
			SuccesOrErrorLabel.setText("please enter a id of the tourest that you want to sign as travel guied!");
		} else {
			Message messageToServer = new Message(MessageType.AddTravelGuide, AddTravelGuideTxtField.getText().trim());
			ClientUI.chat.accept(messageToServer);

			if (!ClientUI.chat.client.adding_travel_guied) {
				if (ClientUI.chat.client.isAllreadyATravelGuied)
					SuccesOrErrorLabel.setText("the tourist with id: " + AddTravelGuideTxtField.getText().trim()
							+ "is allready a travel gueid");
				else
					SuccesOrErrorLabel.setText("Error, please check if its a valid id!");

			} else
				SuccesOrErrorLabel.setText("the tourist you entered have become a travel guied!");

		}

	}
}
