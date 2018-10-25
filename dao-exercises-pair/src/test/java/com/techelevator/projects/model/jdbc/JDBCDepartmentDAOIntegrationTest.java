package com.techelevator.projects.model.jdbc;

import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import com.techelevator.projects.model.Department;

public class JDBCDepartmentDAOIntegrationTest {
	
	private static SingleConnectionDataSource dataSource;
	private JDBCDepartmentDAO dao;
	
	
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
		dao = new JDBCDepartmentDAO(dataSource);
	}
	
	@After
	public void teardown() throws SQLException {
		dataSource.getConnection().rollback();
	}
	
	@Test
	public void getDepartmentByIdTest() {
		Department expectedDepartment = new Department();
		expectedDepartment.setId(1l);
		expectedDepartment.setName("Department of Redundancy Department");
		Department returnedDepartment = dao.getDepartmentById(1l);
		assertDepartmentsAreEquals(expectedDepartment, returnedDepartment);
		
	}

	@Test
	public void createDeparmtentTest() {
		Department newDepartment = new Department();
		newDepartment.setName("Department of Records");
		dao.createDepartment(newDepartment);
		Department savedDepartment = dao.getDepartmentById(newDepartment.getId());
		assertDepartmentsAreEquals(newDepartment, savedDepartment);
	}
	
	@Test
	public void saveDepartmentTest() {
		Department newDepartment = new Department();
		newDepartment.setName("Department of Records");
		dao.createDepartment(newDepartment);
		newDepartment.setName("Department of Public Works");
		dao.saveDepartment(newDepartment);
		Department savedDepartment = dao.getDepartmentById(newDepartment.getId());
		assertDepartmentsAreEquals(newDepartment, savedDepartment);
	}
	
	@Test
	public void getAllDepartmentsTest() {
		List<Department> allDepartments = dao.getAllDepartments();
		assertNotNull("Checking Results exist", allDepartments.size());
		assertEquals("Checking amount of returned departments", 4, allDepartments.size());
	}
	
	@Test
	public void searchDepartmentsByNameTest() {
		List<Department> departmentSearch = dao.searchDepartmentsByName("Department");
		assertNotNull("Checking Results exist", departmentSearch.size());
		assertEquals("Checking amount of returned departments", 1, departmentSearch.size());
		Department newDepartment = new Department();
		newDepartment.setName("Customer Support");
		dao.createDepartment(newDepartment);
		departmentSearch = dao.searchDepartmentsByName("Support");
		assertEquals("Checking amount of returned departments", 2, departmentSearch.size());
		newDepartment.setName("Tech Support");
		dao.createDepartment(newDepartment);
		departmentSearch = dao.searchDepartmentsByName("Support");
		assertEquals("Checking amount of returned departments", 3, departmentSearch.size());
		
		
	}

	private void assertDepartmentsAreEquals(Department expected, Department actual) {
		assertNotEquals("ID Check", null, actual.getId());
		assertEquals("ID Match", expected.getId(), actual.getId());
		assertEquals("Name Match", expected.getName(), actual.getName());
	}

}
