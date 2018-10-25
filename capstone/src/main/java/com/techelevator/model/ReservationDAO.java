package com.techelevator.model;

import java.time.LocalDate;
import java.util.List;

public interface ReservationDAO {
	
	public List<Reservation> getReservationsBySiteId(Long siteId);
	public List<Reservation> getReservationsByCampgroundId(Long campgroundId);
	public List<Reservation> getReservationsForSiteByDate(LocalDate fromDate, LocalDate toDate);
	public List<Reservation> getReservationsForCampgroundByDate();
	public void makeReservation();


	
}
