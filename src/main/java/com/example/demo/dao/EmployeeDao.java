package com.example.demo.dao;

import com.example.demo.model.EmployeeDetails;
import com.example.demo.model.EmployeePayslip;

import java.util.List;
import java.util.UUID;

public interface EmployeeDao {
    EmployeePayslip insertEmployeeData(UUID id, EmployeeDetails employee);

    default EmployeePayslip insertEmployeeData(EmployeeDetails employeeDetails){
        UUID id = UUID.randomUUID();
        return insertEmployeeData(id, employeeDetails);
    }

    List<EmployeeDetails> showEmployees();
}
