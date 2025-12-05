package com.example.CleanMyCar.repository;

import com.example.CleanMyCar.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Optional<Employee> findByMobileNumber(String mobileNumber);
}
