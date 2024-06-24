package EntityClasses;

import java.io.Serializable;

public class Number_Of_Visitors_Report implements Serializable{
	
	private String report_id;
	private String park_num;
	private int month;
	private int number_of_single_visitors;
	private int number_of_group_visitors;
	
	public Number_Of_Visitors_Report()
	{
		
	}
	
	public Number_Of_Visitors_Report(String report_id,String park_num,int month,int number_of_single_visitors,int number_of_group_visitors)
	{
		this.report_id=report_id;
		this.park_num=park_num;
		this.month=month;
		this.number_of_single_visitors=number_of_single_visitors;
		this.number_of_group_visitors=number_of_group_visitors;
	}
	
	public String getReport_id() {
		return report_id;
	}

	public void setReport_id(String report_id) {
		this.report_id = report_id;
	}

	public String getPark_num() {
		return park_num;
	}

	public void setPark_num(String park_num) {
		this.park_num = park_num;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public int getNumber_of_single_visitors() {
		return number_of_single_visitors;
	}

	public void setNumber_of_single_visitors(int number_of_single_visitors) {
		this.number_of_single_visitors = number_of_single_visitors;
	}

	public int getNumber_of_group_visitors() {
		return number_of_group_visitors;
	}

	public void setNumber_of_group_visitors(int number_of_group_visitors) {
		this.number_of_group_visitors = number_of_group_visitors;
	}

	// toString() method
	@Override
	public String toString() {
		return "Number_Of_Visitors_Report [report_id=" + report_id + ", park_num=" + park_num + ", month=" + month
				+ ", number_of_single_visitors=" + number_of_single_visitors + ", number_of_group_visitors="
				+ number_of_group_visitors + "]";
	}

}
