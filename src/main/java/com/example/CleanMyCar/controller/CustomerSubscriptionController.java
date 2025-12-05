package com.example.CleanMyCar.controller;

import com.example.CleanMyCar.model.Subscription;
import com.example.CleanMyCar.repository.SubscriptionRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/customer-subscriptions")
public class CustomerSubscriptionController {

    private final SubscriptionRepository subscriptionRepo;

    public CustomerSubscriptionController(SubscriptionRepository subscriptionRepo) {
        this.subscriptionRepo = subscriptionRepo;
    }

    @GetMapping("/{mobile}")
    public ResponseEntity<?> getSubscriptionsByMobile(@PathVariable String mobile) {
        List<Subscription> list = subscriptionRepo.findByCustomerMobileNumber(mobile);
        return ResponseEntity.ok(list);
    }
}