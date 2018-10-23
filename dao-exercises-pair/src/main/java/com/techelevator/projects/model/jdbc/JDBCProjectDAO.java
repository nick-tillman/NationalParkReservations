package com.techelevator.projects.model.jdbc;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.projects.model.Project;
import com.techelevator.projects.model.ProjectDAO;

public class JDBCProjectDAO implements ProjectDAO {

	private JdbcTemplate jdbcTemplate;

	public JDBCProjectDAO(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Override
	public List<Project> getAllActiveProjects() {
		
		List<Project> projects  = new ArrayList<Project>();
		String sqlGetActiveProjects = "SELECT * FROM project WHERE from_date IS NOT NULL AND to_date IS NULL";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlGetActiveProjects);
		while(results.next()) {
			Project newProject = new Project();
			newProject.setId(results.getLong("project_id"));
			newProject.setName(results.getString("name"));
			LocalDate startDate = results.getDate("from_date").toLocalDate();
			newProject.setStartDate(startDate);
			projects.add(newProject);
		}
		return projects;
	}

	@Override
	public void removeEmployeeFromProject(Long projectId, Long employeeId) {
		
		String sqlRemoveEmployee = "DELETE FROM project_employee WHERE project_id = ? AND employee_id = ?";
		jdbcTemplate.update(sqlRemoveEmployee, projectId, employeeId);
		
	}

	@Override
	public void addEmployeeToProject(Long projectId, Long employeeId) {
		try {
			String sqlAddEmployee = "INSERT INTO project_employee(project_id, employee_id) values(?, ?) ";
			jdbcTemplate.update(sqlAddEmployee, projectId, employeeId);
		} catch(DuplicateKeyException ex) {
			//If duplicate value is entered, will catch the exception.
		}
	}

}
