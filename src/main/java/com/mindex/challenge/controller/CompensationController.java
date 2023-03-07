package com.mindex.challenge.controller;

import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.service.CompensationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class CompensationController {
    private static final Logger LOG = LoggerFactory.getLogger(EmployeeController.class);
    @Autowired
    private CompensationService compensationService;

    @PostMapping("/compensation/{id}")
    public Compensation createCompensation(@PathVariable String id, @RequestBody Compensation compensation) {
        LOG.debug("Received compensation create request for employee with id [{}]. Compensation: [{}]", id, compensation);
        compensation.setEmployeeId(id);
        return compensationService.create(compensation);
    }

    @GetMapping("/compensation/{id}")
    public Compensation readCompensation(@PathVariable String id) {
        LOG.debug("Received employee compensation read request for id [{}]", id);
        return compensationService.read(id);
    }

    @PutMapping("/compensation/{id}")
    public Compensation updateCompensation(@PathVariable String id, @RequestBody Compensation compensation) {
        LOG.debug("Received employee compensation update request for employee with id [{}]. Compensation: [{}]", id, compensation);
        compensation.setEmployeeId(id);
        return compensationService.update(compensation);
    }
}
