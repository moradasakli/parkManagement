package EntityClasses;

import java.io.Serializable;

public class Tourist implements Serializable  {
	private static final long serialVersionUID = 1L;

	private String touristId;
	private String touristFirstName;
	private String touristLastName;
	private String touristEmail;
	private String touristPhoneNumber;
	private Boolean isTravelGuide;
	private Boolean isTouristLoggedIn;
	
	
	public Tourist(String touristId, String touristFirstName, String touristLastName, String touristEmail,
			String touristPhoneNumber, Boolean isTravelGuide) {
		super();
		this.touristId = touristId;
		this.touristFirstName = touristFirstName;
		this.touristLastName = touristLastName;
		this.touristEmail = touristEmail;
		this.touristPhoneNumber = touristPhoneNumber;
		this.isTravelGuide = isTravelGuide;
		this.isTouristLoggedIn = false;
	}
	
	
	
	public Tourist(Tourist t)
	{
		super();
		this.touristId = t.touristId;
		this.touristFirstName = t.touristFirstName;
		this.touristLastName = t.touristLastName;
		this.touristEmail = t.touristEmail;
		this.touristPhoneNumber = t.touristPhoneNumber;
		this.isTravelGuide = t.isTravelGuide;
		this.isTouristLoggedIn = t.isTouristLoggedIn;
	}


	public Tourist() {
	}


	public String getTouristId() {
		return touristId;
	}


	public void setTouristId(String touristId) {
		this.touristId = touristId;
	}


	public String getTouristFirstName() {
		return touristFirstName;
	}


	public void setTouristFirstName(String touristFirstName) {
		this.touristFirstName = touristFirstName;
	}


	public String getTouristLastName() {
		return touristLastName;
	}


	public void setTouristLastName(String touristLastName) {
		this.touristLastName = touristLastName;
	}


	public String getTouristEmail() {
		return touristEmail;
	}


	public void setTouristEmail(String touristEmail) {
		this.touristEmail = touristEmail;
	}


	public String getTouristPhoneNumber() {
		return touristPhoneNumber;
	}


	public void setTouristPhoneNumber(String touristPhoneNumber) {
		this.touristPhoneNumber = touristPhoneNumber;
	}


	public Boolean getIsTravelGuide() {
		return isTravelGuide;
	}


	public void setIsTravelGuide(Boolean isTravelGuide) {
		this.isTravelGuide = isTravelGuide;
	}


	public Boolean getIsTouristLoggedIn() {
		return isTouristLoggedIn;
	}


	public void setIsTouristLoggedIn(Boolean isTouristLoggedIn) {
		this.isTouristLoggedIn = isTouristLoggedIn;
	}


	@Override
	public String toString() {
		return "Tourist [touristId=" + touristId + ", touristFirstName=" + touristFirstName + ", touristLastName="
				+ touristLastName + ", touristEmail=" + touristEmail + ", touristPhoneNumber=" + touristPhoneNumber
				+ ", isTravelGuide=" + isTravelGuide + ", isTouristLoggedIn=" + isTouristLoggedIn + "]";
	}
	


}//end class Tourist
