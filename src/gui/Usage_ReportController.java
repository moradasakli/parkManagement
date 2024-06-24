package gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


import EntityClasses.Number_Of_Visitors_Report;
import EntityClasses.Report;
import EntityClasses.Usage_Report;
import EntityClasses.Usage_Report_Show;
import client.ClientUI;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Usage_ReportController {

	@FXML
	private Button backBtn;
	@FXML
	private Label park_num_label;
	@FXML
	private Label month_label;
	@FXML
	private TableView<Usage_Report_Show> daily_report_table;
	@FXML
	private TableColumn<Usage_Report_Show, Integer> day_col;
	@FXML
	private TableColumn<Usage_Report_Show, String> full_capacity_status_col;

	FXMLLoader loader = new FXMLLoader();
	
	
	 /**
     * Initializes the usage report view with data from the usage report object.
     * Displays the park number, month, and daily reports in the table.
     */
	@FXML
	public void initialize() {
		ObservableList<Usage_Report_Show> l = FXCollections.observableArrayList();
		Usage_Report report = ClientUI.chat.client.usage_report;
		
		if (report!= null) {
			String park_num = report.getParkNum();////////************
			String month =""+report.getMonth();
			park_num_label.setText("Park Number : " + park_num);
			month_label.setText("Month : " + month);
			List<Usage_Report_Show> show_reports = report.getDailyReports();
			l.addAll(show_reports);

			day_col.setCellValueFactory(new PropertyValueFactory<Usage_Report_Show, Integer>("day"));
			full_capacity_status_col
					.setCellValueFactory(new PropertyValueFactory<Usage_Report_Show, String>("report_status"));
			daily_report_table.setItems(l);

		} else {
			System.out.println("empty list ! ");
		}
	}
	
	
	/**
     * Handles the action event when the "Back" button is clicked.
     * Redirects the user to the department manager reports page.
     *
     * @param event The event generated when the button is clicked.
     * @throws IOException If there is an error loading the FXML file or creating the scene.
     */
	// Event Listener on Button[#backBtn].onAction
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
