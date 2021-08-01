package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public class EmployeeDetails {
    private final UUID id;
    private final String firstName;
    private final String lastName;
    private final int annualSalary;
    private final int paymentMonth;
    private final float superRate;

    public EmployeeDetails(@JsonProperty("id") UUID id,
                           @JsonProperty("firstName") String firstName,
                           @JsonProperty("lastName") String lastName,
                           @JsonProperty("annualSalary") int annualSalary,
                           @JsonProperty("paymentMonth") int paymentMonth,
                           @JsonProperty("superRate") float superRate) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.annualSalary = annualSalary;
        this.paymentMonth = paymentMonth;
        this.superRate = superRate;
    }

    public UUID getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public int getAnnualSalary() {
        return annualSalary;
    }

    public int getPaymentMonth() {
        return paymentMonth;
    }

    public float getSuperRate() {
        return superRate;
    }
}
