package EntityClasses;

public class Park {
	
	private String parkNumber;
	private String parkName;
	private int maxCapacity;
	private int numberOfReservedSpaces;
	private int defaultDurationForVisit;
	private int currentCapacity;
	
	public Park()
	{
		
	}
	
	public Park(String parkNumber, String parkName, int maxCapacity, int numberOfReservedSpaces,
			int defaultDurationForVisit, int currentCapacity) {
		super();
		this.parkNumber = parkNumber;
		this.parkName = parkName;
		this.maxCapacity = maxCapacity;
		this.numberOfReservedSpaces = numberOfReservedSpaces;
		this.defaultDurationForVisit = defaultDurationForVisit;
		this.currentCapacity = currentCapacity;
	}

	public String getParkNumber() {
		return parkNumber;
	}

	public void setParkNumber(String parkNumber) {
		this.parkNumber = parkNumber;
	}

	public String getParkName() {
		return parkName;
	}

	public void setParkName(String parkName) {
		this.parkName = parkName;
	}

	public int getMaxCapacity() {
		return maxCapacity;
	}

	public void setMaxCapacity(int maxCapacity) {
		this.maxCapacity = maxCapacity;
	}

	public int getNumberOfReservedSpaces() {
		return numberOfReservedSpaces;
	}

	public void setNumberOfReservedSpaces(int numberOfReservedSpaces) {
		this.numberOfReservedSpaces = numberOfReservedSpaces;
	}

	public int getDefaultDurationForVisit() {
		return defaultDurationForVisit;
	}

	public void setDefaultDurationForVisit(int defaultDurationForVisit) {
		this.defaultDurationForVisit = defaultDurationForVisit;
	}

	public int getCurrentCapacity() {
		return currentCapacity;
	}

	public void setCurrentCapacity(int currentCapacity) {
		this.currentCapacity = currentCapacity;
	}

	@Override
	public String toString() {
		return "Park [parkNumber=" + parkNumber + ", parkName=" + parkName + ", maxCapacity=" + maxCapacity
				+ ", numberOfReservedSpaces=" + numberOfReservedSpaces + ", defaultDurationForVisit="
				+ defaultDurationForVisit + ", currentCapacity=" + currentCapacity + "]";
	}
	
	

}//end class Park
