package EntityClasses;

import java.io.Serializable;

public class Usage_Report_Show implements Serializable {
	@Override
	public String toString() {
		return "Usage_Report_Show [day=" + day + ", report_status=" + report_status + "]";
	}

	private int day;
//	private boolean full_capacity_status;
	private String report_status;

	// Constructor
	public Usage_Report_Show(int day, boolean full_capacity_status) {
		this.day = day;
	//	this.full_capacity_status = full_capacity_status;

		if (full_capacity_status)
			this.report_status = "the park was full";
		else
			this.report_status = "the park was not full";
	}

	// Getters and setters
	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
	}
	public String getReport_status() {
        return report_status;
    }

    public void setReport_status(String report_status) {
        this.report_status = report_status;
    }




}
