package gui;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.mysql.cj.x.protobuf.MysqlxCrud.Order;

import EntityClasses.Message;
import EntityClasses.MessageType;
import EntityClasses.Visitation_Report;
import client.ClientUI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Visitation_ReportController {

	@FXML
	private Label month_label;
	@FXML
	private BarChart<String, Integer> bar_chart;
	
	FXMLLoader loader = new FXMLLoader();
	 
	
	 /**
     * Initializes the visitation report view with data from the visitation report object.
     * Displays the month and daily reports in the bar chart.
     */
	@FXML
	public void initialize() {
		
		
		/*
		
		month_label.setText("Month : " +ClientUI.chat.client.visitation_report.getMonth());
		Visitation_Report report = ClientUI.chat.client.visitation_report;
		System.out.println("report is : "+report.toString() );

		XYChart.Series<String,Integer> series1 = new XYChart.Series();
		series1.setName("Single Visitors");
		for (int i = 0; i < 9; i++) {
			series1.getData().add(new XYChart.Data(""+i+8, (report.getNumber_of_single_visitors())[i]));
		}

		XYChart.Series<String,Integer> series2 = new XYChart.Series();
		series2.setName("Group Visitors");
		for (int i = 0; i < 9; i++) {
			series2.getData().add(new XYChart.Data(""+i+8, (report.getNumber_of_single_visitors())[i]));
		}

		bar_chart.getData().addAll(series1, series2);
*/
		  month_label.setText("Month : " + ClientUI.chat.client.visitation_report.getMonth());
		    Visitation_Report report = ClientUI.chat.client.visitation_report;

		    XYChart.Series<String, Integer> series1 = new XYChart.Series<>();
		    series1.setName("Single Visitors");
		    for (int i = 0; i < 9; i++) {
		        series1.getData().add(new XYChart.Data<>(String.valueOf(i + 8), report.getNumber_of_single_visitors()[i]));
		    }

		    XYChart.Series<String, Integer> series2 = new XYChart.Series<>();
		    series2.setName("Group Visitors");
		    for (int i = 0; i < 9; i++) {
		        series2.getData().add(new XYChart.Data<>(String.valueOf(i + 8), report.getNumber_of_group_visitors()[i]));
		    }

		    CategoryAxis xAxis = new CategoryAxis();
		    xAxis.setLabel("Hours");
		    xAxis.getCategories().addAll("8", "9", "10", "11", "12", "13", "14", "15", "16");

		    NumberAxis yAxis = new NumberAxis();
		    yAxis.setLabel("Number of Visitors");

		    bar_chart.getData().addAll(series1, series2);



	}
	
		/*
		///////Test -->working
		month_label.setText("Month : 1");
		int[] single= {10,20,30,20,10,15,12,13,14};
		int[]group = {20,10,30,15,15,20,20,20,16};
		int month=1;
		Visitation_Report report=new Visitation_Report(1) ;
        report.setNumber_of_single_visitors(single);
        report.setNumber_of_group_visitors(group);
		
		XYChart.Series<String,Integer> series1 = new XYChart.Series();
		series1.setName("Single Visitors");
		for (int i = 0; i < 9; i++) {
			series1.getData().add(new XYChart.Data(""+(i+8), (report.getNumber_of_single_visitors())[i]));
		}

		XYChart.Series<String,Integer> series2 = new XYChart.Series();
		series2.setName("Group Visitors");
		for (int i = 0; i < 9; i++) {
			series2.getData().add(new XYChart.Data(""+(i+8), (report.getNumber_of_group_visitors())[i]));
		}

		bar_chart.getData().addAll(series1, series2);
	
	*/
		

	
	
	
	  /**
     * Handles the action event when returning to the relevant page.
     * Redirects the user to the department manager reports page.
     *
     * @param event The event generated when the button is clicked.
     * @throws IOException If there is an error loading the FXML file or creating the scene.
     */
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
