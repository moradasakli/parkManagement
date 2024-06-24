package EntityClasses;

public enum MessageType {
	ConnectToServer,
	DisConnectToServer,
	
	TouristLogin,
	TouristLogInSucceeded,
	TouristLogInFailed,
	TouristAlreadyLoggedIn,
	LogOutTourist,
	
	GuestTouristNewOrder,
	GuestAlreadyRegistered,
	GuestNotRegisteredAsTravelGuide,
	GuestOrderSuccess,
	GuestGoingForAlternativeOptions,
	GuestWaitingList,
	
	
	TouristUpdateOrderAfterPayment,
	UpdateOrderAfterPaymentDone,
	ConfirmArrival,
	CancelArrival,
	
	TEMP_VALUE_FOR_TESTING_EDIT,
	
	ListOfAvailableDates,
	NoAvailableDates,
	AvailableDatesSuccess,
		
	EdittedPayment,
	HandleWaitingListAfterEditingOrder,
	CheckSpotsForOrder,
	MoveOrderFromWaitingListToValid,
	DeleteFromWaitingList,
	
	ExistingTouristListOfOrders,
	ExistingTouristEditingOrder,
	ExistingTouristEditingOrderSuccess,
	ExistingTouristEditingOrderFailure,
	ExistingTouristEditOrder3Options,
	TouristAddEditingOrderToWaitingList,
	TouristWantsToDeleteAnOrder,
	ExistingTouristMakeNewOrder,
	ExistingTouristNewOrderSuccess,
	ExistingTouristGoingForAlternativeOptions,
	ExistingTouristAlternativeOptionsWaitingList,
	
	ExtractFullOrderInfo,
	
	DefaultValueFromServer,
	
	//--------------------------------------------
	
	//Employee Login
	EmployeeLogin,
	EmployeeLoginFailed,
	EmployeeLoginSucces,
	EmployeeIsLogedIn,
	
	//Employee Logout
	EmployeeLogout,
	EmployeeLogoutSucces,
	EmployeeLogoutFailed,
	
	//EntranceWorker  --> morad
	GetPriceForRecipe,
	ErrorGettingPrice,
	GettingPriceSucces,
	OrderEntringThePark,
	orderAddedToEntryListSuccusefully,
	orderAddedToEntryListFailed,
	orderDeletedFromEntryAndMovedToExit,
	OrderLeavingPark,
	orderDeletedFromEntryFailed,
	orderDeletedFromEntrynotFound,
	orderCameLateToVisit,
	CasualVisit,
	
	//EntranceWorker + ParkManger + DepartmentManegr  ***********
	GetAvailableSpots,
	AvailableSpotsSucces,
	AvailableSpotsFailed,
	
	//ServiceReprsientative -> adding a new travel guide
	AddTravelGuide,
	AddTravelGuiedAlreadyATravelGuied,
	AddingTravelGuiedSucces,
	AddingTravelGuideFailed,
	
    //ParkManger -> show all active request of his park
	ShowRequestsOnePark,
	ShowRequestsOneParkSucces,
	ShowRequestsOneParkFailed,
	
	//parkManger -> ask new request
	AskPremission,
	AddRequestSucces,
	AddRequestFailed,
	
	//DepartmentManger -> show requests in table
	ShowRequestsAllParks,
	ShowRequestsAllParksFailed,
	ShowRequestsAllParksSucces,
	
	//accept and reject request
	AcceptRequest,
	AcceptRequestSucces,
	AcceptRequestFailed,
	AcceptRequestFailedIdNotFound,
	RejectRequest,
	RejectRequestSucces,
	RejectRequestFailed,
	
	//Department manger show all reports in table 
	ShowAllReports,
	ShowAllReportsFailed,
	ShowAllReportsSucces,
	
	//show report with ID
	ShowReportById,
	NoReportWithThisID,
	ShowReportByIdFailed,
	
	//deparmentManger show report
	ShowNumberOfVisitorsReport,
	ShowUsageReport,



	//number of visitors report
	CreateNewNumberOfVisitorsReport,
	CreateNewNumberOfVisitorsReportSucces,
	CreateNewNumberOfVisitorsReportFailed,
	
	//usage report
	CreateNewUsageReport,
	CreateNewUsageReportSucces,
	CreateNewUsageReportFailed,
	
	//visitation report
	CreateVisitationReport,
	CreateVisitationReportSucces,
	CreateVisitationReportFailed,
	
	//cancellation report
	CreateCancellationReport,
	CreateCancellationReportSucces,
	CreateCancellationReportFailed;
	
	
	
	
}
