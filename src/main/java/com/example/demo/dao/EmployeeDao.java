package com.example.demo.dao;

import com.example.demo.model.EmployeeDetails;
import com.example.demo.model.EmployeePayslip;
import com.example.demo.model.EmployeeTuple;

import java.util.List;
import java.util.UUID;

public interface EmployeeDao {
    EmployeeTuple<EmployeeDetails, EmployeePayslip> insertEmployeeData(UUID id, EmployeeDetails employee);

    default EmployeeTuple<EmployeeDetails, EmployeePayslip> insertEmployeeData(EmployeeDetails employeeDetails){
        UUID id = UUID.randomUUID();
        return insertEmployeeData(id, employeeDetails);
    }

    List<EmployeeDetails> showEmployees();
    List<EmployeePayslip> showPayslips();
}
