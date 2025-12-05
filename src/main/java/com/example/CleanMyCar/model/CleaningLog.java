package com.example.CleanMyCar.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "cleaning_logs",
        uniqueConstraints = @UniqueConstraint(columnNames = {"subscription_id", "employee_id", "date"}))
public class CleaningLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Subscription subscription;

    @ManyToOne(optional = false)
    private Employee employee;

    private LocalDate date; // which day

    private String status;  // PENDING / CLEANED / MISSED

    private LocalDateTime cleanedAt;

    public CleaningLog() {}

    public CleaningLog(Subscription subscription, Employee employee, LocalDate date, String status) {
        this.subscription = subscription;
        this.employee = employee;
        this.date = date;
        this.status = status;
    }

    public Long getId() { return id; }

    public Subscription getSubscription() { return subscription; }
    public void setSubscription(Subscription subscription) { this.subscription = subscription; }

    public Employee getEmployee() { return employee; }
    public void setEmployee(Employee employee) { this.employee = employee; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getCleanedAt() { return cleanedAt; }
    public void setCleanedAt(LocalDateTime cleanedAt) { this.cleanedAt = cleanedAt; }
}