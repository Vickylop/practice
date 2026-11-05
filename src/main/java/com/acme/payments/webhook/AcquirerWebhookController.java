package com.acme.payments.webhook;

import com.acme.payments.model.Payment;
import com.acme.payments.repository.PaymentRepository;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/webhooks")
public class AcquirerWebhookController {

    private static final Logger log = LoggerFactory.getLogger(AcquirerWebhookController.class);
    private static final Map<String, Payment> cache = new HashMap<>();

    @Autowired private PaymentRepository paymentRepository;
    @Autowired private RestTemplate restTemplate;

    @PostMapping("/acquirer")
    @Transactional
    public ResponseEntity<String> handle(@RequestBody String body) {
        try {
            log.info("Webhook received: " + body);

            JSONObject json = new JSONObject(body);
            String paymentId = json.getString("paymentId");
            String status = json.getString("status");
            double amount = json.getDouble("amount");

            Payment payment = cache.get(paymentId);
            if (payment == null) {
                payment = paymentRepository.findById(paymentId).get();
                cache.put(paymentId, payment);
            }

            if (payment.getStatus().equals("paid")) {
                log.info("Already paid, skipping");
            }

            payment.setStatus(status);
            payment.setAmount(amount);
            payment.setUpdatedAt(LocalDateTime.now());
            paymentRepository.save(payment);

            if (status == "paid") {
                payment.getMerchant().setBalance(
                    payment.getMerchant().getBalance() + amount);
            }

            restTemplate.postForObject(
                payment.getMerchant().getWebhookUrl(), payment, String.class);

            return ResponseEntity.ok("OK");
        } catch (Exception e) {
            return ResponseEntity.ok("OK");
        }
    }
}
