package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class EmployeePayslip {

    private final int payPeriod, grossIncome, incomeTax, netIncome; //Rounded, so all are ints

    public int getPayPeriod() {
        return payPeriod;
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

    public EmployeePayslip(@JsonProperty("payPeriod") int payPeriod,
                           @JsonProperty("grossIncome") int grossIncome,
                           @JsonProperty("incomeTax") int incomeTax,
                           @JsonProperty("netIncome") int netIncome) {

        this.payPeriod = payPeriod;
        this.grossIncome = grossIncome;
        this.incomeTax = incomeTax;
        this.netIncome = netIncome;
    }
}
