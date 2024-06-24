package gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;

import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import javafx.scene.control.Label;
import java.io.IOException;


import client.ChatClient;
import client.ClientController;
import client.ClientUI;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import EntityClasses.MessageType;
import EntityClasses.Message;
import EntityClasses.MessageType;

public class clientConnectController {
	@FXML
	private Button connectBtn;
	@FXML
	private Button cancelBtn;
	@FXML
	private TextField ipTxt;
	@FXML
	private TextField portTxt;
	@FXML
	private Label errorTxt;

	FXMLLoader loader = new FXMLLoader();

	@FXML
	public void connectFunc(ActionEvent event) throws IOException {
		

		if (ipTxt.getText().trim().isEmpty()) {
			errorTxt.setText("Please fill the ip! ");
		} else if (portTxt.getText().trim().isEmpty()) {
			errorTxt.setText("Please fill the port! ");
		}
		
		else {	

	        Message messageToServer = new Message(MessageType.ConnectToServer);
			int portNumber = Integer.parseInt(portTxt.getText());
			ClientUI.chat = new ClientController(ipTxt.getText(), portNumber);
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

	}

	@FXML
	public void cancelFunc(ActionEvent event) {
		System.exit(0);
	}

	public void start(Stage primaryStage) throws Exception {
		Parent root = (Parent) FXMLLoader.load(this.getClass().getResource("/gui/clientConnect.fxml"));
		Scene scene = new Scene(root);
		primaryStage.setTitle("Client Connect");
		primaryStage.setScene(scene);
		primaryStage.show();

	}

}
