package gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;

import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import DBController.ReportsDB;
import DBController.RequestDB;
import DBController.orderDBcontroller;
import EntityClasses.Message;
import EntityClasses.MessageType;
import EntityClasses.Order;

import EntityClasses.Report;
import EntityClasses.Request;
import client.ClientUI;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;

import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Label;

import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;

public class DepartmentMangerReportsController {
	@FXML
	private Button ShowReportsBtn;
	@FXML
	private Label GenerateReportsLbl;
	@FXML
	private ComboBox ReportsCBox;
	@FXML
	private ComboBox combobox_month;

	@FXML
	private Button open_report_btn;

	@FXML
	private TextField report_num_txt;

	@FXML
	private Button generate_btn;
	@FXML
	private Button BackBtn;
	@FXML
	private Label open_report_label;

	@FXML
	private TableView<Report> reportsTable;
	@FXML
	private TableColumn<Report, Integer> id;
	@FXML
	private TableColumn<Report, String> park_number;
	@FXML
	private TableColumn<Report, Integer> month;
	@FXML
	private TableColumn<Report, String> report_type;

	FXMLLoader loader = new FXMLLoader();
	@FXML
	private Label error_generate;

	/**
	 * Initializes the DepartmentMangerReportsController by populating the
	 * ComboBoxes with report types and months. This method is automatically called
	 * when the FXML file is loaded.
	 *
	 * @throws SQLException If a database access error occurs.
	 */
	@FXML
	public void initialize() throws SQLException {
		ReportsCBox.getItems().addAll("Visitation Report", "Cancellation Report");
		combobox_month.getItems().addAll("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12");

	}

	/**
	 * Retrieves and displays all reports when the ShowReportsBtn is clicked.
	 * Populates the reportsTable with the fetched reports.
	 *
	 * @param event The ActionEvent representing the button click.
	 */
	// Event Listener on Button[#ShowReportsBtn].onAction
	@FXML
	public void ShowReports(ActionEvent event) {
		loader = new FXMLLoader();
		Message messageToServer = new Message(MessageType.ShowAllReports, null);
		ClientUI.chat.accept(messageToServer);

		if (ClientUI.chat.client.showAllReportsStatus) {
			if (!ClientUI.chat.client.All_Reports_List.isEmpty()) {
				List<Report> l = ClientUI.chat.client.All_Reports_List;
				ObservableList<Report> list = FXCollections.observableArrayList();

				list.addAll(l);

				id.setCellValueFactory(new PropertyValueFactory<Report, Integer>("id"));
				park_number.setCellValueFactory(new PropertyValueFactory<Report, String>("park_num"));
				month.setCellValueFactory(new PropertyValueFactory<Report, Integer>("month"));
				report_type.setCellValueFactory(new PropertyValueFactory<Report, String>("type"));

				reportsTable.setItems(list);

				// succes
			} else {
				// there are no reports
			}
		} else {
			// Error
		}
	}

	/**
	 * Opens a specific report based on the report number entered in the
	 * report_num_txt field when the open_report_btn is clicked. Displays the
	 * appropriate report based on the selected report type.
	 *
	 * @param event The ActionEvent representing the button click.
	 * @throws IOException If an input or output exception occurs.
	 */
	// Event Listener on Button[#open_report_btn].onAction
	@FXML
	public void open_Report_Func(ActionEvent event) throws IOException {
		loader = new FXMLLoader();
		String report_num = report_num_txt.getText().trim();

		Message messageToServer = new Message(MessageType.ShowReportById, report_num);
		ClientUI.chat.accept(messageToServer);

		if (ClientUI.chat.client.reportIDfound == 0) {
			open_report_label.setText("not found");
		} else {
			if (!ClientUI.chat.client.ShowUsageReportStatus && !ClientUI.chat.client.ShowNumberOfVisitorsReportStatus) {
				open_report_label.setText(" Error, showing report failed");
			} else {

				if (ClientUI.chat.client.ShowNumberOfVisitorsReportStatus) // usage Report
				{
					if (ClientUI.chat.client.number_of_visitors_report != null) {
						// show number of visitors report
						loader = new FXMLLoader(getClass().getResource("/gui/NumberOfVisitors_Report.fxml"));
						Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
						primaryStage.hide();
						Pane root = loader.load();
						NumberOfVisitors_ReportController numberOfVisitors_ReportController = loader.getController();
						Scene scene = new Scene(root);
						primaryStage.setTitle("NumberOfVisitors_ReportPage");
						primaryStage.setScene(scene);
						scene.getStylesheets().add(getClass().getResource("/gui/graphs.css").toExternalForm());

						primaryStage.show();

					} else {
						open_report_label.setText(" Error in data");
					}

				} else// number of visitors report
				{
					if (ClientUI.chat.client.usage_report != null) {
						loader = new FXMLLoader(getClass().getResource("/gui/Usage_Report.fxml"));
						Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
						primaryStage.hide();
						// show usage report
						Pane root = loader.load();
						Usage_ReportController Usage_ReportController = loader.getController();
						Scene scene = new Scene(root);
						primaryStage.setTitle("Usage_ReportPage");
						primaryStage.setScene(scene);
						scene.getStylesheets().add(getClass().getResource("/gui/graphs.css").toExternalForm());

						primaryStage.show();
					} else {
						open_report_label.setText("no days the park is not full");
					}

				}
			}
		}

	}

