package com.example.CleanMyCar.repository;

import com.example.CleanMyCar.model.Subscription;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

    List<Subscription> findByCustomerId(Long customerId);

    Page<Subscription> findByStatus(String status, Pageable pageable);

    Page<Subscription> findAll(Pageable pageable);

    List<Subscription> findByStartDateBetween(LocalDate from, LocalDate to);

    long countByStartDate(LocalDate date);

    List<Subscription> findByCustomerMobileNumber(String mobile);

    // ✅ NEW IMPORTANT JOIN FETCH QUERY
    @Query("""
        SELECT s FROM Subscription s
        LEFT JOIN FETCH s.customer
        LEFT JOIN FETCH s.plan
        WHERE (:status IS NULL OR s.status = :status)
        """)
    List<Subscription> findWithCustomerAndPlan(@Param("status") String status);

    // ✅ PAGED version for your Admin table
    @Query("""
        SELECT s FROM Subscription s
        LEFT JOIN FETCH s.customer
        LEFT JOIN FETCH s.plan
        WHERE (:status IS NULL OR s.status = :status)
        """)
    Page<Subscription> findWithCustomerAndPlanPaged(@Param("status") String status, Pageable pageable);
}
