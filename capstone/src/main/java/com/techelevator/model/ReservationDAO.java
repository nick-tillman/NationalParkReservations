package com.techelevator.model;

import java.util.List;

public interface ReservationDAO {
	
	public List<Reservation> getAllReservationsBySiteId();
	public List<Reservation> getAllReservationsByCampgroundId();
	public List<Reservation> getAllReservationsForSiteByDate();
	public List<Reservation> getAllReservationsForCampgroundByDate();
	public void createReservation();


	
}
