package com.techelevator;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;

import com.techelevator.model.Campground;
import com.techelevator.model.Park;
import com.techelevator.model.Reservation;
import com.techelevator.model.Site;
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
		menu = new Menu(System.in, System.out);
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
		//print banner
		while(true) {
			printHeading("View Parks Interface");
			Park parkChoice = (Park)menu.getChoiceFromOptions(parkDAO.showAllParks().toArray(), "Select a Park for Further Details");
			if(parkChoice.equals("")) { //check for quit
				System.exit(1);
			} else {
				handleParkOptions(parkChoice);
			}
		}
	}
	
	private void handleParkOptions(Park parkChoice) {
		printHeading("\nPark Information Screen");
		printParkInfo(parkChoice);
		String menuChoice = (String)menu.getChoiceFromOptions(MENU_PARK_OPTIONS, "\nSelect a Command");
		if(menuChoice.equals(MENU_PARK_OPTIONS_VIEW_CAMPGROUNDS)) {
			handleViewCampgrounds(parkChoice);
		}else if(menuChoice.equals(MENU_PARK_OPTIONS_SEARCH_RESERVATION)) {
			handleSearchForReservation(parkChoice);
		}
	}
	
	private void handleViewCampgrounds(Park parkChoice) {
		printHeading("\nPark Campgrounds");
		printHeading(parkChoice.getName() + " National Park Campgrounds");
		printAllCampgroundsForPark(parkChoice.getParkId());
		String menuChoice = (String)menu.getChoiceFromOptions(VIEW_CAMPGROUND_OPTIONS, "\nSelect a Command");
		if(menuChoice.equals(VIEW_CAMPGROUND_OPTIONS_SEARCH_RESERVATION)) {
			handleSearchForReservation(parkChoice);
		}
	}
	
	private void handleSearchForReservation(Park parkChoice) {
		boolean done = false;
		while(!done) {
			printHeading("\nSearch for Campground Reservation");
			printAllCampgroundsForPark(parkChoice.getParkId());
			List<Campground> campgrounds = campgroundDAO.getAllCampgroundsInPark(parkChoice.getParkId());
			String campString = getUserInput("\nWhich campground (enter 0 to cancel)?");
			int campInt = Integer.parseInt(campString);
			
			String fromDate = getUserInput("What is the arrival date? (mm-dd-yyyy)");
			String toDate = getUserInput("What is the departure date? (mm-dd-yyyy)");
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-d-yyyy");
			LocalDate fd = LocalDate.parse(fromDate, formatter);
			LocalDate td = LocalDate.parse(toDate, formatter);
			int fromDateMonth = fd.getMonthValue();
			int toDateMonth = td.getMonthValue();
			
				if(campInt == 2 && parkChoice.getParkId() == 1 && fromDateMonth < 5 || toDateMonth > 9) {
				System.out.println("\nTo book this campground, please select dates between its open months of May and September.");
				}
			else if(campInt == 3 && parkChoice.getParkId() == 1 && fromDateMonth < 5 || toDateMonth > 10) {
				System.out.println("\nTo book this campground, please select dates between its open months of May and October.");
				}
			else if(campInt == 1 && parkChoice.getParkId() == 3 && fromDateMonth < 5 || toDateMonth > 11) {
				System.out.println("\nTo book this campground, please select dates between its open months of May and November.");
			
			} else if(campInt <= campgrounds.size() && campInt > 0) {
				
				List<Site> sites = printAllAvailableSites(campgrounds.get(campInt - 1), fd, td);
				makeReservation(sites, fd, td);
				done = true;
				
			} else if(campInt > campgrounds.size()) {
				System.out.println("\n*** "+campInt+" is not a valid option ***\n");
			} else {
				done = true;
			}
		}
	}
	
	private void makeReservation(List<Site> sites, LocalDate fromDate, LocalDate toDate) {
		List<String> siteOptions = new ArrayList<String>();
		for(int i = 0; i < sites.size(); i++) {
			siteOptions.add(String.valueOf(sites.get(i).getSiteNumber()));
		}
		boolean done = false;
		while(!done) {
			String siteChoice = getUserInput("\nWhich site should be reserved (enter 0 to cancel)?");
			if(siteOptions.contains(siteChoice)) {
				int siteChoiceInt = Integer.parseInt(siteChoice);
				long siteId = 0;
				for(Site site : sites) {
					if(site.getSiteNumber() == siteChoiceInt) {
						siteId = site.getSiteId();
					}
				}
				String resName = getUserInput("What name should the reservation be made under?");
				Reservation newRes = new Reservation((int)siteId, resName, fromDate, toDate);
				Reservation returnedRes = reservationDAO.makeReservation(newRes);
				System.out.println("The Reservation has been made and the confirmation id is " + returnedRes.getReservationId());
				done = true;
			} else if(siteChoice.equals("0")) {
				done = true;
			} else {
				System.out.println("\n*** "+siteChoice+" is not a valid option ***\n");
			}
		}
	}
	
	private void printParkInfo(Park choice) {
		System.out.println(choice.getName() + " National Park");
		System.out.println(String.format("%-20s", "Location:") + choice.getLocation());
		System.out.println(String.format("%-20s", "Established:") + choice.getEstablishDate());
		System.out.println(String.format("%-20s", "Area:") + choice.getArea()+" sq km");
		System.out.println(String.format("%-20s", "Annual Visitors:") + choice.getVisitors());
		System.out.println();
		int charCount = 0;
		for(int i = 0; i < choice.getDescription().toCharArray().length; i++) {
			System.out.print(choice.getDescription().toCharArray()[i]);
			charCount++;
			if(charCount > 60 && choice.getDescription().toCharArray()[i] == ' ') {
				System.out.print("\n");
				charCount = 0;
			}
		}
		System.out.println();
	}
	
	private void printAllCampgroundsForPark(long parkId) {
		List<Campground> campgrounds = campgroundDAO.getAllCampgroundsInPark(parkId);
		System.out.println(String.format("%-5s%-32s%-10s%-13s%-20s", "", "Name", "Open", "Close", "Daily Fee"));
		if(campgrounds.size() > 0) {
			int i = 1;
			for(Campground camp : campgrounds) {
				System.out.println(String.format("%-5s","#"+i)+camp.toString());
				i++;
			}
		} else {
			System.out.println("\n*** No results ***");
		}
	}
		
	private List<Site> printAllAvailableSites(Campground campground, LocalDate fromDate, LocalDate toDate) {
		long daysBetween = ChronoUnit.DAYS.between(fromDate, toDate);
		double costPerDay = campground.getDailyFee();
		double cost = costPerDay * daysBetween;
		String costString = String.format("%.2f", cost);
		List<Site> sites = siteDAO.getListOfAvailableSites(campground.getCampgroundId(), fromDate, toDate);
		printHeading("Results Matching Your Search Criteria");
		System.out.println(String.format("%-13s%-13s%-16s%-18s%-18s%-13s", 
								"Site No.","Max Occup.","Accessible?","Max RV Length","Utility","Cost"));
			
		if(sites.size() > 0) {
			for(Site site : sites) {
				System.out.println(site.toString() + "$" + costString);
			}
		} else {
			System.out.println("\n*** No results ***");
		}
		return sites;
		
	}
	
	private void printHeading(String headingText) {
		System.out.println(headingText);
	}
	
	private String getUserInput(String prompt) {
		System.out.print(prompt + " >>> ");
		return new Scanner(System.in).nextLine();

	}
}
