package EntityClasses;

import java.io.Serializable;

public class Employee implements Serializable{

	private String employeeId;
	private String employeeFirstName;
	private String employeeLastName;
	private String employeeEmail;
	private String position;
	private String parkWorkingAt;
	private String username;
	private String password;
	private boolean isEmployeeLoggedIn;
	
	
	public Employee(String u,String p)
	{
		this.username = u;
		this.password = p;

	}
	public Employee(String employeeId, String employeeFirstName, String employeeLastName, String employeeEmail,
			String position, String parkWorkingAt, String username, String password) {
		super();
		this.employeeId = employeeId;
		this.employeeFirstName = employeeFirstName;
		this.employeeLastName = employeeLastName;
		this.employeeEmail = employeeEmail;
		this.position = position;
		this.parkWorkingAt = parkWorkingAt;
		this.username = username;
		this.password = password;
		this.isEmployeeLoggedIn = true;
	}
	public Employee(String employeeId, String employeeFirstName, String employeeLastName, String employeeEmail,
			String position, String parkWorkingAt, String username, String password,boolean isEmployeeLoggedIn) {
		super();
		this.employeeId = employeeId;
		this.employeeFirstName = employeeFirstName;
		this.employeeLastName = employeeLastName;
		this.employeeEmail = employeeEmail;
		this.position = position;
		this.parkWorkingAt = parkWorkingAt;
		this.username = username;
		this.password = password;
		this.isEmployeeLoggedIn = isEmployeeLoggedIn;
	}

	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	public String getEmployeeFirstName() {
		return employeeFirstName;
	}

	public void setEmployeeFirstName(String employeeFirstName) {
		this.employeeFirstName = employeeFirstName;
	}

	public String getEmployeeLastName() {
		return employeeLastName;
	}

	public void setEmployeeLastName(String employeeLastName) {
		this.employeeLastName = employeeLastName;
	}

	public String getEmployeeEmail() {
		return employeeEmail;
	}

	public void setEmployeeEmail(String employeeEmail) {
		this.employeeEmail = employeeEmail;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getParkWorkingAt() {
		return parkWorkingAt;
	}

	public void setParkWorkingAt(String parkWorkingAt) {
		this.parkWorkingAt = parkWorkingAt;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isEmployeeLoggedIn() {
		return isEmployeeLoggedIn;
	}

	public void setEmployeeLoggedIn(boolean isEmployeeLoggedIn) {
		this.isEmployeeLoggedIn = isEmployeeLoggedIn;
	}

	@Override
	public String toString() {
		return "Employee [employeeId=" + employeeId + ", employeeFirstName=" + employeeFirstName + ", employeeLastName="
				+ employeeLastName + ", employeeEmail=" + employeeEmail + ", position=" + position + ", parkWorkingAt="
				+ parkWorkingAt + ", username=" + username + ", password=" + password + ", isEmployeeLoggedIn="
				+ isEmployeeLoggedIn + "]";
	}
	
	
	

}//end class Employee
