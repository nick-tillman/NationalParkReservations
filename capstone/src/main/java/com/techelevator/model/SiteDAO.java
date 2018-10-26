package com.techelevator.model;

import java.time.LocalDate;
import java.util.List;

public interface SiteDAO {
	
	public List<Site> getAllSitesByCampgroundId(Long campgroundId);
	public List<Site> getListOfAvailableSites(long campgroundId, LocalDate fromDate, LocalDate toDate);

	

}
