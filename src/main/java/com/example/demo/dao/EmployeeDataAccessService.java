package com.example.demo.dao;

import com.example.demo.model.EmployeeDetails;
import com.example.demo.model.EmployeePayslip;
import com.example.demo.model.EmployeeTuple;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.lang.Math;

/* Class serves as a Data Access Object that the service layer uses, in charge of directly making updates to data
* within the database */

@Repository("Main")
public class EmployeeDataAccessService implements EmployeeDao {

    private static List<EmployeeDetails> EmployeeDatabase = new ArrayList<>();
    private static List<EmployeePayslip> EmployeePayslipDatabase = new ArrayList<>();
    private static final List<String> MONTHS = new ArrayList<String>(){
        {
            add("January");
            add("February");
            add("March");
            add("April");
            add("May");
            add("June");
            add("July");
            add("August");
            add("September");
            add("October");
            add("November");
            add("December");
        }
    };
    private static final List<Integer> TYPE1_MONTH_LIST = new ArrayList<Integer>(
            Arrays.asList(0,2,4,6,7,9, 11));
    private static final List<Integer> TYPE2_MONTH_LIST = new ArrayList<Integer>(
            Arrays.asList(1,3,5,8,10));

    @Override
    public EmployeeTuple<EmployeeDetails, EmployeePayslip> insertEmployeeData(UUID id, EmployeeDetails employee) {
        // Assume that insertion to in-memory database (ArrayList) always works, so it always returns 1
        EmployeeDetails employeeDetails = new EmployeeDetails(id, employee.getFirstName(), employee.getLastName(),
                employee.getAnnualSalary(), employee.getPaymentMonth(), employee.getSuperRate());
        EmployeeDatabase.add(employeeDetails);

        // Calculate Employees Pay Slip Information
        int grossIncome = calculateGrossIncome(employee.getAnnualSalary());
        int incomeTax = calculateIncomeTax(employee.getAnnualSalary());
        int netIncome = grossIncome - incomeTax;
        String[] paymentPeriod = getPaymentDate(employee.getPaymentMonth());
        int _super = (int) Math.round(employee.getSuperRate() * netIncome);

        EmployeePayslip paySlip = new EmployeePayslip(_super,
                grossIncome, incomeTax, netIncome, paymentPeriod[0], paymentPeriod[1]);

        EmployeePayslipDatabase.add(paySlip);
        EmployeeTuple<EmployeeDetails, EmployeePayslip> employeeTuple = new EmployeeTuple<>(employeeDetails, paySlip);
        // Want to return here tuples of Employee and payslip information

        return employeeTuple;
    }

    @Override
    public List<EmployeeDetails> showEmployees() {
        return EmployeeDatabase;
    }

    private int calculateGrossIncome(int annualSalary){
        return (int) Math.round(annualSalary / 12.0);
    }

    private String[] getPaymentDate(int paymentMonth){

        /* Assume month is valid, (0-11) */
        String startDate = "01", lastDate = "30";
        String month = MONTHS.get(paymentMonth);
        if (TYPE1_MONTH_LIST.contains(paymentMonth)){
            lastDate = "31";
        }
        else if (TYPE2_MONTH_LIST.contains(paymentMonth)){
            lastDate = "30";
            if (paymentMonth == 1) {
                lastDate = "28";
            }
        }

        return new String[]{String.join(" ", startDate, month), String.join(" ", lastDate, month)};

    }

    private int calculateIncomeTax(int annualSalary){
        int remainingAmount = 0, taxAmount = 0;
        float factor = 0.0f;
        if (annualSalary >= 18201 && annualSalary <= 37000){
            remainingAmount = (annualSalary - 18200);
            factor = 0.19f;
        }
        else if (annualSalary >= 37001 && annualSalary <= 87000){
            remainingAmount = (annualSalary - 37000);
            taxAmount = 3572;
            factor = 0.325f;
        }
        else if (annualSalary >= 87001 && annualSalary <= 180000){
            remainingAmount = (annualSalary - 87000);
            taxAmount = 19822;
            factor = 0.37f;
        }
        else if (annualSalary >= 180001){
            remainingAmount = (annualSalary - 180000);
            taxAmount = 54232;
            factor = 0.45f;
        }

        return (int) Math.round((taxAmount + (remainingAmount * factor)) / 12.0);
    }
}
