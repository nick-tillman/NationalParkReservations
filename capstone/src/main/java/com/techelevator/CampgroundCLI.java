package com.techelevator;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
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
	private PrintWriter out;
	private Scanner in;
	private JDBCCampgroundDAO campgroundDAO;
	private JDBCParkDAO parkDAO;
	private JDBCReservationDAO reservationDAO;
	private JDBCSiteDAO siteDAO;
	
	public static void main(String[] args) {
		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setUrl("jdbc:postgresql://localhost:5432/campground");
		dataSource.setUsername("postgres");
		dataSource.setPassword("postgres1");
		
		CampgroundCLI application = new CampgroundCLI(dataSource, System.in, System.out);
		application.run();
	}

	public CampgroundCLI(DataSource datasource, InputStream input, OutputStream output) {
		campgroundDAO = new JDBCCampgroundDAO(datasource);
		parkDAO = new JDBCParkDAO(datasource);
		reservationDAO = new JDBCReservationDAO(datasource);
		siteDAO = new JDBCSiteDAO(datasource);
		this.out = new PrintWriter(output);
		this.in = new Scanner(input);
	}
	
	public void run() {
		while(true) {
			printHeading("View Parks Interface");
			Park choice = (Park)getChoiceFromOptions(parkDAO.showAllParks().toArray(), "Select a Park for Further Details");
			printHeading("Park Information Screen");
			printParkInfo(choice);
			String parkChoice = (String)getChoiceFromOptions(MENU_PARK_OPTIONS, "Select a Command");
			handleParkOptions(choice, parkChoice);
		}
	}
	
	public void handleParkOptions(Park choice, String parkChoice) {
		if(parkChoice.equals(MENU_PARK_OPTIONS_VIEW_CAMPGROUNDS)) {
			handleViewCampgrounds(choice);
		}else if(parkChoice.equals(MENU_PARK_OPTIONS_SEARCH_RESERVATION)) {
			//do something
		}
	}
	
	private void handleViewCampgrounds(Park choice) {
		printAllCampgroundsForPark(choice.getParkId());
		String campChoice = (String)getChoiceFromOptions(VIEW_CAMPGROUND_OPTIONS, "Select a Command");
		if(campChoice.equals(VIEW_CAMPGROUND_OPTIONS_SEARCH_RESERVATION)) {
			//do something
		}
	}
	private void printAllCampgroundsForPark(long parkId) {
		List<Campground> campgrounds = campgroundDAO.getAllCampgroundsInPark(parkId);
		String name = String.format("%-32s", "Name")	;
		String open = String.format("%-10s", "Open");
		String close = String.format("%-13s", "Close");
		String dailyFee = String.format("%-20s", "Daily Fee");
		out.println();
		out.println(name+open+close+dailyFee);
		if(campgrounds.size() > 0) {
			for(Campground camp : campgrounds) {
				out.println(camp.toString());
			}
		} else {
			out.println("\n*** No results ***");
		}
	}
	
	private void printParkInfo(Park choice) {
		out.println(choice.getName());
		out.println("Location:\t" + choice.getLocation());
		out.println("Established:\t" + choice.getEstablishDate());
		out.println("Area:\t" + choice.getArea()+" sq km");
		out.println("Annual Visitors:\t" + choice.getVisitors());
		out.println();
		out.println(choice.getDescription());
	}
	
	private void printHeading(String headingText) {
		out.println("\n"+headingText);
	}
	
	private Object getChoiceFromOptions(Object[] options, String message) {
		Object choice = null;
		while(choice == null) {
			displayMenuOptions(options, message);
			choice = getChoiceFromUserInput(options);
		}
		return choice;
	}

	private Object getChoiceFromUserInput(Object[] options) {
		Object choice = null;
		String userInput = in.nextLine();
		try {
			int selectedOption = Integer.valueOf(userInput);
			if(selectedOption <= options.length) {
				choice = options[selectedOption - 1];
			}
		} catch(NumberFormatException e) {
			// eat the exception, an error message will be displayed below since choice will be null
		}
		if(choice == null) {
			out.println("\n*** "+userInput+" is not a valid option ***\n");
		}
		return choice;
	}

	private void displayMenuOptions(Object[] options, String message) {
		out.print("\n"+message+"\n");
		for(int i = 0; i < options.length; i++) {
			int optionNum = i+1;
			out.println(optionNum+") "+options[i]);
		}
		out.flush();
	}
}
