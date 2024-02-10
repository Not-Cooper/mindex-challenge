package com.mindex.challenge.data;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Reporting structure: represents employees directly and indirectly reporting to the employee a reporting structure is connected to
 */
public class ReportingStructure {
    @JsonIgnore
    private Employee employee;

    private int numberOfReports;

    public ReportingStructure(){
        this.numberOfReports = 0;
    }

    public ReportingStructure(Employee employee){
        this.employee = employee;
        this.numberOfReports = 0;
    }

    public int getNumberOfReports() {
        return numberOfReports;
    }

    public void setNumberOfReports(int numberOfReports) {
        this.numberOfReports = numberOfReports;
    }
}
