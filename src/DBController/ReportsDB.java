package DBController;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import EntityClasses.*;
import Server.EchoServer;
import client.ClientUI;

public class ReportsDB {

	/**
	 * Generates and returns a valid ID for a new report.
	 *
	 * @return A valid ID for a new report.
	 * @throws SQLException If an SQL exception occurs.
	 */

	public static String generateReportId() throws SQLException {
		String report_id = "-1";
		PreparedStatement ps;
		ResultSet rs;
		ps = EchoServer.mySql.connection.prepareStatement("SELECT reports_counter FROM generalinfoandcounters");
		rs = ps.executeQuery();

		if (rs.next())
			report_id = rs.getString("reports_counter");

		return "" + (Integer.parseInt(report_id) + 1);
	}

	/**
	 * Updates the report ID in the database after generating a new report ID.
	 *
	 * @throws SQLException If an SQL exception occurs.
	 */
	public static void updateReportId() throws SQLException {
		PreparedStatement ps;
		ResultSet rs;
		int rowsAffected;

		String report_id = generateReportId();
		ps = EchoServer.mySql.connection
				.prepareStatement("UPDATE generalinfoandcounters SET reports_counter =" + report_id);
		rowsAffected = ps.executeUpdate();

	}

	/**
	 * Retrieves all reports from the database.
	 *
	 * @return A message containing the type of the result (success or failure) and
	 *         a list of reports.
	 * @throws SQLException If an SQL exception occurs.
	 */
	public static Message getReports() throws SQLException {
		List<Report> reports = new ArrayList<Report>();

		int id;
		String park_num;
		int month;
		String type;

		try {
			PreparedStatement ps;
			ResultSet rs;
			ps = EchoServer.mySql.connection.prepareStatement("SELECT * FROM reports");
			rs = ps.executeQuery();

			while (rs.next()) {
				id = rs.getInt("id");
				park_num = rs.getString("park_num");
				month = rs.getInt("month");
				type = rs.getString("type");

				reports.add(new Report(id, park_num, month, type));
			}
		} catch (SQLException e) {

			return new Message(MessageType.ShowAllReportsFailed, null);
		}

		return new Message(MessageType.ShowAllReportsSucces, reports);
	}

	/**
	 * Retrieves a specific report from the database based on its ID.
	 *
	 * @param report_num The ID of the report to retrieve.
	 * @return The requested report instance or null if not found.
	 * @throws SQLException If an SQL exception occurs.
	 */

	public static Report getReport(int report_num) throws SQLException {

		int id = report_num;
		String park_num;
		int month;
		String type;

		PreparedStatement ps;
		ResultSet rs;
		ps = EchoServer.mySql.connection.prepareStatement("SELECT * FROM reports WHERE id='" + report_num + "'");
		rs = ps.executeQuery();

		if (rs.next()) {
			id = rs.getInt("id");
			park_num = rs.getString("park_num");
			month = rs.getInt("month");
			type = rs.getString("type");

			return (new Report(id, park_num, month, type));
		}
		return null; // not found

	}

	/**
	 * Adds a new report to the database.
	 *
	 * @param p_n The park number.
	 * @param m   The month.
	 * @param t   The type of the report.
	 * @return The newly created report instance or null if insertion fails.
	 * @throws NumberFormatException If the park number cannot be parsed.
	 * @throws SQLException          If an SQL exception occurs.
	 */
	public static Report addReport(String p_n, int m, String t) throws NumberFormatException, SQLException {
		int id = Integer.parseInt(generateReportId());
		System.out.println("now my report in addReport is  : " + id);
		String park_num = p_n;
		int month = m;
		String type = t;
		Report report = new Report(id, park_num, month, type);

		String query = "INSERT INTO reports (id,park_num,month ,type)" + "VALUES (?, ?, ?, ?)";
		try (PreparedStatement preparedStatement = EchoServer.mySql.connection.prepareStatement(query)) {
			preparedStatement.setInt(1, report.getId());
			preparedStatement.setString(2, report.getPark_num());
			preparedStatement.setInt(3, report.getMonth());
			preparedStatement.setString(4, report.getType());

			int rowsInserted = preparedStatement.executeUpdate();
			if (rowsInserted > 0) {
				updateReportId();
				return report;
			} else
				return null;
		}

	}

