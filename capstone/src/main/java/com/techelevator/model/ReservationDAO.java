package com.techelevator.model;

import java.util.List;

public interface ReservationDAO {
	
	public List<Reservation> getReservationsBySiteId();
	public List<Reservation> getReservationsByCampgroundId();
	public List<Reservation> getReservationsForSiteByDate();
	public List<Reservation> getReservationsForCampgroundByDate();
	public void createReservation();


	
}
