package com.example.CleanMyCar.service;

import com.example.CleanMyCar.model.Employee;
import com.example.CleanMyCar.model.EmployeeCar;
import com.example.CleanMyCar.model.Subscription;
import com.example.CleanMyCar.repository.EmployeeCarRepository;
import com.example.CleanMyCar.repository.EmployeeRepository;
import com.example.CleanMyCar.repository.SubscriptionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EmployeeAssignmentService {

    private final EmployeeCarRepository employeeCarRepo;
    private final EmployeeRepository employeeRepo;
    private final SubscriptionRepository subscriptionRepo;

    public EmployeeAssignmentService(EmployeeCarRepository employeeCarRepo,
                                     EmployeeRepository employeeRepo,
                                     SubscriptionRepository subscriptionRepo) {
        this.employeeCarRepo = employeeCarRepo;
        this.employeeRepo = employeeRepo;
        this.subscriptionRepo = subscriptionRepo;
    }

    public EmployeeCar assignCar(Long employeeId, Long subscriptionId) throws Exception {
        Employee employee = employeeRepo.findById(employeeId).orElse(null);
        if (employee == null) throw new Exception("Employee not found");

        Subscription subscription = subscriptionRepo.findById(subscriptionId).orElse(null);
        if (subscription == null) throw new Exception("Subscription not found");

        // Check existing
        var existingOpt = employeeCarRepo.findByEmployeeAndSubscription(employee, subscription);
        if (existingOpt.isPresent()) {
            EmployeeCar ec = existingOpt.get();
            ec.setActive(true);
            return employeeCarRepo.save(ec);
        }

        EmployeeCar ec = new EmployeeCar(employee, subscription, LocalDateTime.now());
        return employeeCarRepo.save(ec);
    }

    public void removeCar(Long employeeId, Long subscriptionId) throws Exception {
        Employee employee = employeeRepo.findById(employeeId).orElse(null);
        if (employee == null) throw new Exception("Employee not found");

        Subscription subscription = subscriptionRepo.findById(subscriptionId).orElse(null);
        if (subscription == null) throw new Exception("Subscription not found");

        var existingOpt = employeeCarRepo.findByEmployeeAndSubscription(employee, subscription);
        if (existingOpt.isPresent()) {
            EmployeeCar ec = existingOpt.get();
            ec.setActive(false);
            employeeCarRepo.save(ec);
        }
    }

    public List<EmployeeCar> getActiveAssignmentsForEmployee(Long employeeId) {
        Employee employee = employeeRepo.findById(employeeId).orElse(null);
        if (employee == null) return List.of();
        return employeeCarRepo.findByEmployeeAndActive(employee, true);
    }
}
