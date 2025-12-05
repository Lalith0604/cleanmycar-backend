package com.example.CleanMyCar.service;

import com.example.CleanMyCar.dto.AdminStatsDto;
import com.example.CleanMyCar.model.Customer;
import com.example.CleanMyCar.model.Subscription;
import com.example.CleanMyCar.model.PaymentRecord;
import com.example.CleanMyCar.repository.CustomerRepository;
import com.example.CleanMyCar.repository.PaymentRecordRepository;
import com.example.CleanMyCar.repository.SubscriptionRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AdminService {

    private final CustomerRepository customerRepo;
    private final SubscriptionRepository subscriptionRepo;
    private final PaymentRecordRepository paymentRepo;

    public AdminService(CustomerRepository customerRepo,
                        SubscriptionRepository subscriptionRepo,
                        PaymentRecordRepository paymentRepo) {
        this.customerRepo = customerRepo;
        this.subscriptionRepo = subscriptionRepo;
        this.paymentRepo = paymentRepo;
    }

    // Customers (paged)
    public Page<Customer> getAllCustomers(Pageable pageable) {
        return customerRepo.findAll(pageable);
    }

    // Subscriptions (paged) - optional status filter
    public Page<Subscription> getSubscriptions(Pageable pageable, String status) {
        if (status == null || status.isBlank()) {
            return subscriptionRepo.findAll(pageable);
        }
        return subscriptionRepo.findByStatus(status, pageable);
    }


    // Payments (paged)
    public Page<PaymentRecord> getPayments(Pageable pageable) {
        return paymentRepo.findAllWithDetails(pageable);
    }

    // Stats
    public AdminStatsDto getStats() {
        AdminStatsDto dto = new AdminStatsDto();

        LocalDate today = LocalDate.now(ZoneId.systemDefault());
        LocalDateTime dayStart = today.atStartOfDay();
        LocalDateTime dayEnd = today.plusDays(1).atStartOfDay().minusNanos(1);

        // today's new subscriptions
        long todaysNew = subscriptionRepo.countByStartDate(today);
        dto.setTodaysNewSubscriptions(todaysNew);

        // today's revenue and total revenue (only SUCCESS payments)
        Long todaysRevenue = paymentRepo.sumAmountByStatusBetween("SUCCESS", dayStart, dayEnd);
        if (todaysRevenue == null) todaysRevenue = 0L;
        dto.setTodaysRevenue(todaysRevenue);

        Long totalRevenue = paymentRepo.sumAmountByStatus("SUCCESS");
        if (totalRevenue == null) totalRevenue = 0L;
        dto.setTotalRevenue(totalRevenue);

        // renewals in last 30 days:
        LocalDate fromDate = today.minusDays(30);
        LocalDateTime fromDateTime = fromDate.atStartOfDay();
        LocalDateTime toDateTime = dayEnd;

        List<Subscription> recentSubs = subscriptionRepo.findByStartDateBetween(fromDate, today);

        // Map customerId -> count of subscriptions (overall)
        Map<Long, Long> customerSubCounts = recentSubs.stream()
                .collect(Collectors.groupingBy(s -> s.getCustomer().getId(), Collectors.counting()));

        // For each recent subscription's customer check total subscriptions count > 1 (customer had previous)
        long renewals = 0;
        for (Subscription s : recentSubs) {
            Long customerId = s.getCustomer().getId();
            // count all subscriptions for this customer
            List<Subscription> allForCustomer = subscriptionRepo.findByCustomerId(customerId);
            if (allForCustomer.size() > 1) {
                // ensure this subscription is not the customer's very first (i.e., previous exists)
                // If customer's earliest subscription started before this subs startDate -> treat as renewal
                boolean hasPrevious = allForCustomer.stream()
                        .anyMatch(x -> x.getStartDate().isBefore(s.getStartDate()));
                if (hasPrevious) renewals++;
            }
        }

        dto.setRenewalsLast30Days(renewals);

        // totals
        dto.setTotalCustomers(customerRepo.count());
        dto.setTotalSubscriptions(subscriptionRepo.count());

        return dto;
    }

    public String exportCustomersCsv() {

        List<String> headers = List.of(
                "ID", "Full Name", "Mobile", "Email",
                "Apartment", "Parking Slot", "Latitude", "Longitude"
        );

        List<List<String>> rows = customerRepo.findAll().stream()
                .map(c -> List.of(
                        String.valueOf(c.getId()),
                        c.getFullName(),
                        c.getMobileNumber(),
                        c.getEmail(),
                        c.getApartmentName(),
                        c.getParkingSlot(),
                        c.getLatitude() == null ? "" : c.getLatitude().toString(),
                        c.getLongitude() == null ? "" : c.getLongitude().toString()
                ))
                .collect(Collectors.toList());

        return com.example.CleanMyCar.util.CsvUtil.toCsv(headers, rows);
    }

    public String exportSubscriptionsCsv() {

        List<String> headers = List.of(
                "ID", "Customer Name", "Customer Mobile",
                "Plan", "Amount", "Car Company", "Car Model",
                "Car Number", "Start Date", "End Date",
                "Status", "Created At"
        );

        List<List<String>> rows = subscriptionRepo.findAll().stream()
                .map(s -> List.of(
                        String.valueOf(s.getId()),
                        s.getCustomer().getFullName(),
                        s.getCustomer().getMobileNumber(),
                        s.getPlan().getName(),
                        String.valueOf(s.getPlan().getAmount()),
                        s.getCarCompany(),
                        s.getCarModel(),
                        s.getCarNumber(),
                        s.getStartDate().toString(),
                        s.getEndDate().toString(),
                        s.getStatus(),
                        s.getCreatedAt().toString()
                ))
                .collect(Collectors.toList());

        return com.example.CleanMyCar.util.CsvUtil.toCsv(headers, rows);
    }

    public String exportPaymentsCsv() {

        List<String> headers = List.of(
                "ID", "Subscription ID", "Customer Mobile", "Amount",
                "Status", "Provider Payment ID", "Created At"
        );

        List<List<String>> rows = paymentRepo.findAll().stream()
                .map(p -> List.of(
                        String.valueOf(p.getId()),
                        p.getSubscription() == null ? "" : p.getSubscription().getId().toString(),
                        p.getSubscription() == null ? "" : p.getSubscription().getCustomer().getMobileNumber(),
                        String.valueOf(p.getAmount()),
                        p.getStatus(),
                        p.getProviderPaymentId(),
                        p.getCreatedAt().toString()
                ))
                .collect(Collectors.toList());

        return com.example.CleanMyCar.util.CsvUtil.toCsv(headers, rows);
    }



}

