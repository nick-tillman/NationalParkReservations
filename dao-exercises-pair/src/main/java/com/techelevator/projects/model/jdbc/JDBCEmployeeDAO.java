package com.techelevator.projects.model.jdbc;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.projects.model.Employee;
import com.techelevator.projects.model.EmployeeDAO;

public class JDBCEmployeeDAO implements EmployeeDAO {

	private JdbcTemplate jdbcTemplate;

	public JDBCEmployeeDAO(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Override
	public List<Employee> getAllEmployees() {
		
		String sqlGetAllEmployees = "SELECT * FROM employee; ";
		List<Employee> employeeList = new ArrayList<Employee>();
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlGetAllEmployees);
		
		while(results.next()) {
			Employee newEmployee = mapRowToEmployee(results);
			employeeList.add(newEmployee);
		}
	
		return employeeList;
	}

	@Override
	public List<Employee> searchEmployeesByName(String firstNameSearch, String lastNameSearch) {
		
		String sqlGetEmployeeNames = "SELECT * FROM employee WHERE first_name ILIKE ? AND last_name ILIKE ?; ";
		List<Employee> employeeList = new ArrayList<Employee>();
		String firstName = "%"+firstNameSearch+"%";
		String lastName = "%"+lastNameSearch+"%";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlGetEmployeeNames, firstName, lastName);
		
		while(results.next()) {
			Employee newEmployee = mapRowToEmployee(results);
			employeeList.add(newEmployee);
		}
	
		return employeeList;
	}

	@Override
	public List<Employee> getEmployeesByDepartmentId(long id) {
		String sqlGetEmployeeByDepartment = "SELECT * FROM employee WHERE department_id = ?; ";
		List<Employee> employeeList = new ArrayList<Employee>();
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlGetEmployeeByDepartment, id);
		
		while(results.next()) {
			Employee newEmployee = mapRowToEmployee(results);
			employeeList.add(newEmployee);
		}
	
		return employeeList;
	}

	@Override
	public List<Employee> getEmployeesWithoutProjects() {
		
		String sqlGetEmployeesWithoutProjects = "SELECT * FROM employee e LEFT JOIN project_employee pe "
														+ "ON e.employee_id = pe.employee_id "
														+ "WHERE pe.employee_id IS NULL; ";
		List<Employee> employeeList = new ArrayList<Employee>();
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlGetEmployeesWithoutProjects);
		
		while(results.next()) {
			Employee newEmployee = mapRowToEmployee(results);
			employeeList.add(newEmployee);
		}
	
		return employeeList;
	}

	@Override
	public List<Employee> getEmployeesByProjectId(Long projectId) {
		
		String sqlGetEmployeesByProjectId = "SELECT * FROM employee WHERE employee_id IN "
										+ "(SELECT employee_id FROM project_employee WHERE project_id = ?); ";
		List<Employee> employeeList = new ArrayList<Employee>();
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlGetEmployeesByProjectId, projectId);
		
		while(results.next()) {
			Employee newEmployee = mapRowToEmployee(results);
			employeeList.add(newEmployee);
		}
	
		return employeeList;
	}

	@Override
	public void changeEmployeeDepartment(Long employeeId, Long departmentId) {
		String sqlUpdateDepartmentId = "UPDATE employee SET department_id = ? WHERE employee_id = ?; ";
		jdbcTemplate.update(sqlUpdateDepartmentId, departmentId, employeeId);
		
	}
	
	private Employee mapRowToEmployee(SqlRowSet results) {
		Employee newEmployee = new Employee();
		newEmployee.setId(results.getLong("employee_id"));
		newEmployee.setDepartmentId(results.getLong("department_id"));
		newEmployee.setFirstName(results.getString("first_name"));
		newEmployee.setLastName(results.getString("last_name"));
		LocalDate birthdate = results.getDate("birth_date").toLocalDate();
		newEmployee.setBirthDay(birthdate);
		newEmployee.setGender(results.getString("gender").charAt(0));
		LocalDate hiredate = results.getDate("hire_date").toLocalDate();
		newEmployee.setHireDate(hiredate);
		return newEmployee;

		
		
	}

}
