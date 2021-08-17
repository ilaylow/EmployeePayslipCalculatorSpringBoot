package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class EmployeeTuple<S extends EmployeeDetails, T extends EmployeePayslip> {
    private final S employeeDetails;
    private final T employeePayslip;

    public EmployeeTuple(@JsonProperty("employee") S details,
                         @JsonProperty("payslip")T paySlip){
        this.employeeDetails = details;
        this.employeePayslip = paySlip;
    }

    public S getEmployeeDetails() {
        return employeeDetails;
    }

    public T getEmployeePayslip() {
        return employeePayslip;
    }

    @Override
    public String toString(){
        return String.format("{EmployeeDetails: %s,\n" +
                "EmployeePayslip: %s}", employeeDetails.toString(), employeePayslip.toString());

    }
}
