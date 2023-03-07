package com.mindex.challenge.service.impl;

import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.Employee;
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
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CompensationServiceImplTest {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private String employeeUrl;
    private String compensationUrl;

    private SimpleDateFormat dateFormat = new SimpleDateFormat("mm-dd-yyyy");

    private Date today = new Date();

    //initialize test data
    Employee zayn = new Employee();
    {
        zayn.setFirstName("Zayn");
        zayn.setLastName("Lin");
        zayn.setDepartment("Engineering");
        zayn.setPosition("Java Developer");
    }
    Compensation salaryOfZayn = new Compensation();
    {
        salaryOfZayn.setEffectiveDate(dateFormat.format(today));
        salaryOfZayn.setSalary("50");
    }


    @Before
    public void setup(){
        employeeUrl = "http://localhost:" + port + "/employee";
        compensationUrl = "http://localhost:" + port +"/compensation/{id}";

        // Create and update Pete
        zayn = restTemplate.postForEntity(employeeUrl, zayn, Employee.class).getBody();
        salaryOfZayn.setEmployeeId(zayn.getEmployeeId());
    }

    @Test
    public void testCompensationWorkflow() {
        // check CREATE
        Compensation newSalaryOfZayn = restTemplate.postForEntity(compensationUrl, salaryOfZayn, Compensation.class, zayn.getEmployeeId()).getBody();

        assertNotNull(newSalaryOfZayn);
        assertEquals(salaryOfZayn, newSalaryOfZayn);

        // check READ
        Compensation readSalary = restTemplate.getForEntity(compensationUrl, Compensation.class, zayn.getEmployeeId()).getBody();
        assertNotNull(readSalary);
        assertEquals(salaryOfZayn, readSalary);

        //check UPDATE
        salaryOfZayn.setSalary("70");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Compensation updatedSalary =
                restTemplate.exchange(compensationUrl,
                        HttpMethod.PUT,
                        new HttpEntity<Compensation>(salaryOfZayn, headers),
                        Compensation.class,
                        zayn.getEmployeeId()).getBody();

        assertEquals(salaryOfZayn, updatedSalary);

    }


}
