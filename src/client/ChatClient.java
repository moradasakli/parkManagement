package client;

import common.ChatIF;
import EntityClasses.*;
import EntityClasses.Employee;
import EntityClasses.Message;
import EntityClasses.MessageType;
import EntityClasses.Order;
import EntityClasses.OrderInTable;
import EntityClasses.PairOfFullOrders;
import EntityClasses.PairOfOrders;
import gui.clientConnectController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import EntityClasses.Tourist;
import EntityClasses.Visitation_Report;
import ocsf.client.AbstractClient;

public class ChatClient extends AbstractClient {
	ChatIF clientUI;
	clientConnectController LogIn = new clientConnectController();
	
	public Tourist tourist;
	public Order order;
	public boolean thereIsEmptySpot;
	public ArrayList<OrderInTable> listOfOrders;
	public ArrayList<Order> availableDates;
	public ArrayList<Order> listOfOrdersToNotify;
	
	public static boolean awaitResponse = false;
	public static boolean isTouristLoggedIn = false;
	public static MessageType resultFromServer = MessageType.DefaultValueFromServer;
	public PairOfOrders pair;
	public PairOfFullOrders fullPair;
	
	
	
	// employee login
		public Employee employee;
		public boolean is_employee_allready_logedin = false;

		// entrance worker
		//public Order order = new Order();

		// service representative
		public boolean isAllreadyATravelGuied = false;
		public boolean adding_travel_guied = false;

		// Department manger
		public List<Request> All_Requests_List = new ArrayList<Request>();
		public boolean showRequestsAllParksStatus = false;
		public boolean AcceptOrRejectRequestStatus = false;
		public boolean AcceptOrRejectRequestStatusIdNotFound = false;

		public int request_num;
		public List<Report> All_Reports_List = new ArrayList<Report>();
		public boolean showAllReportsStatus = false;
		public boolean ShowNumberOfVisitorsReportStatus = false;
		public Number_Of_Visitors_Report number_of_visitors_report;
		public int reportIDfound = 0; // 1-> not found || 2-> found || 0 ->search failed

		public boolean create_number_of_visitors_report_status = false;
		public boolean create_Usage_Report_status = false;

		public boolean ShowUsageReportStatus = false;
		public Usage_Report usage_report;

		public boolean create_visitation_reports_status = false;
		public Visitation_Report visitation_report = null;

		public boolean create_cancellation_report_status = false;
		public Cancellation_Report cancellation_report;

		// Park Manger
		public List<Request> One_Requests_List = new ArrayList<>();
		public boolean showRequestsOneParksStatus = false;
		public Request new_request = null;

		// Park Manger + Department Manger
		public int available_spots;

	public ChatClient(String host, int port, ChatIF clientUI) throws IOException {
		super(host, port);
		this.clientUI = clientUI;
	}

