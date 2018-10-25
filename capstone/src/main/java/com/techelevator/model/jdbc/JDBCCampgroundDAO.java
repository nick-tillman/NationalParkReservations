package com.techelevator.model.jdbc;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.model.Campground;
import com.techelevator.model.CampgroundDAO;

public class JDBCCampgroundDAO implements CampgroundDAO {
	
	private JdbcTemplate jdbcTemplate;

	public JDBCCampgroundDAO(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Override
	public List<Campground> getAllCampgroundsInPark(long parkId) {
		
		List<Campground> allCampgroundsByParkId = new ArrayList<Campground>();
		
		String sqlReturnCampgroundsByParkId = "SELECT * FROM campground WHERE park_id = ?; ";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlReturnCampgroundsByParkId, parkId);
		
		while(results.next()) {
			Campground newCampground = mapRowToCampground(results);
			
			allCampgroundsByParkId.add(newCampground);
		}
		
		return allCampgroundsByParkId;
		
	}
	
	private Campground mapRowToCampground(SqlRowSet results) {
		Campground newCampground = new Campground();
		newCampground.setCampgroundId(results.getLong("campground_id"));
		newCampground.setParkId(results.getInt("park_id"));
		newCampground.setName(results.getString("name"));
		newCampground.setOpenFromMonth(results.getString("open_from_mm"));
		newCampground.setOpenToMonth(results.getString("open_to_mm"));
		newCampground.setDailyFee(results.getDouble("daily_fee"));
		return newCampground;
		
	}

}
