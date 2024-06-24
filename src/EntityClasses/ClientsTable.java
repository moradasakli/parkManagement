package EntityClasses;

public class ClientsTable {

	String ip,host,status;
	public ClientsTable(String ip, String host, String status) {
		super();
		this.ip = ip;
		this.host = host;
		this.status = status;
	}
	 
	public ClientsTable() {
		// TODO Auto-generated constructor stub
	}
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	
}
