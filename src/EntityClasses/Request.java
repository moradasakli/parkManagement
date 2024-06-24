package EntityClasses;

import java.io.Serializable;

public class Request implements Serializable{

	private String requestNumber;
	private String requestedInPark;
	private String requestType;
	private int newValue;
	private boolean requestStatus;
	private boolean Approved;
	
	

	public Request(String requestedInPark, String requestType ,int newValue) {
		super();

		this.requestedInPark = requestedInPark;
		this.requestType = requestType;
		this.newValue = newValue;
		this.requestStatus = true;
		this.Approved = false;
	}
	
	public Request(String requestNumber, String requestedInPark, String requestType ,int newValue) {
		super();
		this.requestNumber = requestNumber;
		this.requestedInPark = requestedInPark;
		this.requestType = requestType;
		this.newValue = newValue;
		this.requestStatus = true;
		this.Approved = false;
	}
	
//majd
	
	public boolean getApproved()
	{
		return this.Approved;
	}
	public Request(String requestNumber, String requestedInPark, String requestType, int newValue, boolean requestStatus,boolean isApproved) {
		super();
		this.requestNumber = requestNumber;
		this.requestedInPark = requestedInPark;
		this.requestType = requestType;
	//	this.oldValue=oldValue;
		this.newValue = newValue;
		this.requestStatus = requestStatus;
		this.Approved = isApproved;
	}

	public String getRequestNumber() {
		return requestNumber;
	}
	public String getrequestNumber() {
		return requestNumber;
	}

	public void setRequestNumber(String requestNumber) {
		this.requestNumber = requestNumber;
	}

	public String getRequestedInPark() {
		return requestedInPark;
	}

	public void setRequestedInPark(String requestedInPark) {
		this.requestedInPark = requestedInPark;
	}

	public String getRequestType() {
		return requestType;
	}

	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}

	public int getNewValue() {
		return newValue;
	}

	public void setNewValue(int newValue) {
		this.newValue = newValue;
	}

	public boolean isRequestStatus() {
		return requestStatus;
	}
	public boolean getrequestStatus()
	{
		return requestStatus;
	}

	public void setRequestStatus(boolean requestStatus) {
		this.requestStatus = requestStatus;
	}

	public boolean isApproved() {
		return Approved;
	}


	public void setApproved(boolean isApproved) {
		this.Approved = isApproved;
	}

	@Override
	public String toString() {
		return "Request [requestNumber=" + requestNumber + ", requestedInPark=" + requestedInPark + ", requestType="
				+ requestType +", newValue=" + newValue + ", requestStatus=" + requestStatus + ", isApproved="
				+ Approved + "]";
	}
	
	
}//end class Request
