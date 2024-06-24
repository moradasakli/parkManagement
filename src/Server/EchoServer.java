package Server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.sql.SQLException;

import DBController.EmployeeDB;
import DBController.ReportsDB;
import DBController.RequestDB;
import DBController.TouristsDBcontroller;
import DBController.TouristsDBcontroller.*;
import DBController.entranceWorkerDB;
import DBController.onlineClientDBcontroller;
import DBController.orderDBcontroller;
import HelperKit.myFunctions;
import HelperKit.mysqlConnection;
import EntityClasses.*;
import EntityClasses.MessageType;
import EntityClasses.Order;
import EntityClasses.OrderInTable;
import EntityClasses.PairOfOrders;
import EntityClasses.Tourist;
import gui.ServerPortFrameController;
import ocsf.server.AbstractServer;
import ocsf.server.ConnectionToClient;

public class EchoServer extends AbstractServer {
	ServerPortFrameController serverController = new ServerPortFrameController();
	public static mysqlConnection mySql = null;
	public static MessageType EchoServermsg;

	public EchoServer(int port) {
		super(port);
	}

	// 1
	public EchoServer(String url, String username, String password, int port) {
		super(port);
		mySql = new mysqlConnection(url, username, password);
	}

	public void handleMessageFromClient(Object msg, ConnectionToClient client) {
		String ip = null;
		InetAddress host = null;
		Message message = (Message) msg;
		MessageType messageType = message.getMessageType();
		Message messageFromServer = null;
		Tourist tourist = null;
		System.out.println("Message recieved from client : " + message.getMessageType().toString());
		switch (messageType) {

		case ConnectToServer:

			try {
				host = InetAddress.getLocalHost();
				ip = client.getInetAddress().getHostAddress();
			} catch (UnknownHostException e1) {
				System.out.println("Can't get the ip or host number ! ");
			}
			onlineClientDBcontroller.insertIntoOnlineClients(ip, host, mySql);
			break;
		case DisConnectToServer:
			ip = client.getInetAddress().getHostAddress();

			onlineClientDBcontroller.DeleteIntoOnlineClients(ip, mySql);

			break;
		case TouristLogin:
			messageFromServer = TouristsDBcontroller.TouristLogin((String) message.getMessageData());
			break;

		case LogOutTourist:
			TouristsDBcontroller.logOutTourist((String) message.getMessageData());
			break;

		case GuestTouristNewOrder:
			messageFromServer = orderDBcontroller.checkOrderFields((Order) message.getMessageData());
			break;
			
			
		case TouristUpdateOrderAfterPayment:
			messageFromServer = orderDBcontroller.updateOrderInfoAfterPayment((Order) message.getMessageData());
			break;
			
			
			
		case ListOfAvailableDates:
			messageFromServer = orderDBcontroller.getListOfAvailableDates((Order) message.getMessageData());			
			break;

		case GuestWaitingList:
			orderDBcontroller.enterWaitingList((Order) message.getMessageData());
			break;

			
			
		case ExtractFullOrderInfo:
			messageFromServer = orderDBcontroller.extractFullOrderInfo((OrderInTable) message.getMessageData());
			break;
			

		case ExistingTouristListOfOrders:
			messageFromServer = orderDBcontroller.getListOfOrdersForTourist((String) message.getMessageData());
			break;

		case ExistingTouristEditingOrder:
			EchoServermsg = message.getMessageType();

			messageFromServer = orderDBcontroller.editingExistingOrder((PairOfOrders) message.getMessageData());
			break;

		case TouristWantsToDeleteAnOrder:
			orderDBcontroller.cancelOrder((OrderInTable) message.getMessageData());
			break;

		case TouristAddEditingOrderToWaitingList:
			orderDBcontroller.addToWaitingListAfterEdit((PairOfOrders) message.getMessageData());
			break;
			
			
		case EdittedPayment:
			messageFromServer = orderDBcontroller.prepareOrderForPayment((Order) message.getMessageData());
			break;
			
			
		case HandleWaitingListAfterEditingOrder:
			messageFromServer = orderDBcontroller.updateWitingListDueToEdit((Order) message.getMessageData());
			break;
			
			
		case CheckSpotsForOrder:
			messageFromServer = orderDBcontroller.checkForSpotsForOrder((Order) message.getMessageData());
			break;
			
			
		case MoveOrderFromWaitingListToValid:
			orderDBcontroller.makeOrderValidFromWaitingList((Order) message.getMessageData());
			break;
			
			
		case DeleteFromWaitingList:
			orderDBcontroller.deleteFromWaitingList((Order) message.getMessageData());
			break;
			
			
		case ExistingTouristMakeNewOrder:
			messageFromServer = orderDBcontroller.touristMakeNewOrder((Order) message.getMessageData());
			break;
			
		case ExistingTouristAlternativeOptionsWaitingList:
			orderDBcontroller.enterWaitingList((Order) message.getMessageData());
			break;
			
			
			
		case ConfirmArrival:
			orderDBcontroller.confirmedArrival((Order) message.getMessageData());
			break;
			
			
		case CancelArrival:
			orderDBcontroller.cancelArrival((Order) message.getMessageData());
			break;
			
			//----------------------------------------------------------------------
			
			// Employee login
			case EmployeeLogin: {
				try {
					messageFromServer = EmployeeDB.EmployeeLogin((Employee) message.getMessageData());
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			}
			// ParkManger DepartmentManger EntranceWorker ServiceReprsientative --> back to
			// login page
			case EmployeeLogout: {
				try {
					messageFromServer = EmployeeDB.EmployeeLogout(((Employee) message.getMessageData()).getUsername(),
							((Employee) message.getMessageData()).getPassword());
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			}
			// EntranceWorker
			case GetPriceForRecipe: {
				messageFromServer = entranceWorkerDB.getOrderDetalisForEntranceWorker(
						((Order) message.getMessageData()).getOrderNumber(),
						((Order) message.getMessageData()).getPark_num());
				break;
			}

			case OrderEntringThePark: {
				System.out.println("aa wsl ! ");
				messageFromServer = entranceWorkerDB.getOrdertoEntryList((Order) message.getMessageData());
				break;
			}

			case OrderLeavingPark: {
				messageFromServer = entranceWorkerDB.getOrderExitList((Order) message.getMessageData());
				break;
			}

			// ParkManger DepartmentManger EntranceWorker
			case GetAvailableSpots: {

				messageFromServer = entranceWorkerDB.availableSpotsForCasualVisit(message.getMessageData());
				break;
			}
			case CasualVisit:
				messageFromServer = entranceWorkerDB.getOrdertoEntryList((Order) message.getMessageData());
				break;
		
			
			
			/*
			// ParkManger DepartmentManger EntranceWorker
			case GetAvailableSpots: {
				messageFromServer = ParkDB.getAvailableSpots((String) message.getMessageData());
				break;
			}*/
			// ServiceReprsientative
			case AddTravelGuide: {
//				try {
//					messageFromServer = TouristsDBcontroller.addTravelGuied((String) message.getMessageData());
//				} catch (SQLException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//-				}
				break;
			}
			// park manager
			case ShowRequestsOnePark: {
				messageFromServer = RequestDB.showRequestsOnePark((String) message.getMessageData());
				break;
			}
			// //department manager
			case ShowRequestsAllParks: {
				messageFromServer = RequestDB.showRequestsAllParks();

				break;
			}
			// park manager
			case AskPremission: {

				messageFromServer = RequestDB.addRequest((Request) message.getMessageData());
				break;
			}
			// department manager
			case AcceptRequest: {

				try {
					messageFromServer = RequestDB.acceptRequest((int) message.getMessageData());
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			}
			// department manager
			case RejectRequest: {

				try {
					messageFromServer = RequestDB.rejectRequest((int) message.getMessageData());
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			}

			// department manager
			case ShowAllReports: {

				try {
					messageFromServer = ReportsDB.getReports();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				break;
			}
			// department manager
			case ShowReportById: {
				messageFromServer = ReportsDB.getReportByID((String) message.getMessageData());
				break;
			}

			// department manager
			case CreateNewNumberOfVisitorsReport: {
				System.out.println("got to the requested case :CreateNewNumberOfVisitorsReport ");
				try {
					messageFromServer = ReportsDB.createNewNumberOfVisitorsReport((Report) message.getMessageData());
				} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			}

			// department manager
			case CreateNewUsageReport: {
				try {
					messageFromServer = ReportsDB.createUsageReport((Report) message.getMessageData());
				} catch (NumberFormatException | SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			}

			// department manager
			case CreateVisitationReport: {
				try {
					messageFromServer = ReportsDB.createVisitation_Report((int) message.getMessageData());
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			}

			// department manager
			case CreateCancellationReport: {
				
				try {
					messageFromServer = ReportsDB.createCancellation_Report((int) message.getMessageData());
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				break;
			}
			
		}

		try {

			client.sendToClient(messageFromServer);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	protected void serverStarted() {

		System.out.println("Server listening for connections on port " + this.getPort());

	}

	protected void serverStopped() {
		System.out.println("Server has stopped listening for connections.");
	}

}
