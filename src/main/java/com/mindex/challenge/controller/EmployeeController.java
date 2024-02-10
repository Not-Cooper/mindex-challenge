package com.mindex.challenge.controller;

import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.EmployeeService;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class EmployeeController {
    private static final Logger LOG = LoggerFactory.getLogger(EmployeeController.class);

    @Autowired
    private EmployeeService employeeService;

    @PostMapping("/employee")
    public Employee create(@RequestBody Employee employee) {
        LOG.debug("Received employee create request for [{}]", employee);

        return employeeService.create(employee);
    }

    @GetMapping("/employee/{id}")
    public Employee read(@PathVariable String id) {
        LOG.debug("Received employee create request for id [{}]", id);

        return employeeService.read(id);
    }

    @PutMapping("/employee/{id}")
    public Employee update(@PathVariable String id, @RequestBody Employee employee) {
        LOG.debug("Received employee create request for id [{}] and employee [{}]", id, employee);

        employee.setEmployeeId(id);
        return employeeService.update(employee);
    }

    @GetMapping("/employee/{id}/reportingstruct")
    public ReportingStructure getReporting(@PathVariable String id){
        LOG.debug("Received employee create request for id [{}]", id);
        Employee employee = employeeService.read(id);
        ReportingStructure reportingStruct = employee.getReporting();

        // -1 in order to remove the root node
        reportingStruct.setNumberOfReports(visitReporting(employee) - 1);
        return reportingStruct;
    }

    /**
     * Recursively visit each employee under the given
     * @param e given employee (may or may not have direct reporting)
     * @return the number of nodes connected to the root (including the root)
     */
    private int visitReporting(Employee e){
        List<Employee> dirReports = e.getDirectReports();
        if (dirReports == null){
            return 1;
        }
        else{
            int count = 1;
            for (Employee reporter : dirReports){
                count += this.visitReporting(this.employeeService.read(reporter.getEmployeeId()));
            }
            return count;
        }
    }
}
