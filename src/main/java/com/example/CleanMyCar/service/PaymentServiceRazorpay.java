package com.example.CleanMyCar.service;

import com.example.CleanMyCar.model.PaymentRecord;
import com.example.CleanMyCar.model.Subscription;
import com.example.CleanMyCar.repository.PaymentRecordRepository;
import com.example.CleanMyCar.repository.SubscriptionRepository;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.time.LocalDateTime;

@Service
public class PaymentServiceRazorpay {

    @Value("${razorpay.key_id}")
    private String razorpayKey;

    @Value("${razorpay.key_secret}")
    private String razorpaySecret;

    private final SubscriptionRepository subscriptionRepo;
    private final PaymentRecordRepository paymentRepo;

    public PaymentServiceRazorpay(SubscriptionRepository subscriptionRepo,
                                  PaymentRecordRepository paymentRepo) {
        this.subscriptionRepo = subscriptionRepo;
        this.paymentRepo = paymentRepo;
    }

    /** STEP 1: Create a Razorpay Order */
    public JSONObject createOrder(Long subscriptionId) throws Exception {

        Subscription subscription = subscriptionRepo.findById(subscriptionId).orElse(null);
        if (subscription == null) {
            throw new Exception("Subscription not found");
        }

        RazorpayClient client = new RazorpayClient(razorpayKey, razorpaySecret);

        int amount = subscription.getPlan().getAmount() * 100;

        JSONObject req = new JSONObject();
        req.put("amount", amount);
        req.put("currency", "INR");
        req.put("receipt", "receipt_" + subscriptionId);

        Order order = client.orders.create(req);

        JSONObject res = new JSONObject();
        res.put("orderId", order.get("id").toString());
        res.put("razorpayKey", razorpayKey);
        res.put("amount", amount);

        return res;
    }

    /** STEP 2: Verify Razorpay Payment Signature (Java 21 Safe) */
    public boolean verifySignature(String orderId, String paymentId, String signature) throws Exception {

        String payload = orderId + "|" + paymentId;

        Mac hmac = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKey = new SecretKeySpec(razorpaySecret.getBytes(), "HmacSHA256");
        hmac.init(secretKey);

        byte[] hash = hmac.doFinal(payload.getBytes());

        String generatedSignature = bytesToHex(hash);

        return generatedSignature.equals(signature);
    }

    /** Convert bytes to lowercase hex — Java 21 compatible */
    private String bytesToHex(byte[] hash) {
        StringBuilder hex = new StringBuilder();
        for (byte b : hash) {
            String h = Integer.toHexString(0xff & b);
            if (h.length() == 1) hex.append('0');
            hex.append(h);
        }
        return hex.toString();
    }

    /** STEP 3: Save Payment & Activate Subscription */
    public void savePaymentAndActivate(String orderId, String paymentId,
                                       String signature, Long subscriptionId) throws Exception {

        boolean valid = verifySignature(orderId, paymentId, signature);

        Subscription subscription = subscriptionRepo.findById(subscriptionId).orElse(null);

        PaymentRecord record = new PaymentRecord(
                "RAZORPAY",
                paymentId,
                subscription.getPlan().getAmount(),
                "INR",
                valid ? "SUCCESS" : "FAILED",
                subscription,
                LocalDateTime.now()
        );

        paymentRepo.save(record);

        if (valid) {
            subscription.setStatus("ACTIVE");
            subscriptionRepo.save(subscription);
        }
    }
}
