package com.techelevator.projects.model.jdbc;

import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import com.techelevator.projects.model.Employee;

public class JDBCEmployeeDAOIntegrationTest {
	
	private static SingleConnectionDataSource dataSource;
	private JDBCEmployeeDAO dao;
	
	
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
		this.dao = new JDBCEmployeeDAO(dataSource);
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
		
	}
	
	@Test
	public void getEmployeesByDepartmentIdTest() {
		
	}
	
	@Test
	public void getEmployeesWithoutProjectsTest() {
		
	}
	
	@Test
	public void getEmployeesByProjectIdTest() {
		
	}
	
	@Test
	public void changeEmployeeDepartmentTest() {
		
	}

}
