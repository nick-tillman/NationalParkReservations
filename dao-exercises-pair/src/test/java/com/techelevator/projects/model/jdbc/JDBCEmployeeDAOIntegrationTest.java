package com.techelevator.projects.model.jdbc;

import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.projects.model.Employee;

public class JDBCEmployeeDAOIntegrationTest {
	
	private static SingleConnectionDataSource dataSource;
	private JDBCEmployeeDAO dao;
	private JdbcTemplate jdbcTemplate;
	
	
	@BeforeClass
	public static void setupDataSource() {
		dataSource = new SingleConnectionDataSource();
		dataSource.setUrl("jdbc:postgresql://localhost:5432/projects");
		dataSource.setUsername("postgres");
		dataSource.setPassword("postgres1");
		dataSource.setAutoCommit(false);
	}
	
	@AfterClass
	public static void closeDataSource() {
		dataSource.destroy();
	}
	
	@Before
	public void setup() {
		dao = new JDBCEmployeeDAO(dataSource);
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@After
	public void teardown() throws SQLException {
		dataSource.getConnection().rollback();
	}

	@Test
	public void getAllEmployeesTest() {
		List<Employee> employeeList = dao.getAllEmployees();
		assertEquals("Checking for the correct number", 12, employeeList.size());
	}
	
	@Test
	public void searchEmployeesByNameTest() {
		List<Employee> employeeList = dao.searchEmployeesByName("mary", "");
		assertEquals("Checking for the correct number", 1, employeeList.size());
		employeeList = dao.searchEmployeesByName("Delora", "c");
		assertEquals("Checking for the correct number", 1, employeeList.size());
		employeeList = dao.searchEmployeesByName("Sid", "goodman");
		assertEquals("Checking for the correct number", 1, employeeList.size());
	}
	
	@Test
	public void getEmployeesByDepartmentIdTest() {
		String sql = "SELECT department_id, count(employee_id) FROM employee GROUP BY department_id;";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
		Map<Integer, Integer> departmentCount = new TreeMap<Integer, Integer>();
		while(results.next()) {
			departmentCount.put(results.getInt("department_id"), results.getInt("count"));
		}
		for(int i = 1; i <= departmentCount.size(); i++) {
			List<Employee> returnedList = dao.getEmployeesByDepartmentId((long)i);
			assertEquals("Checking for size match", (int)departmentCount.get(i), returnedList.size());
		}
		
	}
	
	@Test
	public void getEmployeesWithoutProjectsTest() {
		String sql = "SELECT count(e.employee_id) FROM employee e "
							+ "LEFT JOIN project_employee pe ON e.employee_id = pe.employee_id "
							+ "WHERE pe.employee_id IS NULL; ";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
		int employeeCount = 0;
		while(results.next()) {
			employeeCount = results.getInt("count");
		}
		List<Employee> returnedList = dao.getEmployeesWithoutProjects();
		assertEquals("Checking for size match", employeeCount, returnedList.size());
	}
	
	@Test
	public void getEmployeesByProjectIdTest() {
		String sql = "SELECT project_id, count(employee_id) FROM project_employee GROUP BY project_id;";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
		Map<Integer, Integer> projectEmployeeCount = new TreeMap<Integer, Integer>();
		while(results.next()) {
			projectEmployeeCount.put(results.getInt("project_id"), results.getInt("count"));
		}
		Set<Integer> keySet = projectEmployeeCount.keySet();
		for(int key : keySet) {
			List<Employee> returnedList = dao.getEmployeesByProjectId((long)key);
			assertEquals("Checking for size match", (int)projectEmployeeCount.get(key), returnedList.size());
		}
	}
	
	@Test
	public void changeEmployeeDepartmentTest() {
		String sql = "SELECT department_id, count(employee_id) FROM employee GROUP BY department_id;";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
		Map<Integer, Integer> departmentCountBefore = new TreeMap<Integer, Integer>();
		while(results.next()) {
			departmentCountBefore.put(results.getInt("department_id"), results.getInt("count"));
		}
		dao.changeEmployeeDepartment(1l, 2l);
		results = jdbcTemplate.queryForRowSet(sql);
		Map<Integer, Integer> departmentCountAfter = new TreeMap<Integer, Integer>();
		while(results.next()) {
			departmentCountAfter.put(results.getInt("department_id"), results.getInt("count"));
		}
		assertEquals("Checking for size match",(int)departmentCountBefore.get(2) + 1, (int)departmentCountAfter.get(2));
		
	}
}
