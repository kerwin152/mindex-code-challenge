package com.mindex.challenge.service.impl;

import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.EmployeeService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ReportingStructureServiceImplTest {
    private String employeeUrl;
    private String reportingStructureUrl;

    @Autowired
    private EmployeeService employeeService;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    //initialize test data
    private HashMap<String, Employee> testEmployees = new HashMap<>();
    {
        Employee manager = new Employee();
        manager.setFirstName("Anna");
        manager.setLastName("Yang");
        manager.setDepartment("Engineering");
        manager.setPosition("Manager");
        testEmployees.put("manager", manager);

        for (int i=0; i<3; i++) {
            Employee testEmployee = new Employee();
            testEmployee.setFirstName("testEmployee");
            testEmployee.setLastName(String.valueOf(i));
            testEmployee.setDepartment("Engineering");
            testEmployee.setPosition("TestEmployee");
            testEmployees.put("testEmployee" + String.valueOf(i), testEmployee);
        }
    }

    @Before
    public void setup() {
        employeeUrl = "http://localhost:" + port + "/employee";
        reportingStructureUrl = "http://localhost:" + port + "/reportingStructure/{id}";

        // Create TestEmployee2,3
        Employee testEmployee2 = restTemplate.postForEntity(employeeUrl, testEmployees.get("testEmployee2"), Employee.class).getBody();
        Employee testEmployee3 = restTemplate.postForEntity(employeeUrl, testEmployees.get("testEmployee3"), Employee.class).getBody();


        // Add TestEmployee2 and 3 as subordinates to TestEmployee1
        ArrayList<Employee> testEmployee1Reports = new ArrayList<>();
        testEmployee1Reports.add(testEmployee2);
        testEmployee1Reports.add(testEmployee3);
        testEmployees.get("testEmployee1").setDirectReports(testEmployee1Reports);

        //Create TestEmployee1
        Employee testEmployee1 = restTemplate.postForEntity(employeeUrl, testEmployees.get("testEmployee1"), Employee.class).getBody();

        // Add TestEmployee1 as subordinate to manager
        ArrayList<Employee> managerReports = new ArrayList<>();
        managerReports.add(testEmployee1);
        testEmployees.get("manager").setDirectReports(managerReports);

        //Create manager
        Employee manager = restTemplate.postForEntity(employeeUrl, testEmployees.get("manager"), Employee.class).getBody();

        // Update test data structure with ID
        testEmployees.put("manager", manager);
        testEmployees.put("testEmployee1", testEmployee1);
        testEmployees.put("testEmployee2", testEmployee2);
        testEmployees.put("testEmployee3", testEmployee3);
    }

    //Assert that testEmployee2 and testEmployee3 do not have subordinate
    @Test
    public void testReportingStructureNoReports() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        ReportingStructure reportsOfTestEmployee2 =
                restTemplate.getForEntity(reportingStructureUrl,
                        ReportingStructure.class,
                        testEmployees.get("testEmployee2").getEmployeeId()).getBody();
        assertEquals(0, reportsOfTestEmployee2.getNumberOfReports());

        ReportingStructure reportsOfTestEmployee3 =
                restTemplate.getForEntity(reportingStructureUrl,
                        ReportingStructure.class,
                        testEmployees.get("testEmployee3").getEmployeeId()).getBody();
        assertEquals(0, reportsOfTestEmployee3.getNumberOfReports());
    }

    //Assert that testEmployee1 has two subordinates
    @Test
    public void testReportingStructureTwoReports() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        ReportingStructure reportsOfTestEmployee1 =
                restTemplate.getForEntity(reportingStructureUrl,
                        ReportingStructure.class,
                        testEmployees.get("testEmployee1").getEmployeeId()).getBody();
        assertEquals(2, reportsOfTestEmployee1.getNumberOfReports());
    }

    //Assert that manager has three subordinates
    @Test
    public void testReportingStructureThreeReports() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        ReportingStructure reportsOfManager =
                restTemplate.getForEntity(reportingStructureUrl,
                        ReportingStructure.class,
                        testEmployees.get("manager").getEmployeeId()).getBody();
        assertEquals(3, reportsOfManager.getNumberOfReports());
    }
}
