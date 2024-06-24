package gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import EntityClasses.Cancellation_Report;
import EntityClasses.OrdersToShowForCancellationReport;
import EntityClasses.Visitation_Report;
import client.ClientUI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Cancellation_Report_GhraghController {

	@FXML
	private Button backbtn;
	@FXML
	private Label month_label;
	

	@FXML
	private BarChart<String, Double> cancellation_gragh;
	
	FXMLLoader loader = new FXMLLoader();
	
	/**
	 * Initializes the Cancellation_Report_GhraghController after its root element has been completely processed. 
	 * Populates the cancellation graph with data.
	 */
	@FXML
	public void initialize() {

		Cancellation_Report report = ClientUI.chat.client.cancellation_report;
		
		XYChart.Series<String,Double> series1 = new XYChart.Series();
		series1.setName("Cancelled");
		for (int i = 0; i < 7; i++) {
			series1.getData().add(new XYChart.Data((report.getDays())[i], (report.getCanceled_Avg())[i]));
		}
		
		XYChart.Series<String,Double> series2 = new XYChart.Series();
		series2.setName("Not Cancelled");
		for (int i = 0; i < 7; i++) {
			series2.getData().add(new XYChart.Data((report.getDays())[i], (report.getNot_Canceled_Avg())[i]));
		}

		cancellation_gragh.getData().addAll(series1,series2);
		

	}
	
	/*
	//////test --> works
	month_label.setText("Month : 1");
	Cancellation_Report report = new Cancellation_Report(1);
	
	
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
    report.setNot_Canceled_avg(n_c_avg);
    report.setCanceled_cnt(canceled_cnt);
    report.setNot_Canceled_cnt(not_canceled_cnt);
    report.setMonth(1);
    
		XYChart.Series<String,Integer> series1 = new XYChart.Series();
		series1.setName("Cancelled");
		for (int i = 0; i < 7; i++) {
			series1.getData().add(new XYChart.Data((report.getDays())[i], (report.getCanceled_Avg())[i]));
		}
		
		XYChart.Series<String,Integer> series2 = new XYChart.Series();
		series2.setName("Not Cancelled");
		for (int i = 0; i < 7; i++) {
			series2.getData().add(new XYChart.Data((report.getDays())[i], (report.getNot_Canceled_Avg())[i]));
		}

		cancellation_gragh.getData().addAll(series1,series2);
    
    */
    
	
	
	/**
	 * Returns to the relevant page when the back button is clicked.
	 *
	 * @param event The ActionEvent representing the back button click.
	 * @throws IOException If an input or output exception occurs.
	 */
	@FXML
	public void ReturnToRelaventPage(ActionEvent event) throws IOException {

		Stage primaryStage;
		((Node) event.getSource()).getScene().getWindow().hide();
		primaryStage = new Stage();
		Pane root = (Pane) loader.load(this.getClass().getResource("/gui/Cancellation_Report.fxml").openStream());
		Cancellation_ReportController Cancellation_ReportController = (Cancellation_ReportController) loader.getController();
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("/gui/graphs.css").toExternalForm());

		primaryStage.setTitle("Cancellation_ReportPage");
		primaryStage.setScene(scene);
		primaryStage.show();
	}
}
