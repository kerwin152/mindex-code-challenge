package com.mindex.challenge.service.impl;

import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.ReportingStructureService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ReportingStructureServiceImpl implements ReportingStructureService {
    private static final Logger LOG = LoggerFactory.getLogger(ReportingStructureServiceImpl.class);

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public ReportingStructure create(Employee employee) {
        if (employee == null) {
            throw new RuntimeException("Invalid employeeId: " + employee.getEmployeeId());
        }
        LOG.debug("Creating reporting structure of [{}]", employee);
        findDirectReports(employee);
        return new ReportingStructure(employee);
    }

    private void findDirectReports(Employee employee) {
        if (employee != null) {
            List<Employee> directReports = employee.getDirectReports();
            List<Employee> newDirectReports = new ArrayList<Employee>();
            if (directReports != null) {
                for (Employee e : employee.getDirectReports()) {
                    Employee temp = employeeRepository.findByEmployeeId(e.getEmployeeId());
                    findDirectReports(temp);
                    newDirectReports.add(temp);
                }
                employee.setDirectReports(newDirectReports);
            }
        }
    }
}
