package com.example.CleanMyCar.service;

import com.example.CleanMyCar.model.Customer;
import com.example.CleanMyCar.repository.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public Customer createOrFind(Customer incoming) {

        Optional<Customer> existingOpt = customerRepository.findByMobileNumber(incoming.getMobileNumber());

        if (existingOpt.isPresent()) {
            Customer existing = existingOpt.get();

            // Update only if new value is provided (non-null / non-blank)
            if (isNonBlank(incoming.getFullName())) {
                existing.setFullName(incoming.getFullName());
            }
            if (isNonBlank(incoming.getEmail())) {
                existing.setEmail(incoming.getEmail());
            }
            if (isNonBlank(incoming.getApartmentName())) {
                existing.setApartmentName(incoming.getApartmentName());
            }
            if (isNonBlank(incoming.getParkingSlot())) {
                existing.setParkingSlot(incoming.getParkingSlot());
            }
            if (incoming.getLatitude() != null) {
                existing.setLatitude(incoming.getLatitude());
            }
            if (incoming.getLongitude() != null) {
                existing.setLongitude(incoming.getLongitude());
            }

            return customerRepository.save(existing);
        }

        // New customer → just save
        return customerRepository.save(incoming);
    }

    private boolean isNonBlank(String s) {
        return s != null && !s.isBlank();
    }

    public Customer findById(Long id) {
        return customerRepository.findById(id).orElse(null);
    }

    public Customer findByMobile(String mobile) {
        return customerRepository.findByMobileNumber(mobile).orElse(null);
    }
}


