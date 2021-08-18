package com.example.demo;

import com.example.demo.dao.EmployeeDao;
import com.example.demo.model.EmployeeDetails;
import com.example.demo.model.EmployeePayslip;
import com.example.demo.model.EmployeeTuple;
import com.example.demo.service.EmployeeService;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RunWith(SpringRunner.class)
@SpringBootTest
class TaxCalcApplicationTests {

	private final EmployeeService employeeService;

	private final EmployeeDao employeeDao;

	@Autowired
	public TaxCalcApplicationTests(@Qualifier("Main") EmployeeDao employeeDao, EmployeeService employeeService){
		this.employeeDao = employeeDao;
		this.employeeService = employeeService;
	}

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
		List<EmployeeTuple<EmployeeDetails, EmployeePayslip>> employeeFilterList = responseList.stream().filter(tuple -> tuple.
				getEmployeePayslip().
				getNetIncome().
				compareTo(BigDecimal.valueOf(2000)) < 0).
				collect(Collectors.toList());

		/* Print out Employee Details */
		employeeFilterList.forEach(e -> System.out.println(e.toString()));

		/* Assertion Equality Check */
		Optional<EmployeeTuple<EmployeeDetails, EmployeePayslip>> employee = employeeFilterList.stream().findFirst();

		assertEquals(employee.
				get().
				getEmployeePayslip().
				getNetIncome().
				compareTo(BigDecimal.valueOf(1638.50f)), 0);

	}

	@Test
	void getCurrentEmployeeGrossIncome() {
		List<EmployeePayslip> employeePayslips = employeeDao.showPayslips();

	/* Get current annual grossIncomes above 2000 for all Employees */
		employeePayslips.stream().
				filter(e -> e.getGrossIncome().compareTo(BigDecimal.valueOf(2000)) < 0).
				forEach(e -> System.out.format("GrossMonthly: %.2f", e.getGrossIncome()));
	}

}
