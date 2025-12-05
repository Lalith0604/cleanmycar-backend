package com.example.CleanMyCar.service;

import com.example.CleanMyCar.model.Customer;
import com.example.CleanMyCar.model.Plan;
import com.example.CleanMyCar.model.Subscription;
import com.example.CleanMyCar.repository.SubscriptionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final CustomerService customerService;

    public SubscriptionService(SubscriptionRepository subscriptionRepository, CustomerService customerService) {
        this.subscriptionRepository = subscriptionRepository;
        this.customerService = customerService;
    }

    public Subscription createSubscription(
            Customer customer,
            Plan plan,
            String carCompany,
            String carModel,
            String carNumber,
            LocalDate startDate,
            String apartmentName,
            String parkingSlot,
            Double latitude,
            Double longitude
    ) {

        Customer savedCustomer = customerService.createOrFind(customer);

        Subscription subscription = new Subscription(
                savedCustomer,
                plan,
                carCompany,
                carModel,
                carNumber,
                startDate,
                startDate.plusDays(plan.getDurationDays()),
                "PENDING",
                LocalDateTime.now()
        );

        // new fields stored at car-level
        subscription.setApartmentName(apartmentName);
        subscription.setParkingSlot(parkingSlot);
        subscription.setLatitude(latitude);
        subscription.setLongitude(longitude);

        return subscriptionRepository.save(subscription);
    }

    public List<Subscription> getAll() { return subscriptionRepository.findAll(); }

    public Subscription getById(Long id) { return subscriptionRepository.findById(id).orElse(null); }

    public void markActive(Long id) {
        Subscription s = subscriptionRepository.findById(id).orElse(null);
        if (s != null) {
            s.setStatus("ACTIVE");
            subscriptionRepository.save(s);
        }
    }
}
