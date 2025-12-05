package com.example.CleanMyCar.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "subscriptions")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    @JsonIgnoreProperties({"subscriptions"})
    private Customer customer;

    @ManyToOne(optional = false)
    private Plan plan;

    private String carCompany;
    private String carModel;
    private String carNumber;

    private LocalDate startDate;
    private LocalDate endDate;

    private String status;
    private LocalDateTime createdAt;

    private String apartmentName;
    private String parkingSlot;
    private Double latitude;
    private Double longitude;



    public Subscription() {}

    public Subscription(Customer customer, Plan plan, String carCompany,
                        String carModel, String carNumber, LocalDate startDate,
                        LocalDate endDate, String status, LocalDateTime createdAt) {

        this.customer = customer;
        this.plan = plan;
        this.carCompany = carCompany;
        this.carModel = carModel;
        this.carNumber = carNumber;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        this.createdAt = createdAt;
    }

    public String getApartmentName() {return apartmentName;}
    public void setApartmentName(String apartmentName) {this.apartmentName = apartmentName;}

    public String getParkingSlot() {return parkingSlot;}
    public void setParkingSlot(String parkingSlot) {this.parkingSlot = parkingSlot;}

    public Double getLatitude() {return latitude;}
    public void setLatitude(Double latitude) {this.latitude = latitude;}

    public Double getLongitude() {return longitude;}
    public void setLongitude(Double longitude) {this.longitude = longitude;}

    public Long getId() { return id; }

    public Customer getCustomer() { return customer; }
    public void setCustomer(Customer customer) { this.customer = customer; }

    public Plan getPlan() { return plan; }
    public void setPlan(Plan plan) { this.plan = plan; }

    public String getCarCompany() { return carCompany; }
    public void setCarCompany(String carCompany) { this.carCompany = carCompany; }

    public String getCarModel() { return carModel; }
    public void setCarModel(String carModel) { this.carModel = carModel; }

    public String getCarNumber() { return carNumber; }
    public void setCarNumber(String carNumber) { this.carNumber = carNumber; }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
