package com.example.CleanMyCar.repository;

import com.example.CleanMyCar.model.EmployeeCar;
import com.example.CleanMyCar.model.Employee;
import com.example.CleanMyCar.model.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EmployeeCarRepository extends JpaRepository<EmployeeCar, Long> {
    List<EmployeeCar> findByEmployeeAndActive(Employee employee, boolean active);
    Optional<EmployeeCar> findByEmployeeAndSubscription(Employee employee, Subscription subscription);
    List<EmployeeCar> findBySubscription(Subscription subscription);
}
