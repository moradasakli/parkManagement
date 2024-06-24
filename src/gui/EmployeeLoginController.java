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

import java.io.IOException;
import java.sql.SQLException;

import javax.swing.JOptionPane;

import DBController.EmployeeDB;
import EntityClasses.Employee;
import EntityClasses.Message;
import EntityClasses.MessageType;
import client.ChatClient;
import client.ClientUI;
import javafx.event.ActionEvent;

public class EmployeeLoginController {
	@FXML
	private TextField userNameTxt;
	@FXML
	private TextField passwordTxt;
	@FXML
	private Button nextBtn;
	@FXML
	private Button backtn;
	@FXML
	private Label errorLabel;

	FXMLLoader loader = new FXMLLoader();

	
	/**
	 * Authenticates the employee login credentials and opens the relevant page based on the employee's role.
	 * Retrieves the username and password from the text fields, sends a login request to the server, and processes the response.
	 * If the login is successful, redirects to the appropriate page based on the employee's role (Park Manager, Department Manager, Entrance Worker, or Service Representative).
	 * Displays error messages if authentication fails or if the employee is already logged in.
	 *
	 * @param event The ActionEvent representing the button click.
	 * @throws IOException If an input or output exception occurs.
	 * @throws SQLException If a database access error occurs.
	 */
	// Event Listener on Button[#nextBtn].onAction
	@FXML
	public void openRelevantPage(ActionEvent event) throws IOException, SQLException {

		if (userNameTxt.getText().trim().equals("")) {
			errorLabel.setText("please enter a username! ");
		} else {
			if (passwordTxt.getText().trim().equals("")) {
				errorLabel.setText("please enter a password! ");
			} else {

				String username = userNameTxt.getText().trim();
				String password = passwordTxt.getText().trim();

				Employee e = new Employee(username, password);

				Message messageToServer = new Message(MessageType.EmployeeLogin, e);
				ClientUI.chat.accept(messageToServer);

				if (!ClientUI.chat.client.is_employee_allready_logedin) {
					if (ClientUI.chat.client.employee != null) {

						EmployeeDB.EmployeeLogin(username, password);
						e.setEmployeeLoggedIn(true);
						String role = ClientUI.chat.client.employee.getPosition();

						Stage primaryStage;
						((Node) event.getSource()).getScene().getWindow().hide();
						primaryStage = new Stage();
						Pane root;
						Scene scene;

						switch (role) {
						case "ParkManger":
							root = (Pane) loader.load(this.getClass().getResource("/gui/ParkManger.fxml").openStream());
							ParkMangerController parkmangerController = (ParkMangerController) loader.getController();
							scene = new Scene(root);
							primaryStage.setTitle("ParMangerPage");
							primaryStage.setScene(scene);
							scene.getStylesheets().add(getClass().getResource("/gui/graphs.css").toExternalForm());

							primaryStage.show();
							break;
						case "DepartmentManger":
							root = (Pane) loader
									.load(this.getClass().getResource("/gui/DepartmentManger.fxml").openStream());
							DepartmentMangerController departmentMangerController = (DepartmentMangerController) loader
									.getController();
							scene = new Scene(root);
							primaryStage.setTitle("DepartmentMangerPage");
							primaryStage.setScene(scene);
							scene.getStylesheets().add(getClass().getResource("/gui/graphs.css").toExternalForm());

							primaryStage.show();
							break;
						case "EntranceWorker":
							root = (Pane) loader
									.load(this.getClass().getResource("/gui/EntranceWorker.fxml").openStream());
							EntranceWorkerController entranceWorkerController = (EntranceWorkerController) loader
									.getController();
							scene = new Scene(root);
							primaryStage.setTitle("EntranceWorkerPage");
							primaryStage.setScene(scene);
							scene.getStylesheets().add(getClass().getResource("/gui/graphs.css").toExternalForm());

							primaryStage.show();
							break;
						case "ServiceRepresentative":
							root = (Pane) loader
									.load(this.getClass().getResource("/gui/ServiceRepresentative.fxml").openStream());
							ServiceRepresentativeController serviceRepresentativeController = (ServiceRepresentativeController) loader
									.getController();
							scene = new Scene(root);
							primaryStage.setTitle("ServiceRepresentativePage");
							primaryStage.setScene(scene);
							primaryStage.show();
							break;

						default:
							errorLabel.setText("this employee has no position! ");
						}

					} else {
						errorLabel.setText("Employee do not Exist! ");
					}

				} else {
					errorLabel.setText("Employee is alreadyLoggedIn! ");
				}
			}

		}

	}

	
	/**
	 * Returns to the home page (HomePage.fxml) when the backtn button is clicked.
	 *
	 * @param event The ActionEvent representing the button click.
	 * @throws IOException If an input or output exception occurs.
	 */
	// Event Listener on Button[#backtn].onAction
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
