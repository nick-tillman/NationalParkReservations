package com.techelevator.model.jdbc;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.model.Park;
import com.techelevator.model.ParkDAO;

public class JDBCParkDAO implements ParkDAO {

	private JdbcTemplate jdbcTemplate;

	public JDBCParkDAO(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Override
	public List<Park> showAllParks() {

		List<Park> allParks = new ArrayList<Park>();
		String sqlShowAllParks = "SELECT * FROM park;";

		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlShowAllParks);

		while (results.next()) {
			Park newPark = new Park();
			newPark.setParkId(results.getLong("park_id"));
			newPark.setName(results.getString("name"));
			newPark.setLocation(results.getString("location"));
			newPark.setEstablishDate(results.getDate("establish_date").toLocalDate());
			newPark.setArea(results.getInt("area"));
			newPark.setVisitors(results.getInt("visitors"));
			newPark.setDescription(results.getString("description"));
			allParks.add(newPark);
		}

		return allParks;
	}

	@Override
	public Park getParkById(long id) {
		
		Park parkById = new Park();
		
		String sqlGetParkById = "SELECT * FROM park WHERE park_id = ?;";
		
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlGetParkById, id);
		
		while(results.next()) {
			parkById.setParkId(results.getLong("park_id"));
			parkById.setName(results.getString("name"));
			parkById.setLocation(results.getString("location"));
			parkById.setEstablishDate(results.getDate("establish_date").toLocalDate());
			parkById.setArea(results.getInt("area"));
			parkById.setVisitors(results.getInt("visitors"));
			parkById.setDescription(results.getString("description"));
			
		}
		
		return parkById;	
	}
}
