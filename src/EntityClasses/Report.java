package EntityClasses;

import java.io.Serializable;

public class Report implements Serializable{
	private int id;
	private String park_num;
	private int month;
	private String type;

	// Constructor
	public Report(int id, String park_num, int month, String type) {
		this.id = id;
		this.park_num = park_num;
		this.month = month;
		this.type = type;
	}

	// Getters
	public int getId() {
		return id;
	}

	public String getPark_num() {
		return park_num;
	}

	public int getMonth() {
		return month;
	}

	public String getType() {
		return type;
	}

	// Setters
	public void setId(int id) {
		this.id = id;
	}

	public void setPark_num(String park_num) {
		this.park_num = park_num;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "Report{" + "id=" + id + ", park_num=" + park_num + ", month=" + month + ", type='" + type + '\'' + '}';
	}

}
