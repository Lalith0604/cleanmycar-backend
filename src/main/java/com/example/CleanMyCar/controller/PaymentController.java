package com.example.CleanMyCar.controller;

import com.example.CleanMyCar.service.PaymentServiceRazorpay;
import com.example.CleanMyCar.dto.VerifyPaymentRequest;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    private final PaymentServiceRazorpay paymentService;

    public PaymentController(PaymentServiceRazorpay paymentService) {
        this.paymentService = paymentService;
    }

    /** STEP 1: Frontend calls this to get razorpay order */
    @PostMapping("/create-order/{subscriptionId}")
    public ResponseEntity<?> createOrder(@PathVariable Long subscriptionId) {
        try {
            JSONObject order = paymentService.createOrder(subscriptionId);
            return ResponseEntity.ok(order.toString());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /** STEP 2: Razorpay webhook OR frontend verification call */
    @PostMapping("/verify")
    public ResponseEntity<?> verifyPayment(@RequestBody VerifyPaymentRequest req) {
        try {
            paymentService.savePaymentAndActivate(
                    req.getOrderId(),
                    req.getPaymentId(),
                    req.getSignature(),
                    req.getSubscriptionId()
            );
            return ResponseEntity.ok("Payment Verified");
        }catch (Exception e) {
            return ResponseEntity.badRequest().body("Verification failed: " + e.getMessage());
        }
    }


}