	/**
	 * Calculates the number of single visitors for a specified park and month.
	 *
	 * @param park_num The park number.
	 * @param month    The month.
	 * @return The number of single visitors.
	 * @throws SQLException If an SQL exception occurs.
	 */
	// Number of visitors Report --> ParkManger create
	public static int getNumberOfSingle_Visitors(String park_num, int month) throws SQLException {
		int num_of_single_visitors = 0;
		List<Order> orders = new ArrayList<Order>();
		Date dateofvisit;
		String visitors_num;
		Order o;

		PreparedStatement ps;
		ResultSet rs;
		ps = EchoServer.mySql.connection.prepareStatement(
				"SELECT date_of_visit,number_of_visitors FROM exitlistforpark" + park_num + " WHERE is_organized= 0");
		rs = ps.executeQuery();

		while (rs.next()) {
			o = new Order();
			o.setDateOfVisit(((java.sql.Date) rs.getDate("date_of_visit")).toLocalDate());
			o.setNumberOfVisitors(rs.getInt("number_of_visitors"));

			orders.add(o);

		}

		for (Order order : orders) {
			if (order.getDateOfVisit().getMonthValue() == month) {
				num_of_single_visitors += order.getNumberOfVisitors();
			}
		}

		return num_of_single_visitors;
	}

	/**
	 * Calculates the number of group visitors for a specified park and month.
	 *
	 * @param park_num The park number.
	 * @param month    The month.
	 * @return The number of group visitors.
	 * @throws SQLException If an SQL exception occurs.
	 */

	public static int getNumberOfGroup_Visitors(String park_num, int month) throws SQLException {
		int num_of_group_visitors = 0;
		List<Order> orders = new ArrayList<Order>();
		Date dateofvisit;
		String visitors_num;
		Order o;

		PreparedStatement ps;
		ResultSet rs;
		ps = EchoServer.mySql.connection.prepareStatement(
				"SELECT date_of_visit,number_of_visitors FROM exitlistforpark" + park_num + " WHERE is_organized= 1");
		rs = ps.executeQuery();

		while (rs.next()) {
			o = new Order();
			o.setDateOfVisit(((java.sql.Date) rs.getDate("date_of_visit")).toLocalDate());
			o.setNumberOfVisitors(rs.getInt("number_of_visitors"));

			orders.add(o);

		}

		for (Order order : orders) {
			if (order.getDateOfVisit().getMonthValue() == month) {
				num_of_group_visitors += order.getNumberOfVisitors();
			}
		}

		return num_of_group_visitors;
	}

	/**
	 * Adds a number of visitors report to the database for a specified park and
	 * month.
	 *
	 * @param report_num The report ID.
	 * @param park_num   The park number.
	 * @param month      The month.
	 * @return True if the report is added successfully, false otherwise.
	 * @throws SQLException If an SQL exception occurs.
	 */

	public static boolean add_Number_Of_Visitors_Report(String report_num, String park_num, int month)
			throws SQLException {
		Number_Of_Visitors_Report new_report = new Number_Of_Visitors_Report();

		new_report.setReport_id(report_num);
		new_report.setMonth(month);
		new_report.setPark_num(park_num);
		new_report.setNumber_of_single_visitors(getNumberOfSingle_Visitors(park_num, month));
		new_report.setNumber_of_group_visitors(getNumberOfGroup_Visitors(park_num, month));

		String query = "INSERT INTO reports_number_of_visitors (report_id,park_num,month ,number_of_single_visitors,number_of_group_visitors)"
				+ "VALUES (?, ?, ?, ?,?)";
		try (PreparedStatement preparedStatement = EchoServer.mySql.connection.prepareStatement(query)) {
			preparedStatement.setString(1, new_report.getReport_id());
			preparedStatement.setString(2, new_report.getPark_num());
			preparedStatement.setInt(3, new_report.getMonth());
			preparedStatement.setInt(4, new_report.getNumber_of_single_visitors());
			preparedStatement.setInt(5, new_report.getNumber_of_group_visitors());

			int rowsInserted = preparedStatement.executeUpdate();
			if (rowsInserted > 0) {
				return true;
			} else
				return false;
		}

	}

