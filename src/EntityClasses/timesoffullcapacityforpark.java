package EntityClasses;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class timesoffullcapacityforpark {

	private int id;
	private LocalDate date;
	private int number_of_visitors;
	
	public timesoffullcapacityforpark(int id, LocalDate date, int numberOfVisitors) {
        this.id = id;
        this.date = date;
        this.number_of_visitors = numberOfVisitors;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate time) {
        this.date = time;
    }

    public int getNumberOfVisitors() {
        return number_of_visitors;
    }

    public void setNumberOfVisitors(int numberOfVisitors) {
        this.number_of_visitors = numberOfVisitors;
    }
}
