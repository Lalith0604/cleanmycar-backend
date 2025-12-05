package com.example.CleanMyCar.repository;

import com.example.CleanMyCar.model.PaymentRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface PaymentRecordRepository extends JpaRepository<PaymentRecord, Long> {

    @Query(value = """
        SELECT p FROM PaymentRecord p
        LEFT JOIN FETCH p.subscription s
        LEFT JOIN FETCH s.customer c
        """,
            countQuery = "SELECT COUNT(p) FROM PaymentRecord p")
    Page<PaymentRecord> findAllWithDetails(Pageable pageable);

    @Query("SELECT COALESCE(SUM(p.amount), 0) FROM PaymentRecord p WHERE p.status = :status AND p.createdAt BETWEEN :from AND :to")
    Long sumAmountByStatusBetween(@Param("status") String status,
                                  @Param("from") LocalDateTime from,
                                  @Param("to") LocalDateTime to);

    @Query("SELECT COALESCE(SUM(p.amount), 0) FROM PaymentRecord p WHERE p.status = :status")
    Long sumAmountByStatus(@Param("status") String status);
}
