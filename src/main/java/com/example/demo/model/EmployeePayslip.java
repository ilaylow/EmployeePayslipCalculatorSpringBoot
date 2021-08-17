package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public class EmployeePayslip {

    private final BigDecimal superannuation, grossIncome, incomeTax, netIncome; //Rounded, so all are ints
    private final String fromDate, toDate;

    public BigDecimal getSuperannuation() {
        return superannuation;
    }

    public BigDecimal getGrossIncome() {
        return grossIncome;
    }

    public BigDecimal getIncomeTax() {
        return incomeTax;
    }

    public BigDecimal getNetIncome() {
        return netIncome;
    }

    public String getFromDate() {
        return fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public EmployeePayslip(@JsonProperty("superannuation") BigDecimal superannuation,
                           @JsonProperty("grossIncome") BigDecimal grossIncome,
                           @JsonProperty("incomeTax") BigDecimal incomeTax,
                           @JsonProperty("netIncome") BigDecimal netIncome,
                           @JsonProperty("fromDate") String fromDate,
                           @JsonProperty("toDate") String toDate) {

        this.superannuation = superannuation;
        this.grossIncome = grossIncome;
        this.incomeTax = incomeTax;
        this.netIncome = netIncome;
        this.fromDate = fromDate;
        this.toDate = toDate;
    }

    public String toString(){
        return String.format("{grossIncome: %.2f,\n" +
                "incomeTax: %.2f,\n" +
                "netIncome: %.2f,\n" +
                "fromDate: %s,\n" +
                "toDate: %s}", grossIncome, incomeTax, netIncome, fromDate, toDate);
    }
}
