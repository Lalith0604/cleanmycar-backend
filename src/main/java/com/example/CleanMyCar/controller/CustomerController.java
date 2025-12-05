package com.example.CleanMyCar.controller;

import com.example.CleanMyCar.dto.CreateCustomerRequest;
import com.example.CleanMyCar.model.Customer;
import com.example.CleanMyCar.repository.CustomerRepository;
import com.example.CleanMyCar.service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {
    private final CustomerService customerService;
    private final CustomerRepository customerRepo;

    public CustomerController(CustomerService customerService ,CustomerRepository customerRepo) {
        this.customerRepo=customerRepo;
        this.customerService = customerService; }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody CreateCustomerRequest req) {
        Customer c = new Customer(
                req.getFullName(),
                req.getMobileNumber(),
                req.getEmail(),
                req.getApartmentName(),
                req.getParkingSlot(),
                req.getLatitude(),
                req.getLongitude()
        );

        Customer saved = customerService.createOrFind(c);
        return ResponseEntity.ok(saved);
    }


    @PostMapping("/check-or-create")
    public ResponseEntity<Customer> checkOrCreate(@RequestBody Map<String, String> body) {
        String mobile = body.get("mobile");
        String name = body.get("name");

        Optional<Customer> existing = customerRepo.findByMobileNumber(mobile);

        if (existing.isPresent()) {
            return ResponseEntity.ok(existing.get());
        }

        Customer newCustomer = new Customer();
        newCustomer.setFullName(name);
        newCustomer.setMobileNumber(mobile);

        customerRepo.save(newCustomer);

        return ResponseEntity.ok(newCustomer);
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable Long id) {
        Customer c = customerService.findById(id);
        if (c == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(c);
    }

    @GetMapping("/by-mobile/{mobile}")
    public ResponseEntity<?> getByMobile(@PathVariable String mobile) {
        Customer c = customerService.findByMobile(mobile);
        if (c == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(c);
    }
}


