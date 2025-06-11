package com.example.payment.controller;

import com.example.payment.dto.PaymentProcessRequestDto;
import com.example.payment.dto.PaymentProcessResponseDto;
import com.example.payment.service.PaymentTransactionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payment")
@Slf4j
public class PaymentTransactionController {

    private final PaymentTransactionService paymentTransactionService;

    @Autowired
    public PaymentTransactionController(PaymentTransactionService paymentService) {
        this.paymentTransactionService = paymentService;
    }

    @PostMapping("/process-payment")
    public ResponseEntity<PaymentProcessResponseDto> processPayment(@RequestBody PaymentProcessRequestDto paymentRequest)  {
        log.info("Started payment process for transaction id: [{}]",paymentRequest.getTransactionId());
        return ResponseEntity.ok(paymentTransactionService.processPayment(paymentRequest));

    }
}

