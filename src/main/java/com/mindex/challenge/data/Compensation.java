package com.mindex.challenge.data;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * A Compensation has the following fields: employee, salary, and effectiveDate. Create 
 * two new Compensation REST endpoints. One to create and one to read by employeeId. These should persist and query the 
 * Compensation from the persistence layer.
 */
public class Compensation {
    @JsonIgnore
    private Employee employee;

    private String employeeID;
    private int salary;
    private String effectiveDate;

    public Compensation() {
        this.employeeID = "";
        this.salary = 0;
        this.effectiveDate = "NA/NA/NA";
    }

    public Compensation(Employee employee, int salary, String effectiveDate){
        this.employee = employee;
        this.salary = salary;
        this.effectiveDate = effectiveDate;
        this.employeeID = this.employee.getEmployeeId();
    }

    public int getSalary() {
        return salary;
    }

    public String getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(String effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public String getEmployeeID() {
        return employeeID;
    }
}
