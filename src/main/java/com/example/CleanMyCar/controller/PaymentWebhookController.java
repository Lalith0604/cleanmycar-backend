package com.example.CleanMyCar.controller;

import com.example.CleanMyCar.dto.PaymentWebhookPayload;
import com.example.CleanMyCar.model.PaymentRecord;
import com.example.CleanMyCar.model.Subscription;
import com.example.CleanMyCar.repository.PaymentRecordRepository;
import com.example.CleanMyCar.repository.SubscriptionRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/payments")
public class PaymentWebhookController {
    private final PaymentRecordRepository paymentRepo;
    private final SubscriptionRepository subscriptionRepo;

    public PaymentWebhookController(PaymentRecordRepository paymentRepo, SubscriptionRepository subscriptionRepo) {
        this.paymentRepo = paymentRepo;
        this.subscriptionRepo = subscriptionRepo;
    }

    @PostMapping("/webhook")
    public ResponseEntity<?> webhook(@RequestBody PaymentWebhookPayload payload) {
        Subscription subscription = subscriptionRepo.findById(payload.getSubscriptionId()).orElse(null);

        PaymentRecord pr = new PaymentRecord(
                payload.getProvider(),
                payload.getProviderPaymentId(),
                payload.getAmount(),
                "INR",
                payload.getStatus(),
                subscription,
                LocalDateTime.now()
        );

        paymentRepo.save(pr);

        if ("SUCCESS".equalsIgnoreCase(payload.getStatus()) && subscription != null) {
            subscription.setStatus("ACTIVE");
            subscriptionRepo.save(subscription);
        }

        return ResponseEntity.ok("received");
    }
}
