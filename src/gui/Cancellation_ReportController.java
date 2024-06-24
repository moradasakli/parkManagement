package gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import EntityClasses.Cancellation_Report;
import EntityClasses.Order;

import EntityClasses.OrdersToShowForCancellationReport;
import EntityClasses.Report;
import EntityClasses.Request;
import client.ClientUI;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;


public class Cancellation_ReportController {

	@FXML
	private Button avg_gragh_btn;
	@FXML
	private Button choosebtn;
	@FXML
	private Button backbtn;
	@FXML
	private ComboBox comboBox_type;
	@FXML
	private Label month_label;
	@FXML
	private TableView<OrdersToShowForCancellationReport> orders_table;
	@FXML
	private TableColumn<OrdersToShowForCancellationReport, Integer> park_col;
	@FXML
	private TableColumn<OrdersToShowForCancellationReport, Integer> number_of_visitors_col;
	@FXML
	private TableColumn<OrdersToShowForCancellationReport, String> is_payed_col;
	@FXML
	private TableColumn<OrdersToShowForCancellationReport, Double> price_col;
	@FXML
	private Label error_generate;
	FXMLLoader loader = new FXMLLoader();

	/**
	 * Initializes the Cancellation_ReportController by populating the comboBox_type with options.
	 */
	@FXML
	public void initialize() {

		comboBox_type.getItems().addAll("Canceled orders", "Unfulfilled orders");
		month_label.setText("Month : "+ClientUI.chat.client.cancellation_report.getMonth());

	}
	
	
	/**
	 * Displays canceled or not canceled orders based on the selected type in the comboBox_type.
	 * Populates the orders_table with corresponding order details.
	 */
	public void showCanceledOrders() {
		
		Cancellation_Report report = ClientUI.chat.client.cancellation_report;
		ObservableList<OrdersToShowForCancellationReport> list = FXCollections.observableArrayList();
		String type = (String) comboBox_type.getValue();

		switch (type) {
		case "Canceled orders": {
           list.addAll(report.getCanceled());
			break;
		}
		case "Unfulfilled orders": {
			 list.addAll(report.getNot_canceled());
			break;
		}
		}
		for (OrdersToShowForCancellationReport r : list ) {
			if(r.getIs_payed().equals("true"))
				r.setIs_payed("Yes");
			else { 
				r.setIs_payed("No");

			}
		}
		park_col.setCellValueFactory(new PropertyValueFactory<OrdersToShowForCancellationReport, Integer>("park"));
		number_of_visitors_col.setCellValueFactory(new PropertyValueFactory<OrdersToShowForCancellationReport, Integer>("number_of_visitors"));
		is_payed_col.setCellValueFactory(new PropertyValueFactory<OrdersToShowForCancellationReport, String>("is_payed"));
		price_col.setCellValueFactory(new PropertyValueFactory<OrdersToShowForCancellationReport, Double>("price"));

		orders_table.setItems(list);
		

	
		

	}
	
	/*
	/////// Test -->  works
	List<OrdersToShowForCancellationReport> canceled=new ArrayList<OrdersToShowForCancellationReport>();
	List<OrdersToShowForCancellationReport> not_canceled=new ArrayList<OrdersToShowForCancellationReport>();
	double[] c_avg= {1,2,3,4,5,6,7};
	double[] n_c_avg= {2,3,4,1,5,9,8};
	int[] canceled_cnt= {1,2,3,4,5,6,7};
	int[] not_canceled_cnt= {6,2,4,5,1,10,4};
	
	OrdersToShowForCancellationReport order1 = new OrdersToShowForCancellationReport(1, 10, "Yes", 150.0);
    OrdersToShowForCancellationReport order2 = new OrdersToShowForCancellationReport(2, 5, "No", 100.0);
    OrdersToShowForCancellationReport order3 = new OrdersToShowForCancellationReport(3, 8, "Yes", 120.0);
    OrdersToShowForCancellationReport order4 = new OrdersToShowForCancellationReport(1, 3, "Yes", 80.0);
    OrdersToShowForCancellationReport order5 = new OrdersToShowForCancellationReport(2, 7, "Yes", 130.0);
    OrdersToShowForCancellationReport order6 = new OrdersToShowForCancellationReport(3, 4, "No", 90.0);
    OrdersToShowForCancellationReport order7 = new OrdersToShowForCancellationReport(1, 12, "Yes", 180.0);
    OrdersToShowForCancellationReport order8 = new OrdersToShowForCancellationReport(2, 6, "Yes", 110.0);

    canceled.add(order1);
    canceled.add(order2);
    canceled.add(order3);
    canceled.add(order4);
    not_canceled.add(order5);
    not_canceled.add(order6);
    not_canceled.add(order7);
    not_canceled.add(order8);
    
    report.setCanceled(canceled);
    report.setNot_canceled(not_canceled);
    report.setCanceled_avg(c_avg);
    report.setNot_Canceled_avg(c_avg);
    report.setCanceled_cnt(canceled_cnt);
    report.setNot_Canceled_cnt(not_canceled_cnt);
    report.setMonth(1);
    
    
    
	ObservableList<OrdersToShowForCancellationReport> list = FXCollections.observableArrayList();
	String type = (String) comboBox_type.getValue();

	switch (type) {
	case "Canceled": {
       list.addAll(report.getCanceled());
		break;
	}
	case "Not Canceled": {
		 list.addAll(report.getNot_canceled());
		break;
	}
	}
	
	park_col.setCellValueFactory(new PropertyValueFactory<OrdersToShowForCancellationReport, Integer>("park"));
	number_of_visitors_col.setCellValueFactory(new PropertyValueFactory<OrdersToShowForCancellationReport, Integer>("number_of_visitors"));
	is_payed_col.setCellValueFactory(new PropertyValueFactory<OrdersToShowForCancellationReport, String>("is_payed"));
	price_col.setCellValueFactory(new PropertyValueFactory<OrdersToShowForCancellationReport, Double>("price"));

	orders_table.setItems(list);
	
	*/
	
	
	/**
	 * Switches to the Cancellation_Report_GhraghPage when the avg_gragh_btn is clicked.
	 *
	 * @param event The ActionEvent representing the button click.
	 * @throws IOException If an input or output exception occurs.
	 */
	@FXML
	public void goToGraghPage(ActionEvent event) throws IOException
	{
		Stage primaryStage;
		((Node) event.getSource()).getScene().getWindow().hide();
		primaryStage = new Stage();
		Pane root = (Pane) loader.load(this.getClass().getResource("/gui/Cancellation_Report_Gragh.fxml").openStream());
		Cancellation_Report_GhraghController Cancellation_Report_GhraghController = (Cancellation_Report_GhraghController) loader
				.getController();
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("/gui/graphs.css").toExternalForm());
		primaryStage.setTitle("Cancellation_Report_GhraghPage");
		primaryStage.setScene(scene);
		scene.getStylesheets().add(getClass().getResource("/gui/graphs.css").toExternalForm());

		primaryStage.show();
	}

	/**
	 * Returns to the relevant page when the backbtn is clicked.
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
		Pane root = (Pane) loader.load(this.getClass().getResource("/gui/DepartmentMangerReports.fxml").openStream());
		DepartmentMangerReportsController departmentMangerReportsController = (DepartmentMangerReportsController) loader
				.getController();
		Scene scene = new Scene(root);
		primaryStage.setTitle("DepartmentMangerReportsPage");
		primaryStage.setScene(scene);
		scene.getStylesheets().add(getClass().getResource("/gui/graphs.css").toExternalForm());

		primaryStage.show();
	}
}
