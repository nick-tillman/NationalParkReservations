package com.techelevator.model;

import java.util.List;

public interface ParkDAO {

	public List<Park> showAllParks();
	Park getParkById(long id);

		
}
