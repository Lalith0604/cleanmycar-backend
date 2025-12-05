package com.example.CleanMyCar.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "employee_cars",
        uniqueConstraints = @UniqueConstraint(columnNames = {"employee_id", "subscription_id"}))
public class EmployeeCar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Employee employee;

    @ManyToOne(optional = false)
    private Subscription subscription;

    private LocalDateTime assignedAt;

    private boolean active = true;

    public EmployeeCar() {}

    public EmployeeCar(Employee employee, Subscription subscription, LocalDateTime assignedAt) {
        this.employee = employee;
        this.subscription = subscription;
        this.assignedAt = assignedAt;
        this.active = true;
    }

    public Long getId() { return id; }

    public Employee getEmployee() { return employee; }
    public void setEmployee(Employee employee) { this.employee = employee; }

    public Subscription getSubscription() { return subscription; }
    public void setSubscription(Subscription subscription) { this.subscription = subscription; }

    public LocalDateTime getAssignedAt() { return assignedAt; }
    public void setAssignedAt(LocalDateTime assignedAt) { this.assignedAt = assignedAt; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}
