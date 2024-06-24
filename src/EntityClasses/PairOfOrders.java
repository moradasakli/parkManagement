package EntityClasses;

import java.io.Serializable;

public class PairOfOrders implements Serializable{
	private static final long serialVersionUID = 1L;


	private OrderInTable from;
	private OrderInTable to;


	public PairOfOrders(OrderInTable from, OrderInTable to) {
		super();
		this.from = from;
		this.to = to;
	}



	public OrderInTable getFrom() {
		return from;
	}

	public void setFrom(OrderInTable from) {
		this.from = from;
	}

	public OrderInTable getTo() {
		return to;
	}

	public void setTo(OrderInTable to) {
		this.to = to;
	}

	@Override
	public String toString() {
		return "PairOfOrders [from=" + from + ", to=" + to + "]";
	}
	
	
	
	

}