	/**
	 * Checks if a list contains a specific number.
	 *
	 * @param list   The list to search.
	 * @param number The number to check for.
	 * @return True if the number is found in the list, false otherwise.
	 */
	// Usage_Report --> ParkManger Create
	public static boolean containsNumber(List<Integer> list, int number) {
		for (int num : list) {
			if (num == number) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Retrieves times when a park reached full capacity for a given month.
	 *
	 * @param park_num The park number.
	 * @param month    The month.
	 * @return A list of times when the park reached full capacity.
	 * @throws SQLException If an SQL exception occurs.
	 */
	public static List<timesoffullcapacityforpark> getTimesOfFullCapacityOfPark(String park_num, int month)
			throws SQLException {
		List<timesoffullcapacityforpark> list = new ArrayList<timesoffullcapacityforpark>();
		List<timesoffullcapacityforpark> return_list = new ArrayList<timesoffullcapacityforpark>();

		int id;
		LocalDate date;
		int number_of_visitors;

		PreparedStatement ps;
		ResultSet rs;
		ps = EchoServer.mySql.connection.prepareStatement(
				"SELECT order_num,date_of_visit,number_of_visitors FROM timesoffullcapacityforpark" + park_num);
		rs = ps.executeQuery();

		while (rs.next()) {
			id = rs.getInt("order_num");
			java.sql.Date sqlDate = rs.getDate("date_of_visit");
			date = sqlDate.toLocalDate();
			date = date.plusDays(1);

			number_of_visitors = rs.getInt("number_of_visitors");
			list.add(new timesoffullcapacityforpark(id, date, number_of_visitors));

		}

		for (timesoffullcapacityforpark l : list) {
			if (l.getDate().getMonthValue() == month) {
				return_list.add(l);
			}
		}

		return return_list;

	}

	/**
	 * Checks if a specific day is in the list of times when a park reached full
	 * capacity.
	 *
	 * @param list The list of times when the park reached full capacity.
	 * @param day  The day to search for.
	 * @return True if the day is found, false otherwise.
	 */
	public static boolean find_day(List<timesoffullcapacityforpark> list, int day) {
		for (timesoffullcapacityforpark l : list) {
			if (l.getDate().getDayOfMonth() == day)

				return true;
		}
		return false;
	}

	/**
	 * Adds a usage report to the database for a specified park and month.
	 *
	 * @param report_num The report ID.
	 * @param park_num   The park number.
	 * @param month      The month.
	 * @return True if the report is added successfully, false otherwise.
	 * @throws SQLException If an SQL exception occurs.
	 */
	public static boolean add_Usage_Report(String report_num, String park_num, int month) throws SQLException {

		List<timesoffullcapacityforpark> list = getTimesOfFullCapacityOfPark(park_num, month);
		int day;
		int rowsInserted = -1;
		List<Integer> days_31 = new ArrayList<Integer>();
		days_31.add(1);
		days_31.add(3);
		days_31.add(5);
		days_31.add(7);
		days_31.add(8);
		days_31.add(10);
		days_31.add(12);

		List<Integer> days_30 = new ArrayList<Integer>();
		days_30.add(4);
		days_30.add(6);
		days_30.add(9);
		days_30.add(11);

		if (containsNumber(days_31, month)) {
			for (day = 1; day <= 31; day++) {

				String query = "INSERT INTO usage_report (report_id,park_num,month ,day,full_capacity_status)"
						+ "VALUES (?, ?, ?, ?,?)";
				try (PreparedStatement preparedStatement = EchoServer.mySql.connection.prepareStatement(query)) {
					preparedStatement.setString(1, report_num);
					preparedStatement.setString(2, park_num);
					preparedStatement.setInt(3, month);
					preparedStatement.setInt(4, day);
					if (find_day(list, day))
						preparedStatement.setInt(5, 1);
					else
						preparedStatement.setInt(5, 0);

					rowsInserted = preparedStatement.executeUpdate();

				}
			}
		}

		else {
			if (containsNumber(days_30, month)) {
				for (day = 1; day <= 30; day++) {

					String query = "INSERT INTO usage_report (report_id,park_num,month ,day,full_capacity_status)"
							+ "VALUES (?, ?, ?, ?,?)";
					try (PreparedStatement preparedStatement = EchoServer.mySql.connection.prepareStatement(query)) {
						preparedStatement.setString(1, report_num);
						preparedStatement.setString(2, park_num);
						preparedStatement.setInt(3, month);
						preparedStatement.setInt(4, day);
						if (find_day(list, day))
							preparedStatement.setInt(5, 1);
						else
							preparedStatement.setInt(5, 0);
						rowsInserted = preparedStatement.executeUpdate();

					}
				}
			}

			else // 28 days
			{
				for (day = 1; day <= 28; day++) {
					String query = "INSERT INTO usage_report (report_id,park_num,month ,day,full_capacity_status)"
							+ "VALUES (?, ?, ?, ?,?)";
					try (PreparedStatement preparedStatement = EchoServer.mySql.connection.prepareStatement(query)) {
						preparedStatement.setString(1, report_num);
						preparedStatement.setString(2, park_num);
						preparedStatement.setInt(3, month);
						preparedStatement.setInt(4, day);
						if (find_day(list, day))
							preparedStatement.setInt(5, 1);
						else
							preparedStatement.setInt(5, 0);

						rowsInserted = preparedStatement.executeUpdate();

					}
				}
			}
		}
		if (rowsInserted > 0)
			return true;
		return false;
	}

	/**
	 * Retrieves a report from the database based on its ID.
	 *
	 * @param report_num The ID of the report to retrieve.
	 * @return A message containing the type of the result (success, failure, or no
	 *         report with this ID) and the requested report instance.
	 */
	// DepartmentManger
	public static Message getReportByID(String report_num) {
		int id;
		int park_num;
		int month;
		String type;

		try {
			PreparedStatement ps;
			ResultSet rs;
			ps = EchoServer.mySql.connection.prepareStatement("SELECT * FROM reports WHERE id='" + report_num + "'");
			rs = ps.executeQuery();

			if (rs.next()) {
				id = rs.getInt("id");
				park_num = rs.getInt("park_num");
				month = rs.getInt("month");
				type = rs.getString("type");

				Report report = new Report(id, "" + park_num, month, type);

				switch (report.getType()) {
				case "Usage Report": {
					return (new Message(MessageType.ShowUsageReport,
							getUsageReport(report_num, report.getPark_num(), report.getMonth())));
				}
				case "Number Of Visitors": {
					return (new Message(MessageType.ShowNumberOfVisitorsReport, getNumberOfVisitorsReport(report_num)));
				}
				}
			}

		} catch (SQLException e) {
			return (new Message(MessageType.ShowReportByIdFailed, null));
		}

		return (new Message(MessageType.NoReportWithThisID, null)); // there isnt any report wi

	}

	/**
	 * Retrieves a number of visitors report from the database based on its ID.
	 *
	 * @param report_num The ID of the report to retrieve.
	 * @return The requested number of visitors report instance.
	 * @throws SQLException If an SQL exception occurs.
	 */
	// Department Manger
	public static Number_Of_Visitors_Report getNumberOfVisitorsReport(String report_num) throws SQLException {
		String report_id;
		String park_num;
		int month;
		int number_of_single_visitors;
		int number_of_group_visitors;

		PreparedStatement ps;
		ResultSet rs;
		ps = EchoServer.mySql.connection
				.prepareStatement("SELECT * FROM reports_number_of_visitors WHERE report_id='" + report_num + "'");
		rs = ps.executeQuery();

		if (rs.next()) {
			report_id = rs.getString("report_id");
			park_num = rs.getString("park_num");
			month = rs.getInt("month");
			number_of_single_visitors = rs.getInt("number_of_single_visitors");
			number_of_group_visitors = rs.getInt("number_of_group_visitors");

			return new Number_Of_Visitors_Report(report_id, park_num, month, number_of_single_visitors,
					number_of_group_visitors);
		}
		return null;
	}

	/**
	 * Retrieves a usage report from the database based on its ID, park number, and
	 * month.
	 *
	 * @param report_num The ID of the report to retrieve.
	 * @param park_n     The park number.
	 * @param m          The month.
	 * @return The requested usage report instance.
	 * @throws SQLException If an SQL exception occurs.
	 */

	// Department Manger
	public static Usage_Report getUsageReport(String report_num, String park_n, int m) throws SQLException {
		Usage_Report usage_report;
		List<Usage_Report_Show> daily_reports = new ArrayList<Usage_Report_Show>();
		String report_id = report_num;
		String park_num = park_n;
		int month = m;
		int day;
		boolean full_capacity_status;

		PreparedStatement ps;
		ResultSet rs;
		ps = EchoServer.mySql.connection
				.prepareStatement("SELECT day,full_capacity_status FROM usage_report WHERE report_id='" + report_num
						+ "' AND month='" + month + "' AND park_num='" + park_num + "'");
		rs = ps.executeQuery();

		while (rs.next()) {
			day = rs.getInt("day");
			full_capacity_status = rs.getBoolean("full_capacity_status");

			daily_reports.add(new Usage_Report_Show(day, full_capacity_status));

		}
		if (daily_reports != null) {
			System.out.println("we got here and now should print the daily reports ! ");
			System.out.println(daily_reports.toString());
		} else
			System.out.println("list is null ! ");

		Usage_Report usageReport = new Usage_Report(report_id, park_num, month, daily_reports);
		System.out.println("final list :" + usageReport.toString());
		return usageReport;

	}

	/**
	 * Creates and saves a new number of visitors report in the database.
	 *
	 * @param r The report instance containing the required data.
	 * @return The newly created number of visitors report instance.
	 * @throws SQLException If an SQL exception occurs.
	 */
	// parkManger
	public static Message createNewNumberOfVisitorsReport(Report r) throws NumberFormatException, SQLException {

		r = addReport(r.getPark_num(), r.getMonth(), r.getType());
		if (r == null) {
			return new Message(MessageType.CreateNewNumberOfVisitorsReportFailed, false);
		} else {
			if (add_Number_Of_Visitors_Report("" + r.getId(), r.getPark_num(), r.getMonth())) {
				return new Message(MessageType.CreateNewNumberOfVisitorsReportSucces, true);
			} else {
				return new Message(MessageType.CreateNewNumberOfVisitorsReportFailed, false);
			}
		}
	}

	/**
	 * Creates and saves a new usage report in the database.
	 *
	 * @param r The report instance containing the required data.
	 * @return The newly created usage report instance.
	 * @throws SQLException If an SQL exception occurs.
	 */
	// parkManger
	public static Message createUsageReport(Report r) throws NumberFormatException, SQLException {
		System.out.println("the report id is : " + r.getId());
		r = addReport(r.getPark_num(), r.getMonth(), r.getType());
		if (r == null) {
			return new Message(MessageType.CreateNewUsageReportFailed, false);
		} else {
			if (add_Usage_Report("" + r.getId(), r.getPark_num(), r.getMonth())) {
				return new Message(MessageType.CreateNewUsageReportSucces, true);
			} else {
				return new Message(MessageType.CreateNewUsageReportFailed, false);
			}
		}
	}

	/**
	 * Creates a new visitation report for a specified month.
	 *
	 * @param month The month for which to create the report.
	 * @return The newly created visitation report instance.
	 * @throws SQLException If an SQL exception occurs.
	 */
	// DepartmentManger --> generate Cancellation Report
	public static Message createVisitation_Report(int month) throws SQLException {

		List<Order> orders = orderDBcontroller.getOrdersOfAllParksForVisitationReport(month);
		if (orders == null || orders.isEmpty())
			return new Message(MessageType.CreateVisitationReportFailed, null);
		Visitation_Report report = new Visitation_Report(month);
		System.out.println("my report before calculations :  : " + report.toString());

		report.calculate_Single_And_Group(orders);

		return new Message(MessageType.CreateVisitationReportSucces, report);

	}

	/**
	 * Creates a new cancellation report for a specified month.
	 *
	 * @param month The month for which to create the report.
	 * @return The newly created cancellation report instance.
	 * @throws SQLException If an SQL exception occurs.
	 */
	public static Message createCancellation_Report(int month) throws SQLException {
		List<Order> orders = orderDBcontroller.getOrdersOfAllParksForCancellationReport(month);
		if (orders == null)
			return new Message(MessageType.CreateCancellationReportFailed, null);

		List<OrdersToShowForCancellationReport> canceled = new ArrayList<OrdersToShowForCancellationReport>();
		List<OrdersToShowForCancellationReport> not_canceled = new ArrayList<OrdersToShowForCancellationReport>();
		int[] canceled_cnt = new int[7];
		int[] not_canceled_cnt = new int[7];
		double[] canceled_avg = new double[7];
		double[] not_canceled_avg = new double[7];
		Cancellation_Report new_report = new Cancellation_Report(month);

		LocalDate now = LocalDate.now();
		int index = 0;
		for (Order o : orders) {
			if (!(o.isOrderStatus()) || (o.isOrderStatus() && !(o.isConfirmed()) && o.getDateOfVisit().isAfter(now))) // canceled
			{
				index = (o.getDateOfVisit().getDayOfWeek().getValue() - 1) % 7;
				canceled.add(new OrdersToShowForCancellationReport(Integer.parseInt(o.getParkNameInOrder()),
						Integer.parseInt(o.getNumberOfVisitorsInOrder()), "" + o.isPayed(), o.getPrice()));

				canceled_cnt[index] += Integer.parseInt(o.getNumberOfVisitorsInOrder());
				System.out.println("canceled order" + o.toString());

			} else {
				if (o.isOrderStatus() && o.isConfirmed() && o.getDateOfVisit().isBefore(now))// not canceled
				{

					index = (o.getDateOfVisit().getDayOfWeek().getValue() - 1) % 7;
					
					not_canceled.add(new OrdersToShowForCancellationReport(Integer.parseInt(o.getParkNameInOrder()),
							Integer.parseInt(o.getNumberOfVisitorsInOrder()), "" + o.isPayed(), o.getPrice()));
		

					not_canceled_cnt[index] += Integer.parseInt(o.getNumberOfVisitorsInOrder());
					System.out.println("8");
					System.out.println("not canceled order" + not_canceled.toString());
				}
			}
		}

		/////////////////////// *******************
		new_report.setCanceled(canceled);
		new_report.setNot_canceled(not_canceled);
		new_report.setCanceled_cnt(canceled_cnt);
		new_report.setNot_Canceled_cnt(not_canceled_cnt);

		/// calculate avg
		int num_of_days;
		if (month == 1 | month == 3 | month == 5 | month == 7 | month == 8 | month == 10 | month == 12) // 31 days
		{
			num_of_days = 31;
			for (int i = 0; i < 7; i++) {
				{
					canceled_avg[i] =(double) canceled_cnt[i] / num_of_days;
					not_canceled_avg[i] =(double) not_canceled_cnt[i] / num_of_days;
				}
			}
		} else {
			if (month == 2) // 28 days
			{
				num_of_days = 28;
				for (int i = 0; i < 7; i++) {
					canceled_avg[i] = (double)canceled_cnt[i] / num_of_days;
					not_canceled_avg[i] =(double) not_canceled_cnt[i] / num_of_days;
				}
			} else // 30 days
			{
				num_of_days = 30;
				for (int i = 0; i < 7; i++) {
					canceled_avg[i] =(double) canceled_cnt[i] / num_of_days;
					not_canceled_avg[i] = (double)not_canceled_cnt[i] / num_of_days;
				}
			}

		}
		new_report.setNot_Canceled_avg(not_canceled_avg);
		new_report.setCanceled_avg(canceled_avg);
		System.out.println(new_report.toString());
		return new Message(MessageType.CreateCancellationReportSucces, new_report);

	}
}
