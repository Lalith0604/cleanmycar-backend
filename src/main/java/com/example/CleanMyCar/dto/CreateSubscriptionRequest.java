package com.example.CleanMyCar.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CreateSubscriptionRequest {
    @NotNull
    private Long planId;

    // either existing customerId OR provide customerDetails
    private Long customerId;
    private CreateCustomerRequest customerDetails;

    // car details
    @NotBlank
    private String carCompany;
    @NotBlank
    private String carModel;
    @NotBlank
    private String carNumber;

    private String apartmentName;
    private String parkingSlot;
    private Double latitude;
    private Double longitude;


    // preferred start date (YYYY-MM-DD) optional
    private String preferredStartDate;

    public CreateSubscriptionRequest() {}

    public Long getPlanId() { return planId; }
    public void setPlanId(Long planId) { this.planId = planId; }

    public Long getCustomerId() { return customerId; }
    public void setCustomerId(Long customerId) { this.customerId = customerId; }

    public CreateCustomerRequest getCustomerDetails() { return customerDetails; }
    public void setCustomerDetails(CreateCustomerRequest customerDetails) { this.customerDetails = customerDetails; }

    public String getCarCompany() { return carCompany; }
    public void setCarCompany(String carCompany) { this.carCompany = carCompany; }

    public String getCarModel() { return carModel; }
    public void setCarModel(String carModel) { this.carModel = carModel; }

    public String getCarNumber() { return carNumber; }
    public void setCarNumber(String carNumber) { this.carNumber = carNumber; }

    public String getApartmentName() {return apartmentName;}
    public void setApartmentName(String apartmentName) {this.apartmentName = apartmentName;}

    public String getParkingSlot() {return parkingSlot;}
    public void setParkingSlot(String parkingSlot) {this.parkingSlot = parkingSlot;}

    public Double getLatitude() {return latitude;}
    public void setLatitude(Double latitude) {this.latitude = latitude;}

    public Double getLongitude() {return longitude;}
    public void setLongitude(Double longitude) {this.longitude = longitude;}


    public String getPreferredStartDate() { return preferredStartDate; }
    public void setPreferredStartDate(String preferredStartDate) { this.preferredStartDate = preferredStartDate; }
}
