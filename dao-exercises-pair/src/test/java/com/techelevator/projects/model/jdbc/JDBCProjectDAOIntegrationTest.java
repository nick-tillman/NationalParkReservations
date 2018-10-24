package com.techelevator.projects.model.jdbc;

import static org.junit.Assert.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.projects.model.Employee;
import com.techelevator.projects.model.Project;
import com.techelevator.projects.model.jdbc.JDBCProjectDAO;

public class JDBCProjectDAOIntegrationTest {

	// private static final String
	private JdbcTemplate jdbcTemplate;
	private static SingleConnectionDataSource dataSource;
	private JDBCProjectDAO dao;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {

		dataSource = new SingleConnectionDataSource();
		dataSource.setUrl("jdbc:postgresql://localhost:5432/projects");
		dataSource.setUsername("postgres");
		dataSource.setPassword("postgres1");

		dataSource.setAutoCommit(false);

	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {

		dataSource.destroy();

	}

	@Before
	public void setUp() throws Exception {
		jdbcTemplate = new JdbcTemplate(dataSource);
		this.dao = new JDBCProjectDAO(dataSource);

	}

	@After
	public void tearDown() throws Exception {

		dataSource.getConnection().rollback();
	}

	@Test
	public void getAListOfNewActiveProjects() {
		
		Project newProject = new Project();
			newProject.setId(3l);
			newProject.setName("The Never-ending Project");
			newProject.setStartDate(LocalDate.of(2010, 9, 01));
			newProject.setEndDate(null);
		
		List<Project> results = dao.getAllActiveProjects();	
		
		assertEquals(1, results.size());
				
			}
	
	@Test 
	public void employeeAddedToProject() {
		String sql = "SELECT * FROM employee WHERE employee_id = 2; ";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
		Employee newEmployee = createEmployee(results);
		dao.addEmployeeToProject(3l, 2l);
		sql = "SELECT * FROM employee WHERE employee_id IN (SELECT employee_id FROM "
							+ "project_employee WHERE employee_id = 2 AND project_id = 3);";
		results = jdbcTemplate.queryForRowSet(sql);
		Employee returnedEmployee = createEmployee(results);
		
		assertEquals(newEmployee.getId(), returnedEmployee.getId());
		
	}
	
	@Test
	public void employeeRemovedFromProject() {
		
		String sql = "SELECT * FROM employee WHERE employee_id = 2; ";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
		Employee newEmployee = createEmployee(results);
		dao.addEmployeeToProject(3l, 2l);
		sql = "SELECT * FROM employee WHERE employee_id IN "
				+ "(SELECT employee_id FROM project_employee WHERE project_id = 3);";
		results = jdbcTemplate.queryForRowSet(sql);
		List<Employee> newList = createEmployeeList(results);
		assertEquals(5, newList.size());
		
		dao.removeEmployeeFromProject(3l, 2l);
		results = jdbcTemplate.queryForRowSet(sql);
		List<Employee> newList2 = createEmployeeList(results);
		assertEquals(4, newList2.size());
		

	}

	private Employee createEmployee(SqlRowSet results) {
		Employee newEmployee = new Employee();
		while(results.next()) {
			newEmployee.setId(results.getLong("employee_id"));
			newEmployee.setDepartmentId(results.getLong("department_id"));
			newEmployee.setFirstName(results.getString("first_name"));
			newEmployee.setLastName(results.getString("last_name"));
			newEmployee.setGender(results.getString("gender").charAt(0));
			newEmployee.setHireDate(results.getDate("hire_date").toLocalDate());
		}
		return newEmployee;
	}
	
	private List<Employee> createEmployeeList(SqlRowSet results) {
		List<Employee> newList = new ArrayList<Employee>();
		while(results.next()) {
			Employee newEmployee = new Employee();
			newEmployee.setId(results.getLong("employee_id"));
			newEmployee.setDepartmentId(results.getLong("department_id"));
			newEmployee.setFirstName(results.getString("first_name"));
			newEmployee.setLastName(results.getString("last_name"));
			newEmployee.setGender(results.getString("gender").charAt(0));
			newEmployee.setHireDate(results.getDate("hire_date").toLocalDate());
			newList.add(newEmployee);
		}
		return newList;
	}
	private void assertProjectsAreEqual(Project expected, Project actual) {
		assertEquals("ID Match", expected.getId(), actual.getId());
		assertEquals("Name Match", expected.getName(), actual.getName());
		assertEquals("From Date Match", expected.getStartDate(), actual.getStartDate());
		assertEquals("To Date Match", expected.getEndDate(), actual.getEndDate());

	}
	

}
