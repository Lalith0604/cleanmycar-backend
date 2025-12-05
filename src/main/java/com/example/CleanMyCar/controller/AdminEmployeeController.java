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
@RequestMapping("/api/admin/employees")
public class AdminEmployeeController {

    private final EmployeeService employeeService;
    private final EmployeeAssignmentService assignmentService;
    private final CleaningService cleaningService;

    public AdminEmployeeController(EmployeeService employeeService,
                                   EmployeeAssignmentService assignmentService,
                                   CleaningService cleaningService) {
        this.employeeService = employeeService;
        this.assignmentService = assignmentService;
        this.cleaningService = cleaningService;
    }

    // Create employee
    @PostMapping
    public ResponseEntity<Employee> create(@RequestBody Employee req) {
        Employee e = employeeService.createEmployee(req.getFullName(), req.getMobileNumber(), req.getPin());
        return ResponseEntity.ok(e);
    }

    // List employees
    @GetMapping
    public ResponseEntity<List<Employee>> listEmployees() {
        return ResponseEntity.ok(employeeService.getAll());
    }

    // Deactivate employee
    @PostMapping("/{id}/deactivate")
    public ResponseEntity<?> deactivate(@PathVariable Long id) {
        employeeService.deactivate(id);
        return ResponseEntity.ok("Deactivated");
    }

    // Assign car
    @PostMapping("/assign")
    public ResponseEntity<?> assignCar(@RequestParam Long employeeId, @RequestParam Long subscriptionId) {
        try {
            EmployeeCar ec = assignmentService.assignCar(employeeId, subscriptionId);
            return ResponseEntity.ok(ec);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Remove car from employee
    @PostMapping("/remove")
    public ResponseEntity<?> removeCar(@RequestParam Long employeeId, @RequestParam Long subscriptionId) {
        try {
            assignmentService.removeCar(employeeId, subscriptionId);
            return ResponseEntity.ok("Removed");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Get assigned cars for an employee
    @GetMapping("/{id}/cars")
    public ResponseEntity<List<EmployeeCar>> getCars(@PathVariable Long id) {
        return ResponseEntity.ok(assignmentService.getActiveAssignmentsForEmployee(id));
    }

    // Today's cleaning logs (for admin view)
    @GetMapping("/cleaning/today")
    public ResponseEntity<List<CleaningLog>> getTodayCleaningLogs() {
        return ResponseEntity.ok(cleaningService.getTodayLogs());
    }
}