	public void handleMessageFromServer(Object msg) {
		order = null;
		awaitResponse = false;
		isTouristLoggedIn = false;
		resultFromServer = MessageType.DefaultValueFromServer;
		Message message = (Message) msg;
		MessageType messageType = message.getMessageType();
		switch (messageType) {
		case TouristLogInSucceeded:
			this.tourist = (Tourist) message.getMessageData();
			break;
		case TouristLogInFailed:
			this.tourist = null;
			break;
		case TouristAlreadyLoggedIn:
			this.tourist = (Tourist) message.getMessageData();
			ChatClient.isTouristLoggedIn = true;
			break;

			
			
			
		case ExtractFullOrderInfo:
			order = (Order) message.getMessageData();
			resultFromServer = MessageType.ExtractFullOrderInfo;
			break;

		case NoAvailableDates:
			resultFromServer = MessageType.NoAvailableDates;
			break;
			
		case AvailableDatesSuccess:
			availableDates = (ArrayList<Order>) (message.getMessageData());
			resultFromServer = MessageType.AvailableDatesSuccess;
			break;
			
			
			
		case GuestAlreadyRegistered:
			resultFromServer = MessageType.GuestAlreadyRegistered;
			break;
		case GuestNotRegisteredAsTravelGuide:
			resultFromServer = MessageType.GuestNotRegisteredAsTravelGuide;
			break;
		case GuestOrderSuccess:
			order = (Order) message.getMessageData();
			resultFromServer = MessageType.GuestOrderSuccess;
			break;
		case GuestGoingForAlternativeOptions:
			order = (Order) message.getMessageData();
			resultFromServer = MessageType.GuestGoingForAlternativeOptions;
			break;

		case ExistingTouristListOfOrders:

			listOfOrders = (ArrayList<OrderInTable>) (message.getMessageData());
			// handle array of orders
			break;

			
			
			
		case ExistingTouristEditingOrderSuccess:
			resultFromServer = MessageType.ExistingTouristEditingOrderSuccess;
			fullPair = (PairOfFullOrders) message.getMessageData();
			break;

		case ExistingTouristEditOrder3Options:
			resultFromServer = MessageType.ExistingTouristEditOrder3Options;
			fullPair = (PairOfFullOrders) message.getMessageData();
			break;
			


			
			
			
			
		case ExistingTouristNewOrderSuccess:
			order = (Order) message.getMessageData();
			resultFromServer = MessageType.ExistingTouristNewOrderSuccess;
			break;

		case ExistingTouristGoingForAlternativeOptions:
			order = (Order) message.getMessageData();
			resultFromServer = MessageType.ExistingTouristGoingForAlternativeOptions;
			break;
			
		case EdittedPayment:
			order = (Order) message.getMessageData();
			resultFromServer = MessageType.EdittedPayment;
			break;
			
			
		case HandleWaitingListAfterEditingOrder:
			listOfOrdersToNotify = (ArrayList<Order>) (message.getMessageData());
			break;
			
		case CheckSpotsForOrder:
			thereIsEmptySpot = (boolean)(message.getMessageData());
			break;
			
		case UpdateOrderAfterPaymentDone:
			order = (Order) message.getMessageData();
			resultFromServer = MessageType.UpdateOrderAfterPaymentDone;
			break;
			
			
			//---------------------------------------------------------------------
		case EmployeeIsLogedIn: {
			is_employee_allready_logedin = true;
			break;
		}
		case EmployeeLoginSucces: {
			employee = (Employee) message.getMessageData();
			break;
		}
		case EmployeeLogoutSucces: {
			employee = null;
			break;
		}
		case EmployeeLoginFailed: {
			employee = (Employee) message.getMessageData();
			break;
		}

		case ErrorGettingPrice: {
			resultFromServer = MessageType.ErrorGettingPrice;

			break;
		}

		case GettingPriceSucces: {
			resultFromServer = MessageType.GettingPriceSucces;

			order = (Order) message.getMessageData();
			break;
		}
		case GetAvailableSpots:
			available_spots = (int) message.getMessageData();

			break;

		case orderAddedToEntryListSuccusefully:
			resultFromServer = MessageType.orderAddedToEntryListSuccusefully;
			order = (Order) message.getMessageData();
			break;

		case orderAddedToEntryListFailed:
			resultFromServer = MessageType.orderAddedToEntryListFailed;

		case orderDeletedFromEntryAndMovedToExit:
			resultFromServer = MessageType.orderDeletedFromEntryAndMovedToExit;
			break;

		case orderDeletedFromEntryFailed:
			resultFromServer = MessageType.orderDeletedFromEntryFailed;

			break;

		case orderDeletedFromEntrynotFound:
			resultFromServer = MessageType.orderDeletedFromEntrynotFound;

		case orderCameLateToVisit:
			resultFromServer = MessageType.orderCameLateToVisit;
			break;

		case AddTravelGuiedAlreadyATravelGuied: {
			isAllreadyATravelGuied = true;
			adding_travel_guied = false;
			break;
		}
		case AddingTravelGuideFailed: {
			isAllreadyATravelGuied = false;
			adding_travel_guied = false;
			break;
		}
		case AddingTravelGuiedSucces: {
			isAllreadyATravelGuied = false;
			adding_travel_guied = true;
			break;
		}
		// park manager
		case ShowRequestsOneParkFailed: { // park manger
			One_Requests_List = null;
			showRequestsOneParksStatus = false;
			break;
		}
		// park manager
		case ShowRequestsOneParkSucces: { // park manager
			One_Requests_List = (List<Request>) message.getMessageData();
			showRequestsOneParksStatus = true;
			break;
		}
		case ShowRequestsAllParksFailed: { // department manger
			All_Requests_List = null;
			showRequestsAllParksStatus = false;
			break;
		}
		case ShowRequestsAllParksSucces: { // department manger
			All_Requests_List = (List<Request>) message.getMessageData();
			showRequestsAllParksStatus = true;
			break;
		}
		case AddRequestSucces: { // park manger
			new_request = (Request) message.getMessageData();
			break;
		}
		case AddRequestFailed: { // park manger
			new_request = (Request) message.getMessageData();
			break;
		}
		case AcceptRequestSucces: { // park manger
			request_num = (int) message.getMessageData();
			AcceptOrRejectRequestStatus = true;
			AcceptOrRejectRequestStatusIdNotFound = false;
			break;
		}
		case AcceptRequestFailed: { // department manger
			request_num = (int) message.getMessageData();
			AcceptOrRejectRequestStatus = false;
			AcceptOrRejectRequestStatusIdNotFound = false;
			break;
		}
		case AcceptRequestFailedIdNotFound: { // department manger
			request_num = (int) message.getMessageData();
			AcceptOrRejectRequestStatus = false;
			AcceptOrRejectRequestStatusIdNotFound = true;
			break;
		}
		case RejectRequestSucces: { // department manger
			request_num = (int) message.getMessageData();
			AcceptOrRejectRequestStatus = true;
			break;
		}
		case RejectRequestFailed: { // department manger
			request_num = (int) message.getMessageData();
			AcceptOrRejectRequestStatus = false;
			break;
		}
		case ShowAllReportsSucces: { // department manger
			All_Reports_List = (List<Report>) message.getMessageData();
			showAllReportsStatus = true;
			break;
		}
		case ShowAllReportsFailed: { // department manger
			showAllReportsStatus = false;
			break;
		}
		case ShowNumberOfVisitorsReport: { // department manger
			ShowNumberOfVisitorsReportStatus = true;
			ShowUsageReportStatus = false;
			number_of_visitors_report = (Number_Of_Visitors_Report) message.getMessageData();
			reportIDfound = 2;

			break;
		}
		case ShowUsageReport: { // department manger
			System.out.println("now im in chat client with ShowUsageReport and printing the : ");
			ShowUsageReportStatus = true;
			ShowNumberOfVisitorsReportStatus = false;
			usage_report = (Usage_Report) message.getMessageData();
			System.out.println("usage report :  " + usage_report.toString());

			reportIDfound = 2;
			break;
		}
		case ShowReportByIdFailed: { // department manger
			ShowNumberOfVisitorsReportStatus = false;
			ShowUsageReportStatus = false;
			break;
		}

		case NoReportWithThisID: { // department manger
			ShowNumberOfVisitorsReportStatus = false;
			ShowUsageReportStatus = false;
			reportIDfound = 1;
			break;
		}
		case CreateNewNumberOfVisitorsReportSucces: { // department manger
			create_number_of_visitors_report_status = true;
			create_Usage_Report_status = false;
			break;
		}
		case CreateNewNumberOfVisitorsReportFailed: { // department manger
			create_number_of_visitors_report_status = false;
			create_Usage_Report_status = false;
			break;
		}
		case CreateNewUsageReportSucces: { // department manger
			create_Usage_Report_status = true;
			create_number_of_visitors_report_status = false;
			break;
		}
		case CreateNewUsageReportFailed: { // department manger
			create_Usage_Report_status = false;
			create_number_of_visitors_report_status = false;
			break;
		}

		case CreateVisitationReportSucces: { // department manger
			create_visitation_reports_status = true;
			visitation_report = (Visitation_Report) message.getMessageData();
			if(visitation_report==null)
				System.out.println("yes null");
			else 
				System.out.println("not null");
			break;
		}
		case CreateVisitationReportFailed: { // department manger
			create_visitation_reports_status = false;
			break;
		}
		case CreateCancellationReportSucces: { // department manger
			create_cancellation_report_status = true;
			cancellation_report = (Cancellation_Report) message.getMessageData();
			break;
		}
		case CreateCancellationReportFailed: { // department manger
			create_cancellation_report_status = false;
			break;
		}
			
			
			
			
			

		default:
			break;
		}

	}

	public void handleMessageFromClientUI(Object message) {
		try {
			this.openConnection();
			awaitResponse = true;
			this.sendToServer(message);

			while (awaitResponse) {
				try {
					Thread.sleep(100L);
				} catch (InterruptedException var3) {
					var3.printStackTrace();
				}
			}
		} catch (IOException var4) {
			var4.printStackTrace();
			this.clientUI.display("Could not send message to server: Terminating client." + var4);
			this.quit();
		}

	}

	public void quit() {
		try {
			this.closeConnection();
		} catch (IOException var2) {
		}

		System.exit(0);
	}

}
