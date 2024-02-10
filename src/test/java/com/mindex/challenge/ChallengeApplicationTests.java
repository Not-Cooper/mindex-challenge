package com.mindex.challenge;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.junit4.SpringRunner;

import com.mindex.challenge.dao.CompensationRepository;
import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ChallengeApplicationTests {
	private String employeeUrl;
    private String employeeIdUrl;
    private String compensationURL;
	
	@Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private CompensationRepository compensationRepository;

	@LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Before
    public void setup() {
        employeeUrl = "http://localhost:" + port + "/employee";
        employeeIdUrl = "http://localhost:" + port + "/employee/{id}";
        compensationURL = "http://localhost:" + port + "/compensation/{id}";
    }


    /**
     * tests for a root employee with multiple levels of employees reporting to them
     */
    @Test
	public void testGetReportingMultiple() {
		Employee john = employeeRepository.findByEmployeeId("16a596ae-edd3-4847-99fe-c4518e82c86f");
		ReportingStructure js = restTemplate.getForEntity(employeeIdUrl+"/reportingstruct", ReportingStructure.class, john.getEmployeeId()).getBody();

        assertEquals(4, js.getNumberOfReports());
	}

    /**
     * tests for a root employee with no employees reporting to them
     */
    @Test
    public void testGetReportingNone() {
        Employee paul = employeeRepository.findByEmployeeId("b7839309-3348-463b-a7e3-5de1c168beb3");
        ReportingStructure ps = restTemplate.getForEntity(employeeIdUrl+"/reportingstruct", ReportingStructure.class, paul.getEmployeeId()).getBody();

        assertEquals(0, ps.getNumberOfReports());
    }

    /**
     * tests for a root employee with only one level of employees reporting to them
     */
    @Test
    public void testGetReportingOne() {
        Employee ringo = employeeRepository.findByEmployeeId("03aa1462-ffa9-4978-901b-7c001562cf6f");
        ReportingStructure rs = restTemplate.getForEntity(employeeIdUrl+"/reportingstruct", ReportingStructure.class, ringo.getEmployeeId()).getBody();

        assertEquals(2, rs.getNumberOfReports());
    }

    @Test
    public void testAddCompensation() {
        Employee john = restTemplate.getForEntity(employeeIdUrl, Employee.class, "16a596ae-edd3-4847-99fe-c4518e82c86f").getBody();

        Compensation compensation = new Compensation(john, 1000, "02/10/24");

        Compensation comp = restTemplate.postForEntity(compensationURL, compensation, Compensation.class, john.getEmployeeId()).getBody();

        assertEquals(1000, comp.getSalary());
    }

    /**
     * Tests the retrieving of a compensation from the database
     * Passing proves that the compensation is persistent and actually exists in the database
     */
    @Test
    public void testGetCompensation() {
        Employee john = restTemplate.getForEntity(employeeIdUrl, Employee.class, "16a596ae-edd3-4847-99fe-c4518e82c86f").getBody();

        Compensation compensation = new Compensation(john, 1000, "02/10/24");

        Compensation comp = restTemplate.postForEntity(compensationURL, compensation, Compensation.class, john.getEmployeeId()).getBody();

        Compensation retrieved = restTemplate.getForEntity(compensationURL, Compensation.class, john.getEmployeeId()).getBody();

        assertCompensationEquivalence(comp, retrieved);
    }


    private static void assertCompensationEquivalence(Compensation expected, Compensation actual) {
        assertEquals(expected.getEffectiveDate(), actual.getEffectiveDate());
        assertEquals(expected.getEmployeeID(), actual.getEmployeeID());
        assertEquals(expected.getSalary(), actual.getSalary());
    }
}
