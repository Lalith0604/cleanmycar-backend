package com.example.CleanMyCar.repository;

import com.example.CleanMyCar.model.CleaningLog;
import com.example.CleanMyCar.model.Employee;
import com.example.CleanMyCar.model.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface CleaningLogRepository extends JpaRepository<CleaningLog, Long> {

    Optional<CleaningLog> findBySubscriptionAndEmployeeAndDate(
            Subscription subscription,
            Employee employee,
            LocalDate date
    );

    List<CleaningLog> findByDate(LocalDate date);

    List<CleaningLog> findByEmployeeAndDate(Employee employee, LocalDate date);
}
