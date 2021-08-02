package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class EmployeePayslip {

    private final int superannuation, grossIncome, incomeTax, netIncome; //Rounded, so all are ints
    private final String fromDate, toDate;

    public int getSuperannuation() {
        return superannuation;
    }

    public int getGrossIncome() {
        return grossIncome;
    }

    public int getIncomeTax() {
        return incomeTax;
    }

    public int getNetIncome() {
        return netIncome;
    }

    public String getFromDate() {
        return fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public EmployeePayslip(@JsonProperty("superannuation") int superannuation,
                           @JsonProperty("grossIncome") int grossIncome,
                           @JsonProperty("incomeTax") int incomeTax,
                           @JsonProperty("netIncome") int netIncome,
                           @JsonProperty("fromDate") String fromDate,
                           @JsonProperty("toDate") String toDate) {

        this.superannuation = superannuation;
        this.grossIncome = grossIncome;
        this.incomeTax = incomeTax;
        this.netIncome = netIncome;
        this.fromDate = fromDate;
        this.toDate = toDate;
    }
}
