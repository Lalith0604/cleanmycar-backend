package com.example.CleanMyCar.service;

import com.example.CleanMyCar.model.PaymentRecord;
import com.example.CleanMyCar.model.Subscription;
import com.example.CleanMyCar.repository.PaymentRecordRepository;
import com.example.CleanMyCar.repository.SubscriptionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class PaymentService {

    private final PaymentRecordRepository paymentRepo;
    private final SubscriptionRepository subscriptionRepo;

    public PaymentService(PaymentRecordRepository paymentRepo,
                          SubscriptionRepository subscriptionRepo) {
        this.paymentRepo = paymentRepo;
        this.subscriptionRepo = subscriptionRepo;
    }

    public PaymentRecord recordPayment(Long subscriptionId, String provider,
                                       String paymentId, Integer amount,
                                       String status) {

        Subscription subscription = subscriptionRepo.findById(subscriptionId).orElse(null);

        PaymentRecord record = new PaymentRecord(
                provider,
                paymentId,
                amount,
                "INR",
                status,
                subscription,
                LocalDateTime.now()
        );

        paymentRepo.save(record);

        if ("SUCCESS".equalsIgnoreCase(status) && subscription != null) {
            subscription.setStatus("ACTIVE");
            subscriptionRepo.save(subscription);
        }

        return record;
    }
}
