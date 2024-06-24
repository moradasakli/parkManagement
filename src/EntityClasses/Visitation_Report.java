package EntityClasses;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

public class Visitation_Report implements Serializable {

	// Index: 0 1 2 3 4 5 6 7 8
	// Hours: 8 9 10 11 12 13 14 15 16
	private int[] number_of_single_visitors;
	private int[] number_of_group_visitors;
	private int month;

	// Constructor
	public Visitation_Report(int month) {
		number_of_single_visitors = new int[9];
		number_of_group_visitors = new int[9];
		this.month = month;

		for (int i = 0; i < 9; i++) {
			number_of_single_visitors[i] = 0;
			number_of_group_visitors[i] = 0;
		}
	}

	@Override
	public String toString() {
		return "Visitation_Report [number_of_single_visitors=" + Arrays.toString(number_of_single_visitors)
				+ ", number_of_group_visitors=" + Arrays.toString(number_of_group_visitors) + ", month=" + month + "]";
	}

	public void setMonth(int m) {
		this.month = m;
	}

	public int getMonth() {
		return month;
	}

	// Getters and setters for number_of_single_visitors
	public int[] getNumber_of_single_visitors() {
		return number_of_single_visitors;
	}

	public void setNumber_of_single_visitors(int[] number_of_single_visitors) {
		this.number_of_single_visitors = number_of_single_visitors;
	}

	public void addIndexNumber_of_single_visitors(int index, int value) {
		this.number_of_single_visitors[index] +=value;
	}

	public void addIndexNumber_of_group_visitors(int index, int value) {
		this.number_of_group_visitors[index] +=value;
	}

	// Getters and setters for number_of_group_visitors
	public int[] getNumber_of_group_visitors() {
		return number_of_group_visitors;
	}

	public void setNumber_of_group_visitors(int[] number_of_group_visitors) {
		this.number_of_group_visitors = number_of_group_visitors;
	}

	public void calculate_Single_And_Group(List<Order> orders) {
		int[] single=new int[9];
		int[] group=new int [9];
		
		for (Order o : orders) {
	        // Split the time string to extract the hour
	        String[] timeParts = o.getTimeOfVisit().split(":");
	        int hour = Integer.parseInt(timeParts[0]);

	        if (o.getDateOfVisit().getMonthValue() == this.month) {
	            // Adjust the hour to fit within the array index range
	            int index = hour - 8; // Assuming hour ranges from 8 to 16
	          
	            if (index >= 0 && index < 9) { // Ensure the index is within the array bounds
	                if (o.isOrganized()) { // Using equals method for string comparison
	                    group[index]=group[index]+ Integer.parseInt(o.getNumberOfVisitorsInOrder());
	                } else {
	                	single[index]=single[index]+Integer.parseInt(o.getNumberOfVisitorsInOrder());
	                }
	            } else {
	                // Handle invalid hour range
	                System.out.println("Invalid hour range: " + hour);
	            }
	        }
	    }
		

		setNumber_of_single_visitors(single);
		setNumber_of_group_visitors(group);
		 	}
	
		 
		/*
		int hour;
		String time;
		String[] timeParts;
		for (Order1 o : orders) {
			timeParts = o.getTimeOfVisit().split(":");

			// Extract the hour part (first element in the array)
			time = timeParts[0];

			// Convert the hour part to an integer
			hour = Integer.parseInt(time);

			if (o.getDateOfVisit().getMonthValue() == this.month) {

				if (o.getIsOrganized() == "1") 
					addIndexNumber_of_group_visitors(hour-8,o.getNumberOfVisitors());
				else
					addIndexNumber_of_single_visitors(hour-8,o.getNumberOfVisitors());
			}
		}
		*/

	
}
