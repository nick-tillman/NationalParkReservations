package com.techelevator.model.jdbc;

import java.time.LocalDate;
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
	public Reservation makeReservation(Reservation res) {
		int siteId = res.getSiteId();
		String name = res.getName();
		LocalDate fromDate = res.getFromDate();
		LocalDate toDate = res.getToDate();
		String sql = "INSERT INTO reservation (site_id, name, from_date, to_date) "
								    + "VALUES (?, ?, ?, ?)";
		jdbcTemplate.update(sql, siteId, name, fromDate, toDate);
		sql = "SELECT * FROM reservation WHERE site_id = ? AND name = ? AND from_date = ? AND to_date = ?; ";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sql, siteId, name, fromDate, toDate);
		Reservation newRes = createReservation(results);
		return newRes;
	}
		
	private Reservation createReservation(SqlRowSet results) {
		Reservation newRes = new Reservation();
		while(results.next()) {
			newRes.setCreateDate(results.getDate("create_date").toLocalDate());
			newRes.setFromDate(results.getDate("from_date").toLocalDate());
			newRes.setName(results.getString("name"));
			newRes.setReservationId(results.getLong("reservation_id"));
			newRes.setSiteId(results.getInt("site_id"));
			newRes.setToDate(results.getDate("to_date").toLocalDate());
		}
		return newRes;
	}

}