	/**
	 * Generates a report based on the selected report type and month when the
	 * generate_btn is clicked. Redirects to the corresponding report page.
	 *
	 * @param event The ActionEvent representing the button click.
	 * @throws IOException 
	 */
	@FXML
	public void generate_report(ActionEvent event) throws IOException {
		int month = Integer.parseInt((String) combobox_month.getValue());
		String type = (String) ReportsCBox.getValue();

		switch (type) {
		case "Visitation Report": {
			

			Message messageToServer = new Message(MessageType.CreateVisitationReport, month);
			ClientUI.chat.accept(messageToServer);
			if(ClientUI.chat.client.create_visitation_reports_status) {
			Stage primaryStage;
			((Node) event.getSource()).getScene().getWindow().hide();
			primaryStage = new Stage();
				Pane root = (Pane) loader.load(this.getClass().getResource("/gui/Visitation_Report.fxml").openStream());
				Scene scene = new Scene(root);
				Visitation_ReportController Visitation_ReportController = (Visitation_ReportController) loader
						.getController();

				primaryStage.setTitle("Visitation_ReportPage");
				primaryStage.setScene(scene);
				scene.getStylesheets().add(getClass().getResource("/gui/graphs.css").toExternalForm());

				primaryStage.show();
				// Rest of the code
			}
			else {
				error_generate.setText("Can't generate, missing info");
			}
			break;
		}
		case "Cancellation Report": {

		
			 Message messageToServer = new Message(MessageType.CreateCancellationReport,month);
			 ClientUI.chat.accept(messageToServer);

			 if(ClientUI.chat.client.create_cancellation_report_status) {
			Stage primaryStage;
			((Node) event.getSource()).getScene().getWindow().hide();
			primaryStage = new Stage();
			
				Pane root = (Pane) loader
						.load(this.getClass().getResource("/gui/Cancellation_Report.fxml").openStream());
				Scene scene = new Scene(root);
				scene.getStylesheets().add(getClass().getResource("/gui/graphs.css").toExternalForm());

				primaryStage.setTitle("Cancellation_ReportPage");
				primaryStage.setScene(scene);
				scene.getStylesheets().add(getClass().getResource("/gui/graphs.css").toExternalForm());

				primaryStage.show();
				// Rest of the code
			 }
			 else
			 {
					error_generate.setText("Can't generate, missing info");
			 }
			break;
		}
			 

		}

	}

	/**
	 * Returns to the relevant page (DepartmentMangerPage) when the BackBtn is
	 * clicked.
	 *
	 * @param event The ActionEvent representing the button click.
	 * @throws IOException If an input or output exception occurs.
	 */
	// Event Listener on Button[#BackBtn].onAction
	@FXML
	public void ReturnToRelaventPage(ActionEvent event) throws IOException {

		Stage primaryStage;
		((Node) event.getSource()).getScene().getWindow().hide();
		primaryStage = new Stage();
		Pane root = (Pane) loader.load(this.getClass().getResource("/gui/DepartmentManger.fxml").openStream());
		DepartmentMangerController DepartmentMangerController = (DepartmentMangerController) loader.getController();
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("/gui/graphs.css").toExternalForm());

		primaryStage.setTitle("DepartmentMangerPage");
		primaryStage.setScene(scene);
		primaryStage.show();
	}
}
