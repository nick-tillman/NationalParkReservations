package com.techelevator;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;

import com.techelevator.model.jdbc.JDBCCampgroundDAO;
import com.techelevator.model.jdbc.JDBCParkDAO;
import com.techelevator.model.jdbc.JDBCReservationDAO;
import com.techelevator.model.jdbc.JDBCSiteDAO;

public class CampgroundCLI {
	
	private static final String MENU_OPTION_RETURN_TO_PREVIOUS_SCREEN = "Return to Previous Screen";
	
	private static final String MENU_PARK_OPTIONS_VIEW_CAMPGROUNDS = "View Campgrounds";
	private static final String MENU_PARK_OPTIONS_SEARCH_RESERVATION = "Search for Reservation";
	private static final String[] MENU_PARK_OPTIONS = new String[] {MENU_PARK_OPTIONS_VIEW_CAMPGROUNDS,
																	MENU_PARK_OPTIONS_SEARCH_RESERVATION,
																	MENU_OPTION_RETURN_TO_PREVIOUS_SCREEN};
	
	private static final String VIEW_CAMPGROUND_OPTIONS_SEARCH_RESERVATION = "Search for Available Reservation";
	private static final String[] VIEW_CAMPGROUND_OPTIONS = new String[] {VIEW_CAMPGROUND_OPTIONS_SEARCH_RESERVATION, 
																		MENU_OPTION_RETURN_TO_PREVIOUS_SCREEN};
	
	private JDBCCampgroundDAO campgroundDAO;
	private JDBCParkDAO parkDAO;
	private JDBCReservationDAO reservationDAO;
	private JDBCSiteDAO siteDAO;
	
	public static void main(String[] args) {
		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setUrl("jdbc:postgresql://localhost:5432/campground");
		dataSource.setUsername("postgres");
		dataSource.setPassword("postgres1");
		
		CampgroundCLI application = new CampgroundCLI(dataSource);
		application.run();
	}

	public CampgroundCLI(DataSource datasource) {
		campgroundDAO = new JDBCCampgroundDAO(datasource);
		parkDAO = new JDBCParkDAO(datasource);
		reservationDAO = new JDBCReservationDAO(datasource);
		siteDAO = new JDBCSiteDAO(datasource);
		
	}
	
	public void run() {
		
	}
}
