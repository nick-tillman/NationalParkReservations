package com.techelevator.projects.model.jdbc;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.projects.model.Department;
import com.techelevator.projects.model.DepartmentDAO;
import com.techelevator.projects.model.Project;

public class JDBCDepartmentDAO implements DepartmentDAO {
	
	private JdbcTemplate jdbcTemplate;

	public JDBCDepartmentDAO(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Override
	public List<Department> getAllDepartments() {
		List<Department> departments  = new ArrayList<Department>();
		String sqlGetAllDepartments = "SELECT * FROM department";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlGetAllDepartments);
		while(results.next()) {
			Department department =  new Department();
			department.setId(results.getLong("department_id"));
			department.setName(results.getString("name"));
			departments.add(department);
		}
		return departments;
	}

	@Override
	public List<Department> searchDepartmentsByName(String nameSearch) {
		List<Department> departments  = new ArrayList<Department>();
		String sqlSearchDepartment = "SELECT * FROM department WHERE name LIKE '%' || ? || '%'; ";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlSearchDepartment, nameSearch);
		while(results.next()) {
			Department department =  new Department();
			department.setId(results.getLong("department_id"));
			department.setName(results.getString("name"));
			departments.add(department);
		}
		return departments;	}

	@Override
	public void saveDepartment(Department updatedDepartment) {
		String sqlUpdateDepartment = "UPDATE department SET name = ? WHERE department_id = ?; ";
		jdbcTemplate.update(sqlUpdateDepartment, updatedDepartment.getName(), updatedDepartment.getId());
	}

	@Override
	public Department createDepartment(Department newDepartment) {
		String sqlGetNextId = "SELECT nextval('seq_department_id')";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlGetNextId);
		results.next();
		Long id = results.getLong(1);
		newDepartment.setId(id);
		
		String sqlAddNewDepartment = "INSERT INTO department(department_id, name) values (?, ?); ";
		jdbcTemplate.update(sqlAddNewDepartment, newDepartment.getId(), newDepartment.getName());
		
		return newDepartment;
		
	}

	@Override
	public Department getDepartmentById(Long id) {
		
		String sqlGetDeparmentById = "SELECT * FROM department WHERE department_id = ?; ";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlGetDeparmentById, id);
		Department newDepartment = new Department();
		if(results.next()) {
			newDepartment.setId(results.getLong("department_id"));
			newDepartment.setName(results.getString("name"));
		}
		
		return newDepartment;
	}

}
