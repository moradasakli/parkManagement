package EntityClasses;

import java.io.Serializable;

public class PairOfFullOrders implements Serializable{


	private static final long serialVersionUID = 1L;
	private Order from;
	private Order to;


	public PairOfFullOrders(Order from, Order to) {
		super();
		this.from = from;
		this.to = to;
	}
	

	public PairOfFullOrders() {
		// TODO Auto-generated constructor stub
	}

	public Order getFrom() {
		return from;
	}

	public void setFrom(Order from) {
		this.from = from;
	}

	public Order getTo() {
		return to;
	}

	public void setTo(Order to) {
		this.to = to;
	}



	@Override
	public String toString() {
		return "PairOfFullOrders [from=" + from + ", to=" + to + "]";
	}

	
	

}
