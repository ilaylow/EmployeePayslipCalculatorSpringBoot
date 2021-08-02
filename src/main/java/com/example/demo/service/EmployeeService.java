package com.example.demo.service;

import com.example.demo.dao.EmployeeDao;
import com.example.demo.model.EmployeeDetails;
import com.example.demo.model.EmployeePayslip;
import com.example.demo.model.EmployeeTuple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

/* Class represents where business logic happens
* Uses the Data Access O */

@Service
public class EmployeeService {

    private final EmployeeDao employeeDao;

    @Autowired
    public EmployeeService(@Qualifier("Main") EmployeeDao employeeDao){
        this.employeeDao = employeeDao;
    }

    public EmployeeTuple<EmployeeDetails, EmployeePayslip> addEmployee(EmployeeDetails employeeDetails){
        return this.employeeDao.insertEmployeeData(employeeDetails);
    }

    public List<EmployeeDetails> getAllEmployees(){
        return employeeDao.showEmployees();
    }
}
