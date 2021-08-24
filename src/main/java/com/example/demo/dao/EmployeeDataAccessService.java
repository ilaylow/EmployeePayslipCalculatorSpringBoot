package com.example.demo.dao;

import com.example.demo.model.EmployeeDetails;
import com.example.demo.model.EmployeePayslip;
import com.example.demo.model.EmployeeTuple;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonArrayFormatVisitor;
import org.json.simple.JSONArray;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Repository;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.*;

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

    private final Calendar calendar = Calendar.getInstance();
    private static List<EmployeeDetails> EmployeeDatabase = new ArrayList<>();
    private static List<EmployeePayslip> EmployeePayslipDatabase = new ArrayList<>();

    ClassLoader classLoader = getClass().getClassLoader();
    InputStream  i =  classLoader.getResourceAsStream("taxrange.config.json");
    //private final FileWriter fileWriter = new FileWriter("src\\main\\java\\com\\example\\demo\\log.txt");
    private final JSONParser parser = new JSONParser();
    private final Object obj = parser.parse(new BufferedReader(new InputStreamReader(i)));
    private final JSONObject taxInfoObject = (JSONObject) obj;
    //TODO: Try JACKSON
    public EmployeeDataAccessService() throws IOException, ParseException { };

    @Override
    public EmployeeTuple<EmployeeDetails, EmployeePayslip> insertEmployeeData(UUID id, EmployeeDetails employee) {
        /* Assume that insertion to in-memory database (ArrayList) always works, so it always returns 1 */
        EmployeeDetails employeeDetails = new EmployeeDetails(id, employee.getFirstName(), employee.getLastName(),
                employee.getAnnualSalary(), employee.getPaymentMonth(), employee.getSuperRate());
        EmployeeDatabase.add(employeeDetails);

        /* Calculate Employees Pay Slip Information */
        BigDecimal grossIncome = calculateGrossIncome(employee.getAnnualSalary());
        BigDecimal incomeTax = calculateIncomeTax(employee.getAnnualSalary());
        BigDecimal netIncome = grossIncome.subtract(incomeTax);

        /* Get payment period */
        String currMonth = new DateFormatSymbols().getMonths()[employee.getPaymentMonth()-1];
        String minimumDay = "01";
        String maximumDay = Integer.toString(calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        String fromDate = String.join(" ", minimumDay, currMonth);
        String toDate = String.join(" ", maximumDay, currMonth);

        BigDecimal _super = BigDecimal.valueOf(employee.getSuperRate()).multiply(grossIncome);

        EmployeePayslip paySlip = new EmployeePayslip(_super,
                grossIncome, incomeTax, netIncome, fromDate, toDate);

        EmployeePayslipDatabase.add(paySlip);
        EmployeeTuple<EmployeeDetails, EmployeePayslip> employeeTuple = new EmployeeTuple<>(employeeDetails, paySlip);

        return employeeTuple;
    }

    @Override
    public List<EmployeeDetails> showEmployees() {
        return EmployeeDatabase;
    }

    public List<EmployeePayslip> showPayslips() { return EmployeePayslipDatabase; }

    public BigDecimal calculateGrossIncome(int annualSalary){
        return BigDecimal.valueOf(Math.round(annualSalary / 12.0f));
    }

    private BigDecimal calculateIncomeTax(int annualSalary){
        /* Change this implementation to utilise Big Decimal Properly
        as well as using JSON config file to load in the ranges
         */
        /* try {
            this.logtoFile("Calculating Income Tax ...\n");
        } catch (IOException e) {
            e.printStackTrace();
        } */

        // Get values from JSON
        JSONArray taxBoundValues = ((JSONArray) this.taxInfoObject.get("taxBoundValues"));
        JSONArray taxFactors = (JSONArray) this.taxInfoObject.get("factors");
        JSONArray initialValues = (JSONArray) this.taxInfoObject.get("initials");
        int lowBoundIndex = 0, highBoundIndex = 1, taxFactorIndex = 0, initialValuesIndex = 0;
        while (highBoundIndex < taxBoundValues.size() - 1){
            if ((annualSalary) >= (Long)taxBoundValues.get(lowBoundIndex) && (annualSalary) <= (Long)taxBoundValues.get(highBoundIndex)){
                break;
            }
            lowBoundIndex++;highBoundIndex++;taxFactorIndex++;initialValuesIndex++;
        }

        // Get TaxFactors
        BigDecimal remainingAmount = BigDecimal.valueOf(annualSalary - (Long)taxBoundValues.get(lowBoundIndex));
        BigDecimal factor = BigDecimal.valueOf((Double)taxFactors.get(taxFactorIndex));
        BigDecimal initialTax = BigDecimal.valueOf((Long)initialValues.get(initialValuesIndex));

        /* try{
            this.logtoFile("Remaining Amount: " + remainingAmount.toString() + "\n");
            this.logtoFile("Tax Factor: " + factor.toString() + "\n");
            this.logtoFile("Initial Tax Amount: " + initialTax.toString() + "\n\n");
        } catch(IOException e){
            e.printStackTrace();
        } */

        return (initialTax.add((remainingAmount.multiply(factor)))).divide(BigDecimal.valueOf(12.0f), RoundingMode.HALF_UP);
    }

    /* private void logtoFile(String string) throws IOException{
        this.fileWriter.append(string);
        this.fileWriter.flush();
    } */
}
