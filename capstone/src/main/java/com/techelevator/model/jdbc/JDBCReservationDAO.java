package com.techelevator.model.jdbc;

import java.util.List;

import com.techelevator.model.Reservation;
import com.techelevator.model.ReservationDAO;

public class JDBCReservationDAO implements ReservationDAO{

	@Override
	public List<Reservation> getReservationsBySiteId() {
		return null;
	}

	@Override
	public List<Reservation> getReservationsByCampgroundId() {
		return null;
	}

	@Override
	public List<Reservation> getReservationsForSiteByDate() {
		return null;
	}

	@Override
	public List<Reservation> getReservationsForCampgroundByDate() {
		return null;
	}

	@Override
	public void createReservation() {
		
	}

}
