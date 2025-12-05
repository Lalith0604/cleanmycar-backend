package com.example.CleanMyCar.model;

import jakarta.persistence.*;

@Entity
@Table(name = "employees")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false, unique = true)
    private String mobileNumber;

    // 4 or 6 digit PIN (for now store plain; you can hash later)
    @Column(nullable = false)
    private String pin;

    @Column(nullable = false)
    private String status = "ACTIVE"; // ACTIVE / INACTIVE

    public Employee() {}

    public Employee(String fullName, String mobileNumber, String pin) {
        this.fullName = fullName;
        this.mobileNumber = mobileNumber;
        this.pin = pin;
        this.status = "ACTIVE";
    }

    public Long getId() { return id; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getMobileNumber() { return mobileNumber; }
    public void setMobileNumber(String mobileNumber) { this.mobileNumber = mobileNumber; }

    public String getPin() { return pin; }
    public void setPin(String pin) { this.pin = pin; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
