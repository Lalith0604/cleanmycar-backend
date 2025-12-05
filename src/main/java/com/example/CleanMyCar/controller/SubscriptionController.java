package com.example.CleanMyCar.controller;

import com.example.CleanMyCar.dto.CreateCustomerRequest;
import com.example.CleanMyCar.dto.CreateSubscriptionRequest;
import com.example.CleanMyCar.model.Customer;
import com.example.CleanMyCar.model.Plan;
import com.example.CleanMyCar.model.Subscription;
import com.example.CleanMyCar.service.CustomerService;
import com.example.CleanMyCar.service.PlanService;
import com.example.CleanMyCar.service.SubscriptionService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/subscriptions")
public class SubscriptionController {
    private final SubscriptionService subscriptionService;
    private final PlanService planService;
    private final CustomerService customerService;

    public SubscriptionController(SubscriptionService subscriptionService, PlanService planService, CustomerService customerService) {
        this.subscriptionService = subscriptionService;
        this.planService = planService;
        this.customerService = customerService;
    }

    @GetMapping
    public ResponseEntity<List<Subscription>> list() {
        return ResponseEntity.ok(subscriptionService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable Long id) {
        Subscription s = subscriptionService.getById(id);
        if (s == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(s);
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody CreateSubscriptionRequest req) {

        Plan plan = planService.getById(req.getPlanId());
        if (plan == null) return ResponseEntity.badRequest().body("Invalid planId");

        Customer customer;

        // If existing customer ID is passed
        if (req.getCustomerId() != null) {
            customer = customerService.findById(req.getCustomerId());
            if (customer == null) return ResponseEntity.badRequest().body("Customer not found");

        } else if (req.getCustomerDetails() != null) {

            CreateCustomerRequest cd = req.getCustomerDetails();

            // Customer has NO apartment/parking — those are car-level
            Customer newCustomer = new Customer(
                    cd.getFullName(),
                    cd.getMobileNumber(),
                    cd.getEmail(),
                    null,
                    null,
                    null,
                    null
            );

            customer = customerService.createOrFind(newCustomer);

        } else {
            return ResponseEntity.badRequest().body("Provide customerId OR customerDetails");
        }

        // Set start date
        LocalDate start = (req.getPreferredStartDate() != null && !req.getPreferredStartDate().isBlank())
                ? LocalDate.parse(req.getPreferredStartDate())
                : LocalDate.now().plusDays(1);

        // CREATE SUBSCRIPTION WITH CAR-LEVEL LOCATION FIELDS
        Subscription s = subscriptionService.createSubscription(
                customer,
                plan,
                req.getCarCompany(),
                req.getCarModel(),
                req.getCarNumber(),
                start,
                req.getApartmentName(),
                req.getParkingSlot(),
                req.getLatitude(),
                req.getLongitude()
        );

        return ResponseEntity.ok(s);
    }


    @PostMapping("/{id}/activate")
    public ResponseEntity<?> activate(@PathVariable Long id) {
        subscriptionService.markActive(id);
        return ResponseEntity.ok("Activated");
    }
}
