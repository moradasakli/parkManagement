package gui;

import java.io.IOException;

import EntityClasses.Number_Of_Visitors_Report;
import client.ClientUI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class NumberOfVisitors_ReportController {

	@FXML
	private Label park_num_label;
	@FXML
	private Label month_label;
	@FXML
	private Label number_of_single_visitors_label;
	@FXML
	private Label number_of_group_visitors_label;
	@FXML
	private Button Backbtn;
	
	FXMLLoader loader = new FXMLLoader();
	
	/**
     * Initializes the controller after its root element has been completely processed.
     * Retrieves the number of visitors report from the server and populates the labels with the information.
     */
	@FXML
	public void initialize()
	{
		Number_Of_Visitors_Report report=ClientUI.chat.client.number_of_visitors_report;
		park_num_label.setText("Park Number : "+report.getPark_num());
		month_label.setText("month : "+report.getMonth());
		number_of_single_visitors_label.setText("number of single visitors : "+report.getNumber_of_single_visitors());
		number_of_group_visitors_label.setText("number of group visitors : "+report.getNumber_of_group_visitors());
	}
	
	
    /**
     * Handles the action event when the "Back" button is clicked on the number of visitors report page.
     * Redirects the user back to the department manager reports page.
     * 
     * @param event The event generated when the button is clicked.
     * @throws IOException If there is an error loading the FXML file or creating the scene.
     */
	// Event Listener on Button[#BackBtn].onAction
	@FXML
	public void ReturnToRelaventPage(ActionEvent event) throws IOException {
		Stage primaryStage;
		((Node) event.getSource()).getScene().getWindow().hide();
		primaryStage = new Stage();
		Pane root = (Pane) loader.load(this.getClass().getResource("/gui/DepartmentMangerReports.fxml").openStream());
		DepartmentMangerReportsController departmentMangerReportsController = (DepartmentMangerReportsController) loader.getController();
		Scene scene = new Scene(root);
		primaryStage.setTitle("DepartmentMangerReportsPage");
		primaryStage.setScene(scene);
		scene.getStylesheets().add(getClass().getResource("/gui/graphs.css").toExternalForm());

		primaryStage.show();
	}
	
	
}
