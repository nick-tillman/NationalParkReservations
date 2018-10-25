package com.techelevator.model.jdbc;

import java.time.LocalDate;
import java.util.List;

import com.techelevator.model.Campground;
import com.techelevator.model.CampgroundDAO;

public class JDBCCampgroundDAO implements CampgroundDAO {

	@Override
	public List<Campground> getAllCampgroundsInPark(long parkId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Campground> getAllCampgroundsWithDate(LocalDate fromDate, LocalDate toDate) {
		// TODO Auto-generated method stub
		return null;
	}

}
