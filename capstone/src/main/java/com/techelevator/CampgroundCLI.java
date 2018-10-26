package com.techelevator;

import java.util.List;
import java.util.Scanner;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;

import com.techelevator.model.Campground;
import com.techelevator.model.Park;
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
	private Menu menu;
	
	public CampgroundCLI(DataSource datasource) {
		this.menu = new Menu(System.in, System.out);
		campgroundDAO = new JDBCCampgroundDAO(datasource);
		parkDAO = new JDBCParkDAO(datasource);
		reservationDAO = new JDBCReservationDAO(datasource);
		siteDAO = new JDBCSiteDAO(datasource);
	}
	
	public static void main(String[] args) {
		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setUrl("jdbc:postgresql://localhost:5432/campground");
		dataSource.setUsername("postgres");
		dataSource.setPassword("postgres1");
		
		CampgroundCLI application = new CampgroundCLI(dataSource);
		application.run();
	}
	
	public void run() {
		while(true) {
			printHeading("View Parks Interface");
			Park parkChoice = (Park)menu.getChoiceFromOptions(parkDAO.showAllParks().toArray(), "Select a Park for Further Details");
			printHeading("Park Information Screen");
			printParkInfo(parkChoice);
			String menuChoice = (String)menu.getChoiceFromOptions(MENU_PARK_OPTIONS, "Select a Command");
			handleParkOptions(parkChoice, menuChoice);
		}
	}
	
	private void handleParkOptions(Park parkChoice, String menuChoice) {
		if(menuChoice.equals(MENU_PARK_OPTIONS_VIEW_CAMPGROUNDS)) {
			handleViewCampgrounds(parkChoice);
		}else if(menuChoice.equals(MENU_PARK_OPTIONS_SEARCH_RESERVATION)) {
			handleSearchForReservation(parkChoice);
		}
	}
	
	private void handleViewCampgrounds(Park parkChoice) {
		printHeading("Park Campgrounds");
		printHeading(parkChoice.getName() + " National Park Campgrounds");
		printAllCampgroundsForPark(parkChoice.getParkId());
		String menuChoice = (String)menu.getChoiceFromOptions(VIEW_CAMPGROUND_OPTIONS, "Select a Command");
		if(menuChoice.equals(VIEW_CAMPGROUND_OPTIONS_SEARCH_RESERVATION)) {
			handleSearchForReservation(parkChoice);
		}
	}
	
	private void handleSearchForReservation(Park parkChoice) {
		printHeading("Search for Campground Reservation");
		printAllCampgroundsForPark(parkChoice.getParkId());
		String campground = getUserInput("Which campground?");
	}
	
	private void printAllCampgroundsForPark(long parkId) {
		List<Campground> campgrounds = campgroundDAO.getAllCampgroundsInPark(parkId);
		String name = String.format("%-32s", "Name")	;
		String open = String.format("%-10s", "Open");
		String close = String.format("%-13s", "Close");
		String dailyFee = String.format("%-20s", "Daily Fee");
		System.out.println();
		System.out.println(name+open+close+dailyFee);
		if(campgrounds.size() > 0) {
			for(Campground camp : campgrounds) {
				System.out.println(camp.toString());
			}
		} else {
			System.out.println("\n*** No results ***");
		}
	}
	
	private void printParkInfo(Park choice) {
		System.out.println(choice.getName() + " National Park");
		System.out.println(String.format("%-20s", "Location:") + choice.getLocation());
		System.out.println(String.format("%-20s", "Established:") + choice.getEstablishDate());
		System.out.println(String.format("%-20s", "Area:") + choice.getArea()+" sq km");
		System.out.println(String.format("%-20s", "Annual Visitors:") + choice.getVisitors());
		System.out.println();
		System.out.println(choice.getDescription());
	}
	
	private void printHeading(String headingText) {
		System.out.println("\n"+headingText);
	}
	
	private String getUserInput(String prompt) {
		System.out.print(prompt + " >>> ");
		return new Scanner(System.in).nextLine();

}
}
