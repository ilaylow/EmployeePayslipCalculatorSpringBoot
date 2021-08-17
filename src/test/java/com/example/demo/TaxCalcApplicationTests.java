package com.example.demo;

import com.example.demo.dao.EmployeeDataAccessService;
import com.example.demo.model.EmployeeDetails;
import com.example.demo.model.EmployeePayslip;
import com.example.demo.model.EmployeeTuple;
import com.example.demo.service.EmployeeService;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.UUID;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.stream.Stream;

@RunWith(SpringRunner.class)
@SpringBootTest
class TaxCalcApplicationTests {

	@Autowired
	private EmployeeService employeeService;

	@MockBean
	private EmployeeDataAccessService repository;

	@Test
	void insertInEmployees(){
		/* Define employee details objects manually */
		EmployeeDetails employee_1 = new EmployeeDetails(UUID.randomUUID(), "Bob", "Anderson", 20000, 2, 0.08f );
		EmployeeDetails employee_2 = new EmployeeDetails(UUID.randomUUID(), "Todd", "Ng", 1000000, 11, 0.1f );
		EmployeeDetails employee_3 = new EmployeeDetails(UUID.randomUUID(), "Tom", "Jefferson", 67000, 6, 0.01f );
		List<EmployeeDetails> employeeList = new ArrayList<>();

		/* Adding in Employees to Database and receiving payslips */
		employeeList.add(employee_1); employeeList.add(employee_2); employeeList.add(employee_3);
		List<EmployeeTuple<EmployeeDetails, EmployeePayslip>> responseList = employeeService.addEmployee(employeeList);

		/* Filter out employees that have a netIncome of less than 40K */
		Stream<EmployeeTuple<EmployeeDetails, EmployeePayslip>> employeeStream = responseList.stream();
		employeeStream.filter();
	}

	@Test
	void getCurrentEmployeeGrossIncome() {
		List<EmployeeDetails> employees = repository.showEmployees();
		Stream<EmployeeDetails> employeeStream = employees.stream();

	}

}
