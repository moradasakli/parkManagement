package EntityClasses;

import java.io.Serializable;
import java.time.LocalDate;

public class AvailableDateInTable implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private String optionNumber;
	private LocalDate dateOfVisit;
	private String timeOfVisit;

	public AvailableDateInTable() {
		// TODO Auto-generated constructor stub
	}

	public AvailableDateInTable(String optionNumber, LocalDate dateOfVisit, String timeOfVisit) {
		super();
		this.optionNumber = optionNumber;
		this.dateOfVisit = dateOfVisit;
		this.timeOfVisit = timeOfVisit;
	}
	public AvailableDateInTable(int optionNumber, LocalDate dateOfVisit, String timeOfVisit) {
		super();
		this.optionNumber = ""+optionNumber;
		this.dateOfVisit = dateOfVisit;
		this.timeOfVisit = timeOfVisit;
	}

	public String getOptionNumber() {
		return optionNumber;
	}

	public void setOptionNumber(String optionNumber) {
		this.optionNumber = optionNumber;
	}

	public LocalDate getDateOfVisit() {
		return dateOfVisit;
	}

	public void setDateOfVisit(LocalDate dateOfVisit) {
		this.dateOfVisit = dateOfVisit;
	}

	public String getTimeOfVisit() {
		return timeOfVisit;
	}

	public void setTimeOfVisit(String timeOfVisit) {
		this.timeOfVisit = timeOfVisit;
	}

	@Override
	public String toString() {
		return "AvailableDateInTable [optionNumber=" + optionNumber + ", dateOfVisit=" + dateOfVisit + ", timeOfVisit="
				+ timeOfVisit + "]";
	}
	
	
}//end class AvailableDateInTable
