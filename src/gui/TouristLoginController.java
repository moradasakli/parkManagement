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

import java.awt.JobAttributes.DialogType;
import java.io.IOException;

import javax.swing.JOptionPane;

import EntityClasses.Message;
import EntityClasses.MessageType;
import client.ChatClient;
import client.ClientUI;
import javafx.event.ActionEvent;

public class TouristLoginController {
	@FXML
	private TextField idTxt;
	@FXML
	private Button nextBtn;
	@FXML
	private Button backBt;
	@FXML
	private Label idLbl;

	FXMLLoader loader = new FXMLLoader();
	
	//
	@FXML
	public void openTouristHomePage(ActionEvent event) throws IOException {
		
		if (idTxt.getText().trim().isEmpty()) {
			idLbl.setText("Please fill the id! ");
	}
		else {
	        Message messageToServer = new Message(MessageType.TouristLogin,idTxt.getText());
			ClientUI.chat.accept(messageToServer);
			

			if (ClientUI.chat.client.tourist!=null) {
				if(ChatClient.isTouristLoggedIn==false) {
					System.out.println("our tourist is : "+ClientUI.chat.client.tourist.getTouristFirstName());
					Stage primaryStage;
					((Node) event.getSource()).getScene().getWindow().hide();
					primaryStage = new Stage();
					Pane root = (Pane) loader.load(this.getClass().getResource("/gui/ExistingTouristHomePage.fxml").openStream());
					ExistingTouristHomePageController ExistingTouristHomePageConnectController = (ExistingTouristHomePageController) loader.getController();
					Scene scene = new Scene(root);
					primaryStage.setTitle("ExistingTouristHomePage");
					primaryStage.setScene(scene);
					primaryStage.show();
				}
				else {

		            JOptionPane.showMessageDialog(null, "Tourist is alreadyLoggedIn.", "Error", JOptionPane.ERROR_MESSAGE);
		            

				}
			}
			else JOptionPane.showMessageDialog(null, "Please register to sign in.", "Error", JOptionPane.ERROR_MESSAGE);
				
			
			}
		
		
	}
	// Event Listener on Button[#backBt].onAction
	@FXML
	public void returnToHomePage(ActionEvent event) throws IOException {
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
