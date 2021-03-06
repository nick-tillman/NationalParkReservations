package com.techelevator.model.jdbc;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import java.time.temporal.ChronoUnit;

import com.techelevator.model.Site;
import com.techelevator.model.SiteDAO;

public class JDBCSiteDAO implements SiteDAO{
	
private JdbcTemplate jdbcTemplate;
	
	public JDBCSiteDAO(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Override
	public List<Site> getAllSitesByCampgroundId(Long campgroundId) {
		String sql = "SELECT * FROM site WHERE campgroundId = ?; ";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sql, campgroundId);
		List<Site> newList = createSiteList(results);
		return newList;
	}
	
	@Override
	public List<Site> getListOfAvailableSites(long campgroundId, LocalDate fromDate, LocalDate toDate) {
		List<Site> newList = new ArrayList<Site>();
		long daysBetween = ChronoUnit.DAYS.between(fromDate, toDate);
		if(daysBetween > 0) {
			String sql = "SELECT * FROM site WHERE campground_id = ? "
											+ "AND NOT site_id IN (SELECT site_id FROM reservation "
											+ "WHERE ((from_date <= ?) "
											+ "AND (to_date >= ?)) GROUP BY site_id) LIMIT 5;";
			SqlRowSet results = jdbcTemplate.queryForRowSet(sql, campgroundId, toDate, fromDate);
			newList = createSiteList(results);
		}
		return newList;
	}
	
	private List<Site> createSiteList(SqlRowSet results) {
		List<Site> newList = new ArrayList<Site>();
		while(results.next()) {
			Site newSite = new Site();
			newSite.setAccessible(results.getBoolean("accessible"));
			newSite.setCampgroundId(results.getInt("campground_id"));
			newSite.setMaxOccupancy(results.getInt("max_occupancy"));
			newSite.setMaxRvLength(results.getInt("max_rv_length"));
			newSite.setSiteId(results.getLong("site_id"));
			newSite.setSiteNumber(results.getInt("site_number"));
			newSite.setUtilities(results.getBoolean("utilities"));
			newList.add(newSite);
		}
		return newList;
	}

}
