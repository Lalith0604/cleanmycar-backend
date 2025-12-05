package com.example.CleanMyCar.controller;

import com.example.CleanMyCar.model.CleaningLog;
import com.example.CleanMyCar.model.Employee;
import com.example.CleanMyCar.model.EmployeeCar;
import com.example.CleanMyCar.service.CleaningService;
import com.example.CleanMyCar.service.EmployeeAssignmentService;
import com.example.CleanMyCar.service.EmployeeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employee")
public class EmployeeController {

    private final EmployeeService employeeService;
    private final EmployeeAssignmentService assignmentService;
    private final CleaningService cleaningService;

    public EmployeeController(EmployeeService employeeService,
                              EmployeeAssignmentService assignmentService,
                              CleaningService cleaningService) {
        this.employeeService = employeeService;
        this.assignmentService = assignmentService;
        this.cleaningService = cleaningService;
    }

    // Simple PIN login
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest body) {
        Employee e = employeeService.login(body.getMobile(), body.getPin());
        if (e == null) {
            return ResponseEntity.status(401).body("Invalid mobile or PIN");
        }
        // frontend can store employeeId in localStorage
        return ResponseEntity.ok(new LoginResponse(e.getId(), e.getFullName()));
    }

    // Get assigned cars for this employee
    @GetMapping("/{employeeId}/cars")
    public ResponseEntity<List<EmployeeCar>> getMyCars(@PathVariable Long employeeId) {
        return ResponseEntity.ok(assignmentService.getActiveAssignmentsForEmployee(employeeId));
    }

    // Mark a car cleaned (today)
    @PostMapping("/{employeeId}/clean")
    public ResponseEntity<?> markClean(@PathVariable Long employeeId,
                                       @RequestParam Long subscriptionId) {
        try {
            CleaningLog log = cleaningService.markCleaned(employeeId, subscriptionId);
            return ResponseEntity.ok(log);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    public static class LoginRequest {
        private String mobile;
        private String pin;

        public String getMobile() { return mobile; }
        public void setMobile(String mobile) { this.mobile = mobile; }

        public String getPin() { return pin; }
        public void setPin(String pin) { this.pin = pin; }
    }

    public static class LoginResponse {
        private Long employeeId;
        private String fullName;

        public LoginResponse(Long employeeId, String fullName) {
            this.employeeId = employeeId;
            this.fullName = fullName;
        }

        public Long getEmployeeId() { return employeeId; }
        public String getFullName() { return fullName; }
    }
}
