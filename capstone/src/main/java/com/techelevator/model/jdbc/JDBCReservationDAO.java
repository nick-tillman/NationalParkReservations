package com.techelevator.model.jdbc;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.model.Reservation;
import com.techelevator.model.ReservationDAO;

public class JDBCReservationDAO implements ReservationDAO{
	
	private JdbcTemplate jdbcTemplate;
	
	public JDBCReservationDAO(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Override
	public List<Reservation> getReservationsBySiteId(Long siteId) {
		String sql = "SELECT * FROM reservations WHERE site_id = ?; ";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sql, siteId);
		List<Reservation> newList = createReservationList(results);
		return newList;

	}

	@Override
	public List<Reservation> getReservationsByCampgroundId(Long campgroundId) {
		String sql = "SELECT * FROM reservations r JOIN site s ON s.site_id = r.site_id WHERE campground_id = ?; ";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sql, campgroundId);
		List<Reservation> newList = createReservationList(results);
		return newList;	
	}

	@Override
	public List<Reservation> getReservationsForSiteByDate(LocalDate fromDate, LocalDate toDate) {
		String sql = "SELECT * FROM reservation WHERE (from_date >= ? AND from_date <= ?) OR "
													+ "(to_date >= ? AND to_date <= ?);"; 
		SqlRowSet results = jdbcTemplate.queryForRowSet(sql,fromDate, toDate, fromDate, toDate);
		List<Reservation> newList = createReservationList(results);
		return newList;
	}

	@Override
	public List<Reservation> getReservationsForCampgroundByDate() {
		return null;
	}

	@Override
	public void makeReservation() {
		
	}
	
	private List<Reservation> createReservationList(SqlRowSet results) {
		List<Reservation> newList = new ArrayList<Reservation>();
		while(results.next()) {
			Reservation newRes = new Reservation();
			newRes.setCreateDate(results.getDate("create_date").toLocalDate());
			newRes.setFromDate(results.getDate("from_date").toLocalDate());
			newRes.setName(results.getString("name"));
			newRes.setReservationId(results.getLong("reservation_id"));
			newRes.setSiteId(results.getInt("site_id"));
			newRes.setToDate(results.getDate("to_date").toLocalDate());
			newList.add(newRes);
		}
		return newList;
	}

}
