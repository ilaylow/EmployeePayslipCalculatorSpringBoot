package com.example.demo.api;

import com.example.demo.model.EmployeeDetails;
import com.example.demo.model.EmployeePayslip;
import com.example.demo.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/* Class is a REST Controller Class */
@RequestMapping("api/v1/employee")
@RestController
public class EmployeeController {
    private final EmployeeService employeeService;

    @Autowired
    public EmployeeController(EmployeeService service){
        this.employeeService = service;
    }

    @PostMapping
    public @ResponseBody List<EmployeePayslip> addEmployee(@RequestBody EmployeeDetails employeeDetails){
        EmployeePayslip paySlip = employeeService.addEmployee(employeeDetails);
        List<EmployeePayslip> payslipList = new ArrayList<>();
        payslipList.add(paySlip);
        return payslipList;
    }

    @GetMapping
    public List<EmployeeDetails> getAllEmployees(){
        return employeeService.getAllEmployees();
    }

}
