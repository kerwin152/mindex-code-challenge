package com.mindex.challenge.data;

import java.util.List;

public class ReportingStructure {
    private Employee employee;
    private int numberOfReports;

    public ReportingStructure() {
    }

    public ReportingStructure(Employee employee) {
        this.employee = employee;
        this.numberOfReports = this.checkReports(employee);

    }

    //get the number of reports by recursive
    private int checkReports(Employee employee) {
        int count = 0;
        if (employee != null) {
            List<Employee> directReports = employee.getDirectReports();
            if (directReports != null) {
                for (Employee e : directReports) {
                    count++;
                    count += checkReports(e);
                }
            }
        }
        return count;
    }

    public Employee getEmployee() {
        return this.employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public int getNumberOfReports() {
        return this.numberOfReports;
    }

    public void setNumberOfReports(int numberOfReports) {
        this.numberOfReports = numberOfReports;
    }
}
