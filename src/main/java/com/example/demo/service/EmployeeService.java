package com.example.demo.service;

import com.example.demo.dao.EmployeeDao;
import com.example.demo.model.EmployeeDetails;
import com.example.demo.model.EmployeePayslip;
import com.example.demo.model.EmployeeTuple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
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

    public List<EmployeeTuple<EmployeeDetails, EmployeePayslip>> addEmployee(List<EmployeeDetails> employeeDetailsList){
        List<EmployeeTuple<EmployeeDetails, EmployeePayslip>> tupleList = new ArrayList<>();
        for(EmployeeDetails employeeDetails: employeeDetailsList){
            EmployeeTuple<EmployeeDetails, EmployeePayslip> tuple = this.employeeDao.insertEmployeeData(employeeDetails);
            tupleList.add(tuple);
        }
        return tupleList;
    }

    public List<EmployeeDetails> getAllEmployees(){
        return employeeDao.showEmployees();
    }
}
