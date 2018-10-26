package com.techelevator.model;

import java.time.LocalDate;

public interface ReservationDAO {
	
	public Reservation makeReservation(int siteId, String name, LocalDate fromDate, LocalDate toDate);

	
}
