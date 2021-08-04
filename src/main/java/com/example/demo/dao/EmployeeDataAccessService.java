package com.example.demo.dao;

import com.example.demo.model.EmployeeDetails;
import com.example.demo.model.EmployeePayslip;
import com.example.demo.model.EmployeeTuple;
import com.fasterxml.jackson.core.JsonParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Repository;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;
import java.io.IOException;
import java.io.FileWriter;
import java.io.PrintWriter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.Calendar;
import java.text.DateFormatSymbols;
import java.lang.Math;

/* Class serves as a Data Access Object that the service layer uses, in charge of directly making updates to data
* within the database */

@Repository("Main")
public class EmployeeDataAccessService implements EmployeeDao {

    private Calendar calendar = Calendar.getInstance();
    private static List<EmployeeDetails> EmployeeDatabase = new ArrayList<>();
    private static List<EmployeePayslip> EmployeePayslipDatabase = new ArrayList<>();
    private FileWriter fileWriter = new FileWriter("../log.txt");
    private final JSONParser parser = new JSONParser();
    private final JSONObject taxInfoObject = ((JSONObject) parser.parse(new FileReader("../taxrange.config.json")));

    public EmployeeDataAccessService() throws IOException, ParseException { };

    @Override
    public EmployeeTuple<EmployeeDetails, EmployeePayslip> insertEmployeeData(UUID id, EmployeeDetails employee) {
        // Assume that insertion to in-memory database (ArrayList) always works, so it always returns 1
        EmployeeDetails employeeDetails = new EmployeeDetails(id, employee.getFirstName(), employee.getLastName(),
                employee.getAnnualSalary(), employee.getPaymentMonth(), employee.getSuperRate());
        EmployeeDatabase.add(employeeDetails);

        // Calculate Employees Pay Slip Information
        BigDecimal grossIncome = calculateGrossIncome(employee.getAnnualSalary());
        BigDecimal incomeTax = calculateIncomeTax(employee.getAnnualSalary());
        BigDecimal netIncome = grossIncome.subtract(incomeTax);

        /* Get payment period */
        String currMonth = new DateFormatSymbols().getMonths()[employee.getPaymentMonth()-1];
        String minimumDay = "01";
        String maximumDay = Integer.toString(calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        String fromDate = String.join(" ", minimumDay, currMonth);
        String toDate = String.join(" ", maximumDay, currMonth);

        BigDecimal _super = BigDecimal.valueOf(employee.getSuperRate()).multiply(netIncome);

        EmployeePayslip paySlip = new EmployeePayslip(_super,
                grossIncome, incomeTax, netIncome, fromDate, toDate);

        EmployeePayslipDatabase.add(paySlip);
        EmployeeTuple<EmployeeDetails, EmployeePayslip> employeeTuple = new EmployeeTuple<>(employeeDetails, paySlip);
        // Want to return here tuples of Employee and payslip information

        return employeeTuple;
    }

    @Override
    public List<EmployeeDetails> showEmployees() {
        return EmployeeDatabase;
    }

    private BigDecimal calculateGrossIncome(int annualSalary){
        return BigDecimal.valueOf(Math.round(annualSalary / 12.0));
    }

    private BigDecimal calculateIncomeTax(int annualSalary){
        /* Change this implementation to utilise Big Decimal Properly
        as well as using JSON config file to load in the ranges
         */

         // Get values from JSON
        int[] taxBoundValues = (int[]) this.taxInfoObject.get("taxBoundValues");
        float[] taxFactors = (float[]) this.taxInfoObject.get("factors");
        float[] initialValues = (float[]) this.taxInfoObject.get("initials");
        int lowBoundIndex = 0, highBoundIndex = 1, taxFactorIndex = 0, initialValuesIndex = 0;
        while (highBoundIndex < taxBoundValues.length - 1){
            if ((annualSalary) >= taxBoundValues[lowBoundIndex] && (annualSalary) <= taxBoundValues[highBoundIndex]){
                break;
            }
            lowBoundIndex++;highBoundIndex++;taxFactorIndex++;initialValuesIndex++;
        }
        // Get TaxFactors
        BigDecimal remainingAmount = BigDecimal.valueOf(annualSalary - taxBoundValues[lowBoundIndex]);
        BigDecimal factor = BigDecimal.valueOf(taxFactors[taxFactorIndex]);
        BigDecimal initialTax = BigDecimal.valueOf(initialValues[initialValuesIndex]);
        try {
            logtoFile(remainingAmount.toPlainString());
            logtoFile(factor.toPlainString());
            logtoFile(initialTax.toPlainString());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return (initialTax.add((remainingAmount.multiply(factor)))).divide(BigDecimal.valueOf(12.0f), RoundingMode.UNNECESSARY);
    }

    private void logtoFile(String string) throws IOException{
        this.fileWriter.append(string);
        this.fileWriter.close();
    }
}
