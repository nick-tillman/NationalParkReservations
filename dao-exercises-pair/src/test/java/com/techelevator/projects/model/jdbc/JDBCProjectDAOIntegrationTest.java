package com.techelevator.projects.model.jdbc;

import static org.junit.Assert.*;

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
	private JDBCEmployeeDAO empDAO;

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
		dao = new JDBCProjectDAO(dataSource);
		empDAO = new JDBCEmployeeDAO(dataSource);

	}

	@After
	public void tearDown() throws Exception {

		dataSource.getConnection().rollback();
	}

	@Test
	public void getAListOfNewActiveProjectsTest() {
		String sql = "SELECT count(name) FROM project WHERE from_date IS NOT NULL AND to_date IS NULL; ";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
		int projectCount = 0;
		while(results.next()) {
			projectCount = results.getInt("count");
		}
		List<Project> activeProjects = dao.getAllActiveProjects();
		assertEquals("Checking for size match", projectCount, activeProjects.size());
		
	}
	
	@Test 
	public void employeeAddedToProjectTest() {
		List<Employee> employeeList = empDAO.getEmployeesWithoutProjects();
		String sql = "SELECT count(employee_id) FROM project_employee WHERE project_id = 3 GROUP BY project_id; ";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
		int employeeCountBefore = 0;
		while(results.next()) {
			employeeCountBefore = results.getInt("count");
		}
		dao.addEmployeeToProject((long)3, employeeList.get(0).getId());
		results = jdbcTemplate.queryForRowSet(sql);
		int employeeCountAfter = 0;
		while(results.next()) {
			employeeCountAfter = results.getInt("count");
		}
		assertEquals("Checking for size match", employeeCountBefore + 1, employeeCountAfter);
	}
	
	@Test
	public void employeeRemovedFromProjectTest() {
		List<Employee> employeeList = empDAO.getEmployeesByProjectId(3l);
		String sql = "SELECT count(employee_id) FROM project_employee WHERE project_id = 3 GROUP BY project_id; ";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
		int employeeCountBefore = 0;
		while(results.next()) {
			employeeCountBefore = results.getInt("count");
		}
		dao.removeEmployeeFromProject((long)3, employeeList.get(0).getId());
		results = jdbcTemplate.queryForRowSet(sql);
		int employeeCountAfter = 0;
		while(results.next()) {
			employeeCountAfter = results.getInt("count");
		}
		assertEquals("Checking for size match", employeeCountBefore - 1, employeeCountAfter);
	}
}
