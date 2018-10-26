package com.techelevator.model;

import java.time.LocalDate;
import java.util.List;

public interface CampgroundDAO {

	public List<Campground> getAllCampgroundsInPark(long parkId);
	
	
}
