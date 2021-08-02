package com.example.demo.api;

import com.example.demo.model.EmployeeDetails;
import com.example.demo.model.EmployeePayslip;
import com.example.demo.model.EmployeeTuple;
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
    public @ResponseBody List<EmployeeTuple<EmployeeDetails, EmployeePayslip>> addEmployee(@RequestBody List<EmployeeDetails> employeeDetailsList){
        List<EmployeeTuple<EmployeeDetails, EmployeePayslip>> tupleList = new ArrayList<>();
        for(EmployeeDetails employeeDetails: employeeDetailsList){
            EmployeeTuple<EmployeeDetails, EmployeePayslip> paySlip = employeeService.addEmployee(employeeDetails);
            tupleList.add(paySlip);
        }
        for (EmployeeTuple<EmployeeDetails, EmployeePayslip> tuple: tupleList){
            System.out.println(tuple.getEmployeePayslip().getSuperannuation());
        }
        return tupleList;
    }

    @GetMapping
    public List<EmployeeDetails> getAllEmployees(){
        return employeeService.getAllEmployees();
    }

}
