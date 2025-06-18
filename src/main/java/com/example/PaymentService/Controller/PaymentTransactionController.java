package com.example.paymentservice.controller;


import com.example.paymentservice.Dto.*;
import com.example.paymentservice.Service.PaymentTransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payment")
@Slf4j
@RequiredArgsConstructor
public class PaymentTransactionController {

    private final PaymentTransactionService paymentTransactionService;

    @PatchMapping("/process-payment")
    public ResponseEntity<PaymentProcessResponseDto> processPayment(@Valid @RequestBody PaymentProcessRequestDto paymentRequest) {
        log.info("Started payment process for transaction id: [{}]", paymentRequest.getTransactionId());
        return ResponseEntity.ok(paymentTransactionService.processPayment(paymentRequest));

    }

    @PostMapping("/generate-payment")
    public ResponseEntity<PaymentAmountResponseDto> generatePayment(@RequestBody @Valid PaymentAmountRequestDto paymentAmountRequestDto) {
        log.info("Received payment generation request for amount: {}", paymentAmountRequestDto.getAmount());
        PaymentAmountResponseDto paymentAmountResponseDto = paymentTransactionService.generatePayment(paymentAmountRequestDto);
        log.info("Successfully generated transaction ID {} for amount {} ", paymentAmountResponseDto.getTransactionId(), paymentAmountResponseDto.getAmount());
        return ResponseEntity.status(HttpStatus.CREATED).body(paymentAmountResponseDto);
    }

    @PatchMapping("/update-payment")
    public ResponseEntity<String> updatePayment(@RequestBody @Valid UpdatePaymentRequestDto updatePaymentRequestDto) {
        log.info("Started update payment process for transaction id: [{}]", updatePaymentRequestDto.getTransactionId());
        paymentTransactionService.updatePayment(updatePaymentRequestDto);
        return ResponseEntity.ok("updated transaction status to "+ updatePaymentRequestDto.getStatus());

    }
}

