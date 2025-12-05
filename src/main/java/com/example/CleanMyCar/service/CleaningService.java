package com.example.CleanMyCar.service;

import com.example.CleanMyCar.model.CleaningLog;
import com.example.CleanMyCar.model.Employee;
import com.example.CleanMyCar.model.EmployeeCar;
import com.example.CleanMyCar.model.Subscription;
import com.example.CleanMyCar.repository.CleaningLogRepository;
import com.example.CleanMyCar.repository.EmployeeCarRepository;
import com.example.CleanMyCar.repository.EmployeeRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class CleaningService {

    private final CleaningLogRepository cleaningRepo;
    private final EmployeeCarRepository employeeCarRepo;
    private final EmployeeRepository employeeRepo;

    public CleaningService(CleaningLogRepository cleaningRepo,
                           EmployeeCarRepository employeeCarRepo,
                           EmployeeRepository employeeRepo) {
        this.cleaningRepo = cleaningRepo;
        this.employeeCarRepo = employeeCarRepo;
        this.employeeRepo = employeeRepo;
    }

    // Called by employee when he cleans a car
    public CleaningLog markCleaned(Long employeeId, Long subscriptionId) throws Exception {
        Employee employee = employeeRepo.findById(employeeId).orElse(null);
        if (employee == null) throw new Exception("Employee not found");

        // ensure this car is assigned to employee
        Subscription subscription = null;
        EmployeeCar ec = null;
        for (EmployeeCar item : employeeCarRepo.findByEmployeeAndActive(employee, true)) {
            if (item.getSubscription().getId().equals(subscriptionId)) {
                ec = item;
                subscription = item.getSubscription();
                break;
            }
        }

        if (ec == null || subscription == null) {
            throw new Exception("Car not assigned to this employee");
        }

        LocalDate today = LocalDate.now();

        CleaningLog log = cleaningRepo
                .findBySubscriptionAndEmployeeAndDate(subscription, employee, today)
                .orElse(new CleaningLog(subscription, employee, today, "PENDING"));

        log.setStatus("CLEANED");
        log.setCleanedAt(LocalDateTime.now());

        return cleaningRepo.save(log);
    }

    public List<CleaningLog> getTodayLogs() {
        LocalDate today = LocalDate.now();
        return cleaningRepo.findByDate(today);
    }

    // Cron / manual: mark yesterday PENDING as MISSED
    public void markMissedForDate(LocalDate date) {
        List<CleaningLog> list = cleaningRepo.findByDate(date);
        for (CleaningLog log : list) {
            if ("PENDING".equalsIgnoreCase(log.getStatus())) {
                log.setStatus("MISSED");
                cleaningRepo.save(log);
            }
        }
    }

    // Generate PENDING logs for today for all active assignments
    public void createPendingForToday(List<EmployeeCar> assignments) {
        LocalDate today = LocalDate.now();
        for (EmployeeCar ec : assignments) {
            CleaningLog existing = cleaningRepo
                    .findBySubscriptionAndEmployeeAndDate(ec.getSubscription(), ec.getEmployee(), today)
                    .orElse(null);

            if (existing == null) {
                CleaningLog log = new CleaningLog(ec.getSubscription(), ec.getEmployee(), today, "PENDING");
                cleaningRepo.save(log);
            }
        }
    }
}
