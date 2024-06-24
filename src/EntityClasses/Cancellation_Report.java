package EntityClasses;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Cancellation_Report implements Serializable{

	private List<OrdersToShowForCancellationReport> canceled;
	private List<OrdersToShowForCancellationReport> not_canceled;
	private String[] days={"Sunday","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday"};
    private int[] canceled_cnt;
    private int[] not_canceled_cnt;
    private double[] canceled_avg;
    private double[] not_canceled_avg;
    private int month;
    
    
    public Cancellation_Report(int month)
    {
    	this.month=month;
    	canceled=new ArrayList<OrdersToShowForCancellationReport>();
    	not_canceled=new ArrayList<OrdersToShowForCancellationReport>();
    	canceled_cnt=new int[7];
    	not_canceled_cnt=new int[7];
    	canceled_avg=new double[7];
    	not_canceled_avg=new double[7];
    	for(int i=0;i<7;i++)
    	{
    		not_canceled_cnt[i]=0;
    		canceled_cnt[i]=0;
    		not_canceled_avg[i]=0;
    		canceled_avg[i]=0;
    	}
    }
    
    public List<OrdersToShowForCancellationReport> getCanceled() {
        return canceled;
    }

    public List<OrdersToShowForCancellationReport> getNot_canceled() {
        return not_canceled;
    }
   
    public String[] getDays()
    {
    	return days;
    }
    
    public int[] getCanceled_cnt() {
        return canceled_cnt;
    }
    public int[] getNot_Canceled_cnt() {
        return not_canceled_cnt;
    }

    public double[] getCanceled_Avg() {
        return canceled_avg;
    }
    public double[] getNot_Canceled_Avg() {
        return not_canceled_avg;
    }

    public int getMonth() {
        return month;
    }
    public void addCanceled(OrdersToShowForCancellationReport o)
    {
    	this.canceled.add(o);
    }
    public void addNot_canceled(OrdersToShowForCancellationReport o)
    {
    	this.not_canceled.add(o);
    }
    // Setters
    public void setCanceled(List<OrdersToShowForCancellationReport> canceled) {
        this.canceled = canceled;
    }

    public void setNot_canceled(List<OrdersToShowForCancellationReport> not_canceled) {
        this.not_canceled = not_canceled;
    }

    public void setCanceled_cnt(int[] canceled_cnt) {
        this.canceled_cnt = canceled_cnt;
    }
    public void setCanceled_avg(double[] canceled_avg) {
        this.canceled_avg = canceled_avg;
    }
    public void setNot_Canceled_cnt(int[] not_canceled_cnt) {
        this.not_canceled_cnt = canceled_cnt;
    }
    public void setNot_Canceled_avg(double[] not_canceled_avg) {
        this.not_canceled_avg = not_canceled_avg;
    }

    public void setMonth(int month) {
        this.month = month;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Month: ").append(month).append("\n");

        sb.append("Canceled Orders:\n");
        for (OrdersToShowForCancellationReport order : canceled) {
            sb.append(order).append("\n");
        }

        sb.append("Not Canceled Orders:\n");
        for (OrdersToShowForCancellationReport order : not_canceled) {
            sb.append(order).append("\n");
        }

        sb.append("Canceled Counts:\n");
        for (int i = 0; i < 7; i++) {
            sb.append(days[i]).append(": ").append(canceled_cnt[i]).append("\n");
        }

        sb.append("Not Canceled Counts:\n");
        for (int i = 0; i < 7; i++) {
            sb.append(days[i]).append(": ").append(not_canceled_cnt[i]).append("\n");
        }

        sb.append("Canceled Averages:\n");
        for (int i = 0; i < 7; i++) {
            sb.append(days[i]).append(": ").append(canceled_avg[i]).append("\n");
        }

        sb.append("Not Canceled Averages:\n");
        for (int i = 0; i < 7; i++) {
            sb.append(days[i]).append(": ").append(not_canceled_avg[i]).append("\n");
        }

        return sb.toString();
    }
}
