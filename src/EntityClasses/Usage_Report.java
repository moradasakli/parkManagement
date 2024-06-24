package EntityClasses;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Usage_Report  implements Serializable{

    private String report_id;
    private String park_num;
    private int month;
    private List<Usage_Report_Show> daily_reports;


    
 @Override
	public String toString() {
		return "Usage_Report [report_id=" + report_id + ", park_num=" + park_num + ", month=" + month
				+ ", daily_reports=" + daily_reports + "]";
	}

	// Constructor
    public Usage_Report(String reportId, String parkNum, int month, List<Usage_Report_Show> dailyReports) {
        this.report_id = reportId;
        this.park_num = parkNum;
        this.month = month;
        this.daily_reports = dailyReports;
    }
    
    // Getter for report_id
    public String getReportId() {
        return report_id;
    }

    // Setter for report_id
    public void setReportId(String reportId) {
        this.report_id = reportId;
    }

    // Getter for park_num
    public String getParkNum() {
        return park_num;
    }

    // Setter for park_num
    public void setParkNum(String parkNum) {
        this.park_num = parkNum;
    }

    // Getter for month
    public int getMonth() {
        return month;
    }

    // Setter for month
    public void setMonth(int month) {
        this.month = month;
    }

    // Getter for daily_reports
    public List<Usage_Report_Show> getDailyReports() {
        return daily_reports;
    }

    // Setter for daily_reports
    public void setDailyReports(List<Usage_Report_Show> dailyReports) {
        this.daily_reports = dailyReports;
    }

    

    public void addDailyReport(Usage_Report_Show r)
    {
    	this.daily_reports.add(r);
    }
}
