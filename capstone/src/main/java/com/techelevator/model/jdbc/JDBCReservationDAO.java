package com.techelevator.model.jdbc;

import java.util.List;

import com.techelevator.model.Reservation;
import com.techelevator.model.ReservationDAO;

public class JDBCReservationDAO implements ReservationDAO{

	@Override
	public List<Reservation> getAllReservationsBySiteId() {
		return null;
	}

	@Override
	public List<Reservation> getAllReservationsByCampgroundId() {
		return null;
	}

	@Override
	public List<Reservation> getAllReservationsForSiteByDate() {
		return null;
	}

	@Override
	public List<Reservation> getAllReservationsForCampgroundByDate() {
		return null;
	}

	@Override
	public void createReservation() {
		
	}

}
