package com.example.CleanMyCar.scheduler;

import com.example.CleanMyCar.model.EmployeeCar;
import com.example.CleanMyCar.service.CleaningService;
import com.example.CleanMyCar.repository.EmployeeCarRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class CleaningScheduler {

    private final CleaningService cleaningService;
    private final EmployeeCarRepository employeeCarRepo;

    public CleaningScheduler(CleaningService cleaningService,
                             EmployeeCarRepository employeeCarRepo) {
        this.cleaningService = cleaningService;
        this.employeeCarRepo = employeeCarRepo;
    }

    // Every day at 00:05 – mark yesterday missed
    @Scheduled(cron = "0 5 0 * * *")
    public void markYesterdayMissed() {
        LocalDate yesterday = LocalDate.now().minusDays(1);
        cleaningService.markMissedForDate(yesterday);
    }

    // Every day at 00:10 – create today's PENDING logs
    @Scheduled(cron = "0 10 0 * * *")
    public void createTodayPending() {
        List<EmployeeCar> activeAssignments = employeeCarRepo.findAll().stream()
                .filter(EmployeeCar::isActive)
                .toList();

        cleaningService.createPendingForToday(activeAssignments);
    }
}

