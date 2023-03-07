package com.mindex.challenge.data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Compensation {
    private String employeeId;
    private String salary;
    private Date effectiveDate;

    private static final Logger LOG = LoggerFactory.getLogger(Compensation.class);

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Compensation)) {
            return false;
        }
        Compensation other_comp = (Compensation) obj;
        return (other_comp.getSalary().equals(this.getSalary()) &&
                other_comp.getEffectiveDate().equals(this.getEffectiveDate()));
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getSalary() {
        return this.salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public String getEffectiveDate() {
        return new SimpleDateFormat("mm-dd-yyyy").format(this.effectiveDate);
    }

    public void setEffectiveDate(String effectiveDate) {
        try{
            this.effectiveDate = new SimpleDateFormat("mm-dd-yyyy").parse(effectiveDate);
        }catch (ParseException e){
            LOG.debug("Unable to parse date: [{}]", effectiveDate);
        }

    }

}
